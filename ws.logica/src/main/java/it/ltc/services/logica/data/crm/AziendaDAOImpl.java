package it.ltc.services.logica.data.crm;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.dao.common.IndirizzoDao;
import it.ltc.database.model.centrale.Azienda;
import it.ltc.database.model.centrale.AziendaBrand;
import it.ltc.database.model.centrale.AziendaContatti;
import it.ltc.database.model.centrale.Indirizzo;

@Repository
public class AziendaDAOImpl extends CRUDDao<Azienda> implements AziendaDAO {
	
	private static final Logger logger = Logger.getLogger("AziendaDAOImpl");
	
	@Autowired
	private IndirizzoDao daoIndirizzi;
	
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
		oldEntity.setDescrizione(entity.getDescrizione());
	}

	@Override
	public Indirizzo trovaIndirizzo(int idAzienda) {
		Azienda azienda = trova(idAzienda);
		Indirizzo indirizzo = azienda != null && azienda.getIndirizzo() != null ? daoIndirizzi.trovaDaID(azienda.getIndirizzo()) : null;
		return indirizzo;
	}

	@Override
	public Indirizzo salvaIndirizzo(int idAzienda, Indirizzo indirizzo) {
		Indirizzo entity = daoIndirizzi.salvaIndirizzo(indirizzo);
		Azienda azienda = trova(idAzienda);
		if (entity != null && azienda != null) {
			azienda.setIndirizzo(entity.getId());
			azienda = aggiorna(azienda);
			//Se l'aggiornamento non riesce imposto l'indirizzo a null per comunicare l'errore.
			if (azienda == null) {
				logger.warn("L'aggiornamento dell'indirizzo dell'azienda non è riuscito.");
				entity = null;
			}
		}
		return entity;
	}

}
