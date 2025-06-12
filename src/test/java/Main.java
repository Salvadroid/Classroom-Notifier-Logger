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
    
    @Test
    @DisplayName("Criterio extra: Integración exitosa con sistema existente")
    void testIntegracionExitosaSistemaExistente() {
        // Registrar logger como observador
        application.addObserver(logger);
        application.addCurrentObservers(logger.getClass().getSimpleName());
        
        // Enviar una notificación de prueba
        String mensajePrueba = "Se modificó el aula 118 a aula 205 - " + 
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        application.notifyObservers(mensajePrueba);
        
        // Esperar un poco para asegurar que el logger haya escrito
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignorar
        }
        
        // Verificar que el mensaje fue registrado correctamente
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(MEMORY_FILE)));
            
            // Verificar que el archivo no está vacío
            assertFalse(contenido.isEmpty(), "El archivo debería contener el mensaje registrado");
            
            // Verificar que contiene el mensaje
            assertTrue(contenido.contains("Se modificó el aula 118 a aula 205"), 
                "El logger debería haber recibido y registrado la notificación de cambio de aula");
                
            // Verificar formato del timestamp
            assertTrue(contenido.matches("^Fecha: \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} - Se modificó el aula 118 a aula 205 - .*(\\n)?$"), 
                "El registro debería incluir un timestamp en formato correcto");
                
        } catch (Exception e) {
            fail("Error al verificar el registro de integración: " + e.getMessage());
        }
    }
} 