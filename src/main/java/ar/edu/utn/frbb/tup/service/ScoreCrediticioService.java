package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Service;

@Service
public class ScoreCrediticioService {


    // simula la consulta de un score crediticio de un cliente

    public boolean verificarScore(long numeroCliente) {
        int random = (int) (Math.random() * 100);
        if (random % 2 == 0) {
           return true;
        } else {
           return false;
        }
    }
}
