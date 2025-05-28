package models.entities.solicitudes;

import ar.utn.frba.ddsi.agregador.models.entities.TestBuilder;

public class Main {
    public static void main(String[] args) {
        TestBuilder t = TestBuilder.builder().name("Test").age(30).build();
        System.out.println(t.getName());
    }
}