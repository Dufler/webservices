package it.ltc.services.logica.data.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.AziendaNote;

@Repository
public class AziendeNoteDAOImpl extends CRUDDao<AziendaNote> implements AziendeNoteDAO {

	public AziendeNoteDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, AziendaNote.class);
	}

	@Override
	public List<AziendaNote> trovaTutti() {
		List<AziendaNote> entitites = findAll();
		return entitites;
	}

	@Override
	public List<AziendaNote> trovaDaAzienda(int idAzienda) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AziendaNote> criteria = cb.createQuery(AziendaNote.class);
        Root<AziendaNote> member = criteria.from(AziendaNote.class);
        criteria.select(member).where(cb.equal(member.get("azienda"), idAzienda));
		List<AziendaNote> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public List<AziendaNote> trovaDaContatto(int idContatto) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AziendaNote> criteria = cb.createQuery(AziendaNote.class);
        Root<AziendaNote> member = criteria.from(AziendaNote.class);
        criteria.select(member).where(cb.equal(member.get("contatto"), idContatto));
		List<AziendaNote> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}
	
	@Override
	public List<AziendaNote> trovaDaParola(String parola) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AziendaNote> criteria = cb.createQuery(AziendaNote.class);
        Root<AziendaNote> member = criteria.from(AziendaNote.class);
        criteria.select(member).where(cb.like(member.get("note"), "%" + parola + "%"));
		List<AziendaNote> lista = em.createQuery(criteria).getResultList();
		em.close();
        return lista;
	}

	@Override
	public AziendaNote trova(int id) {
		AziendaNote entity = findByID(id);
		return entity;
	}

	@Override
	public AziendaNote inserisci(AziendaNote note) {
		AziendaNote entity = insert(note);
		return entity;
	}

	@Override
	public AziendaNote aggiorna(AziendaNote note) {
		AziendaNote entity = update(note, note.getId());
		return entity;
	}

	@Override
	public AziendaNote elimina(AziendaNote note) {
		AziendaNote entity = delete(note.getId());
		return entity;
	}

	@Override
	protected void updateValues(AziendaNote oldEntity, AziendaNote entity) {
		oldEntity.setAutore(entity.getAutore());
		oldEntity.setAzienda(entity.getAzienda());
		oldEntity.setContatto(entity.getContatto());
		oldEntity.setDataNota(entity.getDataNota());
		oldEntity.setNote(entity.getNote());
	}

}
