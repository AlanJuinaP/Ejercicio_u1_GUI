package vista;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Clase que representa la ventana principal de la aplicación.
 */
public class ventana extends JFrame {
    private final JTable tablaContactos;
    private final DefaultTableModel modeloTabla;
    private final JTextField txtNombres, txtTelefono, txtEmail;
    private final JComboBox<String> cmbCategoria;
    private final JCheckBox chbFavorito;
    private final JButton btnAdd, btnModificar, btnEliminar, btnExportar, btnImportar;

    public ventana() {
        setTitle("Gestión de Contactos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        FlatLightLaf.setup();
        UIManager.put("Table.alternateRowColor", new Color(245, 245, 245));

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Contacto"));

        txtNombres = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        cmbCategoria = new JComboBox<>(new String[]{"Familia", "Amigos", "Trabajo", "Otros"});
        chbFavorito = new JCheckBox("Favorito");

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombres);
        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Email:"));
        panelFormulario.add(txtEmail);
        panelFormulario.add(new JLabel("Categoría:"));
        panelFormulario.add(cmbCategoria);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(chbFavorito);

        btnAdd = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnExportar = new JButton("Exportar JSON");
        btnImportar = new JButton("Importar JSON");

        panelFormulario.add(btnAdd);
        panelFormulario.add(btnModificar);
        panelFormulario.add(btnEliminar);
        panelFormulario.add(btnExportar);
        panelFormulario.add(btnImportar);

        String[] columnas = {"Nombre", "Teléfono", "Email", "Categoría", "Favorito"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaContactos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaContactos);

        setLayout(new BorderLayout(10, 10));
        add(panelFormulario, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
    }

    public JTable getTablaContactos() { return tablaContactos; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JTextField getTxtNombres() { return txtNombres; }
    public JTextField getTxtTelefono() { return txtTelefono; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JComboBox<String> getCmbCategoria() { return cmbCategoria; }
    public JCheckBox getChbFavorito() { return chbFavorito; }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnModificar() { return btnModificar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnExportar() { return btnExportar; }
    public JButton getBtnImportar() { return btnImportar; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ventana v = new ventana();
            new controlador.logica_ventana(v); // Vincula la lógica/controlador
            v.setVisible(true);
        });
    }
}