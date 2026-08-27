package com.reservas.reservas.creacionales.singleton;
import java.util.concurrent.atomic.AtomicLong;
public final class GeneradorFolioFiscal {
    private final AtomicLong contador = new AtomicLong();
    private GeneradorFolioFiscal() {}
    private static class Holder { private static final GeneradorFolioFiscal INSTANCE = new GeneradorFolioFiscal(); }
    public static GeneradorFolioFiscal getInstance() { return Holder.INSTANCE; }
    public String generarFolio() { return "FAC-2026-%05d".formatted(contador.incrementAndGet()); }
}
