package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoMoneda;

import java.util.List;
import java.util.Map;

public class PrestamoEntity extends BaseEntity {
    private long numeroCliente;
    private double montoPrestamo;
    private TipoMoneda moneda;
    private int plazoMeses;
    private List<Map<String, Object>> planPagos;

    public PrestamoEntity(Prestamo prestamo) {
        super(System.currentTimeMillis()); // Usar un ID generado automáticamente
        this.numeroCliente = prestamo.getNumeroCliente();
        this.montoPrestamo = prestamo.getMontoPrestamo();
        this.moneda = prestamo.getMoneda();
        this.plazoMeses = prestamo.getPlazoMeses();
        this.planPagos = prestamo.getPlanPagos();
    }

    public Prestamo toPrestamo() {
        Prestamo prestamo = new Prestamo();
        prestamo.setNumeroCliente(this.numeroCliente);
        prestamo.setMontoPrestamo(this.montoPrestamo);
        prestamo.setMoneda(this.moneda);
        prestamo.setPlazoMeses(this.plazoMeses);
        prestamo.setPlanPagos(this.planPagos);
        return prestamo;
    }

    // Getters and Setters
    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public double getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(double montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public List<Map<String, Object>> getPlanPagos() {
        return planPagos;
    }

    public void setPlanPagos(List<Map<String, Object>> planPagos) {
        this.planPagos = planPagos;
    }
}
