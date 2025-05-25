# Classroom Notifier Logger

## Introducción
El Classroom Notifier Logger es una aplicación Java diseñada para registrar y monitorear las notificaciones enviadas por una aplicación principal de notificaciones de aula. Utilizando el patrón Observer, esta aplicación se integra con el sistema existente para capturar y almacenar todas las notificaciones generadas, proporcionando un registro histórico detallado de las interacciones del sistema.

## Descripción del Proyecto
El sistema está compuesto por varios componentes clave:
- **LoggerFactory**: Implementa el patrón Singleton para gestionar la creación de instancias del logger.
- **Logger**: Implementa el patrón Observer para recibir y procesar las notificaciones.
- **Writer**: Maneja la escritura de logs en un archivo de memoria con timestamps.
- **Componentes de Test**: Incluye SourceLogger y TimerLogger para validar el funcionamiento del sistema.

La aplicación está diseñada para ser no intrusiva, permitiendo su integración con el sistema existente sin afectar su funcionamiento principal.

## User Story
**Como** profesor del aula  
**Quiero** registrar todas las notificaciones enviadas a los estudiantes  
**Para** tener un historial completo de las comunicaciones y poder auditar el sistema cuando sea necesario

### Contexto
- El sistema principal de notificaciones ya está implementado y funcionando
- Las notificaciones pueden ser de diferentes tipos (recordatorios, anuncios, alertas)
- Es necesario mantener un registro temporal de todas las notificaciones
- El registro debe ser accesible para consultas posteriores

### Criterios de Aceptación

1. **Registro de Notificaciones**
   - **Dado que** el sistema de notificaciones envía un mensaje
   - **Cuando** el logger está activo y configurado
   - **Entonces** el mensaje debe ser registrado en el archivo memory.txt
   - **Y** debe incluir un timestamp preciso
   - **Y** debe mantener el formato especificado (YYYY-MM-DD HH:mm:ss - Message)

2. **Integración con Sistema Existente**
   - **Dado que** el sistema principal está en funcionamiento
   - **Cuando** se inicia el logger
   - **Entonces** debe registrarse correctamente como observador
   - **Y** debe recibir todas las notificaciones sin interrumpir el flujo normal del sistema
   - **Y** debe poder ser desvinculado del sistema sin afectar su funcionamiento

## Estructura del Proyecto
```
src/
├── main/java/org/classroomNotifier/
│   ├── init/
│   │   └── LoggerFactory.java
│   └── logger/
│       ├── Logger.java
│       └── Writer.java
└── test/
    ├── java/
    │   ├── Main.java
    │   ├── SourceLogger.java
    │   └── TimerLogger.java
    └── resources/
        └── memory.txt
```

## Tecnologías Utilizadas
- Java
- Patrón Observer
- Patrón Singleton
- Java Timer para pruebas
- Sistema de logging basado en archivos
