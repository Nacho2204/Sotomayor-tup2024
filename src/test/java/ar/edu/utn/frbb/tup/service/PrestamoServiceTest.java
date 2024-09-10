package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.model.EstadoDelPrestamo;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrestamoServiceTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private PrestamoDao prestamoDao;

    @Mock
    private ScoreCrediticioService scoreCreditService;

    @InjectMocks
    private PrestamoService prestamoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void darDeAltaPrestamoSucces() throws Exception, ClienteNoEncontradoException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(29857643);
        prestamoDto.setMontoPrestamo(2000);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMoneda("P");

        when(scoreCreditService.verificarScore(29857643)).thenReturn(true);
        doNothing().when(clienteService).agregarPrestamo(any(Prestamo.class), anyLong());
        doNothing().when(cuentaService).actualizarCuenta(any(Prestamo.class));

        PrestamoResultado result = prestamoService.solicitarPrestamo(prestamoDto);

        assertNotNull(result);
        assertEquals(EstadoDelPrestamo.APROBADO, result.getEstado());
        assertEquals("El monto del préstamo fue acreditado en su cuenta", result.getMensaje());

        verify(prestamoDao).save(any(Prestamo.class));
    }

    @Test
    void prestamoRechazado() throws Exception, ClienteNoEncontradoException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(29857643);
        prestamoDto.setMontoPrestamo(2000);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMoneda("P");

        when(scoreCreditService.verificarScore(29857643)).thenReturn(false);

        PrestamoResultado result = prestamoService.solicitarPrestamo(prestamoDto);

        assertNotNull(result);
        assertEquals(EstadoDelPrestamo.RECHAZADO, result.getEstado());
        assertEquals("El cliente no tiene un credito apto para solicitar un prestamo", result.getMensaje());

        verify(prestamoDao, never()).save(any(Prestamo.class));
    }

    @Test
    void getPrestamosByClienteSucces() throws ClienteNoEncontradoException, Exception {
        long dni = 29857643;
        List<Prestamo> prestamos = Arrays.asList(
                new Prestamo(dni, 12, 2000, TipoMoneda.PESOS),
                new Prestamo(dni, 24, 3000, TipoMoneda.DOLARES)
        );

        when(clienteService.buscarClientePorDni(dni)).thenReturn(null); // Simulamos que el cliente existe
        when(prestamoDao.getPrestamosByCliente(dni)).thenReturn(prestamos);

        List<Prestamo> result = prestamoService.getPrestamosByCliente(dni);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dni, result.get(0).getNumeroCliente());
        assertEquals(dni, result.get(1).getNumeroCliente());
    }

    @Test
    void getPrestamosByClienteRechazado() throws ClienteNoEncontradoException, Exception {
        long dni = 29857643;

        when(clienteService.buscarClientePorDni(dni)).thenThrow(new ClienteNoEncontradoException("El cliente no existe"));

        assertThrows(ClienteNoEncontradoException.class, () -> prestamoService.getPrestamosByCliente(dni));
    }


    @Test
    void falloEnActualizacionDeCuenta() throws Exception, ClienteNoEncontradoException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(29857643);
        prestamoDto.setMontoPrestamo(2000);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMoneda("P");

        when(scoreCreditService.verificarScore(29857643)).thenReturn(true);
        doNothing().when(clienteService).agregarPrestamo(any(Prestamo.class), anyLong());
        doThrow(new RuntimeException("Error al actualizar la cuenta")).when(cuentaService).actualizarCuenta(any(Prestamo.class));

        Exception exception = assertThrows(RuntimeException.class, () -> prestamoService.solicitarPrestamo(prestamoDto));
        assertEquals("Error al actualizar la cuenta", exception.getMessage());

        verify(prestamoDao, never()).save(any(Prestamo.class));
    }
}
