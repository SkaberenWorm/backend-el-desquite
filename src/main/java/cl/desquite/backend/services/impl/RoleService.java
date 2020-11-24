package cl.desquite.backend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

import cl.desquite.backend.entities.Role;
import cl.desquite.backend.repositories.RoleRepository;
import cl.desquite.backend.services.IRoleService;
import cl.desquite.backend.utils.Util;
import cl.desquite.backend.utils.ResultadoProc;

@Service
@CommonsLog
public class RoleService implements IRoleService {

  @Autowired
  RoleRepository roleRepository;

  @Override
  public ResultadoProc<Role> findById(final int roleId) {
    final ResultadoProc.Builder<Role> salida = new ResultadoProc.Builder<Role>();
    try {
      final Role role = roleRepository.findById(roleId).orElse(null);
      if (role == null) {
        salida.fallo("No se ha encontrado el role");
      }
      salida.exitoso(role);
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      salida.fallo("Se produjo un error inesperado al intentar obtener el role");
    }
    return salida.build();
  }

  @Override
  public ResultadoProc<Role> save(final Role role) {
    final ResultadoProc.Builder<Role> salida = new ResultadoProc.Builder<Role>();
    try {
      roleRepository.save(role);
      salida.exitoso(role, "Role registrado correctamente");
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      salida.fallo("Se produjo un error inesperado al intentar registrar el role");
    }
    return salida.build();
  }

  @Override
  public ResultadoProc<Role> update(final Role role) {
    final ResultadoProc.Builder<Role> salida = new ResultadoProc.Builder<Role>();
    try {
      roleRepository.save(role);
      salida.exitoso(role, "Role actualizado correctamente");
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      salida.fallo("Se produjo un error inesperado al intentar actualizar el role");
    }
    return salida.build();
  }

  @Override
  public ResultadoProc<Page<Role>> findAllPaginatedBySearch(final String search, final PageRequest pageable) {
    final ResultadoProc.Builder<Page<Role>> salida = new ResultadoProc.Builder<Page<Role>>();
    try {
      salida.exitoso(roleRepository.findAllBySearch(search, pageable));
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      salida.fallo("Se produjo un error inesperado al intentar listar los roles");
    }
    return salida.build();
  }

  @Override
  public ResultadoProc<List<Role>> findAllActivos() {
    final ResultadoProc.Builder<List<Role>> salida = new ResultadoProc.Builder<List<Role>>();
    try {
      salida.exitoso(this.roleRepository.findAllActivos());
    } catch (final Exception e) {
      Util.printError("findAllActivos()", e);
      salida.fallo("Se produjo un error inesperado al intentar listar los roles activos");
    }
    return salida.build();
  }
}
