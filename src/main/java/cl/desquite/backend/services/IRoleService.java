package cl.desquite.backend.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cl.desquite.backend.entities.Role;
import cl.desquite.backend.utils.ResultadoProc;

public interface IRoleService {

  /**
   * Obtiene un {@link Role} por su identificador
   * 
   * @param roleId Identificador del {@link Role}
   * @return {@link Role} coincidente con el identificador
   */
  ResultadoProc<Role> findById(int roleId);

  /**
   * Registra un nuevo {@link Role}
   * 
   * @param role {@link Role}
   * @return {@link Role} registrado
   */
  ResultadoProc<Role> save(Role role);

  /**
   * Actualiza un nuevo {@link Role}
   * 
   * @param role {@link Role}
   * @return {@link Role} actualizado
   */
  ResultadoProc<Role> update(Role roleParam);

  /**
   * Obtiene un {@link Page} de todos los {@link Role} que coincidan con lo
   * buscado
   * 
   * @param pageable {@link PageRequest} contiene los datos de la paginación
   * @param search   Texto a buscar dentro de los atributos del {@link Role}
   * @return {@link Page} de los {@link Role} coincidentes con lo buscado
   */
  ResultadoProc<Page<Role>> findAllPaginatedBySearch(String search, PageRequest pageable);

  /**
   * Obtiene el listado de todos los roles activos
   * 
   * @return
   */
  ResultadoProc<List<Role>> findAllActivos();
}
