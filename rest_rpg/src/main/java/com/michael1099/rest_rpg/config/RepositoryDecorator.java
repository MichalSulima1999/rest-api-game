package com.michael1099.rest_rpg.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// Tydzień 3, Wzorzec Decorator
// Dekorujemy JpaRepository, dzięki czemu możliwe jest przykładowo logowanie po zapisie encji
// Przykład podany w SkillService
@Slf4j
public class RepositoryDecorator<T, ID extends Serializable> implements JpaRepository<T, ID> {

    private final JpaRepository<T, ID> decoratedRepository;

    @Autowired
    public RepositoryDecorator(JpaRepository<T, ID> decoratedRepository) {
        this.decoratedRepository = decoratedRepository;
    }

    @Override
    public <S extends T> S save(S entity) {
        log.info("Trying to save entity {}", entity.getClass().getSimpleName());
        return decoratedRepository.save(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        log.info("Trying to find entity with id {}", id);
        return decoratedRepository.findById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return decoratedRepository.existsById(id);
    }

    @Override
    public void flush() {
        decoratedRepository.flush();
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return decoratedRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
        return decoratedRepository.saveAllAndFlush(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<T> entities) {
        decoratedRepository.deleteAllInBatch(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<ID> ids) {
        decoratedRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public void deleteAllInBatch() {
        decoratedRepository.deleteAllInBatch();
    }

    @Override
    public T getOne(ID id) {
        return decoratedRepository.getOne(id);
    }

    @Override
    public T getById(ID id) {
        return decoratedRepository.getById(id);
    }

    @Override
    public T getReferenceById(ID id) {
        return decoratedRepository.getReferenceById(id);
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        return decoratedRepository.findOne(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return decoratedRepository.findAll(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return decoratedRepository.findAll(example, sort);
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return decoratedRepository.findAll(example, pageable);
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return decoratedRepository.count(example);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return decoratedRepository.exists(example);
    }

    @Override
    public <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return decoratedRepository.findBy(example, queryFunction);
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return decoratedRepository.saveAll(entities);
    }

    @Override
    public List<T> findAll() {
        return decoratedRepository.findAll();
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return decoratedRepository.findAllById(ids);
    }

    @Override
    public long count() {
        return decoratedRepository.count();
    }

    @Override
    public void deleteById(ID id) {
        decoratedRepository.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        decoratedRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        decoratedRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        decoratedRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        decoratedRepository.deleteAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return decoratedRepository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return decoratedRepository.findAll(pageable);
    }
}
// Koniec Tydzień 3, Wzorzec Decorator
