package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoEstado;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
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

    //test para comprobar que se da de alta un prestamo
    @Test
    void darDeAltaPrestamoSucces() throws Exception, ClienteNoEncontradoException, MontoMinimoException, ClienteNull, PlazoMesesPrestamoExxception {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(29857643);
        prestamoDto.setMontoPrestamo(2500);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMoneda("P");

        //simular un comportamiento
        when(scoreCreditService.verificarScore(29857643)).thenReturn(true);
        //asegurar que no realice nada cuando se llama a estos metodos, para que no genere errores
        doNothing().when(clienteService).agregarPrestamo(any(Prestamo.class), anyLong());
        doNothing().when(cuentaService).actualizarCuenta(any(Prestamo.class));

        PrestamoResultado result = prestamoService.solicitarPrestamo(prestamoDto);

        //asegurar que el resultado no sea nulo
        assertNotNull(result);
        //lo que esperamos que salga cuando el prestamo fue aprobado
        assertEquals(PrestamoEstado.APROBADO, result.getEstado());
        assertEquals("Monto acreditado a su cuenta!", result.getMensaje());

        verify(prestamoDao).save(any(Prestamo.class));
    }

    //test para comprobar que el prestamo fue rechazado
    @Test
    void prestamoRechazado() throws Exception, ClienteNoEncontradoException, MontoMinimoException, ClienteNull, PlazoMesesPrestamoExxception {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(29857643);
        prestamoDto.setMontoPrestamo(2500);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMoneda("P");

        //simular un comportamiento
        when(scoreCreditService.verificarScore(29857643)).thenReturn(false);

        PrestamoResultado result = prestamoService.solicitarPrestamo(prestamoDto);
        //asegurar que el resultado no sea nulo
        assertNotNull(result);
        //lo que esperamos que salga cuando el prestamo fue aprobado
        assertEquals(PrestamoEstado.RECHAZADO, result.getEstado());
        assertEquals("No cuenta con la puntuacion adecuada para ser beneficiario del prestamo", result.getMensaje());

        verify(prestamoDao, never()).save(any(Prestamo.class));
    }

    //test para comprobar que se obtienen los prestamos de un cliente
    @Test
    void getPrestamosByClienteSucces() throws ClienteNoEncontradoException, Exception {
        long dni = 29857643;
        List<Prestamo> prestamos = Arrays.asList(
                new Prestamo(dni, 12, 2000, TipoMoneda.PESOS),
                new Prestamo(dni, 24, 3000, TipoMoneda.DOLARES)
        );
        // Simulamos que el cliente existe
        when(clienteService.buscarClientePorDni(dni)).thenReturn(null);
        //simula que devuelva los prestamos
        when(prestamoDao.getPrestamosByCliente(dni)).thenReturn(prestamos);

        List<Prestamo> result = prestamoService.buscarPrestamoByDni(dni);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dni, result.get(0).getNumeroCliente());
        assertEquals(dni, result.get(1).getNumeroCliente());
    }

    //test para comprobar que hubo un error al buscar el cliente
    @Test
    void getPrestamosByClienteRechazado() throws ClienteNoEncontradoException, Exception {
        long dni = 29857643;

        when(clienteService.buscarClientePorDni(dni)).thenReturn(null);

        //verificar que se lance una excepcion
        assertThrows(PrestamoNoExisteException.class, () -> prestamoService.buscarPrestamoByDni(dni));
    }


    //test para comprobar que muestre el error de que el monto es menor al minimo
    @Test
    void montoMinimoPrestamoExcepcionSucces() throws Exception, ClienteNoEncontradoException, MontoMinimoException, ClienteNull, PlazoMesesPrestamoExxception{
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(29857643);
        prestamoDto.setMontoPrestamo(500);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMoneda("P");

        //verificar que se lance una excepcion
        MontoMinimoException exception = assertThrows(MontoMinimoException.class, () -> prestamoService.solicitarPrestamo(prestamoDto));
        assertEquals("El monto del préstamo debe ser mayor a 2000", exception.getMessage());

        //verificar que no se guardo el prestamo
        verify(prestamoDao, never()).save(any(Prestamo.class));
    }

    //test para comprobar que muestre el error de que el plazo es invalido
    @Test
    void plazoMesesInvalidoExceptionSucces() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(29857643);
        prestamoDto.setMontoPrestamo(2000);
        prestamoDto.setPlazoMeses(1);
        prestamoDto.setMoneda("P");

        //verificar que se lance una excepcion
        PlazoMesesPrestamoExxception exception = assertThrows(PlazoMesesPrestamoExxception.class, () -> prestamoService.solicitarPrestamo(prestamoDto));
        assertEquals("El plazo debe estar entre 3 y 60 meses.", exception.getMessage());

        //verificar que no se guardo el prestamo
        verify(prestamoDao, never()).save(any(Prestamo.class));
    }

    //test para comprobar que muestre el error de que el cliente no existe
    @Test
    void numeroClienteInvalidoExceptionSucces() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(0);
        prestamoDto.setMontoPrestamo(2000);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMoneda("P");

        //verificar que se lance una excepcion
        ClienteNull exception = assertThrows(ClienteNull.class, () -> prestamoService.solicitarPrestamo(prestamoDto));
        assertEquals("El número de cliente no es válido.", exception.getMessage());

        //verificar que no se guardo el prestamo
        verify(prestamoDao, never()).save(any(Prestamo.class));
    }

    //test para comprobar que muestre el error de que el cliente no tiene prestamos
    @Test
    void getPrestamosByClienteSinPrestamos() throws ClienteNoEncontradoException, Exception {
        long dni = 29857643;

        // simular que el cliente existe
        when(clienteService.buscarClientePorDni(dni)).thenReturn(null);
        //simula que no devuelva prestamos
        when(prestamoDao.getPrestamosByCliente(dni)).thenReturn(Collections.emptyList());

        //verificar que se lance una excepcion
        PrestamoNoExisteException exception = assertThrows(PrestamoNoExisteException.class, () -> prestamoService.buscarPrestamoByDni(dni));
        assertEquals("El cliente no tiene préstamos", exception.getMessage());
    }

}
