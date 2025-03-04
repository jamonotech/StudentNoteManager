package m1.uasz.sn.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsablePedagogique extends Utilisateur {
    @OneToMany(mappedBy = "responsablePedagogique")
    private List<Formation> formations;

    public ResponsablePedagogique(String nom, String prenom, String email, String password) {
        super(nom, prenom, email, password);
    }
}