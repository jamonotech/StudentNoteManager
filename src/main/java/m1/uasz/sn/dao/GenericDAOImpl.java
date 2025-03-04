package m1.uasz.sn.dao;

import java.util.List;

public interface GenericDAOImpl<T, ID> {
    void create(T entity);
    T findById(ID id);
    List<T> findAll();
    void update(T entity);
    void delete(T entity);
}