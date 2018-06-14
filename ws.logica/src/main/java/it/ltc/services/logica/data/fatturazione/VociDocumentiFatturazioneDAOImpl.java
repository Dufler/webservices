package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.FatturaVoce;
import it.ltc.database.model.centrale.Spedizione;
import it.ltc.database.model.centrale.SpedizioneGiacenza;
import it.ltc.database.model.centrale.enumcondivise.Fatturazione;
import it.ltc.services.logica.model.fatturazione.ElementoFatturazioneJSON;

@Repository
public class VociDocumentiFatturazioneDAOImpl extends CRUDDao<FatturaVoce> implements VociDocumentiFatturazioneDAO {
	
	private static final Logger logger = Logger.getLogger("VociDocumentiFatturazioneDAOImpl");

	public VociDocumentiFatturazioneDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, FatturaVoce.class);
	}

	@Override
	public List<FatturaVoce> trovaTuttePerFattura(int idDocumento) {
//		EntityManager em = getManager();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//      CriteriaQuery<FatturaVoce> criteria = cb.createQuery(FatturaVoce.class);
//      Root<FatturaVoce> member = criteria.from(FatturaVoce.class);
//      criteria.select(member).where(cb.equal(member.get("idDocumento"), idDocumento));
//		List<FatturaVoce> lista = em.createQuery(criteria).getResultList();
//		em.close();
//      return lista;
		List<FatturaVoce> lista = findAllEqualTo("idDocumento", idDocumento);
		return lista;
	}

	@Override
	public FatturaVoce trova(int id) {
		FatturaVoce entity = findByID(id);
		return entity;
	}

	@Override
	public FatturaVoce inserisci(FatturaVoce voce) {
		FatturaVoce entity = insert(voce);
		return entity;
	}

	@Override
	public FatturaVoce aggiorna(FatturaVoce voce) {
		FatturaVoce entity = update(voce, voce.getId());
		return entity;
	}

	@Override
	public FatturaVoce elimina(FatturaVoce voce) {
		FatturaVoce entity = delete(voce.getId());
		return entity;
	}

	@Override
	protected void updateValues(FatturaVoce oldEntity, FatturaVoce entity) {
		oldEntity.setCostoUnitario(entity.getCostoUnitario());
		oldEntity.setIdAmbito(entity.getIdAmbito());
		oldEntity.setIdCommessa(entity.getIdCommessa());
		oldEntity.setIdDocumento(entity.getIdDocumento());
		oldEntity.setIdListino(entity.getIdListino());
		oldEntity.setIdRiferimento(entity.getIdRiferimento());
		oldEntity.setIdSottoAmbito(entity.getIdSottoAmbito());
		oldEntity.setIdVoce(entity.getIdVoce());
		oldEntity.setImporto(entity.getImporto());
		oldEntity.setNote(entity.getNote());
		oldEntity.setQuantita(entity.getQuantita());
	}
	
	
	
	/**
	 * Eseguo operazioni di aggiornamento diverse in base all'ambito di fatturazione.
	 * @param voce La voce di fatturazione che contiene le informazioni necessarie all'aggiornamento del dato base.
	 */
	@Override
	public boolean aggiornaStatoFatturazioneDatoBase(int ambito, int riferimento, double totale, Fatturazione stato, EntityManager em) {
		boolean esito; //TODO - Mettere delle costanti al posto dei numeri.
		switch (ambito) {
			case 1 : esito = aggiornaSpedizione(riferimento, totale, stato, em); break;
			case 2 : esito = aggiornaGiacenza(riferimento, totale, stato, em); break;
			default : esito = false; break;
		}
		return esito;
	}
	
	private boolean aggiornaGiacenza(int riferimento, double totale, Fatturazione stato, EntityManager em) {
		boolean esito;
		SpedizioneGiacenza giacenza = em.find(SpedizioneGiacenza.class, riferimento);
		if (giacenza != null) {
			giacenza.setFatturazione(stato);
			giacenza.setRicavo(totale);
			em.merge(giacenza);
			esito = true;
		} else {
			esito = false;
			logger.error("Impossibile aggiornare lo stato di fatturazione, giacenza non trovata (ID: " + riferimento +")");
		}
		return esito;
	}

	private boolean aggiornaSpedizione(int riferimento, double totale, Fatturazione stato, EntityManager em) {
		boolean esito;
		Spedizione spedizione = em.find(Spedizione.class, riferimento);
		if (spedizione != null) {
			spedizione.setFatturazione(stato);
			spedizione.setRicavo(totale);
			em.merge(spedizione);
			esito = true;
		} else {
			esito = false;
			logger.error("Impossibile aggiornare lo stato di fatturazione, spedizione non trovata (ID: " + riferimento +")");
		}
		return esito;
	}

	@Override
	public boolean inserisciVoci(List<ElementoFatturazioneJSON> elementi) {
		boolean esito;
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			for (ElementoFatturazioneJSON elemento : elementi) {
				aggiornaStatoFatturazioneDatoBase(elemento.getAmbito(), elemento.getRiferimento(), elemento.getTotale(), Fatturazione.FATTURATA, em);
				for (FatturaVoce voce : elemento.getVoci()) {
					em.persist(voce);
				}
			}
			t.commit();
			esito = true;
		} catch (Exception e) {
			esito = false;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return esito;
	}

}
