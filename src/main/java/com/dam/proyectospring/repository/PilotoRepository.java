package com.dam.proyectospring.repository;

import com.dam.proyectospring.model.Piloto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PilotoRepository extends MongoRepository<Piloto, String> {
    Optional<Piloto> findPilotoBy_id(String id);
}
