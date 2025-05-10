package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para manejar operaciones relacionadas con la persistencia de datos de "persona".
 */
public class personaDAO {
    private final List<persona> contactos; // Lista de contactos en memoria

    /**
     * Constructor de la clase personaDAO.
     * Carga los contactos desde el archivo al iniciar.
     */
    public personaDAO() {
        this.contactos = new ArrayList<>();
        cargarContactosDesdeArchivo();
    }

    /**
     * Verifica si un contacto ya existe en la lista.
     * @param contacto El contacto a verificar.
     * @return true si el contacto existe, false en caso contrario.
     */
    public boolean existeContacto(persona contacto) {
        return contactos.stream().anyMatch(c -> c.getNombre().equals(contacto.getNombre()) &&
                c.getTelefono().equals(contacto.getTelefono()) &&
                c.getEmail().equals(contacto.getEmail()));
    }

    /**
     * Guarda un nuevo contacto en la lista y en el archivo.
     * @param contacto El contacto a guardar.
     */
    public void guardarContacto(persona contacto) {
        contactos.add(contacto);
        escribirContactosEnArchivo();
    }

    /**
     * Modifica un contacto existente en la lista.
     * @param contacto El contacto modificado.
     */
    public void modificarContacto(persona contacto) {
        for (int i = 0; i < contactos.size(); i++) {
            if (contactos.get(i).getNombre().equals(contacto.getNombre())) {
                contactos.set(i, contacto);
                break;
            }
        }
        escribirContactosEnArchivo();
    }

    /**
     * Elimina un contacto de la lista basado en el índice.
     * @param indice El índice del contacto a eliminar.
     */
    public void eliminarContacto(int indice) {
        if (indice >= 0 && indice < contactos.size()) {
            contactos.remove(indice);
            escribirContactosEnArchivo();
        }
    }

    /**
     * Exporta los contactos a un archivo CSV.
     * @param rutaArchivo La ruta donde se guardará el archivo CSV.
     */
    public void exportarCSV(String rutaArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write("Nombre,Teléfono,Email,Categoría,Favorito");
            writer.newLine();
            for (persona c : contactos) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        c.getNombre(), c.getTelefono(), c.getEmail(),
                        c.getCategoria(), c.isFavorito() ? "Sí" : "No"));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga los contactos desde un archivo al iniciar.
     */
    private void cargarContactosDesdeArchivo() {
        File archivo = new File("contactos.txt");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 5) {
                    persona contacto = new persona(
                            datos[0], datos[1], datos[2], datos[3], Boolean.parseBoolean(datos[4]));
                    contactos.add(contacto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escribe los contactos actuales en el archivo.
     */
    private void escribirContactosEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contactos.txt"))) {
            for (persona c : contactos) {
                writer.write(String.format("%s;%s;%s;%s;%s",
                        c.getNombre(), c.getTelefono(), c.getEmail(),
                        c.getCategoria(), c.isFavorito()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}