package com.dam.proyectospring.service;

import com.dam.proyectospring.model.Piloto;
import com.dam.proyectospring.repository.PilotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PilotoServicioImpl implements PilotoServicio {
    private final PilotoRepository pilotoRepositorio;

    @Autowired
    public PilotoServicioImpl(PilotoRepository pilotoRepositorio){
        this.pilotoRepositorio = pilotoRepositorio;
    }

    @Override
    public List<Piloto> findAllPilotos() {
        return pilotoRepositorio.findAll();
    }

    @Override
    public Optional<Piloto> findById(String id) {
        return pilotoRepositorio.findById(id);
    }

    @Override
    public Piloto savePilot(Piloto pilot) {
        return pilotoRepositorio.save(pilot);
    }

    @Override
    public void removePilotById(String id) {
        pilotoRepositorio.delete(pilotoRepositorio.findPilotoBy_id(id).get());
    }
}
