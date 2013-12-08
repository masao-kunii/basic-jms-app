package basic.jms.app.service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import basic.jms.app.model.Log;


/**
 * 
 */
@Stateless
public class LogBean {
  @PersistenceContext(unitName = "default")
  private EntityManager em;
  @Inject
  private Logger log;

  public Log insert(String text) {
	log.fine(String.format("insert message\"%s\" to the log table.", text));
    Log entity = new Log();
    entity.setText(text);
    entity.setEntryDate(new Date());

    em.persist(entity);
    return entity;
  }

  public boolean deleteById(long id) {
    Log entity = em.find(Log.class, id);
    if (entity == null) {
      return false;
    }
    em.remove(entity);
    return true;
  }

  public Log findById(long id) {
    TypedQuery<Log> findByIdQuery = em
        .createQuery(
            "SELECT a FROM Log a WHERE a.id = :entityId",
            Log.class);
    findByIdQuery.setParameter("entityId", id);
    Log entity = findByIdQuery.getSingleResult();
    return entity;
  }

  public List<Log> listAll() {
    final List<Log> results = em.createQuery(
        "SELECT a FROM Log a", Log.class)
        .getResultList();
    return results;
  }

  public Log update(Log entity) {
    entity = em.merge(entity);
    return entity;
  }
}