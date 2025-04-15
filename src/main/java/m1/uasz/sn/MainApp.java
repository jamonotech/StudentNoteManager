package m1.uasz.sn;

import m1.uasz.sn.models.ResponsablePedagogique;
import m1.uasz.sn.ui.LoginFrame;

import m1.uasz.sn.services.UtilisateurService;
import m1.uasz.sn.models.Utilisateur;

public class MainApp {
    public static void main(String[] args) {
        initialiserDonnees();
        new LoginFrame();
    }

    private static void initialiserDonnees() {
        UtilisateurService service = new UtilisateurService();

        boolean responsableExiste = service
                .listerUtilisateurs()
                .stream()
                .anyMatch(u -> "RESPONSABLE".equalsIgnoreCase(u.getRole()));

        if (!responsableExiste) {
            Utilisateur responsable = new ResponsablePedagogique(
                    "Responsable",
                    "Responsable",
                    "responsable@zig-uasz.sn",
                    "responsable"
            );
            responsable.setNom("Responsable");
            responsable.setPrenom("Responsable");
            responsable.setRole("responsable".toUpperCase());
            responsable.setPassword("responsable");
            service.enregistrerUtilisateur(responsable, "responsable");
            service.connexion("responsable@zig-uasz.sn", "responsable");

            System.out.println("Utilisateur RESPONSABLE ajouté par défaut.");
        }
    }
}
