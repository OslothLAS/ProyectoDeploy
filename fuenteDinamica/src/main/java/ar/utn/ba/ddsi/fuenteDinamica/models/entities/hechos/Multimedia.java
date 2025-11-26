package ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "multimedia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Multimedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Aquí guardaremos la ruta relativa (ej: "/uploads/foto-uuid.jpg")
    @Column(name = "url", nullable = false)
    private String url;

    // --- RELACIÓN CON EL PADRE (HECHO) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hecho_id", nullable = false) // FK en la base de datos
    @JsonBackReference // ¡CRUCIAL! Evita que al pedir la foto, intente serializar al padre infinitamente
    private Hecho hecho;
}