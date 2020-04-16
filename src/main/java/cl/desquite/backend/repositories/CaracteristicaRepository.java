package cl.desquite.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.Caracteristica;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {

}
