package com.agrosellnova.Agrosellnova.servicio;

import com.agrosellnova.Agrosellnova.modelo.Resena;
import com.agrosellnova.Agrosellnova.repositorio.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public void guardar(Resena resena) {
        resenaRepository.save(resena);
    }

    public List<Resena> listarTodas() {
        return resenaRepository.findAll();
    }
}
