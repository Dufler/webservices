package it.ltc.services.logica.data.crm;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.Azienda;
import it.ltc.database.model.centrale.AziendaBrand;
import it.ltc.database.model.centrale.AziendaContatti;

@Repository
public class AziendaDAOImpl extends CRUDDao<Azienda> implements AziendaDAO {
	
	@Autowired
	private AssociazioneAziendaBrandDAO aziendaBrandDao;
	
	@Autowired
	private AssociazioneAziendaContattiDAO aziendaContattiDao;

	public AziendaDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, Azienda.class);
	}

	@Override
	public List<Azienda> trovaTutte() {
		List<Azienda> entities = findAll();
		return entities;
	}

	@Override
	public List<Azienda> trovaDaContatto(int idContatto) {
//		EntityManager em = getManager();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<AziendaContatti> criteria = cb.createQuery(AziendaContatti.class);
//        Root<AziendaContatti> member = criteria.from(AziendaContatti.class);
//        criteria.select(member).where(cb.equal(member.get("contatto"), idContatto));
//		List<AziendaContatti> lista = em.createQuery(criteria).getResultList();
//		em.close();
		List<AziendaContatti> lista = aziendaContattiDao.trovaDaContatto(idContatto);
		List<Azienda> aziende = new LinkedList<Azienda>();
		for (AziendaContatti match : lista) {
			Azienda azienda = trova(match.getAzienda());
			if (azienda != null)
				aziende.add(azienda);
		}
		return aziende;
	}
	
	@Override
	public List<Azienda> trovaDaBrand(int idBrand) {
//		EntityManager em = getManager();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<AziendaBrand> criteria = cb.createQuery(AziendaBrand.class);
//        Root<AziendaBrand> member = criteria.from(AziendaBrand.class);
//        criteria.select(member).where(cb.equal(member.get("brand"), idBrand));
//		List<AziendaBrand> lista = em.createQuery(criteria).getResultList();
//		em.close();
		List<AziendaBrand> lista = aziendaBrandDao.trovaDaBrand(idBrand);
		List<Azienda> aziende = new LinkedList<Azienda>();
		for (AziendaBrand match : lista) {
			Azienda azienda = trova(match.getAzienda());
			if (azienda != null)
				aziende.add(azienda);
		}
		return aziende;
	}
	
	@Override
	public List<Azienda> trovaDaNome(String nome) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Azienda> criteria = cb.createQuery(Azienda.class);
        Root<Azienda> member = criteria.from(Azienda.class);
        criteria.select(member).where(cb.like(member.get("ragioneSociale"), "%" + nome + "%"));
		List<Azienda> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public Azienda trova(int id) {
		Azienda entity = findByID(id);
		return entity;
	}

	@Override
	public Azienda inserisci(Azienda azienda) {
		Azienda entity = insert(azienda);
		return entity;
	}

	@Override
	public Azienda aggiorna(Azienda azienda) {
		Azienda entity = update(azienda, azienda.getId());
		return entity;
	}

	@Override
	public Azienda elimina(Azienda azienda) {
		Azienda entity = delete(azienda.getId());
		return entity;
	}

	@Override
	protected void updateValues(Azienda oldEntity, Azienda entity) {
		oldEntity.setAppetibile(entity.getAppetibile());
		oldEntity.setEmail(entity.getEmail());
		oldEntity.setIndirizzo(entity.getIndirizzo());
		oldEntity.setInTrattiva(entity.getInTrattiva());
		oldEntity.setPartitaIva(entity.getPartitaIva());
		oldEntity.setRagioneSociale(entity.getRagioneSociale());
		oldEntity.setSitoWeb(entity.getSitoWeb());
		oldEntity.setTelefono(entity.getTelefono());
		oldEntity.setTipoLogistica(entity.getTipoLogistica());
		oldEntity.setValutazione(entity.getValutazione());
	}

}
