package cl.desquite.backend.services;

import java.util.List;

import cl.desquite.backend.entities.GrupoPrivilegio;
import cl.desquite.backend.utils.ResultadoProc;

public interface IGrupoPrivilegioService {

  /**
   * Obtiene un {@link GrupoPrivilegio} por su identificador
   * 
   * @param grupoPrivilegioId Identificador del {@link GrupoPrivilegio}
   * @return {@link GrupoPrivilegio} coincidente con el identificador
   */
  ResultadoProc<GrupoPrivilegio> findById(int grupoPrivilegioId);

  ResultadoProc<List<GrupoPrivilegio>> findAllActivos();
}
