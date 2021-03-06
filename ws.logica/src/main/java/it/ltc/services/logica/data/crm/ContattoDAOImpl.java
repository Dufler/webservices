package it.ltc.services.logica.data.crm;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.common.ContattoDao;
import it.ltc.database.dao.common.IndirizzoDao;
import it.ltc.database.model.centrale.AziendaContatti;
import it.ltc.database.model.centrale.Contatto;
import it.ltc.database.model.centrale.Indirizzo;
import it.ltc.services.custom.exception.CustomException;

@Repository
public class ContattoDAOImpl extends ContattoDao implements ContattoDAO {
	
	@Autowired
	private IndirizzoDao daoIndirizzi;
	
	@Autowired
	private AssociazioneAziendaContattiDAO aziendaContattiDao;

	public ContattoDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME);
	}

	@Override
	public List<Contatto> trovaTutti() {
		List<Contatto> entities = findAll();
		return entities;
	}

	@Override
	public List<Contatto> trovaDaAzienda(int idAzienda) {
		List<AziendaContatti> lista = aziendaContattiDao.trovaDaAzienda(idAzienda);
		List<Contatto> contatti = new LinkedList<Contatto>();
		for (AziendaContatti match : lista) {
			Contatto contatto = trovaDaID(match.getContatto());
			if (contatto != null)
				contatti.add(contatto);
		}
		return contatti;
	}
	
	@Override
	public List<Contatto> trovaDaNome(String nome) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contatto> criteria = cb.createQuery(Contatto.class);
        Root<Contatto> member = criteria.from(Contatto.class);
        Predicate condizioneNome = cb.like(member.get("nome"), nome + "%");
        Predicate condizioneCognome = cb.like(member.get("cognome"), "%" + nome + "%");
        criteria.select(member).where(cb.or(condizioneNome, condizioneCognome));
		List<Contatto> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public Contatto inserisci(Contatto contatto) {
		Contatto esistente = trovaDaNomeCognome(contatto.getNome(), contatto.getCognome());
		if (esistente != null)
			throw new CustomException("Esiste già un contatto che si chiama così (Nome: " + esistente.getNome() + ", Cognome: " + esistente.getCognome() + ", Ruolo: " + esistente.getRuolo() + ")");
		Contatto entity = insert(contatto);
		return entity;
	}

	@Override
	public Indirizzo trovaIndirizzo(int idContatto) {
		Contatto contatto = trovaDaID(idContatto);
		Indirizzo indirizzo = contatto != null && contatto.getIndirizzo() != null ? daoIndirizzi.trovaDaID(contatto.getIndirizzo()) : null;
		return indirizzo;
	}

	@Override
	public Indirizzo salvaIndirizzo(int idContatto, Indirizzo indirizzo) {
		Indirizzo entity = daoIndirizzi.salvaIndirizzo(indirizzo);
		Contatto contatto = trovaDaID(idContatto);
		if (entity != null && contatto != null) {
			contatto.setIndirizzo(entity.getId());
			contatto = aggiorna(contatto);
			if (contatto == null) {
				entity = null;
			}
		}
		return entity;
	}

}
