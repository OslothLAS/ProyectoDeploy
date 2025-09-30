package ar.utn.frba.ddsi.agregador.models.entities.colecciones.consenso.strategies;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AlgoritmoConsensoConverter implements AttributeConverter<IAlgoritmoConsenso, String> {

    @Override
    public String convertToDatabaseColumn(IAlgoritmoConsenso attribute) {
        if (attribute == null) return null;
        return attribute.getNombre(); // se guarda el nombre en VARCHAR
    }

    @Override
    public IAlgoritmoConsenso convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        return switch (dbData) {
            case "absoluta" -> new Absoluta();
            case "mayoria" -> new Mayoria();
            case "multiple_mencion" -> new MultipleMencion();
            default -> throw new IllegalArgumentException("Algoritmo desconocido: " + dbData);
        };
    }
}