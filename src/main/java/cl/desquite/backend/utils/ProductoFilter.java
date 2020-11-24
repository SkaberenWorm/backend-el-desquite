package cl.desquite.backend.utils;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductoFilter implements Serializable {

	private static final long serialVersionUID = 8963219261360568669L;

	private String query;
	private List<Integer> categoriasId;
}
