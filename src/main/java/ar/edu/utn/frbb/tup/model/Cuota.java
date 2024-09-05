package ar.edu.utn.frbb.tup.model;

public class Cuota {
    private int cuotaNro;
    private double monto;

    public Cuota(int cuotaNro, double monto) {
        this.cuotaNro = cuotaNro;
        this.monto = monto;
    }

    public Cuota() {
    }

    // Getters y Setters
    public int getCuotaNro() {
        return cuotaNro;
    }

    public void setCuotaNro(int cuotaNro) {
        this.cuotaNro = cuotaNro;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
