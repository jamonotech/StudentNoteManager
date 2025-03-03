package m1.uasz.sn.dao;

import java.util.List;

import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Module;

public class ModuleDAO extends GenericDAO<Module, Long> {
    public ModuleDAO() {
        super(Module.class);
    }

    public List<Etudiant> getEtudiants(Long moduleId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEtudiants'");
    }
}
