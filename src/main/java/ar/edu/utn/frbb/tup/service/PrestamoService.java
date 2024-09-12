package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoEstado;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoService {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private PrestamoDao prestamoDao;

    @Autowired
    private ScoreCrediticioService scoreCreditService;

    //metodo para crear el prestamo
    public PrestamoResultado solicitarPrestamo (PrestamoDto prestamoDto) throws Exception, ClienteNoEncontradoException, ClienteNull, PlazoMesesPrestamoExxception, MontoMinimoException {
        Prestamo prestamo = new Prestamo(prestamoDto);

        if (prestamoDto.getNumeroCliente() <= 0) {
            throw new ClienteNull("El número de cliente no es válido.");
        }

        if (prestamoDto.getPlazoMeses() < 3 || prestamoDto.getPlazoMeses() > 60) {
            throw new PlazoMesesPrestamoExxception("El plazo debe estar entre 3 y 60 meses.");
        }

        if (prestamoDto.getMontoPrestamo()< 2000){
            throw new MontoMinimoException("El monto del préstamo debe ser mayor a 2000");
        }

        if (!scoreCreditService.verificarScore(prestamo.getNumeroCliente())) {
            PrestamoResultado prestamoResultado = new PrestamoResultado();
            prestamoResultado.setEstado(PrestamoEstado.RECHAZADO);
            prestamoResultado.setMensaje("No cuenta con la puntuacion adecuada para ser beneficiario del prestamo");
            return prestamoResultado;
        }


        clienteService.agregarPrestamo(prestamo, prestamo.getNumeroCliente());
        cuentaService.actualizarCuenta(prestamo);
        prestamoDao.save(prestamo);


        PrestamoResultado prestamoResultado = new PrestamoResultado();
        prestamoResultado.setEstado(PrestamoEstado.APROBADO);
        prestamoResultado.setMensaje("Monto acreditado a su cuenta!");
        prestamoResultado.setPlanPago(prestamo.getPlazoMeses(), prestamo.getMontoPedido());
        return prestamoResultado;
    }

    //metodo para obtener los prestamos de un cliente

    public List<Prestamo> buscarPrestamoByDni(long dni) throws Exception, ClienteNoEncontradoException,PrestamoNoExisteException {
        List<Prestamo> prestamos = prestamoDao.getPrestamosByCliente(dni);
        if (prestamos.isEmpty()) {
            // lanzar una excepción si no tiene préstamos
            throw new PrestamoNoExisteException("El cliente no tiene préstamos");
        }
        clienteService.buscarClientePorDni(dni);
        return prestamoDao.getPrestamosByCliente(dni);
    }

}