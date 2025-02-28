package m1.uasz.sn.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double controleContinu;
    private double examen;

    @ManyToOne
    private Etudiant etudiant;

    @ManyToOne
    private Module module;

    public Note() {}

    public Note(double controleContinu, double examen, Etudiant etudiant, Module module) {
        this.controleContinu = controleContinu;
        this.examen = examen;
        this.etudiant = etudiant;
        this.module = module;
    }

    public double calculerMoyenne() { return (controleContinu + examen) / 2; }
}
