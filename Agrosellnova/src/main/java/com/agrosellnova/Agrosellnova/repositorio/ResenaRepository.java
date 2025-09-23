package com.agrosellnova.Agrosellnova.repositorio;

import com.agrosellnova.Agrosellnova.modelo.Productor;
import com.agrosellnova.Agrosellnova.modelo.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
}
