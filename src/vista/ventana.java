package vista;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;

public class ventana extends JFrame {

    public JPanel contentPane;
    public JTextField txt_nombres;
    public JTextField txt_telefono;
    public JTextField txt_email;
    public JCheckBox chb_favorito;
    public JComboBox<String> cmb_categoria;
    public JButton btn_add;
    public JButton btn_modificar;
    public JButton btn_eliminar;
    public JButton btn_exportar;
    public JTable tabla_contactos;

    private JComboBox<String> cmb_idiomas; // Selector de idioma
    private ResourceBundle bundle;

    private JLabel lblNombres, lblTelefono, lblEmail, lblCategoria;

    /**
     * Launch the application.
     */
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

    /**
     * Create the frame.
     */
    public ventana() {
        // Configuración básica de la ventana
        setTitle("Gestión de Contactos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 768);
        setResizable(false);

        // Configuración del idioma inicial (Español por defecto)
        configurarIdioma(new Locale("es", "ES"));

        // Panel principal
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Color.LIGHT_GRAY);
        setContentPane(contentPane);

        // Panel superior: Selector de idioma
        JPanel panelIdiomas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelIdiomas.setBackground(Color.LIGHT_GRAY);
        JLabel lblIdioma = new JLabel("Idioma:");
        cmb_idiomas = new JComboBox<>(new String[] { "Español", "English", "Português" });
        cmb_idiomas.setSelectedIndex(0);
        cmb_idiomas.addActionListener(e -> {
            String idiomaSeleccionado = (String) cmb_idiomas.getSelectedItem();
            if (idiomaSeleccionado.equals("Español")) {
                configurarIdioma(new Locale("es", "ES"));
            } else if (idiomaSeleccionado.equals("English")) {
                configurarIdioma(new Locale("en", "US"));
            } else if (idiomaSeleccionado.equals("Português")) {
                configurarIdioma(new Locale("pt", "BR"));
            }
            actualizarTextos();
        });
        panelIdiomas.add(lblIdioma);
        panelIdiomas.add(cmb_idiomas);
        contentPane.add(panelIdiomas, BorderLayout.NORTH);

        // Panel central: Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        contentPane.add(panelFormulario, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo: Nombres
        lblNombres = new JLabel("Nombres:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(lblNombres, gbc);

        txt_nombres = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelFormulario.add(txt_nombres, gbc);

        // Campo: Teléfono
        lblTelefono = new JLabel("Teléfono:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(lblTelefono, gbc);

        txt_telefono = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelFormulario.add(txt_telefono, gbc);

        // Campo: Email
        lblEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(lblEmail, gbc);

        txt_email = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelFormulario.add(txt_email, gbc);

        // Campo: Categoría
        lblCategoria = new JLabel("Categoría:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(lblCategoria, gbc);

        cmb_categoria = new JComboBox<>(new String[] { "Familia", "Amigos", "Trabajo" });
        gbc.gridx = 1;
        gbc.gridy = 3;
        panelFormulario.add(cmb_categoria, gbc);

        // CheckBox: Favorito
        chb_favorito = new JCheckBox("Favorito");
        chb_favorito.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panelFormulario.add(chb_favorito, gbc);

        // Panel inferior: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(Color.LIGHT_GRAY);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        btn_add = new JButton("Agregar");
        panelBotones.add(btn_add);

        btn_modificar = new JButton("Modificar");
        panelBotones.add(btn_modificar);

        btn_eliminar = new JButton("Eliminar");
        panelBotones.add(btn_eliminar);

        btn_exportar = new JButton("Exportar CSV");
        panelBotones.add(btn_exportar);

        // Actualizar textos iniciales
        actualizarTextos();
    }

    /**
     * Configurar el idioma de la interfaz.
     */
    private void configurarIdioma(Locale locale) {
        bundle = ResourceBundle.getBundle("i18n/Mensajes", locale);
    }

    /**
     * Actualizar los textos de la interfaz según el idioma seleccionado.
     */
    private void actualizarTextos() {
        // Cambiar el título de la ventana
        setTitle(bundle.getString("titulo"));

        // Actualizar textos de las etiquetas del formulario
        lblNombres.setText(bundle.getString("nombres"));
        lblTelefono.setText(bundle.getString("telefono"));
        lblEmail.setText(bundle.getString("email"));
        lblCategoria.setText(bundle.getString("categoria"));
        chb_favorito.setText(bundle.getString("favorito"));

        // Actualizar textos de los botones
        btn_add.setText(bundle.getString("agregar"));
        btn_modificar.setText(bundle.getString("modificar"));
        btn_eliminar.setText(bundle.getString("eliminar"));
        btn_exportar.setText(bundle.getString("exportar"));
    }
}