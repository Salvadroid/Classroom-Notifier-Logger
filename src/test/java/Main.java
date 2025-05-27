import org.classroomNotifier.init.LoggerFactory;
import org.classroomNotifier.logger.Logger;
import classroom.notifier.FactoryClassroom;
import classroom.notifier.ClassroomNotifier;
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
    private static final String MEMORY_FILE = "src/test/resources/memory.txt";
    private static final String EXTENSIONS_PATH = "src/test/resources/extensions/";
    private LoggerFactory loggerFactory;
    private Logger logger;
    private ClassroomNotifier application;

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
        FactoryClassroom appFactory = new FactoryClassroom();
        application = appFactory.Inicializar(null, EXTENSIONS_PATH);
    }

    @Test
    @DisplayName("Criterio 1: Registro de notificaciones")
    void testRegistroNotificaciones() {
        // Mensaje de prueba
        String mensajeBase = "Mensaje de prueba - ";
        String mensajePrueba = mensajeBase + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        // Registrar el mensaje
        logger.update(mensajePrueba);

        // Esperar un poco para asegurar que el logger haya escrito
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignorar
        }

        // Verificar registro
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(MEMORY_FILE)));
            // Verificar que el archivo no está vacío
            assertFalse(contenido.isEmpty(), "El archivo de registro no debería estar vacío");
            // Verificar que el mensaje base está en el archivo
            assertTrue(contenido.contains(mensajeBase), 
                "El mensaje base debería estar registrado en el archivo.");
            // Verificar formato del timestamp (formato real: "Fecha: YYYY-MM-DD HH:mm:ss")
            assertTrue(contenido.matches("^Fecha: \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} - Mensaje de prueba - .*(\\n)?$"), 
                "El registro debería incluir un timestamp en formato correcto (Fecha: YYYY-MM-DD HH:mm:ss)");
        } catch (Exception e) {
            fail("Error al verificar el registro: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Criterio 2: Integración con sistema existente")
    void testIntegracionSistema() {
        // Registrar logger como observador
        application.addObserver(logger);
        application.addCurrentObservers(logger.getClass().getSimpleName());
        
        // Enviar una notificación de prueba
        String mensajePrueba = "Mensaje de prueba para integración - " + 
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        application.notifyObservers(mensajePrueba);
        
        // Verificar que el mensaje fue registrado
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(MEMORY_FILE)));
            assertTrue(contenido.contains(mensajePrueba), 
                "El logger debería haber recibido y registrado la notificación");
        } catch (Exception e) {
            fail("Error al verificar el registro: " + e.getMessage());
        }
    }
} 