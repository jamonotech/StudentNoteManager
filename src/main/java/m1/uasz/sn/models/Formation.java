package m1.uasz.sn.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Formation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String niveau;
    private String responsableNom;
    private String responsableEmail;

    @OneToMany(mappedBy = "formation")
    private List<Module> modules;

    public Formation() {}

    public Formation(String nom, String niveau, String responsableNom, String responsableEmail) {
        this.nom = nom;
        this.niveau = niveau;
        this.responsableNom = responsableNom;
        this.responsableEmail = responsableEmail;
    }
}