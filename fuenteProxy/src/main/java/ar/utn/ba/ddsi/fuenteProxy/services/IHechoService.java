package ar.utn.ba.ddsi.fuenteProxy.services;

import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;

import java.util.List;

public interface IHechoService {
    List<HechoDto> getHechos();
}
