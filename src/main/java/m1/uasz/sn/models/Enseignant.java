package m1.uasz.sn.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Enseignant extends Utilisateur {
    @Column(unique = true)
    private String matricule;
    private LocalDate dateNaissance;
    private String sexe;
    private String adresse;
    private String grade;
    private String specialite;
    private String bureau;
    private String institution;

    @OneToMany(mappedBy = "enseignantResponsable")
    private List<Module> modules;

    public Enseignant() {}

    public Enseignant(String matricule, String prenoms, String nom, LocalDate dateNaissance, String sexe, String adresse, String email, String grade, String specialite, String bureau, String institution) {
        super(nom, prenoms, email, matricule);
        this.matricule = matricule;
//        this.prenoms = prenoms;
//        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.adresse = adresse;
//        this.email = email;
        this.grade = grade;
        this.specialite = specialite;
        this.bureau = bureau;
        this.institution = institution;
    }
}
