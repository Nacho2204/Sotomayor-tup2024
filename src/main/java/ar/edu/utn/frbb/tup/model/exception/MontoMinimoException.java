package ar.edu.utn.frbb.tup.model.exception;

//el prestamo tiene un monto minimo establecido, en el caso de que el monto no llegue al minimo, existe esta excepcion
public class MontoMinimoException extends Throwable {
    public MontoMinimoException(String message) {
        super(message);
    }
}