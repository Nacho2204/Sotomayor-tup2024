package ar.edu.utn.frbb.tup.model.exception;

//el prestamo tiene un plazo de meses establecido, en el caso de que el plazo no sea el correcto, existe esta excepcion
public class PlazoMesesPrestamoExxception extends Throwable {
    public PlazoMesesPrestamoExxception(String message) {
        super(message);
    }
}
