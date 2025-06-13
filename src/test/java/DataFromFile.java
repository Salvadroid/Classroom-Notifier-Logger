import classroom.notifier.aplicacion.ClassroomMateriaAula;
import classroom.notifier.interfaces.InformadorDatos;
import classroom.notifier.interfaces.Observer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataFromFile implements InformadorDatos {
    private String archivosOrigenMaterias;
    private Set<Observer> observador;
    public DataFromFile(String archivosOrigenMaterias){
        this.archivosOrigenMaterias = archivosOrigenMaterias;
        this.observador = new HashSet<>();

    }
    @Override
    public void enviar() {

        Map<String, String> resultado = new HashMap<String,String>();
        try{
            String content = ReadFile(archivosOrigenMaterias);

        // Crear instancia de ObjectMapper
            JSONObject json = new JSONObject(content);
            JSONArray materias = json.getJSONArray("materias");
            // Recorrer los elementos del JSONArray
            for (int i = 0; i < materias.length(); i++) {
                JSONObject amigo = materias.getJSONObject(i);
                String nombre = amigo.getString("nombre");
                String aula = amigo.getString("aula");
                resultado.put(nombre,aula);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!resultado.isEmpty())
            observador.forEach(o -> o.update(new ClassroomMateriaAula(resultado)));

    }

    @Override
    public void suscribir(Object o) {
        observador.add((Observer) o);
    }


    private String ReadFile(String file){
        String content = "";
        try{
            content = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/"+file)));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return content;

    }

    public void updateFile(String json){
        archivosOrigenMaterias = json;
    }
}
