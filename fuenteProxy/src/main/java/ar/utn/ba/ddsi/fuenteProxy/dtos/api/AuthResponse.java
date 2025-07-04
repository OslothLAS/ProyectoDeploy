// AuthResponse.java
package ar.utn.ba.ddsi.fuenteProxy.dtos.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    private boolean error;
    private String message;
    private AuthDatas data;

    public AuthResponse() {}

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthDatas getData() {
        return data;
    }

    public void setData(AuthDatas data) {
        this.data = data;
    }

    public String getToken() {
        return data != null ? data.getAccess_token() : null;
    }
}
