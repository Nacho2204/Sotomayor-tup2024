package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.MontoMinimoException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNull;
import ar.edu.utn.frbb.tup.model.exception.PlazoMesesPrestamoExxception;
import org.springframework.stereotype.Component;

@Component
public class PrestamoValidator {

    //metodo de validaciones para el prestamo

    public void validate(PrestamoDto prestamoDto) throws TipoMonedaNoSoportadaException, MontoMinimoException, ClienteNull, PlazoMesesPrestamoExxception {
        // Validar que la moneda sea "P" o "D"
        if ((!"P".equals(prestamoDto.getMoneda()) && !"D".equals(prestamoDto.getMoneda()))) {
            throw new TipoMonedaNoSoportadaException("El tipo de moneda no es correcto o es nulo");
        }

        //Establecer un monto minimo al prestamo
        if (prestamoDto.getMontoPrestamo() <= 1000) {
            throw new MontoMinimoException("El monto del préstamo debe ser mayor a 0.");
        }

        //Establecer un plazo de meses al prestamo
        if (prestamoDto.getPlazoMeses() < 3 || prestamoDto.getPlazoMeses() > 120) {
            throw new PlazoMesesPrestamoExxception("El plazo debe estar entre 3 y 120 meses.");
        }

        // Validar que el número de dni del cliente no sea null o 0
        if (prestamoDto.getNumeroCliente() <= 0) {
            throw new ClienteNull("El número de cliente no es válido.");
        }
    }
}