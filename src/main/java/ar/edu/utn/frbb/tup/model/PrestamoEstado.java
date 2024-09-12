package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.PrestamoNoValido;
//estado del prestamo
public enum PrestamoEstado {
    APROBADO("A"),
    RECHAZADO("R");

    private final String codigo;

    PrestamoEstado(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

}