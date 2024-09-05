package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import ar.edu.utn.frbb.tup.persistence.entity.PrestamoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PrestamoDao extends AbstractBaseDao {

    @Autowired
    private CuentaDao cuentaDao;

    @Override
    protected String getEntityName() {
        return "PRESTAMO";
    }

    public Prestamo almacenarDatosPrestamo(Prestamo prestamo) {
        // Convertir Prestamo a PrestamoEntity
        PrestamoEntity entity = new PrestamoEntity(prestamo);

        // Almacenar el préstamo en la base de datos en memoria
        getInMemoryDatabase().put(entity.getId(), entity);

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
            // Manejar el caso donde el cliente no tiene una cuenta con la moneda del préstamo
            throw new RuntimeException("El cliente no tiene una cuenta con la moneda " + prestamo.getMoneda());
        }

        return prestamo;
    }

    public PrestamoEntity save(PrestamoEntity prestamoEntity) {
        // Almacenar el préstamo en la base de datos en memoria
        getInMemoryDatabase().put(prestamoEntity.getId(), prestamoEntity);
        return prestamoEntity;
    }

    public Prestamo find(long id) {
        PrestamoEntity entity = (PrestamoEntity) getInMemoryDatabase().get(id);
        if (entity == null) {
            return null;
        }
        return entity.toPrestamo();
    }


    public List<Prestamo> findByClienteId(long clienteId) {
        // Filtrar los préstamos por el número del cliente
        return getInMemoryDatabase().values().stream()
                .filter(obj -> obj instanceof PrestamoEntity)
                .map(obj -> (PrestamoEntity) obj)
                .filter(entity -> entity.getNumeroCliente() == clienteId)
                .map(PrestamoEntity::toPrestamo)
                .collect(Collectors.toList());
    }
}
