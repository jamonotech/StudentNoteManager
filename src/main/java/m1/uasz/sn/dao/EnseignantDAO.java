package m1.uasz.sn.dao;

import m1.uasz.sn.models.Enseignant;

public class EnseignantDAO extends GenericDAO<Enseignant, String> {
    public EnseignantDAO() {
        super(Enseignant.class);
    }

    public void affecterEnseignantAModule(Long enseignantId, Long moduleId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'affecterEnseignantAModule'");
    }
}
