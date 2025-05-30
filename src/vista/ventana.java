package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controlador.logica_ventana;

/**
 * Clase "ventana" que representa la vista principal de la aplicación.
 * Maneja la interfaz gráfica de usuario (GUI) y delega las acciones al controlador.
 */
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

    /**
     * Método principal para ejecutar la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ventana frame = new ventana();
                new logica_ventana(frame); // Vincular el controlador con la vista
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructor de la clase "ventana".
     * Configura la interfaz gráfica y los componentes principales.
     */
    public ventana() {
        setTitle("Gestión de Contactos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 768);
        setResizable(false);

        configurarIdioma(new Locale("es", "ES"));

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // Panel superior para selección de idioma
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

        // Panel central para el formulario de entrada
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

        // Inicializar la tabla y el modelo
        modeloTabla = new DefaultTableModel(
                new String[]{"Nombre", "Teléfono", "Email", "Categoría", "Favorito"}, 0);
        tabla_contactos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tabla_contactos);
        contentPane.add(scrollTabla, BorderLayout.CENTER);

        // Panel inferior con botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btn_add = new JButton("Agregar");
        btn_modificar = new JButton("Modificar");
        btn_eliminar = new JButton("Eliminar");
        btn_exportar = new JButton("Exportar CSV");
        panelBotones.add(btn_add);
        panelBotones.add(btn_modificar);
        panelBotones.add(btn_eliminar);
        panelBotones.add(btn_exportar);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        actualizarTextos();
    }

    /**
     * Configura el idioma de la aplicación utilizando un Locale.
     * @param locale El idioma y región a configurar.
     */
    private void configurarIdioma(Locale locale) {
        bundle = ResourceBundle.getBundle("i18n/Mensajes", locale);
    }

    /**
     * Actualiza los textos de la interfaz gráfica según el idioma seleccionado.
     */
    private void actualizarTextos() {
        setTitle(bundle.getString("titulo"));
        btn_add.setText(bundle.getString("agregar"));
        btn_modificar.setText(bundle.getString("modificar"));
        btn_eliminar.setText(bundle.getString("eliminar"));
        btn_exportar.setText(bundle.getString("exportar"));
    }

    // Métodos getter para acceder a los campos desde el controlador
    public JTextField getTxtNombres() {
        return txt_nombres;
    }

    public JTextField getTxtTelefono() {
        return txt_telefono;
    }

    public JTextField getTxtEmail() {
        return txt_email;
    }

    public JComboBox<String> getCmbCategoria() {
        return cmb_categoria;
    }

    public JCheckBox getChbFavorito() {
        return chb_favorito;
    }

    public JTable getTablaContactos() {
        return tabla_contactos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}