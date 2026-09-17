# Reservas-Hotel

Sistema de reservas hoteleras desarrollado en Java 21 y Spring Boot 3.5.16 como ejercicio de Programación Orientada a Objetos.

## Patrones incluidos

- Creacionales: Builder, Factory Method y Singleton.
- Estructurales: Adapter y Decorator.
- Comportamentales: State, Strategy y Observer.

La entidad `Reserva` delega su ciclo de vida a estados polimórficos, recibe dinámicamente una estrategia de cancelación y publica la cancelación a observadores independientes. `GestorHabitaciones` libera la habitación y `NotificadorCliente` simula el correo de cancelación.

El diagrama UML actualizado está en [`docs/diagrama-clases.puml`](docs/diagrama-clases.puml).

## Modelo de habitaciones y reservas

`Habitacion` es abstracta: `HabitacionEstandar` añade un número positivo de camas y `SuitePresidencial` contiene amenidades de lujo y la disponibilidad de servicio de mayordomo. Cada habitación expone una copia no modificable de su historial; crear una `ReservaHabitacion` la registra automáticamente y cancelarla conserva ese registro.

Toda `Reserva` contiene un cliente, un estado dinámico y un `RangoFechas` inmutable con salida posterior a la entrada. `getEstadia()` es la única fuente de las fechas. Los constructores antiguos con una sola fecha representan una noche; `setFechaInicio()` reemplaza el rango conservando su duración.

`agregarPoliticaRecargo()` incorpora implementaciones de `PoliticaRecargo`; `getPoliticasRecargo()` devuelve una copia no modificable. Ambos flujos de cálculo de tarifas aplican estos recargos después de los descuentos, sumándolos sobre la misma base sin capitalización. `calcularTotalConRecargos()` permite consultar el resultado sin modificar el total; la confirmación guarda el importe final.

## Spring Boot

`Reservas` inicia el contexto con `@SpringBootApplication`. `DemoReservas` implementa `CommandLineRunner` y recibe por constructor el procesador, el repositorio, el facturador y el canal de notificación. Al arrancar ejecuta la demostración original de confirmación, facturación y cancelación, y termina: sigue siendo una aplicación de consola, sin servidor HTTP.

`configuracion/ReservasConfiguration` declara los beans de Spring. Las entidades y los patrones de diseño siguen siendo clases Java independientes del framework. El procesador usa alcance `prototype` para mantener sus contadores por consumidor. También se registra `ServicioConfirmacionReservas`, con descuento por membresía, recargos, persistencia y notificaciones. Las otras políticas de descuento se conservan para combinarlas explícitamente en el calculador cuando corresponda.

La demostración guarda la confirmación mediante `ReservaRepository`, usando el formato del repositorio existente: `id|cliente|habitacion=numero|entrada|salida|tarifaFinal=importe`. Es un registro de confirmaciones en archivo; la cancelación posterior no elimina ni actualiza esa línea. Las notificaciones y la facturación siguen siendo simuladas.

## Requisitos y ejecución

Se requiere JDK 21. Se incluye Maven Wrapper, por lo que no es necesario instalar Maven. La primera ejecución necesita conexión para descargar Maven y las dependencias.

En Windows (PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
sh mvnw spring-boot:run
```

Si tienes Maven instalado, también puedes usar `mvn spring-boot:run`.

## Configuración

Los valores están en `src/main/resources/application.properties`:

| Propiedad | Valor predeterminado | Uso |
| --- | --- | --- |
| `reservas.demo.enabled` | `true` | Ejecuta la demostración al arrancar |
| `reservas.archivo` | `reservas.txt` | Archivo de confirmaciones; admite la variable `RESERVAS_ARCHIVO` |
| `reservas.facturacion.nit` | `900123456-7` | NIT del adaptador; admite la variable `RESERVAS_NIT` |

Para iniciar únicamente el contexto, sin ejecutar la demostración ni escribir reservas:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--reservas.demo.enabled=false"
```

## Pruebas y empaquetado

```powershell
.\mvnw.cmd verify
java -jar target/Reservas-1.0-SNAPSHOT.jar
```

`verify` ejecuta las pruebas y genera un JAR ejecutable con sus dependencias. Las pruebas anteriores del modelo y los patrones se ejecutan mediante JUnit, con las aserciones habilitadas por Maven Surefire. Las pruebas de integración arrancan Spring, verifican la inyección de servicios, la confirmación y cancelación, el archivo generado y la activación/desactivación de la demostración. Utilizan directorios temporales.

Para ejecutar solo las pruebas: `.\mvnw.cmd test`.

Compatibilidad de Java y Maven: [requisitos oficiales de Spring Boot 3.5](https://docs.spring.io/spring-boot/3.5/system-requirements.html).