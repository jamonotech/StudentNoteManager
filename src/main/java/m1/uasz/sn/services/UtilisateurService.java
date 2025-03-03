package m1.uasz.sn.services;

import m1.uasz.sn.dao.UtilisateurDAO;
import m1.uasz.sn.models.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

public class UtilisateurService {
    private final UtilisateurDAO utilisateurDAO;
    private Utilisateur utilisateurConnecte;

    public UtilisateurService(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    public boolean inscrire(String email, String motDePasse, Utilisateur.Role role) {
        if (utilisateurDAO.existeEmail(email)) {
            return false;
        }

        String hash = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
        Utilisateur utilisateur = new Utilisateur(null, email, hash, role);
        utilisateurDAO.create(utilisateur);
        return true;
    }

    public boolean authentifier(String email, String motDePasse) {
        Optional<Utilisateur> utilisateurOpt = utilisateurDAO.trouverParEmail(email);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            if (BCrypt.checkpw(motDePasse, utilisateur.getMotDePasse())) {
                utilisateurConnecte = utilisateur;
                return true;
            }
        }
        return false;
    }

    public void deconnecter() {
        utilisateurConnecte = null;
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public List<Utilisateur> findAll() {
        return utilisateurDAO.findAll();
    }

    public Optional<Utilisateur> findById(Long id) {
        return utilisateurDAO.trouverParId(id);
    }

    public void supprimerUtilisateur(Long id) {
        utilisateurDAO.supprimerParId(id);
    }

    public void mettreAJourUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.mettreAJour(utilisateur);
    }
}
