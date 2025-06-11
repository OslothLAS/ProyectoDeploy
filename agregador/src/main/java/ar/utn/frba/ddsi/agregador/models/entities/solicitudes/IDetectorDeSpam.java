package ar.utn.frba.ddsi.agregador.models.entities.solicitudes;

public interface IDetectorDeSpam {

    public boolean isSpam(String texto);
}
