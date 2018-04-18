package it.ltc.services.logica.data.trasporti;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.SpedizioneGiacenza;
import it.ltc.services.logica.model.trasporti.CriteriUltimaModifica;

@Repository
public class GiacenzeDAOImpl extends CRUDDao<SpedizioneGiacenza> implements GiacenzeDAO {

	public GiacenzeDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, SpedizioneGiacenza.class);
	}

	@Override
	public List<SpedizioneGiacenza> trovaTutte() {
		List<SpedizioneGiacenza> lista = findAll();
		return lista;
	}

	@Override
	public SpedizioneGiacenza trova(int id) {
		SpedizioneGiacenza giacenza = findByID(id);
		return giacenza;
	}
	
	@Override
	public List<SpedizioneGiacenza> trovaDaUltimaModifica(CriteriUltimaModifica criteri) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SpedizioneGiacenza> criteria = cb.createQuery(SpedizioneGiacenza.class);
        Root<SpedizioneGiacenza> member = criteria.from(SpedizioneGiacenza.class);
        criteria.select(member).where(cb.greaterThan(member.get("dataUltimaModifica"), criteri.getDataUltimaModifica()));
        List<SpedizioneGiacenza> giacenze = em.createQuery(criteria).getResultList();
        em.close();
        return giacenze;
	}

	@Override
	public SpedizioneGiacenza inserisci(SpedizioneGiacenza giacenza) {
		SpedizioneGiacenza entity = insert(giacenza);
		return entity;
	}

	@Override
	public SpedizioneGiacenza aggiorna(SpedizioneGiacenza giacenza) {
		SpedizioneGiacenza entity = update(giacenza, giacenza.getId());
		return entity;
	}

	@Override
	public SpedizioneGiacenza elimina(SpedizioneGiacenza giacenza) {
		SpedizioneGiacenza entity = delete(giacenza.getId());
		return entity;
	}

	@Override
	protected void updateValues(SpedizioneGiacenza oldEntity, SpedizioneGiacenza entity) {
		oldEntity.setCosto(entity.getCosto());
		oldEntity.setDataApertura(entity.getDataApertura());
		oldEntity.setDataChiusura(entity.getDataChiusura());
		oldEntity.setFatturazione(entity.getFatturazione());
		oldEntity.setIdCommessa(entity.getIdCommessa());
		oldEntity.setIdDestinatario(entity.getIdDestinatario());
		oldEntity.setIdDocumento(entity.getIdDocumento());
		oldEntity.setIdMittente(entity.getIdMittente());
		oldEntity.setIdSpedizione(entity.getIdSpedizione());
		oldEntity.setLetteraDiVettura(entity.getLetteraDiVettura());
		oldEntity.setLetteraDiVetturaOriginale(entity.getLetteraDiVetturaOriginale());
		oldEntity.setNote(entity.getNote());
		oldEntity.setRicavo(entity.getRicavo());
	}

}
