package cl.desquite.backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import cl.desquite.backend.entities.Usuario;
import cl.desquite.backend.util.ResultadoProc;

public interface IUsuarioService {

	/**
	 * Busca el ususario por su email o user
	 * 
	 * @param emailOrUsuario (Email o user del usuario)
	 * @return ResultadoProc&lt;Usuario&gt; El usuario que coincida con el email o
	 *         el user
	 */
	ResultadoProc<Usuario> findByEmail(String emailOrUsuario);

	/**
	 * Busca un usuario por su ID.
	 * 
	 * @param usuarioId (Identificador del usuario)
	 * @return ResultadoProc&lt;Usuario&gt; Usuario con el ID dado
	 */
	ResultadoProc<Usuario> findById(int usuarioId);

	/**
	 * Busca un usuario por su ID y que se encuentre activo
	 * 
	 * @param usuarioId (Identificador del usuario)
	 * @return ResultadoProc&lt;Usuario&gt; Usuario activo con el ID dado
	 */
	ResultadoProc<Usuario> findByIdAndActivoTrue(int usuarioId);

	/**
	 * Retorna un {@link Page} de todos los usuarios que coinciden con la query
	 * 
	 * @param pageable Entidad {@link PageRequest} que contiene los datos de la
	 *                 paginación
	 * @param buscador Texto usado para filtrar por el nombre y descripcion del
	 *                 usuario
	 * @return ResultadoProc&lt;Page&lt;Usuario&gt;&gt; Una página de usuarios
	 *         coincidentes con los filtros
	 */
	ResultadoProc<Page<Usuario>> findAllPaginatedWithFilters(PageRequest pageable, String buscador);

	/**
	 * Guarda o actualiza un usuario
	 * 
	 * @param usuario {@link Usuario}
	 * @return ResultadoProc&lt;Usuario&gt; Usuario guardado/actualizado
	 */
	ResultadoProc<Usuario> save(Usuario usuario);

	/**
	 * Cambia el estado del usuario <br>
	 * Si activo es <code><b>true</b></code> lo cambia a <code><b>false</b></code>
	 * <br>
	 * Si activo es <code><b>false</b></code> lo cambia a <code><b>true</b></code>
	 * <br>
	 * 
	 * @param usuarioId (Id del usuario)
	 * @return ResultadoProc&lt;Usuario&gt; El usuario al que le fue cambiado el
	 *         estado
	 */
	ResultadoProc<Usuario> changeState(int usuarioId);

}
