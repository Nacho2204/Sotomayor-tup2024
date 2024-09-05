package ar.edu.utn.frbb.tup.controller.dto;

import java.util.List;
import java.util.Map;

public class PrestamoDto {
    private long numeroCliente;
    private double montoPrestamo;
    private String moneda;
    private int plazoMeses;
    private List<Map<String, Object>> planPagos;
    private Long id;

    // Getters y setters
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

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
