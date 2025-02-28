package m1.uasz.sn.dao;

import m1.uasz.sn.models.Formation;

public class FormationDAO extends GenericDAO<Formation, Long> {
    public FormationDAO() {
        super(Formation.class);
    }
}
