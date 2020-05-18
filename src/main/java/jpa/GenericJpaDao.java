package jpa;

import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class GenericJpaDao<T>
{
    protected Class<T> entityClass;
    protected EntityManager entityManager;

    public GenericJpaDao()
    {
        Class clazz = ! getClass().getName().contains("$$EnhancerByGuice$$") ? getClass() : getClass().getSuperclass(); // dirty Guice trick
        entityClass = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void persist(T entity) {
        entityManager.persist(entity);
    }

    @Transactional
    public Optional<T> find(Object primaryKey)
    {
        return Optional.ofNullable(entityManager.find(entityClass, primaryKey));
    }

    @Transactional
    public List<T> findAll()
    {
        TypedQuery<T> typedQuery = entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
        return typedQuery.getResultList();
    }

    @Transactional
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Transactional
    public void update(T entity) {
        entityManager.merge(entity);
    }
}
