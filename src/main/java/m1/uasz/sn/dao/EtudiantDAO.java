package m1.uasz.sn.dao;

import m1.uasz.sn.models.Etudiant;

public class EtudiantDAO extends GenericDAO<Etudiant, String> {
    public EtudiantDAO() {
        super(Etudiant.class);
    }

    public void inscrireEtudiantAFormation(Long etudiantId, Long formationId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inscrireEtudiantAFormation'");
    }
}
