package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaccion {
    private long numeroCuenta;
    private int monto;
    private TipoTransaccion tipoTransaccion;
    private LocalDateTime fecha;


    private static List<Transaccion> transacciones = new ArrayList<>();

    public Transaccion(long numeroCuenta, int monto, TipoTransaccion tipoTransaccion, LocalDateTime fecha) {
        this.numeroCuenta = numeroCuenta;
        this.monto = monto;
        this.tipoTransaccion = tipoTransaccion;
        this.fecha = fecha;
        transacciones.add(this);
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public int getMonto() {
        return monto;
    }

    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public static List<Transaccion> getTransacciones() {
        return transacciones;
    }
}
