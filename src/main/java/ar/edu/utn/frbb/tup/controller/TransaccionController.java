package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.TransaccionDto;
import ar.edu.utn.frbb.tup.model.Transaccion;
import ar.edu.utn.frbb.tup.service.TransaccionService;
import ar.edu.utn.frbb.tup.model.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.CantidadNegativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaccion")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @PostMapping
    public Transaccion registrarTransaccion(@RequestBody TransaccionDto transaccionDto) throws NoAlcanzaException, CantidadNegativaException {
        return transaccionService.registrarTransaccion(
                transaccionDto.getNumeroCuenta(),
                transaccionDto.getMonto(),
                transaccionDto.getTipo()
        );
    }

    @GetMapping
    public List<Transaccion> obtenerTransacciones() {
        return transaccionService.obtenerTransacciones();
    }
}
