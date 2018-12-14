package it.ltc.services.logica.data.cdg;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.CdgPezzo;
import it.ltc.database.model.centrale.CdgPezzoEvento;

@Repository
public class PezzoDAOImpl extends CRUDDao<CdgPezzo> implements PezzoDAO {
	
	private static final Logger logger = Logger.getLogger("PezzoDAOImpl");

	public PezzoDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, CdgPezzo.class);
	}

	@Override
	public List<CdgPezzo> trovaTutte() {
		List<CdgPezzo> entities = findAll();
		for (CdgPezzo pezzo : entities) {
			recuperaSpacchettamentiAssociati(pezzo);
		}
		return entities;
	}

	@Override
	public CdgPezzo trova(int id) {
		CdgPezzo entity = findByID(id);
		if (entity != null)
			recuperaSpacchettamentiAssociati(entity);
		return entity;
	}
	
	@Override
	public CdgPezzo inserisci(CdgPezzo entity) {
		if (entity != null) {
			EntityManager em = getManager();
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.persist(entity);
				for (CdgPezzoEvento spacchettamento : entity.getSpacchettamenti()) {
					spacchettamento.setPezzo(entity.getId());
					em.persist(spacchettamento);
				}
				t.commit();
			} catch (Exception e) {
				logger.error(e);
				if (t != null && t.isActive())
					t.rollback();
				entity = null;
			} finally {
				em.close();
			}
		}
		return entity;
	}

	@Override
	public CdgPezzo aggiorna(CdgPezzo entity) {
		if (entity != null) {
			EntityManager em = getManager();
			CdgPezzo oldEntity = em.find(CdgPezzo.class, entity.getId());
			//Se l'ho trovata provvedo all'aggiornamento
			if (oldEntity != null) {
				updateValues(oldEntity, entity);
				EntityTransaction t = em.getTransaction();
				try {
					t.begin();
					em.merge(oldEntity);
					//Se mi hanno dato gli spacchettameni allora aggiorno anche quelli
					List<CdgPezzoEvento> spacchettamenti = entity.getSpacchettamenti();
					if (spacchettamenti != null && !spacchettamenti.isEmpty()) {
						//Elimino tutti gli spacchettamenti già presenti
						CriteriaBuilder cb = em.getCriteriaBuilder();
				        CriteriaQuery<CdgPezzoEvento> criteria = cb.createQuery(CdgPezzoEvento.class);
				        Root<CdgPezzoEvento> member = criteria.from(CdgPezzoEvento.class);
				        criteria.select(member).where(cb.equal(member.get("pezzo"), oldEntity.getId()));
						List<CdgPezzoEvento> lista = em.createQuery(criteria).getResultList();
						for (CdgPezzoEvento abbinamento : lista) {
							em.remove(abbinamento);
						}
						//Inserisco quelli che mi hanno dato
						for (CdgPezzoEvento spacchettamento : spacchettamenti) {
							em.persist(spacchettamento);
						}
					}
					t.commit();
				} catch (Exception e) {
					logger.error(e);
					if (t != null && t.isActive())
						t.rollback();
					entity = null;
				} finally {
					em.close();
				}
			} else {
				em.close();
			}
		} else {
			entity = null;
		}
		return entity;
	}

	@Override
	public CdgPezzo elimina(CdgPezzo pezzo) {
		CdgPezzo entity = delete(pezzo.getId()); //Non ho bisogno di fare altro, in cascata si porta dietro tutti gli spacchettamenti.
		return entity;
	}

	@Override
	protected void updateValues(CdgPezzo oldEntity, CdgPezzo entity) {
		oldEntity.setCategoriaMerceologica(entity.getCategoriaMerceologica());
		oldEntity.setCommessa(entity.getCommessa());
		oldEntity.setCosto(entity.getCosto());
		oldEntity.setRicavo(entity.getRicavo());
	}
	
	protected void recuperaSpacchettamentiAssociati(CdgPezzo pezzo) {
		List<CdgPezzoEvento> spacchettamenti = trovaSpacchettamentoDaPezzo(pezzo);
		pezzo.setSpacchettamenti(spacchettamenti);
	}
	
	protected List<CdgPezzoEvento> trovaSpacchettamentoDaPezzo(CdgPezzo pezzo) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CdgPezzoEvento> criteria = cb.createQuery(CdgPezzoEvento.class);
        Root<CdgPezzoEvento> member = criteria.from(CdgPezzoEvento.class);
        criteria.select(member).where(cb.equal(member.get("pezzo"), pezzo.getId()));
		List<CdgPezzoEvento> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

}
