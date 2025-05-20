package services;

import dtos.HechoDto;

import java.util.List;

public interface IHechoService {
    List<HechoDto> getHechos();
}
