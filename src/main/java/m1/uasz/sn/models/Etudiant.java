package m1.uasz.sn.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Etudiant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String ine;
    private String prenoms;
    private String nom;
    private LocalDate dateNaissance;
    private String sexe;
    private String adresse;
    private String email;

    @ManyToOne
    private Formation formation;

    @ManyToMany
    private List<Module> modules;

    @OneToMany(mappedBy = "etudiant")
    private List<Note> notes;

    public Etudiant() {}

    public Etudiant(String ine, String prenoms, String nom, LocalDate dateNaissance, String sexe, String adresse, String email, Formation formation) {
        this.ine = ine;
        this.prenoms = prenoms;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.adresse = adresse;
        this.email = email;
        this.formation = formation;
    }
}