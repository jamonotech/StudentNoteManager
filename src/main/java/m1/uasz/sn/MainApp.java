package m1.uasz.sn;

import m1.uasz.sn.dao.UtilisateurDAO;
import m1.uasz.sn.models.Utilisateur;
import m1.uasz.sn.services.UtilisateurService;
import m1.uasz.sn.ui.LoginFrame;

public class MainApp {
    public static void main(String[] args) {
        UtilisateurService utilisateurService = new UtilisateurService(new UtilisateurDAO());

        // Vérifier si l'utilisateur existe déjà
        String email = "admin@gmail.com";
        String password = "admin";

        if (utilisateurService.inscrire(email, password, Utilisateur.Role.RESPONSABLE_PEDAGOGIQUE)) {
            System.out.println("Utilisateur créé avec succès !");
        } else {
            System.out.println("L'utilisateur existe déjà.");
        }

        new LoginFrame();;
    }
}
