import classroom.notifier.aplicacion.ClassroomNotifier;
import classroom.notifier.inicializacion.FactoryClassroom;
import org.classroomNotifier.init.LoggerFactory;
import org.classroomNotifier.logger.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;

public class Main {
    private static final String MEMORY_FILE = "log.txt";
    private static final String EXTENSIONS_PATH = "src/test/resources/extensions/";
    private LoggerFactory loggerFactory;
    private Logger logger;
    private ClassroomNotifier application;
    private DataFromFile  data;
    @BeforeEach
    void setUp() throws FileNotFoundException {
        // Limpiar el archivo de memoria antes de cada test
        try {
            Files.write(Paths.get(MEMORY_FILE), "".getBytes());
        } catch (Exception e) {
            fail("Error al limpiar el archivo de memoria: " + e.getMessage());
        }

        // Inicializar logger
        loggerFactory = LoggerFactory.getInstance(MEMORY_FILE);
        logger = loggerFactory.createLogger();

        // Inicializar sistema principal
        data = new DataFromFile("stockActual.json");
        FactoryClassroom appFactory = new FactoryClassroom();
        application = appFactory.crear(data, EXTENSIONS_PATH);
        data.enviar();
    }


    @Test
    @DisplayName("Criterio 1: Registro de notificaciones")
    void testRegistroNotificaciones() {
        // Mensaje de prueba
        String mensajeBase = "Se modifico el aula de la materia 002 PP2, la nueva aula";
        //String mensajePrueba = mensajeBase + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        application.addObserver(logger);
        application.addCurrentObservers("Logger");
        data.updateFile("stockActualizado.json");
        data.enviar();

        // Verificar registro
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(MEMORY_FILE)));
             // Verificar que el mensaje base está en el archivo
            assertTrue(contenido.contains(mensajeBase), 
                "El mensaje base debería estar registrado en el archivo.");

        } catch (Exception e) {
            fail("Error al verificar el registro: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Criterio 2: Logger no registrado como observer (caso negativo)")
    void testLoggerNoRegistradoComoObserver() {
        // NO registrar el logger como observador intencionalmente
        // application.addObserver(logger); <- Esta línea comentada intencionalmente
        
        // Obtener contenido inicial del archivo
        String contenidoInicial = "";
        try {
            contenidoInicial = new String(Files.readAllBytes(Paths.get(MEMORY_FILE)));
        } catch (Exception e) {
            fail("Error al leer el archivo inicial: " + e.getMessage());
        }
        
        // Enviar una notificación de prueba sin el logger registrado
        String mensajePrueba = "Mensaje que NO debería registrarse - " + 
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        // El sistema debería funcionar normalmente aunque el logger no esté registrado
        assertDoesNotThrow(() -> {
            application.notifyObservers(mensajePrueba);
        }, "El sistema debería funcionar normalmente sin el logger registrado");
        
        // Esperar un poco para asegurar que no se escriba nada
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignorar
        }
        
        // Verificar que el mensaje NO fue registrado
        try {
            String contenidoFinal = new String(Files.readAllBytes(Paths.get(MEMORY_FILE)));
            
            // El archivo debería permanecer igual (vacío o sin el nuevo mensaje)
            assertEquals(contenidoInicial, contenidoFinal, 
                "El contenido del archivo no debería cambiar cuando el logger no está registrado");
            
            // Verificar específicamente que el mensaje de prueba no está presente
            assertFalse(contenidoFinal.contains(mensajePrueba), 
                "El mensaje no debería estar registrado cuando el logger no es un observer");
                
        } catch (Exception e) {
            fail("Error al verificar que el mensaje no fue registrado: " + e.getMessage());
        }
    }
} 