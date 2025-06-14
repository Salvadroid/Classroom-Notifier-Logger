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
        String mensajeBase = "Se modifico el aula de la materia 002 PP2, la nueva aula es 7011";
        //String mensajePrueba = mensajeBase + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        application.addObserver(logger);
        application.addCurrentObservers("Logger");
        data.updateFile("stockActualizadoTest1.json");
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
        String mensajePrueba = "Se modifico el aula de la materia 001 Matematicas, la nueva aula es 7015";

        data.updateFile("stockActualizadoTest2.json");
        data.enviar();
        
        // Verificar que el mensaje NO fue registrado
        try {
            String contenidoFinal = new String(Files.readAllBytes(Paths.get(MEMORY_FILE)));

            assertFalse(contenidoFinal.contains(mensajePrueba),
                    "El mensaje base debería estar registrado en el archivo.");

        } catch (Exception e) {
            fail("Error al verificar que el mensaje no fue registrado: " + e.getMessage());
        }
    }
} 