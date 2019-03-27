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

import it.ltc.database.dao.common.AziendaDao;
import it.ltc.database.dao.common.IndirizzoDao;
import it.ltc.database.model.centrale.Azienda;
import it.ltc.database.model.centrale.AziendaBrand;
import it.ltc.database.model.centrale.AziendaContatti;
import it.ltc.database.model.centrale.Indirizzo;
import it.ltc.services.custom.exception.CustomException;

@Repository
public class AziendaDAOImpl extends AziendaDao implements AziendaDAO {
	
	private static final Logger logger = Logger.getLogger("AziendaDAOImpl");
	
	@Autowired
	private IndirizzoDao daoIndirizzi;
	
	@Autowired
	private AssociazioneAziendaBrandDAO aziendaBrandDao;
	
	@Autowired
	private AssociazioneAziendaContattiDAO aziendaContattiDao;

	public AziendaDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME);
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
			Azienda azienda = trovaDaID(match.getAzienda());
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
			Azienda azienda = trovaDaID(match.getAzienda());
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
	public Azienda inserisci(Azienda azienda) {
		//Controllo che non esista già un azienda con la stessa ragione sociale o p. iva
		Azienda esistente = trovaDaRagioneSociale(azienda.getRagioneSociale());
		if (esistente != null)
			throw new CustomException("Esiste già un'azienda con la stessa ragione sociale. (Azienda: " + esistente.getRagioneSociale() + ", P.IVA/C.F. " + esistente.getPartitaIva() + ")");
		esistente = trovaDaPIVA(azienda.getPartitaIva());
		if (esistente != null)
			throw new CustomException("Esiste già un'azienda con la stessa P. IVA/C.F. (Azienda: " + esistente.getRagioneSociale() + ", P.IVA/C.F. " + esistente.getPartitaIva() + ")");
		Azienda entity = insert(azienda);
		return entity;
	}

	@Override
	public Indirizzo trovaIndirizzo(int idAzienda) {
		Azienda azienda = trovaDaID(idAzienda);
		Indirizzo indirizzo = azienda != null && azienda.getIndirizzo() != null ? daoIndirizzi.trovaDaID(azienda.getIndirizzo()) : null;
		return indirizzo;
	}

	@Override
	public Indirizzo salvaIndirizzo(int idAzienda, Indirizzo indirizzo) {
		Indirizzo entity = daoIndirizzi.salvaIndirizzo(indirizzo);
		Azienda azienda = trovaDaID(idAzienda);
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
