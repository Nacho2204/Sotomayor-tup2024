package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Transaccion;
import ar.edu.utn.frbb.tup.model.TipoTransaccion;
import ar.edu.utn.frbb.tup.model.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.CantidadNegativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    @Autowired
    private CuentaService cuentaService;

    public Transaccion registrarTransaccion(long numeroCuenta, int monto, String tipo) throws NoAlcanzaException, CantidadNegativaException {
        if (monto < 0) {
            throw new CantidadNegativaException();
        }

        TipoTransaccion tipoTransaccion;
        try {
            tipoTransaccion = TipoTransaccion.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de transacción inválido: " + tipo);
        }

        Cuenta cuenta = cuentaService.find(numeroCuenta); // Usa el servicio de cuentas para obtener la cuenta

        if (tipoTransaccion == TipoTransaccion.RETIRO) {
            cuenta.debitarDeCuenta(monto);
        } else if (tipoTransaccion == TipoTransaccion.DEPOSITO) {
            cuenta.setBalance(cuenta.getBalance() + monto);
        }

        Transaccion transaccion = new Transaccion(numeroCuenta, monto, tipoTransaccion, LocalDateTime.now());
        return transaccion;
    }

    public List<Transaccion> obtenerTransacciones() {
        return Transaccion.getTransacciones();
    }

    public List<Transaccion> obtenerTransaccionesPorCliente(long dni) {
        List<Cuenta> cuentas = cuentaService.getCuentasByCliente(dni); // Usa el servicio de cuentas para obtener las cuentas del cliente
        return Transaccion.getTransacciones().stream()
                .filter(transaccion -> cuentas.stream().anyMatch(cuenta -> cuenta.getNumeroCuenta() == transaccion.getNumeroCuenta()))
                .collect(Collectors.toList());
    }
}
