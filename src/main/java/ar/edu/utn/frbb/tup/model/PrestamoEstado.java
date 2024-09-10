package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.PrestamoNoValido;

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

    //para convertir el codigo en el estado del prestamo
    public static PrestamoEstado fromCodigo(String codigo) throws PrestamoNoValido {
        for (PrestamoEstado estado : values()) {
            if (estado.getCodigo().equals(codigo)) {
                return estado;
            }
        }
        throw new PrestamoNoValido("Código de estado inválido: " + codigo);
    }
}