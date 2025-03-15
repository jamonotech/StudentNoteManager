package m1.uasz.sn.services;

import m1.uasz.sn.dao.UtilisateurDAO;
import m1.uasz.sn.models.Utilisateur;
import m1.uasz.sn.utils.SessionManager;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UtilisateurService {
    private UtilisateurDAO utilisateurDAO;
    private Utilisateur utilisateurConnecte;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.create(utilisateur);
    }

    public Utilisateur trouverUtilisateur(Long id) {
        return utilisateurDAO.findById(id);
    }

    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurDAO.findAll();
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        String password = utilisateur.getPassword();

        // Vérifier si le mot de passe est déjà haché
        if (!password.startsWith("$2a$") && !password.startsWith("$2b$") && !password.startsWith("$2y$")) {
            // Hacher le mot de passe s'il n'est pas encore haché
            password = BCrypt.hashpw(password, BCrypt.gensalt());
        }

        utilisateur.setPassword(password);
        utilisateurDAO.update(utilisateur);
    }

    public void supprimerUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.delete(utilisateur);
    }

    /**
     * Enregistre un utilisateur après avoir vérifié s'il existe déjà
     */
    public void enregistrerUtilisateur(Utilisateur utilisateur, String password) {
        // Vérifier si un utilisateur avec cet email existe déjà
        Utilisateur existant = utilisateurDAO.findByEmail(utilisateur.getEmail());

        if (existant != null) {
            System.out.println("L'utilisateur avec l'email " + utilisateur.getEmail() + " existe déjà !");
            return;
        }

        // Hachage du mot de passe avant stockage
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        utilisateur.setPassword(hashedPassword);
        utilisateurDAO.create(utilisateur);
        System.out.println("Utilisateur enregistré avec succès !");
    }

    /**
     * Authentifie un utilisateur en vérifiant ses identifiants
     */
    public Utilisateur connexion(String email, String password) {
        Utilisateur utilisateur = utilisateurDAO.findByEmail(email);

        if (utilisateur != null && BCrypt.checkpw(password, utilisateur.getPassword())) {
            SessionManager.setUtilisateurConnecte(utilisateur);
            System.out.println("Connexion réussie pour " + email);
            return utilisateur;
        }

        System.out.println("Échec de connexion : email ou mot de passe incorrect !");
        return null;
    }

    public void deconnexion() {
        SessionManager.deconnecter();
        System.out.println("Déconnexion réussie.");
    }

    public Utilisateur getUtilisateurConnecte() {
        return SessionManager.getUtilisateurConnecte();
    }
}
