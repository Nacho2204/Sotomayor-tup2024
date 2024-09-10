package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CuentaDao extends AbstractBaseDao {

    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Cuenta find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(id)).toCuenta();
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Object object : getInMemoryDatabase().values()) {
            CuentaEntity cuenta = ((CuentaEntity) object);
            // Comparar el dni del titular, no el objeto Cliente completo
            if (cuenta.getTitular() != null && cuenta.getTitular().getDni() == dni) {
                cuentasDelCliente.add(cuenta.toCuenta());
            }
        }
        return cuentasDelCliente;
    }

    public Cuenta findByMoneda(TipoMoneda moneda) {
        for (Object object : getInMemoryDatabase().values()) {
            CuentaEntity cuentaEntity = ((CuentaEntity) object);
            TipoMoneda cuentaMoneda = TipoMoneda.valueOf(cuentaEntity.getMoneda());
            if (cuentaMoneda.equals(moneda)) {
                return cuentaEntity.toCuenta();
            }
        }
        return null;
    }


    public void update(Cuenta cuenta) {
            // Actualiza la cuenta en la base de datos
            getInMemoryDatabase().put(cuenta.getNumeroCuenta(), new CuentaEntity(cuenta));
    }

    public boolean verificarCuenta(long numeroCliente, String moneda) {
        for (Object object : getInMemoryDatabase().values()) {
            CuentaEntity cuentaEntity = (CuentaEntity) object;

            if (cuentaEntity.getTitular().equals(numeroCliente) && cuentaEntity.getMoneda().equals(moneda)) {
                return true;
            }
        }
        return false;
    }
}
