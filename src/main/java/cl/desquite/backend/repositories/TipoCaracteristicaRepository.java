package cl.desquite.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.TipoCaracteristica;

@Repository
public interface TipoCaracteristicaRepository extends JpaRepository<TipoCaracteristica, Integer> {

}
