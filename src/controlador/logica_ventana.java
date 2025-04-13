package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import vista.ventana;
import modelo.persona;
import modelo.personaDAO;

// Clase logica_ventana que implementa ActionListener e ItemListener.
public class logica_ventana implements ActionListener, ItemListener {
    private ventana delegado;
    private String nombres, email, telefono, categoria = "";
    private boolean favorito = false;

    // Constructor
    public logica_ventana(ventana delegado) {
        this.delegado = delegado;

        // Asignar eventos a botones
        this.delegado.btn_add.addActionListener(this);
        this.delegado.btn_modificar.addActionListener(this);
        this.delegado.btn_eliminar.addActionListener(this);
        this.delegado.btn_exportar.addActionListener(this); // Botón de exportar

        // Listener para el JComboBox y JCheckBox
        this.delegado.cmb_categoria.addItemListener(this);
        this.delegado.chb_favorito.addItemListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == delegado.btn_add) {
            agregarContacto();
        } else if (e.getSource() == delegado.btn_modificar) {
            modificarContacto();
        } else if (e.getSource() == delegado.btn_eliminar) {
            eliminarContacto();
        } else if (e.getSource() == delegado.btn_exportar) {
            exportarContactosCSV();
        }
    }

    // Método para exportar contactos a un archivo CSV.
    private void exportarContactosCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Contactos como CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos CSV", "csv"));

        int userSelection = fileChooser.showSaveDialog(delegado);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();

            // Asegurarse de que el archivo tenga extensión .csv
            String rutaArchivo = archivoSeleccionado.getAbsolutePath();
            if (!rutaArchivo.endsWith(".csv")) {
                rutaArchivo += ".csv";
            }

            try {
                // Obtener contactos de la tabla
                List<persona> contactos = obtenerContactosDesdeTabla();

                // Exportar contactos a CSV
                new personaDAO(null).exportarCSV(contactos, rutaArchivo);
                JOptionPane.showMessageDialog(delegado, "Contactos exportados exitosamente a: " + rutaArchivo);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(delegado, "Error al exportar contactos: " + ex.getMessage());
            }
        }
    }

    // Método para obtener contactos visibles en el JTable.
    private List<persona> obtenerContactosDesdeTabla() {
        List<persona> contactos = new ArrayList<>();
        DefaultTableModel modeloTabla = (DefaultTableModel) delegado.tabla_contactos.getModel();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String nombre = modeloTabla.getValueAt(i, 0).toString();
            String telefono = modeloTabla.getValueAt(i, 1).toString();
            String email = modeloTabla.getValueAt(i, 2).toString();
            String categoria = modeloTabla.getValueAt(i, 3).toString();
            boolean favorito = modeloTabla.getValueAt(i, 4).toString().equals("Sí");

            contactos.add(new persona(nombre, telefono, email, categoria, favorito));
        }
        return contactos;
    }

    // Métodos para agregar, modificar y eliminar contactos (ya implementados previamente).
    private void agregarContacto() {
        // Implementación existente
    }

    private void modificarContacto() {
        // Implementación existente
    }

    private void eliminarContacto() {
        // Implementación existente
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == delegado.cmb_categoria) {
            categoria = delegado.cmb_categoria.getSelectedItem().toString();
        } else if (e.getSource() == delegado.chb_favorito) {
            favorito = delegado.chb_favorito.isSelected();
        }
    }
}