package it.ltc.services.logica.data.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.AziendaBrand;
import it.ltc.database.model.centrale.AziendaBrandPK;
import it.ltc.services.custom.exception.CustomException;

@Repository
public class AssociazioneAziendaBrandDAOImpl extends CRUDDao<AziendaBrand> implements AssociazioneAziendaBrandDAO {

	public AssociazioneAziendaBrandDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, AziendaBrand.class);
	}

	@Override
	public List<AziendaBrand> trovaTutti() {
		List<AziendaBrand> entities = findAll();
		return entities;
	}

	@Override
	public List<AziendaBrand> trovaDaAzienda(int idAzienda) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AziendaBrand> criteria = cb.createQuery(AziendaBrand.class);
        Root<AziendaBrand> member = criteria.from(AziendaBrand.class);
        criteria.select(member).where(cb.equal(member.get("azienda"), idAzienda));
		List<AziendaBrand> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public List<AziendaBrand> trovaDaBrand(int idBrand) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AziendaBrand> criteria = cb.createQuery(AziendaBrand.class);
        Root<AziendaBrand> member = criteria.from(AziendaBrand.class);
        criteria.select(member).where(cb.equal(member.get("brand"), idBrand));
		List<AziendaBrand> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public AziendaBrand trova(int idAzienda, int idBrand) {
		AziendaBrandPK id = new AziendaBrandPK();
		id.setAzienda(idAzienda);
		id.setBrand(idBrand);
		AziendaBrand entity = findByID(id);
		return entity;
	}

	@Override
	public AziendaBrand inserisci(AziendaBrand associazione) {
		//Controllo se esiste già un'associazione fra l'azienda e il brand.
		AziendaBrand match = trova(associazione.getAzienda(), associazione.getBrand());
		if (match != null)
			throw new CustomException("L'azienda e il brand selezionati sono già associati!");
		AziendaBrand entity = insert(associazione);
		return entity;
	}

	@Override
	public AziendaBrand aggiorna(AziendaBrand associazione) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AziendaBrand elimina(AziendaBrand associazione) {
		AziendaBrandPK id = new AziendaBrandPK();
		id.setAzienda(associazione.getAzienda());
		id.setBrand(associazione.getBrand());
		AziendaBrand entity = delete(id);
		return entity;
	}

	@Override
	protected void updateValues(AziendaBrand oldEntity, AziendaBrand entity) {
		//DO NOTHING!
	}

}
