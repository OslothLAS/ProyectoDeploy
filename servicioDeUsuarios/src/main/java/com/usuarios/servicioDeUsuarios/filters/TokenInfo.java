package com.usuarios.servicioDeUsuarios.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class TokenInfo {
    private String username;
    private String rol;
}
