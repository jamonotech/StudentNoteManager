package m1.uasz.sn;

import m1.uasz.sn.dao.UtilisateurDAO;
import m1.uasz.sn.models.ResponsablePedagogique;
import m1.uasz.sn.models.Utilisateur;
import m1.uasz.sn.services.UtilisateurService;
import m1.uasz.sn.ui.LoginFrame;

public class MainApp {
    public static void main(String[] args) {
        UtilisateurService utilisateurService = new UtilisateurService();

        String email = "admin@gmail.com";
        String password = "admin";
        String prenom = "admin";
        String nom = "admin";

        // Vérifier si l'utilisateur existe avant de le recréer
        Utilisateur user = utilisateurService.connexion(email, password);

        if (user == null) {
            ResponsablePedagogique responsable = new ResponsablePedagogique(nom, prenom, email, password);
            utilisateurService.enregistrerUtilisateur(responsable, password);
            user = utilisateurService.connexion(email, password);
        }

        if (user != null) {
            System.out.println("Utilisateur connecté avec succès !");
        } else {
            System.out.println("Échec de connexion !");
        }

        new LoginFrame();
    }
}
