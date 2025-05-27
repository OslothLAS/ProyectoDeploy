package ar.utn.ba.ddsi.fuenteProxy.dtos;

import java.util.List;

public class DesastresResponse {
    private List<HechoDto> data;

    public List<HechoDto> getData() {
        return data;
    }

    public void setData(List<HechoDto> data) {
        this.data = data;
    }
}
