package com.reservas.reservas.estructurales.adapter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import com.reservas.reservas.modelo.Reserva;
public final class DianInvoiceAdapter implements FacturadorElectronico {
    private final DianInvoiceSDK sdk; private final String nitEmpresa;
    public DianInvoiceAdapter(String nitEmpresa) { this(new DianInvoiceSDK(), nitEmpresa); }
    public DianInvoiceAdapter(DianInvoiceSDK sdk, String nitEmpresa) { this.sdk=Objects.requireNonNull(sdk); this.nitEmpresa=Objects.requireNonNull(nitEmpresa); }
    public String emitirFactura(Reserva reserva, double total) { Objects.requireNonNull(reserva); if(total<0) throw new IllegalArgumentException("El total no puede ser negativo"); String xml="<factura reserva='"+reserva.getId()+"' cliente='"+reserva.getCliente().getNombre()+"' total='"+total+"'/>"; return "DIAN-"+sdk.sendXmlDocument(nitEmpresa, xml.getBytes(StandardCharsets.UTF_8)); }
}
