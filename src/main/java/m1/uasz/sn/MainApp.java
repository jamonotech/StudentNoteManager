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
            Utilisateur responsable = new ResponsablePedagogique();
            responsable.setNom("Admin");
            responsable.setPrenom("Principal");
            responsable.setEmail("admin@zig-uasz.sn");
            responsable.setRole("RESPONSABLE");

            String motDePasse = "admin"; // Tu peux forcer un changement au 1er login si besoin
            service.enregistrerUtilisateur(responsable, motDePasse);

            System.out.println("Utilisateur RESPONSABLE ajouté par défaut.");
        }
    }
}
