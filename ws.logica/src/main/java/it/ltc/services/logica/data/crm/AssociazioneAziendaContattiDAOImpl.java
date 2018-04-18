package it.ltc.services.logica.data.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.AziendaContatti;
import it.ltc.database.model.centrale.AziendaContattiPK;
import it.ltc.services.custom.exception.CustomException;

@Repository
public class AssociazioneAziendaContattiDAOImpl extends CRUDDao<AziendaContatti> implements AssociazioneAziendaContattiDAO {

	public AssociazioneAziendaContattiDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, AziendaContatti.class);
	}

	@Override
	public List<AziendaContatti> trovaTutti() {
		List<AziendaContatti> entities = findAll();
		return entities;
	}

	@Override
	public List<AziendaContatti> trovaDaAzienda(int idAzienda) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AziendaContatti> criteria = cb.createQuery(AziendaContatti.class);
        Root<AziendaContatti> member = criteria.from(AziendaContatti.class);
        criteria.select(member).where(cb.equal(member.get("azienda"), idAzienda));
		List<AziendaContatti> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public List<AziendaContatti> trovaDaContatto(int idContatto) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AziendaContatti> criteria = cb.createQuery(AziendaContatti.class);
        Root<AziendaContatti> member = criteria.from(AziendaContatti.class);
        criteria.select(member).where(cb.equal(member.get("contatto"), idContatto));
		List<AziendaContatti> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public AziendaContatti trova(int idAzienda, int idContatto) {
		AziendaContattiPK id = new AziendaContattiPK();
		id.setAzienda(idAzienda);
		id.setContatto(idContatto);
		AziendaContatti entity = findByID(id);
		return entity;
	}

	@Override
	public AziendaContatti inserisci(AziendaContatti associazione) {
		//Controllo se l'associazione esiste già.
		AziendaContatti match = trova(associazione.getAzienda(), associazione.getContatto());
		if (match != null)
			throw new CustomException("L'azienda e il contatto selezionati sono già associati!");
		AziendaContatti entity = insert(associazione);
		return entity;
	}

	@Override
	public AziendaContatti aggiorna(AziendaContatti associazione) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AziendaContatti elimina(AziendaContatti associazione) {
		AziendaContattiPK id = new AziendaContattiPK();
		id.setAzienda(associazione.getAzienda());
		id.setContatto(associazione.getContatto());
		AziendaContatti entity = delete(id);
		return entity;
	}

	@Override
	protected void updateValues(AziendaContatti oldEntity, AziendaContatti entity) {
		//DO NOTHING!		
	}

}
