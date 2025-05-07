package vista;

import controlador.logica_ventana;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ventana extends JFrame {

    private JPanel contentPane;
    private JTextField txt_nombres;
    private JTextField txt_telefono;
    private JTextField txt_email;
    public JCheckBox chb_favorito;
    public JComboBox<String> cmb_categoria;
    public JButton btn_add;
    public JButton btn_modificar;
    public JButton btn_eliminar;
    public JButton btn_exportar;
    public JTable tabla_contactos;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> cmb_idiomas; // Selector de idioma
    private ResourceBundle bundle;

    private ArrayList<Contacto> listaContactos = new ArrayList<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ventana frame = new ventana();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ventana() {
        setTitle("Gestión de Contactos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 768);
        setResizable(false);

        configurarIdioma(new Locale("es", "ES"));

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // Panel superior
        JPanel panelIdiomas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblIdioma = new JLabel("Idioma:");
        cmb_idiomas = new JComboBox<>(new String[]{"Español", "English", "Português"});
        cmb_idiomas.setSelectedIndex(0);
        cmb_idiomas.addActionListener(e -> {
            switch (cmb_idiomas.getSelectedItem().toString()) {
                case "Español" -> configurarIdioma(new Locale("es", "ES"));
                case "English" -> configurarIdioma(new Locale("en", "US"));
                case "Português" -> configurarIdioma(new Locale("pt", "BR"));
            }
            actualizarTextos();
        });
        panelIdiomas.add(lblIdioma);
        panelIdiomas.add(cmb_idiomas);
        contentPane.add(panelIdiomas, BorderLayout.NORTH);

        // Panel central
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNombres = new JLabel("Nombres:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(lblNombres, gbc);

        txt_nombres = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelFormulario.add(txt_nombres, gbc);

        JLabel lblTelefono = new JLabel("Teléfono:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(lblTelefono, gbc);

        txt_telefono = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelFormulario.add(txt_telefono, gbc);

        JLabel lblEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(lblEmail, gbc);

        txt_email = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelFormulario.add(txt_email, gbc);

        JLabel lblCategoria = new JLabel("Categoría:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(lblCategoria, gbc);

        cmb_categoria = new JComboBox<>(new String[]{"Familia", "Amigos", "Trabajo"});
        gbc.gridx = 1;
        gbc.gridy = 3;
        panelFormulario.add(cmb_categoria, gbc);

        chb_favorito = new JCheckBox("Favorito");
        gbc.gridx = 1;
        gbc.gridy = 4;
        panelFormulario.add(chb_favorito, gbc);

        contentPane.add(panelFormulario, BorderLayout.WEST);

        modeloTabla = new DefaultTableModel(
                new String[]{"Nombre", "Teléfono", "Email", "Categoría", "Favorito"}, 0);
        tabla_contactos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tabla_contactos);
        contentPane.add(scrollTabla, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btn_add = new JButton("Agregar");
        btn_add.addActionListener(e -> agregarContacto());
        btn_modificar = new JButton("Modificar");
        btn_modificar.addActionListener(e -> modificarContacto());
        btn_eliminar = new JButton("Eliminar");
        btn_eliminar.addActionListener(e -> eliminarContacto());
        btn_exportar = new JButton("Exportar CSV");
        btn_exportar.addActionListener(e -> exportarCSV());
        panelBotones.add(btn_add);
        panelBotones.add(btn_modificar);
        panelBotones.add(btn_eliminar);
        panelBotones.add(btn_exportar);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        actualizarTextos();
    }

    private void configurarIdioma(Locale locale) {
        bundle = ResourceBundle.getBundle("i18n/Mensajes", locale);
    }

    private void actualizarTextos() {
        setTitle(bundle.getString("titulo"));
        btn_add.setText(bundle.getString("agregar"));
        btn_modificar.setText(bundle.getString("modificar"));
        btn_eliminar.setText(bundle.getString("eliminar"));
        btn_exportar.setText(bundle.getString("exportar"));
    }

    private void agregarContacto() {
        String nombre = txt_nombres.getText();
        String telefono = txt_telefono.getText();
        String email = txt_email.getText();
        String categoria = (String) cmb_categoria.getSelectedItem();
        boolean favorito = chb_favorito.isSelected();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        Contacto nuevoContacto = new Contacto(nombre, telefono, email, categoria, favorito);
        listaContactos.add(nuevoContacto);
        modeloTabla.addRow(new Object[]{nombre, telefono, email, categoria, favorito ? "Sí" : "No"});
        limpiarFormulario();
    }

    private void modificarContacto() {
        int filaSeleccionada = tabla_contactos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto para modificar.");
            return;
        }

        String nombre = txt_nombres.getText();
        String telefono = txt_telefono.getText();
        String email = txt_email.getText();
        String categoria = (String) cmb_categoria.getSelectedItem();
        boolean favorito = chb_favorito.isSelected();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        Contacto contactoModificado = new Contacto(nombre, telefono, email, categoria, favorito);
        listaContactos.set(filaSeleccionada, contactoModificado);
        modeloTabla.setValueAt(nombre, filaSeleccionada, 0);
        modeloTabla.setValueAt(telefono, filaSeleccionada, 1);
        modeloTabla.setValueAt(email, filaSeleccionada, 2);
        modeloTabla.setValueAt(categoria, filaSeleccionada, 3);
        modeloTabla.setValueAt(favorito ? "Sí" : "No", filaSeleccionada, 4);
        limpiarFormulario();
    }

    private void eliminarContacto() {
        int filaSeleccionada = tabla_contactos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto para eliminar.");
            return;
        }

        listaContactos.remove(filaSeleccionada);
        modeloTabla.removeRow(filaSeleccionada);
    }

    private void exportarCSV() {
        try (FileWriter writer = new FileWriter("contactos.csv")) {
            writer.append("Nombre,Teléfono,Email,Categoría,Favorito\n");
            for (Contacto contacto : listaContactos) {
                writer.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        contacto.getNombre(), contacto.getTelefono(), contacto.getEmail(),
                        contacto.getCategoria(), contacto.isFavorito() ? "Sí" : "No"));
            }
            JOptionPane.showMessageDialog(this, "Contactos exportados correctamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar contactos.");
        }
    }

    private void limpiarFormulario() {
        txt_nombres.setText("");
        txt_telefono.setText("");
        txt_email.setText("");
        cmb_categoria.setSelectedIndex(0);
        chb_favorito.setSelected(false);
    }

    public void addActionListener(logica_ventana aThis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static class Contacto {
        private String nombre, telefono, email, categoria;
        private boolean favorito;

        public Contacto(String nombre, String telefono, String email, String categoria, boolean favorito) {
            this.nombre = nombre;
            this.telefono = telefono;
            this.email = email;
            this.categoria = categoria;
            this.favorito = favorito;
        }

        public String getNombre() { return nombre; }
        public String getTelefono() { return telefono; }
        public String getEmail() { return email; }
        public String getCategoria() { return categoria; }
        public boolean isFavorito() { return favorito; }
    }
}