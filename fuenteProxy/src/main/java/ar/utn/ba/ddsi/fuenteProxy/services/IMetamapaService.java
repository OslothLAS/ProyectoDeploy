package ar.utn.ba.ddsi.fuenteProxy.services;

import entities.colecciones.Handle;
import entities.hechos.Hecho;
import io.netty.channel.RecvByteBufAllocator;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IMetamapaService {
    List<Hecho> getHechos();
    List<Hecho> getHechosXcoleccionXmetamapa(Handle id_coleccion, Long metamapa);
    List<Hecho> getHechosXcategoria(String categoria);
    List<Hecho> getHechosXmetamapa(Long id);
}
