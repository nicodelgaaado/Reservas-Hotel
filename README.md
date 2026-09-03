# Reservas-Hotel

Sistema de reservas hoteleras desarrollado en Java como ejercicio de Programación Orientada a Objetos.

## Patrones incluidos

- Creacionales: Builder, Factory Method y Singleton.
- Estructurales: Adapter y Decorator.
- Comportamentales: State, Strategy y Observer.

La entidad `Reserva` delega su ciclo de vida a estados polimórficos, recibe dinámicamente una estrategia de cancelación y publica la cancelación a observadores independientes. `GestorHabitaciones` libera la habitación y `NotificadorCliente` simula el correo de cancelación.

El diagrama UML actualizado está en [`docs/diagrama-clases.puml`](docs/diagrama-clases.puml).

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
```
