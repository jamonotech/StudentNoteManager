package m1.uasz.sn.dao;

import m1.uasz.sn.models.Module;

public class ModuleDAO extends GenericDAO<Module, Long> {
    public ModuleDAO() {
        super(Module.class);
    }
}
