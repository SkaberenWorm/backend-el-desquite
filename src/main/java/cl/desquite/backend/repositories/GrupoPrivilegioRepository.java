package cl.desquite.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.GrupoPrivilegio;

@Repository
public interface GrupoPrivilegioRepository extends JpaRepository<GrupoPrivilegio, Integer> {

  @Query("select g from GrupoPrivilegio g where g.activo = 1")
  List<GrupoPrivilegio> findAllActivos();
}
