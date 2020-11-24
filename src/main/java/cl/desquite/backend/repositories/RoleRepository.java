package cl.desquite.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cl.desquite.backend.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

  @Query("select p from Role p where p.nombre like %?1%")
  Page<Role> findAllBySearch(String search, Pageable pageable);

  /**
   * Obtiene un listado de todos los roles activos
   * 
   * @return
   */
  @Query("select p from Role p where p.activo = 1")
  List<Role> findAllActivos();

}
