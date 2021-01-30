package cl.desquite.backend.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Clave implements Serializable {

    private static final long serialVersionUID = 2388783639197676167L;

    private String clave;
    private String claveConfirm;
}
