package it.ltc.services.logica.data.trasporti;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.dao.common.model.CriteriUltimaModifica;
import it.ltc.database.model.centrale.SpedizioneContrassegno;

@Repository
public class ContrassegniDAOImpl extends CRUDDao<SpedizioneContrassegno> implements ContrassegniDAO {

	public ContrassegniDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, SpedizioneContrassegno.class);
	}

	@Override
	public List<SpedizioneContrassegno> trovaTutti() {
		List<SpedizioneContrassegno> lista = findAll();
		return lista;
	}

	@Override
	public SpedizioneContrassegno trova(int id) {
		SpedizioneContrassegno contrassegno = findByID(id);
		return contrassegno;
	}
	
	@Override
	public List<SpedizioneContrassegno> trovaDaUltimaModifica(CriteriUltimaModifica criteri) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SpedizioneContrassegno> criteria = cb.createQuery(SpedizioneContrassegno.class);
        Root<SpedizioneContrassegno> member = criteria.from(SpedizioneContrassegno.class);
        criteria.select(member).where(cb.greaterThan(member.get("dataUltimaModifica"), criteri.getDataUltimaModifica()));
        List<SpedizioneContrassegno> contrassegni = em.createQuery(criteria).getResultList();
        em.close();
        return contrassegni;
	}

	@Override
	public SpedizioneContrassegno inserisci(SpedizioneContrassegno contrassegno) {
		SpedizioneContrassegno entity = insert(contrassegno);
		return entity;
	}

	@Override
	public SpedizioneContrassegno aggiorna(SpedizioneContrassegno contrassegno) {
		SpedizioneContrassegno entity = update(contrassegno, contrassegno.getIdSpedizione());
		return entity;
	}

	@Override
	public SpedizioneContrassegno elimina(SpedizioneContrassegno contrassegno) {
		SpedizioneContrassegno entity = delete(contrassegno.getIdSpedizione());
		return entity;
	}

	@Override
	protected void updateValues(SpedizioneContrassegno oldEntity, SpedizioneContrassegno entity) {
		oldEntity.setAnnullato(entity.getAnnullato());
		oldEntity.setIdSpedizione(entity.getIdSpedizione());
		oldEntity.setTipo(entity.getTipo());
		oldEntity.setValore(entity.getValore());
		oldEntity.setValuta(entity.getValuta());
	}

}
