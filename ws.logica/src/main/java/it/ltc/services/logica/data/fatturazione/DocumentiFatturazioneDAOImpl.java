package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.FatturaDocumento;

@Repository
public class DocumentiFatturazioneDAOImpl extends CRUDDao<FatturaDocumento> implements DocumentiFatturazioneDAO {

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

	@Override
	public FatturaDocumento elimina(FatturaDocumento documento) {
		FatturaDocumento entity = delete(documento.getId());
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
