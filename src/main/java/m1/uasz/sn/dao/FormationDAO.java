package m1.uasz.sn.dao;

import java.util.List;

import m1.uasz.sn.models.Enseignant;
import m1.uasz.sn.models.Formation;
import m1.uasz.sn.models.Module;

public class FormationDAO extends GenericDAO<Formation, Long> {
    public FormationDAO() {
        super(Formation.class);
    }

    public List<Module> getModules(Long formationId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getModules'");
    }

    public List<Enseignant> getEnseignants(Long formationId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnseignants'");
    }
}
