package ar.edu.utn.frbb.tup.model;


public class PrestamoResultado {
    private PrestamoEstado estado;
    private String mensaje;
    private Cuota cuota;

    public PrestamoResultado() {
    }

    public PrestamoResultado(PrestamoEstado estado, String mensaje, Cuota cuota) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.cuota = cuota;
    }

    public PrestamoEstado getEstado() {
        return estado;
    }

    public void setEstado(PrestamoEstado estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Cuota getPlanPago() {
        return cuota;
    }

    public void setPlanPago(int cuotaNro, double cuotaMonto) {
        this.cuota = new Cuota(cuotaNro, cuotaMonto);
    }
}