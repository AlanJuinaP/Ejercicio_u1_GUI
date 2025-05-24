package controlador;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.persona;
import modelo.personaDAO;
import vista.ventana;

/**
 * Clase que contiene la lógica de interacción entre la ventana y el modelo.
 */
public class logica_ventana {
    private final ventana delegado;              // Referencia a la ventana principal
    private final personaDAO dao;                // Referencia al DAO de persona
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // Pool para tareas de fondo
    private final Object lock = new Object();    // Objeto para sincronización

    /**
     * Constructor: inicializa la lógica y vincula los listeners de los botones.
     */
    public logica_ventana(ventana ventana) {
        this.delegado = ventana;
        this.dao = new personaDAO();
        cargarContactosEnTabla();

        // Listeners de los botones
        delegado.getBtnAdd().addActionListener(e -> validarYAgregarContacto());
        delegado.getBtnModificar().addActionListener(e -> modificarContacto());
        delegado.getBtnEliminar().addActionListener(e -> eliminarContacto());
        delegado.getBtnExportar().addActionListener(e -> exportarContactosJSON());
        delegado.getBtnImportar().addActionListener(e -> importarContactosJSON());
    }

    /**
     * Llena la tabla de la ventana con los contactos actuales.
     */
    private void cargarContactosEnTabla() {
        DefaultTableModel modelo = delegado.getModeloTabla();
        modelo.setRowCount(0); // Limpia la tabla primero
        for (persona contacto : dao.getContactos()) {
            modelo.addRow(new Object[]{
                contacto.getNombre(),
                contacto.getTelefono(),
                contacto.getEmail(),
                contacto.getCategoria(),
                contacto.isFavorito() ? "Sí" : "No"
            });
        }
    }

    /**
     * Valida y agrega un nuevo contacto en un hilo aparte.
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
            dao.modificarContacto(filaSeleccionada, contactoModificado);
            actualizarContactoEnTabla(filaSeleccionada, contactoModificado);
            mostrarNotificacion("Contacto modificado con éxito.");
            limpiarFormulario();
        }
    }

    /**
     * Elimina el contacto seleccionado de la tabla y la lista.
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
     * Exporta los contactos a un archivo JSON.
     */
    private void exportarContactosJSON() {
        executorService.submit(() -> {
            synchronized (lock) {
                try {
                    dao.exportarContactosJSON("contactos.json");
                    SwingUtilities.invokeLater(() -> mostrarNotificacion("Exportación JSON completada con éxito."));
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(delegado, "Error al exportar contactos a JSON."));
                }
            }
        });
    }

    /**
     * Importa contactos desde un archivo JSON.
     */
    private void importarContactosJSON() {
        executorService.submit(() -> {
            synchronized (lock) {
                try {
                    dao.importarContactosJSON("contactos.json");
                    SwingUtilities.invokeLater(() -> {
                        cargarContactosEnTabla();
                        mostrarNotificacion("Importación JSON completada con éxito.");
                    });
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(delegado, "Error al importar contactos desde JSON."));
                }
            }
        });
    }

    /**
     * Agrega un contacto a la tabla de la interfaz.
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
     * Actualiza un contacto en la tabla de la interfaz.
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
     * Muestra una notificación simple.
     */
    private void mostrarNotificacion(String mensaje) {
        JOptionPane.showMessageDialog(delegado, mensaje);
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        delegado.getTxtNombres().setText("");
        delegado.getTxtTelefono().setText("");
        delegado.getTxtEmail().setText("");
        delegado.getCmbCategoria().setSelectedIndex(0);
        delegado.getChbFavorito().setSelected(false);
    }
}