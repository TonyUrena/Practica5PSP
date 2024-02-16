package com.dam.proyectospring.service;

import com.dam.proyectospring.model.Piloto;

import java.util.List;
import java.util.Optional;

public interface PilotoServicio {
    List<Piloto> findAllPilotos();
    Optional<Piloto> findById(String id);
    Piloto savePilot(Piloto pilot);
    void removePilotById(String id);
}
