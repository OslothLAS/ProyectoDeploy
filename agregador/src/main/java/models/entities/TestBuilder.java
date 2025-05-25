package models.entities;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestBuilder {
    private String name;
    private int age;
}