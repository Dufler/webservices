package it.ltc.services.logica.data.cdg;

import java.util.LinkedList;
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
import it.ltc.database.model.centrale.CdgPezzoEventoPK;
import it.ltc.database.model.centrale.json.CdgPezzoEventoJSON;

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
				for (CdgPezzoEventoJSON spacchettamento : entity.getSpacchettamenti()) {
					spacchettamento.setPezzo(entity.getId());
					CdgPezzoEvento abbinamento = deserializza(spacchettamento);
					em.persist(abbinamento);
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
					List<CdgPezzoEventoJSON> spacchettamenti = entity.getSpacchettamenti();
					if (spacchettamenti != null && !spacchettamenti.isEmpty()) {
						//Elimino tutti gli spacchettamenti già presenti
						CriteriaBuilder cb = em.getCriteriaBuilder();
				        CriteriaQuery<CdgPezzoEvento> criteria = cb.createQuery(CdgPezzoEvento.class);
				        Root<CdgPezzoEvento> member = criteria.from(CdgPezzoEvento.class);
				        criteria.select(member).where(cb.equal(member.get("id").get("pezzo"), oldEntity.getId()));
						List<CdgPezzoEvento> lista = em.createQuery(criteria).getResultList();
						for (CdgPezzoEvento abbinamento : lista) {
							em.remove(abbinamento);
						}
						//Inserisco quelli che mi hanno dato
						for (CdgPezzoEventoJSON spacchettamento : spacchettamenti) {
							CdgPezzoEvento abbinamento = deserializza(spacchettamento);
							em.persist(abbinamento);
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
		List<CdgPezzoEventoJSON> abbinamenti = new LinkedList<>();
		for (CdgPezzoEvento spacchettamento : spacchettamenti)
			abbinamenti.add(serializza(spacchettamento));
		pezzo.setSpacchettamenti(abbinamenti);
	}
	
	protected List<CdgPezzoEvento> trovaSpacchettamentoDaPezzo(CdgPezzo pezzo) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CdgPezzoEvento> criteria = cb.createQuery(CdgPezzoEvento.class);
        Root<CdgPezzoEvento> member = criteria.from(CdgPezzoEvento.class);
        criteria.select(member).where(cb.equal(member.get("id").get("pezzo"), pezzo.getId()));
		List<CdgPezzoEvento> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}
	
	protected CdgPezzoEvento deserializza(CdgPezzoEventoJSON json) {
		CdgPezzoEvento entity;
		if (json != null) {
			entity = new CdgPezzoEvento();
			CdgPezzoEventoPK key = new CdgPezzoEventoPK();
			key.setPezzo(json.getPezzo());
			key.setEvento(json.getEvento());
			entity.setId(key);
			entity.setCosto(json.getCosto());
			entity.setRicavo(json.getRicavo());
		} else {
			entity = null;
		}
		return entity;
	}
	
	protected CdgPezzoEventoJSON serializza(CdgPezzoEvento entity) {
		CdgPezzoEventoJSON json;
		if (entity != null) {
			json = new CdgPezzoEventoJSON();
			json.setPezzo(entity.getId().getPezzo());
			json.setEvento(entity.getId().getEvento());
			json.setCosto(entity.getCosto());
			json.setRicavo(entity.getRicavo());
		} else {
			json = null;
		}
		return json;
	}

}
