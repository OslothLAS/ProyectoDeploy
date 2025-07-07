package ar.utn.frba.ddsi.agregador.models.entities.solicitudes;

public interface IDetectorDeSpam {
    boolean isSpam(String texto);
}
