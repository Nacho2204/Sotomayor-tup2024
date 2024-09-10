package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private CuentaDao cuentaDao;

    @InjectMocks
    private ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Cliente crearClienteMock(){
        Cliente mockCliente = new Cliente();
        mockCliente.setDni(29857643);
        mockCliente.setNombre("Pepe");
        mockCliente.setApellido("Rino");
        mockCliente.setFechaNacimiento(LocalDate.of(1978,2,7));
        mockCliente.setBanco("Macro");
        mockCliente.setTipoPersona(TipoPersona.PERSONA_FISICA);

        return mockCliente;
    }

    @Test
    public void testClienteMenor18Años() {
        ClienteDto clienteMenorDeEdad = new ClienteDto();
        clienteMenorDeEdad.setFechaNacimiento("2020-03-18");
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException, ClienteMenorDeEdadException, Exception {
        ClienteDto cliente = new ClienteDto();
        cliente.setFechaNacimiento("1978-03-18");
        cliente.setDni(29857643);
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA.toString());
        Cliente clienteEntity = clienteService.darDeAltaCliente(cliente);

        verify(clienteDao, times(1)).save(clienteEntity);
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        ClienteDto pepeRino = new ClienteDto();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento("1978-03-18");
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA.toString());

        when(clienteDao.find(26456437, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }



    @Test
    public void testAgregarCuentaAClienteSuccess() throws Exception, ClienteNoEncontradoException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta, pepeRino.getDni());

        verify(clienteDao, times(1)).save(pepeRino);

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(pepeRino, cuenta.getTitular());

    }


    @Test
    public void testAgregarCuentaAClienteDuplicada() throws Exception, ClienteNoEncontradoException {
        Cliente luciano = new Cliente();
        luciano.setDni(26456439);
        luciano.setNombre("Pepe");
        luciano.setApellido("Rino");
        luciano.setFechaNacimiento(LocalDate.of(1978, 3,25));
        luciano.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(luciano);

        clienteService.agregarCuenta(cuenta, luciano.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, luciano.getDni()));
        verify(clienteDao, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano, cuenta.getTitular());

    }


    //Agregar una CA$ y CC$ --> success 2 cuentas, titular peperino
    @Test
    public void agregarCuentasSucces() throws Exception, ClienteNoEncontradoException {
        Cliente peperino = crearClienteMock();

        Cuenta cuentaAhorro = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(30000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cuenta cuentaCorriente = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(20000)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        when(clienteDao.find(peperino.getDni(), true)).thenReturn(peperino);

        clienteService.agregarCuenta(cuentaAhorro, peperino.getDni());

        clienteService.agregarCuenta(cuentaCorriente, peperino.getDni());

        verify(clienteDao, times(2)).save(peperino);

        assertEquals(2, peperino.getCuentas().size());
        assertEquals(peperino, cuentaAhorro.getTitular());
        assertEquals(peperino, cuentaCorriente.getTitular());
    }

    //Agregar una CA$ y CAU$S --> success 2 cuentas, titular peperino...

    @Test
    public void cuentaEnPesosYDolaresSucces() throws Exception, ClienteNoEncontradoException {
        Cliente peperino = crearClienteMock();

        Cuenta cuenta1 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(20000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.DOLARES)
                .setBalance(240000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(29857643, true)).thenReturn(peperino);

        clienteService.agregarCuenta(cuenta1, peperino.getDni());
        clienteService.agregarCuenta(cuenta2, peperino.getDni());

        verify(clienteDao, times(2)).save(peperino);

        assertEquals(2, peperino.getCuentas().size());
        assertEquals(peperino, cuenta1.getTitular());
        assertEquals(peperino, cuenta2.getTitular());

    }

    //Testear clienteService.buscarPorDni

    @Test
    public void buscarPorDniSucces() throws ClienteNoEncontradoException, Exception {
        Cliente buscado = crearClienteMock();

        when(clienteDao.find(buscado.getDni(), true)).thenReturn(buscado);

        Cliente encontrado = clienteService.buscarClientePorDni(buscado.getDni());

        verify(clienteDao, times(1)).find(buscado.getDni(), true);
        assertEquals(buscado.getDni(), encontrado.getDni());
    }

    @Test
    public void buscarPorDniFallo(){
        assertThrows(IllegalArgumentException.class,() -> clienteService.buscarClientePorDni(12365478));
    }

}