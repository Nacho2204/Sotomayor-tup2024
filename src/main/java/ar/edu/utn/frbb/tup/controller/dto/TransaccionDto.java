package ar.edu.utn.frbb.tup.controller.dto;

public class TransaccionDto {
    private long numeroCuenta;
    private int monto;
    private String tipo; // Puede ser "DEPOSITO" o "RETIRO"

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
