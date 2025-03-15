package m1.uasz.sn.services;

import m1.uasz.sn.dao.EtudiantDAO;
import m1.uasz.sn.dao.ResponsablePedagogiqueDAO;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.models.ResponsablePedagogique;

import java.util.List;
import java.util.stream.Collectors;

public class ResponsablePedagogiqueService {
    private ResponsablePedagogiqueDAO responsablePedagogiqueDAO;
    private EtudiantService etudiantService;
    private EtudiantDAO etudiantDAO;

    public ResponsablePedagogiqueService() {
        this.responsablePedagogiqueDAO = new ResponsablePedagogiqueDAO();
        this.etudiantService = new EtudiantService();
        this.etudiantDAO = new EtudiantDAO();
    }

    public void ajouterResponsable(ResponsablePedagogique responsable) {
        responsablePedagogiqueDAO.createResp(responsable);
    }

    public ResponsablePedagogique trouverResponsable(Long id) {
        return responsablePedagogiqueDAO.findById(id);
    }

    public List<ResponsablePedagogique> listerResponsables() {
        return responsablePedagogiqueDAO.findAll();
    }

    public void modifierResponsable(ResponsablePedagogique responsable) {
        responsablePedagogiqueDAO.update(responsable);
    }

    public void supprimerResponsable(ResponsablePedagogique responsable) {
        responsablePedagogiqueDAO.delete(responsable);
    }

    public void deliberer(Formation formation) {
        List<Etudiant> etudiants = etudiantDAO.findEtudiantsByFormation(formation);

        for (Etudiant etudiant : etudiants) {
            double moyenne = etudiantService.calculerMoyenneEtudiant(etudiant);
            if (moyenne == -1) {
                System.out.println("Délibération impossible : toutes les notes ne sont pas encore saisies.");
                return;
            }
            etudiant.setMoyenne(moyenne);
        }

        // Trier les étudiants par ordre de mérite
        List<Etudiant> admis = etudiants.stream()
                .filter(e -> e.getMoyenne() >= 10)
                .sorted((e1, e2) -> Double.compare(e2.getMoyenne(), e1.getMoyenne()))
                .collect(Collectors.toList());

        // Affichage des résultats
        System.out.println("Liste des étudiants admis :");
        for (Etudiant etudiant : admis) {
            System.out.println(etudiant.getIne() + " - " + etudiant.getPrenoms() + " " + etudiant.getNom() +
                    " - Moyenne: " + etudiant.getMoyenne() + " - Mention: " + getMention(etudiant.getMoyenne()));
        }
    }

    private String getMention(double moyenne) {
        if (moyenne >= 16) return "Très bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez bien";
        return "Passable";
    }
}
