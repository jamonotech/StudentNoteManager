package m1.uasz.sn.dao;

import m1.uasz.sn.models.Etudiant;

public class EtudiantDAO extends GenericDAO<Etudiant, String> {
    public EtudiantDAO() {
        super(Etudiant.class);
    }
}
