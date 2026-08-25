package com.reservas.reservas.utilidades;

import java.util.concurrent.ThreadLocalRandom;

public class IdRandomizer {
    public static int generar() { return ThreadLocalRandom.current().nextInt(1, 1_000_000); }
}
