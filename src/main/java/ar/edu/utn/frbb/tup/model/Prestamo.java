package ar.edu.utn.frbb.tup.model;

import java.util.List;
import java.util.Map;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;


public class Prestamo {
    private long numeroCliente;
    private double montoPrestamo;
    private TipoMoneda moneda;
    private int plazoMeses;
    private List<Map<String, Object>> planPagos; // Lista de mapas para el plan de pagos
    private int pagosRealizados;
    private double saldoRestante;

    public Prestamo() {
    }

    public Prestamo(PrestamoDto prestamoDto) {
        this.numeroCliente = prestamoDto.getNumeroCliente();
        this.montoPrestamo = prestamoDto.getMontoPrestamo();
        this.moneda = TipoMoneda.fromString(prestamoDto.getMoneda());
        this.plazoMeses = prestamoDto.getPlazoMeses();
        this.planPagos = prestamoDto.getPlanPagos();
    }

    // Getters y Setters
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

    public Prestamo setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public int getPagosRealizados() {
        return pagosRealizados;
    }

    public double getSaldoRestante() {
        return saldoRestante;
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
