package modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar la lista de personas y su persistencia.
 */
public class personaDAO {
    private final List<persona> contactos = new ArrayList<>();
    private final String archivoTexto = "contactos.txt";

    /**
     * Constructor: carga los contactos desde el archivo al iniciar.
     */
    public personaDAO() {
        cargarContactosDesdeArchivo();
    }

    /**
     * Devuelve una copia de la lista de contactos.
     */
    public List<persona> getContactos() {
        return new ArrayList<>(contactos);
    }

    /**
     * Verifica si un contacto ya existe en la lista (por nombre, teléfono y email).
     */
    public synchronized boolean existeContacto(persona contacto) {
        return contactos.stream().anyMatch(c ->
            c.getNombre().equalsIgnoreCase(contacto.getNombre())
            && c.getTelefono().equals(contacto.getTelefono())
            && c.getEmail().equalsIgnoreCase(contacto.getEmail())
        );
    }

    /**
     * Agrega un contacto a la lista y actualiza el archivo de texto.
     */
    public synchronized void guardarContacto(persona contacto) {
        contactos.add(contacto);
        escribirContactosEnArchivo();
    }

    /**
     * Modifica un contacto existente por índice y actualiza el archivo.
     */
    public synchronized void modificarContacto(int indice, persona contacto) {
        if (indice >= 0 && indice < contactos.size()) {
            contactos.set(indice, contacto);
            escribirContactosEnArchivo();
        }
    }

    /**
     * Elimina un contacto por índice y actualiza el archivo.
     */
    public synchronized void eliminarContacto(int indice) {
        if (indice >= 0 && indice < contactos.size()) {
            contactos.remove(indice);
            escribirContactosEnArchivo();
        }
    }

    /**
     * Carga los contactos desde el archivo de texto al iniciar la aplicación.
     */
    private void cargarContactosDesdeArchivo() {
        File archivo = new File(archivoTexto);
        if (!archivo.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 5) {
                    persona contacto = new persona(
                        datos[0], datos[1], datos[2], datos[3], Boolean.parseBoolean(datos[4])
                    );
                    contactos.add(contacto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escribe la lista actual de contactos en el archivo de texto.
     */
    private void escribirContactosEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTexto))) {
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

    // Funciones para manejo de JSON con Gson

    /**
     * Exporta todos los contactos a un archivo JSON.
     */
    public synchronized void exportarContactosJSON(String archivo) throws IOException {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(archivo)) {
            gson.toJson(this.contactos, writer);
        }
    }

    /**
     * Importa contactos desde un archivo JSON externo y reemplaza la lista actual.
     */
    public synchronized void importarContactosJSON(String archivo) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(archivo)) {
            List<persona> lista = gson.fromJson(reader, new TypeToken<List<persona>>(){}.getType());
            if (lista != null) {
                this.contactos.clear();
                this.contactos.addAll(lista);
                escribirContactosEnArchivo();
            }
        }
    }
}