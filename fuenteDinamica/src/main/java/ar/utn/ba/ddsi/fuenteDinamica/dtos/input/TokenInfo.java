    package ar.utn.ba.ddsi.fuenteDinamica.dtos.input;

    import lombok.AllArgsConstructor;
    import lombok.Data;

    @Data
    @AllArgsConstructor
    public class TokenInfo {
        private String username;
        private String rol;
    }
