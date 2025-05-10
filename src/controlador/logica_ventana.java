package controlador;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.persona;
import modelo.personaDAO;
import vista.ventana;

/**
 * Clase logica_ventana que implementa la lógica del controlador.
 * Maneja validaciones, búsquedas, exportaciones y sincronización con subprocesos.
 */
public class logica_ventana {
    private final ventana delegado; // Referencia a la vista
    private final personaDAO dao;  // Referencia al modelo
    private final ExecutorService executorService; // Para manejar subprocesos

    // Objeto para sincronización
    private final Object lock = new Object();

    /**
     * Constructor de la clase logica_ventana.
     * @param delegado La referencia a la ventana (vista).
     */
    public logica_ventana(ventana delegado) {
        this.delegado = delegado;
        this.dao = new personaDAO();
        this.executorService = Executors.newCachedThreadPool();

        // Asignar acciones a los botones de la vista
        delegado.btn_add.addActionListener(e -> validarYAgregarContacto());
        delegado.btn_modificar.addActionListener(e -> modificarContacto());
        delegado.btn_eliminar.addActionListener(e -> eliminarContacto());
        delegado.btn_exportar.addActionListener(e -> exportarContactos());
    }

    /**
     * Valida y agrega un contacto en segundo plano para evitar duplicados.
     */
    private void validarYAgregarContacto() {
        String nombre = delegado.getTxtNombres().getText();
        String telefono = delegado.getTxtTelefono().getText();
        String email = delegado.getTxtEmail().getText();
        String categoria = (String) delegado.getCmbCategoria().getSelectedItem();
        boolean favorito = delegado.getChbFavorito().isSelected();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(delegado, "Todos los campos son obligatorios.");
            return;
        }

        persona nuevoContacto = new persona(nombre, telefono, email, categoria, favorito);

        // Validar en segundo plano
        executorService.submit(() -> {
            synchronized (lock) {
                boolean duplicado = dao.existeContacto(nuevoContacto);
                SwingUtilities.invokeLater(() -> {
                    if (duplicado) {
                        JOptionPane.showMessageDialog(delegado, "El contacto ya está registrado.");
                    } else {
                        dao.guardarContacto(nuevoContacto);
                        agregarContactoATabla(nuevoContacto);
                        mostrarNotificacion("Contacto guardado con éxito.");
                        limpiarFormulario();
                    }
                });
            }
        });
    }

    /**
     * Modifica el contacto seleccionado en la tabla.
     */
    private void modificarContacto() {
        int filaSeleccionada = delegado.getTablaContactos().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(delegado, "Seleccione un contacto para modificar.");
            return;
        }

        String nombre = delegado.getTxtNombres().getText();
        String telefono = delegado.getTxtTelefono().getText();
        String email = delegado.getTxtEmail().getText();
        String categoria = (String) delegado.getCmbCategoria().getSelectedItem();
        boolean favorito = delegado.getChbFavorito().isSelected();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(delegado, "Todos los campos son obligatorios.");
            return;
        }

        persona contactoModificado = new persona(nombre, telefono, email, categoria, favorito);

        synchronized (lock) {
            dao.modificarContacto(contactoModificado);
            actualizarContactoEnTabla(filaSeleccionada, contactoModificado);
            mostrarNotificacion("Contacto modificado con éxito.");
            limpiarFormulario();
        }
    }

    /**
     * Elimina el contacto seleccionado en la tabla.
     */
    private void eliminarContacto() {
        int filaSeleccionada = delegado.getTablaContactos().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(delegado, "Seleccione un contacto para eliminar.");
            return;
        }

        synchronized (lock) {
            dao.eliminarContacto(filaSeleccionada);
            DefaultTableModel modelo = delegado.getModeloTabla();
            modelo.removeRow(filaSeleccionada);
            mostrarNotificacion("Contacto eliminado con éxito.");
        }
    }

    /**
     * Exporta los contactos a un archivo CSV en segundo plano.
     */
    private void exportarContactos() {
        executorService.submit(() -> {
            synchronized (lock) {
                try {
                    dao.exportarCSV("contactos.csv");
                    SwingUtilities.invokeLater(() -> mostrarNotificacion("Exportación completada con éxito."));
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(delegado, "Error al exportar contactos."));
                }
            }
        });
    }

    /**
     * Agrega un contacto a la tabla de la interfaz gráfica.
     * @param contacto El contacto a agregar.
     */
    private void agregarContactoATabla(persona contacto) {
        DefaultTableModel modelo = delegado.getModeloTabla();
        modelo.addRow(new Object[]{
                contacto.getNombre(),
                contacto.getTelefono(),
                contacto.getEmail(),
                contacto.getCategoria(),
                contacto.isFavorito() ? "Sí" : "No"
        });
    }

    /**
     * Actualiza un contacto en la tabla de la interfaz gráfica.
     * @param fila La fila seleccionada.
     * @param contacto El contacto modificado.
     */
    private void actualizarContactoEnTabla(int fila, persona contacto) {
        DefaultTableModel modelo = delegado.getModeloTabla();
        modelo.setValueAt(contacto.getNombre(), fila, 0);
        modelo.setValueAt(contacto.getTelefono(), fila, 1);
        modelo.setValueAt(contacto.getEmail(), fila, 2);
        modelo.setValueAt(contacto.getCategoria(), fila, 3);
        modelo.setValueAt(contacto.isFavorito() ? "Sí" : "No", fila, 4);
    }

    /**
     * Muestra una notificación en la interfaz gráfica.
     * @param mensaje El mensaje de la notificación.
     */
    private void mostrarNotificacion(String mensaje) {
        JOptionPane.showMessageDialog(delegado, mensaje);
    }

    /**
     * Limpia el formulario de entrada en la interfaz gráfica.
     */
    private void limpiarFormulario() {
        delegado.getTxtNombres().setText("");
        delegado.getTxtTelefono().setText("");
        delegado.getTxtEmail().setText("");
        delegado.getCmbCategoria().setSelectedIndex(0);
        delegado.getChbFavorito().setSelected(false);
    }
}