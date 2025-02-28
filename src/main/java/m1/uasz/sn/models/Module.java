package m1.uasz.sn.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Module {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String nom;
    private int volumeHoraire;
    private double coefficient;
    private int credits;

    @ManyToOne
    private Enseignant enseignant;

    @ManyToOne
    private Formation formation;

    @ManyToMany(mappedBy = "modules")
    private List<Etudiant> etudiants;

    public Module() {}

    public Module(String code, String nom, int volumeHoraire, double coefficient, int credits, Enseignant enseignant, Formation formation) {
        this.code = code;
        this.nom = nom;
        this.volumeHoraire = volumeHoraire;
        this.coefficient = coefficient;
        this.credits = credits;
        this.enseignant = enseignant;
        this.formation = formation;
    }
}
