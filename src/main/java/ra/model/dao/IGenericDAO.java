package ra.model.dao;

import ra.model.entity.User;

import java.util.List;

public interface IGenericDAO<T, Id> {
    List<T> getAll();
    int save(T t);
    boolean delete(Id id);
    T getById(Id id);
}
