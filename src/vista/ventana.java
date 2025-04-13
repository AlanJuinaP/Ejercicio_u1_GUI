package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import controlador.logica_ventana;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class ventana extends JFrame {

    public JPanel contentPane; // Panel principal que contendrá todos los componentes de la interfaz.
    public JTextField txt_nombres; // Campo de texto para ingresar nombres.
    public JTextField txt_telefono; // Campo de texto para ingresar números de teléfono.
    public JTextField txt_email; // Campo de texto para ingresar direcciones de correo electrónico.
    public JTextField txt_buscar; // Campo de texto adicional.
    public JCheckBox chb_favorito; // Casilla de verificación para marcar un contacto como favorito.
    public JComboBox cmb_categoria; // Menú desplegable para seleccionar la categoría de contacto.
    public JButton btn_add; // Botón para agregar un nuevo contacto.
    public JButton btn_modificar; // Botón para modificar un contacto existente.
    public JButton btn_eliminar; // Botón para eliminar un contacto.
    public JButton btn_exportar; // Botón para exportar contactos a CSV.
    public JTable tabla_contactos; // Tabla para visualizar contactos.
    public JScrollPane scrTabla; // Panel de desplazamiento para la tabla de contactos.

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ventana frame = new ventana();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ventana() {
        setTitle("GESTION DE CONTACTOS"); // Establece el título de la ventana.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define el comportamiento al cerrar la ventana.
        setResizable(false); // Evita que la ventana sea redimensionable.
        setBounds(100, 100, 1026, 800); // Establece el tamaño y la posición inicial de la ventana.
        contentPane = new JPanel(); // Crea un nuevo panel de contenido.
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Establece un borde vacío alrededor del panel.

        setContentPane(contentPane); // Establece el panel de contenido como el panel principal de la ventana.
        contentPane.setLayout(null); // Configura el diseño del panel como nulo para posicionar manualmente los componentes.

        // Creación del JTabbedPane para organizar la interfaz en pestañas.
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(10, 10, 990, 740);
        contentPane.add(tabbedPane);

        // Pestaña de Contactos
        JPanel panelContactos = new JPanel();
        panelContactos.setLayout(null);
        tabbedPane.addTab("Contactos", null, panelContactos, "Gestión de Contactos");

        // Componentes de la pestaña de contactos.
        JLabel lbl_etiqueta1 = new JLabel("NOMBRES:");
        lbl_etiqueta1.setBounds(25, 30, 100, 25);
        lbl_etiqueta1.setFont(new Font("Tahoma", Font.BOLD, 15));
        panelContactos.add(lbl_etiqueta1);

        txt_nombres = new JTextField();
        txt_nombres.setBounds(140, 30, 400, 25);
        txt_nombres.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panelContactos.add(txt_nombres);

        JLabel lbl_etiqueta2 = new JLabel("TELEFONO:");
        lbl_etiqueta2.setBounds(25, 70, 100, 25);
        lbl_etiqueta2.setFont(new Font("Tahoma", Font.BOLD, 15));
        panelContactos.add(lbl_etiqueta2);

        txt_telefono = new JTextField();
        txt_telefono.setBounds(140, 70, 400, 25);
        txt_telefono.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panelContactos.add(txt_telefono);

        JLabel lbl_etiqueta3 = new JLabel("EMAIL:");
        lbl_etiqueta3.setBounds(25, 110, 100, 25);
        lbl_etiqueta3.setFont(new Font("Tahoma", Font.BOLD, 15));
        panelContactos.add(lbl_etiqueta3);

        txt_email = new JTextField();
        txt_email.setBounds(140, 110, 400, 25);
        txt_email.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panelContactos.add(txt_email);

        JLabel lbl_categoria = new JLabel("CATEGORIA:");
        lbl_categoria.setFont(new Font("Tahoma", Font.BOLD, 15));
        lbl_categoria.setBounds(25, 150, 120, 25);
        panelContactos.add(lbl_categoria);

        cmb_categoria = new JComboBox<>();
        cmb_categoria.setFont(new Font("Tahoma", Font.PLAIN, 15));
        cmb_categoria.setBounds(140, 150, 180, 25);
        panelContactos.add(cmb_categoria);

        String[] categorias = { "Elija una Categoria", "Familia", "Amigos", "Trabajo" };
        for (String categoria : categorias)
            cmb_categoria.addItem(categoria);

        chb_favorito = new JCheckBox("CONTACTO FAVORITO");
        chb_favorito.setBounds(340, 150, 200, 25);
        chb_favorito.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panelContactos.add(chb_favorito);

        btn_add = new JButton("AGREGAR");
        btn_add.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_add.setBounds(600, 30, 120, 40);
        panelContactos.add(btn_add);

        btn_modificar = new JButton("MODIFICAR");
        btn_modificar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_modificar.setBounds(730, 30, 120, 40);
        panelContactos.add(btn_modificar);

        btn_eliminar = new JButton("ELIMINAR");
        btn_eliminar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_eliminar.setBounds(860, 30, 120, 40);
        panelContactos.add(btn_eliminar);

        // Agregar botón de exportar contactos a CSV.
        btn_exportar = new JButton("EXPORTAR CSV");
        btn_exportar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_exportar.setBounds(860, 100, 120, 40);
        panelContactos.add(btn_exportar);

        // JTable para mostrar los contactos.
        tabla_contactos = new JTable();
        tabla_contactos.setFont(new Font("Tahoma", Font.PLAIN, 15)); // Corrección aplicada aquí
        tabla_contactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrTabla = new JScrollPane(tabla_contactos);
        scrTabla.setBounds(25, 200, 950, 450);
        panelContactos.add(scrTabla);

        // Pestaña de Estadísticas
        JPanel panelEstadisticas = new JPanel();
        tabbedPane.addTab("Estadísticas", null, panelEstadisticas, "Estadísticas de Contactos");
        panelEstadisticas.setLayout(null);

        JLabel lblEstadisticas = new JLabel("Sección de estadísticas");
        lblEstadisticas.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblEstadisticas.setBounds(10, 10, 400, 30);
        panelEstadisticas.add(lblEstadisticas);

        // Instanciar el controlador para usar el delegado
        logica_ventana lv = new logica_ventana(this);
    }
}