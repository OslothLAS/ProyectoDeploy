package com.usuarios.servicioDeUsuarios.filters;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenInfo {
    private String username;
    private String rol;
}
