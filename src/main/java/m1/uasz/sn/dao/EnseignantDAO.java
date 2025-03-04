package m1.uasz.sn.dao;

import m1.uasz.sn.models.Enseignant;

public class EnseignantDAO extends GenericDAO<Enseignant, String> {
    public EnseignantDAO() {
        super(Enseignant.class);
    }
}
