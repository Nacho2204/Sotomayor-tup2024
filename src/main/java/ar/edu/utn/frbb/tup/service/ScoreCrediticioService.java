package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreCrediticioService {

    @Autowired
    private ClienteDao clienteDao;


    public boolean verificarScore(long numeroCliente) {
        int random = (int) (Math.random() * 100);
        if (random % 2 == 0) {
           return true;
        } else {
           return false;
        }
    }
}
