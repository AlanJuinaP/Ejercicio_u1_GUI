package modelo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// Clase para manejar operaciones relacionadas con la persistencia de datos de "persona".
public class personaDAO {
    private persona contacto; // Objeto persona que será manipulado.

    // Constructor que recibe un objeto persona.
    public personaDAO(persona contacto) {
        this.contacto = contacto;
    }

    // Método para escribir un contacto en un archivo.
    public void escribirArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contactos.txt", true))) {
            writer.write(contacto.datosContacto());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para leer todos los contactos del archivo.
    public List<persona> leerArchivo() throws IOException {
        // Implementación existente para leer contactos.
        return null; // Reemplazar con la lectura real del archivo.
    }

    // Método para exportar contactos a un archivo CSV.
    public void exportarCSV(List<persona> contactos, String rutaArchivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            // Escribir encabezados en el archivo CSV.
            writer.write("Nombre,Teléfono,Email,Categoría,Favorito");
            writer.newLine();

            // Escribir cada contacto como una fila en el archivo CSV.
            for (persona contacto : contactos) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        contacto.getNombre(),
                        contacto.getTelefono(),
                        contacto.getEmail(),
                        contacto.getCategoria(),
                        contacto.isFavorito() ? "Sí" : "No"));
                writer.newLine();
            }
        }
    }
}