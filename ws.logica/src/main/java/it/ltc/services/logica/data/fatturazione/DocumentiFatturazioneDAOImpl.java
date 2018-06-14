package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.FatturaDocumento;
import it.ltc.database.model.centrale.FatturaVoce;
import it.ltc.database.model.centrale.enumcondivise.Fatturazione;

@Repository
public class DocumentiFatturazioneDAOImpl extends CRUDDao<FatturaDocumento> implements DocumentiFatturazioneDAO {
	
	private static final Logger logger = Logger.getLogger("DocumentiFatturazioneDAOImpl");
	
	@Autowired
	private VociDocumentiFatturazioneDAO daoVoci;

	public DocumentiFatturazioneDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, FatturaDocumento.class);
	}

	@Override
	public List<FatturaDocumento> trovaTutti() {
		List<FatturaDocumento> entities = findAll();
		return entities;
	}

	@Override
	public FatturaDocumento trova(int id) {
		FatturaDocumento entity = findByID(id);
		return entity;
	}

	@Override
	public FatturaDocumento inserisci(FatturaDocumento documento) {
		FatturaDocumento entity = insert(documento);
		return entity;
	}

	@Override
	public FatturaDocumento aggiorna(FatturaDocumento documento) {
		FatturaDocumento entity = update(documento, documento.getId());
		return entity;
	}

//	@Override
//	public FatturaDocumento elimina(FatturaDocumento documento) {
//		FatturaDocumento entity = delete(documento.getId());
//		return entity;
//	}
	
	@Override
	public FatturaDocumento elimina(FatturaDocumento documento) {
		//Creo un'unica transazione in cui vado ad eliminare il documento di fatturazione e riporto lo stato degli elementi base a fatturabile.
		//le voci correlate vengono eliminate in cascata dal DB a causa della FK.
		FatturaDocumento entity;
		List<FatturaVoce> voci = daoVoci.trovaTuttePerFattura(documento.getId());
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			for (FatturaVoce voce : voci) {
				daoVoci.aggiornaStatoFatturazioneDatoBase(documento.getIdAmbito(), voce.getIdRiferimento(), 0.0, Fatturazione.FATTURABILE, em);
			}
			entity = em.find(c, documento.getId());
			em.remove(entity);
			t.commit();
		} catch (Exception e) {
			logger.error(e);
			entity = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return entity;
	}

	@Override
	protected void updateValues(FatturaDocumento oldEntity, FatturaDocumento entity) {
		oldEntity.setAnnoFattura(entity.getAnnoFattura());
		oldEntity.setCoordinatePagamento(entity.getCoordinatePagamento());
		oldEntity.setDataEmissione(entity.getDataEmissione());
		oldEntity.setDataGenerazione(entity.getDataGenerazione());
		oldEntity.setDescrizioneFattura(entity.getDescrizioneFattura());
		oldEntity.setIdAmbito(entity.getIdAmbito());
		oldEntity.setIdCommessa(entity.getIdCommessa());
		oldEntity.setIva(entity.getIva());
		oldEntity.setMeseAnno(entity.getMeseAnno());
		oldEntity.setModalitaPagamento(entity.getModalitaPagamento());
		oldEntity.setNote(entity.getNote());
		oldEntity.setNumeroFattura(entity.getNumeroFattura());
		oldEntity.setStato(entity.getStato());
		oldEntity.setUtenteCreatore(entity.getUtenteCreatore());
	}

}
