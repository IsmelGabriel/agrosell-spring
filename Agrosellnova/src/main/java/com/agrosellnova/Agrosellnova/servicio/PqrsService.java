package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Pqrs;
import com.agrosellnova.Agrosellnova.repositorio.PqrsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PqrsService {

    @Autowired
    private PqrsRepository pqrsRepository;

    public List<Pqrs> listarTodas() {
        return pqrsRepository.findAll();
    }
}
