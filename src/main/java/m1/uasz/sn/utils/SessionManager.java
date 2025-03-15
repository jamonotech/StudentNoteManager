package m1.uasz.sn.utils;

import lombok.Getter;
import lombok.Setter;
import m1.uasz.sn.models.Utilisateur;

public class SessionManager {
    @Getter
    @Setter
    private static Utilisateur utilisateurConnecte;

    private SessionManager() { }

    public static void deconnecter() {
        utilisateurConnecte = null;
    }
}
