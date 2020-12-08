package cl.desquite.backend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

import cl.desquite.backend.entities.GrupoPrivilegio;
import cl.desquite.backend.repositories.GrupoPrivilegioRepository;
import cl.desquite.backend.services.IGrupoPrivilegioService;
import cl.desquite.backend.utils.ResultadoProc;
import cl.desquite.backend.utils.Util;

@Service
@CommonsLog
public class GrupoPrivilegioService implements IGrupoPrivilegioService {

  @Autowired
  GrupoPrivilegioRepository grupoPrivilegioRepository;

  @Override
  public ResultadoProc<GrupoPrivilegio> findById(final int grupoPrivilegioId) {
    final ResultadoProc.Builder<GrupoPrivilegio> salida = new ResultadoProc.Builder<GrupoPrivilegio>();
    try {
      final GrupoPrivilegio grupoPrivilegio = grupoPrivilegioRepository.findById(grupoPrivilegioId).orElse(null);
      if (grupoPrivilegio == null) {
        salida.fallo("No se ha encontrado el grupoPrivilegio");
      }
      salida.exitoso(grupoPrivilegio);
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      salida.fallo("Se produjo un error inesperado al intentar obtener el grupoPrivilegio");
    }
    return salida.build();
  }

  @Override
  public ResultadoProc<List<GrupoPrivilegio>> findAllActivos() {
    final ResultadoProc.Builder<List<GrupoPrivilegio>> salida = new ResultadoProc.Builder<List<GrupoPrivilegio>>();
    try {
      salida.exitoso(this.grupoPrivilegioRepository.findAllActivos());
    } catch (final Exception e) {
      Util.printError("findAllActivos()", e);
      salida.fallo("Se produjo un error inesperado al intentar listar los grupos de privilegios activos");
    }
    return salida.build();
  }

}
