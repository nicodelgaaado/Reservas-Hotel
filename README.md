# Reservas-Hotel

Sistema de reservas hoteleras desarrollado en Java como ejercicio de Programación Orientada a Objetos.

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

## Ejecución

```bash
mvn compile exec:java
```

Sin Maven también se puede compilar con el JDK:

```powershell
$fuentes = Get-ChildItem -Recurse -Filter *.java src/main/java | ForEach-Object FullName
javac -encoding UTF-8 -d target/classes $fuentes
java -cp target/classes com.reservas.reservas.Reservas
```

## Pruebas sin dependencias externas

```powershell
$fuentes = Get-ChildItem -Recurse -Filter *.java src/main/java,src/test/java | ForEach-Object FullName
javac -encoding UTF-8 -d target/test-classes $fuentes
java -ea -cp target/test-classes com.reservas.reservas.PruebaPatronesComportamentales
java -ea -cp target/test-classes com.reservas.reservas.PruebaModeloHotel
```
