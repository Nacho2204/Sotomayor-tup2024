package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.PrestamoNoValido;

public enum EstadoDelPrestamo {
    APROBADO("A"),
    RECHAZADO("R");

    private final String codigo;

    EstadoDelPrestamo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static EstadoDelPrestamo fromCodigo(String codigo) throws PrestamoNoValido {
        for (EstadoDelPrestamo estado : values()) {
            if (estado.getCodigo().equals(codigo)) {
                return estado;
            }
        }
        throw new PrestamoNoValido("Código de estado inválido: " + codigo);
    }
}