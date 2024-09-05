package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prestamo")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<?> solicitarPrestamo(@RequestBody PrestamoDto prestamoDto) {
        try {
            // Crear objeto Prestamo usando el constructor que acepta PrestamoDto
            Prestamo prestamo = new Prestamo(prestamoDto);
            Prestamo prestamoGuardado = prestamoService.solicitarPrestamo(prestamo);

            if (prestamoGuardado == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "estado", "RECHAZADO",
                        "mensaje", "Calificación crediticia no válida"
                ));
            }

            // Calcular el monto mensual
            double montoMensual = prestamoGuardado.getMontoPrestamo() / prestamoDto.getPlazoMeses();
            List<Map<String, Object>> planPagos = new ArrayList<>();
            for (int i = 1; i <= prestamoDto.getPlazoMeses(); i++) {
                planPagos.add(Map.of(
                        "cuotaNro", i,
                        "monto", montoMensual
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "estado", "APROBADO",
                    "mensaje", "El monto del préstamo fue acreditado en su cuenta",
                    "planPagos", planPagos
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "estado", "RECHAZADO",
                    "mensaje", e.getMessage()
            ));
        }
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<?> consultarEstadoPrestamos(@PathVariable long clienteId) {
        try {
            List<Prestamo> prestamos = prestamoService.obtenerPrestamosPorCliente(clienteId);

            List<Map<String, Object>> prestamosInfo = prestamos.stream()
                    .map(p -> {
                        Map<String, Object> prestamoMap = new HashMap<>();
                        prestamoMap.put("monto", p.getMontoPrestamo());
                        prestamoMap.put("plazoMeses", p.getPlazoMeses());
                        prestamoMap.put("pagosRealizados", p.getPagosRealizados()); // Asegúrate de que estos métodos existen en Prestamo
                        prestamoMap.put("saldoRestante", p.getSaldoRestante());      // Asegúrate de que estos métodos existen en Prestamo
                        return prestamoMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "numeroCliente", clienteId,
                    "prestamos", prestamosInfo
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "estado", "ERROR",
                    "mensaje", e.getMessage()
            ));
        }
    }

}
