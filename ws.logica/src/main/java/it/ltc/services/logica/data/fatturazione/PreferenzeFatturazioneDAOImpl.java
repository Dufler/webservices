package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.FatturaPreferenzeCommessa;
import it.ltc.database.model.centrale.FatturaPreferenzeCommessaPK;

@Repository
public class PreferenzeFatturazioneDAOImpl extends CRUDDao<FatturaPreferenzeCommessa> implements PreferenzeFatturazioneDAO {

	public PreferenzeFatturazioneDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, FatturaPreferenzeCommessa.class);
	}

	@Override
	public List<FatturaPreferenzeCommessa> trovaTutti() {
		List<FatturaPreferenzeCommessa> entities = findAll();
		return entities;
	}

	@Override
	public FatturaPreferenzeCommessa trova(int ambito, int commessa) {
		FatturaPreferenzeCommessaPK id = new FatturaPreferenzeCommessaPK();
		id.setAmbito(ambito);
		id.setCommessa(commessa);
		FatturaPreferenzeCommessa entity = findByID(id);
		return entity;
	}

	@Override
	public FatturaPreferenzeCommessa inserisci(FatturaPreferenzeCommessa preferenze) {
		FatturaPreferenzeCommessa entity = insert(preferenze);
		return entity;
	}

	@Override
	public FatturaPreferenzeCommessa aggiorna(FatturaPreferenzeCommessa preferenze) {
		FatturaPreferenzeCommessa entity = update(preferenze, preferenze.getPK());
		return entity;
	}

	@Override
	public FatturaPreferenzeCommessa elimina(FatturaPreferenzeCommessa preferenze) {
		FatturaPreferenzeCommessa entity = delete(preferenze.getPK());
		return entity;
	}

	@Override
	protected void updateValues(FatturaPreferenzeCommessa oldEntity, FatturaPreferenzeCommessa entity) {
		oldEntity.setDescrizioneFattura(entity.getDescrizioneFattura());
		oldEntity.setModalitaPagamento(entity.getModalitaPagamento());
		oldEntity.setCoordinatePagamento(entity.getCoordinatePagamento());
		oldEntity.setLayout(entity.getLayout());
	}

}
