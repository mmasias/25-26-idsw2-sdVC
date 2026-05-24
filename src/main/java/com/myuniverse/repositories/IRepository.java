package com.myuniverse.repositories;

import java.util.List;

public interface IRepository<T> {
    List<T> findAll();
    T findById(String id);
    T save(T entity);
    boolean update(T entity);
    boolean deleteById(String id);
}
