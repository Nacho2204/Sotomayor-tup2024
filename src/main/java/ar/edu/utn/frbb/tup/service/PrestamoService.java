package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import ar.edu.utn.frbb.tup.persistence.entity.PrestamoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private ScoreCrediticioService scoreCrediticioService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private PrestamoDao prestamoDao;

    @Autowired
    private CuentaDao cuentaDao;

    public Prestamo solicitarPrestamo(Prestamo prestamo) {
        boolean scoreValido = scoreCrediticioService.verificarScore(prestamo.getNumeroCliente());

        if (!scoreValido) {
            // Manejar el caso de score no válido (retornar null, lanzar excepción, etc.)
            return null;
        }

        double intereses = calcularIntereses(prestamo.getMontoPrestamo(), prestamo.getPlazoMeses());
        prestamo.setMontoPrestamo(prestamo.getMontoPrestamo() + intereses);

        // Almacenar el préstamo y actualizar la cuenta del cliente en un solo método
        Prestamo prestamoGuardado = almacenarYActualizarPrestamo(prestamo);

        return prestamoGuardado;
    }

    private Prestamo almacenarYActualizarPrestamo(Prestamo prestamo) {
        // Convertir Prestamo a PrestamoEntity
        PrestamoEntity entity = new PrestamoEntity(prestamo);

        // Almacenar el préstamo en la base de datos en memoria
        prestamoDao.save(entity);

        // Buscar las cuentas del cliente por su DNI
        List<Cuenta> cuentas = cuentaDao.getCuentasByCliente(prestamo.getNumeroCliente());

        // Verificar si el cliente tiene una cuenta con la moneda del préstamo
        boolean cuentaEncontrada = false;
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getMoneda().equals(prestamo.getMoneda())) {
                if (cuenta.getTitular() != null && cuenta.getTitular().getDni() == prestamo.getNumeroCliente()) {
                    cuenta.setBalance(cuenta.getBalance() + prestamo.getMontoPrestamo());
                    cuentaDao.update(cuenta);
                    cuentaEncontrada = true;
                    break;
                } else {
                    throw new RuntimeException("La cuenta encontrada no tiene un titular válido");
                }
            }
        }

        if (!cuentaEncontrada) {
            throw new IllegalStateException("No se encontró una cuenta con la moneda especificada para el cliente " + prestamo.getNumeroCliente());
        }

        return prestamo;
    }

    private double calcularIntereses(double monto, int plazoMeses) {
        // Lógica para calcular los intereses
        return monto * 0.05 * plazoMeses; // Ejemplo: 5% de interés mensual
    }

    public List<Prestamo> obtenerPrestamosPorCliente(long clienteId) {
        return prestamoDao.findByClienteId(clienteId);
    }
}
