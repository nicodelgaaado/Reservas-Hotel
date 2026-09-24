# Reservas-Hotel

Sistema de reservas hoteleras desarrollado en Java 21 y Spring Boot 4.1.1 como ejercicio de Programación Orientada a Objetos.

## Organización de paquetes

Dentro de `src/main/java/com/reservas/reservas`:

```text
controller/      API REST de clientes, habitaciones y reservas; manejo de errores
domain/          Entidades y objetos de valor; antes modelo
dto/
  request/       Contratos de entrada
  response/      Contratos de salida
mapper/          Conversiones entre DTO y dominio
repository/      Contratos, entidades en memoria y confirmaciones en archivo
services/        Casos de uso y cálculo de tarifas; antes servicios
configuracion/   Composición de dependencias y propiedades de Spring
```

Cada paquete incluye código funcional y un `package-info.java` con su responsabilidad.
Los controladores validan `dto/request`, delegan en `HotelService` y devuelven
`dto/response`. `HotelMapper` convierte los datos sin consultar repositorios.
`HotelService` coordina las entidades y reutiliza `ServicioConfirmacionReservas`.
Los servicios dependen de contratos de repositorio y el dominio permanece independiente
de Spring. Los paquetes de patrones de diseño conservan su organización para facilitar
su estudio.

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

`Reservas` inicia la API HTTP en el puerto 8080 con `@SpringBootApplication`.
La demostración `DemoReservas` está desactivada por defecto para evitar crear datos al
arrancar el servidor. Puede ejecutarse explícitamente en modo consola (ver abajo).

`configuracion/ReservasConfiguration` declara los beans de Spring. Las entidades y los patrones de diseño siguen siendo clases Java independientes del framework. El procesador usa alcance `prototype` para mantener sus contadores por consumidor. También se registra `ServicioConfirmacionReservas`, con descuento por membresía, recargos, persistencia y notificaciones. Las otras políticas de descuento se conservan para combinarlas explícitamente en el calculador cuando corresponda.

La demostración guarda la confirmación mediante `ReservaRepository`, usando el formato del repositorio existente: `id|cliente|habitacion=numero|entrada|salida|tarifaFinal=importe`. Es un registro de confirmaciones en archivo; la cancelación posterior no elimina ni actualiza esa línea. Las notificaciones y la facturación siguen siendo simuladas.

## API REST

| Método | Ruta | Operación |
| --- | --- | --- |
| POST | `/api/clientes` | Registrar cliente |
| GET | `/api/clientes/{id}` | Consultar cliente |
| POST | `/api/habitaciones` | Registrar habitación estándar o suite |
| GET | `/api/habitaciones/{numero}` | Consultar habitación y disponibilidad |
| POST | `/api/reservas` | Crear reserva pendiente para un cliente y habitación existentes |
| GET | `/api/reservas/{id}` | Consultar reserva |
| POST | `/api/reservas/{id}/confirmacion` | Confirmar, guardar el registro y notificar |
| POST | `/api/reservas/{id}/cancelacion` | Cancelar una reserva confirmada y liberar la habitación |

Los POST de creación devuelven `201` y la cabecera `Location`; las consultas y
transiciones devuelven `200`. Los errores usan `application/problem+json`: `400` para
entradas inválidas, `404` para recursos inexistentes, `409` para duplicados o conflictos
de estado y `500` si falla el archivo. Un fallo de escritura deja la reserva pendiente
y permite reintentar la confirmación.

Ejemplo completo en PowerShell, con el servidor iniciado:

```powershell
$baseUri = 'http://localhost:8080/api'
Invoke-RestMethod "$baseUri/clientes" -Method Post -ContentType 'application/json' -Body '{"id":1,"nombre":"Ana","documentoIdentidad":"DOC-1","email":"ana@example.com","telefono":"3001234567"}'
Invoke-RestMethod "$baseUri/habitaciones" -Method Post -ContentType 'application/json' -Body '{"numero":101,"capacidadMaxima":2,"tipo":"ESTANDAR","numeroCamas":2}'
$solicitud = @{
    clienteId = 1
    numeroHabitacion = 101
    fechaEntrada = (Get-Date).AddDays(10).ToString('yyyy-MM-dd')
    fechaSalida = (Get-Date).AddDays(12).ToString('yyyy-MM-dd')
} | ConvertTo-Json
$reserva = Invoke-RestMethod "$baseUri/reservas" -Method Post -ContentType 'application/json' -Body $solicitud
Invoke-RestMethod "$baseUri/reservas/$($reserva.id)/confirmacion" -Method Post -ContentType 'application/json' -Body '{"tarifaBase":100000}'
Invoke-RestMethod "$baseUri/reservas/$($reserva.id)"
Invoke-RestMethod "$baseUri/reservas/$($reserva.id)/cancelacion" -Method Post
```

Para una suite, usar `tipo: "SUITE_PRESIDENCIAL"`, `amenidadesLujo: ["Jacuzzi"]` y
`servicioMayordomo: true`, omitiendo `numeroCamas`. La tarifa base es el total de la
estadía antes de descuentos y recargos; se recibe con hasta dos decimales y el dominio
existente mantiene sus cálculos con `double`. El cliente se registra con membresía REGULAR.

### Alcance del almacenamiento y disponibilidad

Los clientes, habitaciones y reservas se almacenan **en memoria**: se pierden al reiniciar
y los identificadores de reserva vuelven a empezar. El archivo solo conserva un registro
de confirmaciones, no restaura las entidades ni refleja cancelaciones. La demo usa sus
propias entidades y no carga el catálogo de la API.

La disponibilidad conserva el modelo original: una habitación confirmada queda ocupada
hasta cancelar, sin agenda de disponibilidad por rangos de fechas. Crear una reserva
pendiente no ocupa la habitación; confirmar vuelve a comprobar su disponibilidad.
Las mutaciones de la API se serializan en una sola instancia del servicio. Esta versión
académica no incluye autenticación ni almacenamiento transaccional para varias instancias.

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
| `reservas.demo.enabled` | `false` | Ejecuta la demostración al arrancar cuando se activa explícitamente |
| `reservas.archivo` | `reservas.txt` | Archivo de confirmaciones; admite la variable `RESERVAS_ARCHIVO` |
| `reservas.facturacion.nit` | `900123456-7` | NIT del adaptador; admite la variable `RESERVAS_NIT` |

Para ejecutar únicamente la demostración original en consola y terminar:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--spring.main.web-application-type=none --reservas.demo.enabled=true"
```

## Pruebas y empaquetado

```powershell
.\mvnw.cmd verify
java -jar target/Reservas-1.0-SNAPSHOT.jar
```

`verify` ejecuta las pruebas y genera un JAR ejecutable con sus dependencias. Las pruebas anteriores del modelo y los patrones se ejecutan mediante JUnit, con las aserciones habilitadas por Maven Surefire. Las pruebas de integración arrancan Spring, verifican la inyección de servicios, la confirmación y cancelación, el archivo generado y la activación/desactivación de la demostración. Utilizan directorios temporales.

Para ejecutar solo las pruebas: `.\mvnw.cmd test`.

`ReservasApiTest` inicia contextos web aislados y prueba las rutas con MockMvc y los
servicios reales: ciclo completo, tipos de habitación, validación, recursos ausentes,
duplicados, conflictos de disponibilidad y recuperación ante un fallo de escritura.

Compatibilidad de Java y Maven: [requisitos oficiales de Spring Boot](https://docs.spring.io/spring-boot/system-requirements.html).
