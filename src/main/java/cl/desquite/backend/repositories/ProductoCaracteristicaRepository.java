package cl.desquite.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.ProductoCaracteristica;

@Repository
public interface ProductoCaracteristicaRepository extends JpaRepository<ProductoCaracteristica, Integer> {

}
