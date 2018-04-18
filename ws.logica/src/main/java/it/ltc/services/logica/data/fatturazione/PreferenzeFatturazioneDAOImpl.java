package it.ltc.services.logica.data.fatturazione;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.FatturaPreferenzeCommessa;
import it.ltc.database.model.centrale.FatturaPreferenzeCommessaPK;
import it.ltc.database.model.centrale.json.PreferenzeFatturazioneJSON;

@Repository
public class PreferenzeFatturazioneDAOImpl extends CRUDDao<FatturaPreferenzeCommessa> implements PreferenzeFatturazioneDAO {

	public PreferenzeFatturazioneDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, FatturaPreferenzeCommessa.class);
	}

	@Override
	public List<PreferenzeFatturazioneJSON> trovaTutti() {
		List<FatturaPreferenzeCommessa> entities = findAll();
		List<PreferenzeFatturazioneJSON> list = new LinkedList<>();
		for (FatturaPreferenzeCommessa entity : entities) {
			PreferenzeFatturazioneJSON json = serializza(entity);
			list.add(json);
		}
		return list;
	}

	@Override
	public PreferenzeFatturazioneJSON trova(int ambito, int commessa) {
		FatturaPreferenzeCommessaPK id = new FatturaPreferenzeCommessaPK();
		id.setAmbito(ambito);
		id.setCommessa(commessa);
		FatturaPreferenzeCommessa entity = findByID(id);
		PreferenzeFatturazioneJSON json = serializza(entity);
		return json;
	}

	@Override
	public PreferenzeFatturazioneJSON inserisci(PreferenzeFatturazioneJSON preferenze) {
		FatturaPreferenzeCommessa entity = insert(deserializza(preferenze));
		PreferenzeFatturazioneJSON json = serializza(entity);
		return json;
	}

	@Override
	public PreferenzeFatturazioneJSON aggiorna(PreferenzeFatturazioneJSON preferenze) {
		FatturaPreferenzeCommessa update = deserializza(preferenze);
		FatturaPreferenzeCommessa entity = update(update, update.getId());
		PreferenzeFatturazioneJSON json = serializza(entity);
		return json;
	}

	@Override
	public PreferenzeFatturazioneJSON elimina(PreferenzeFatturazioneJSON preferenze) {
		FatturaPreferenzeCommessa delete = deserializza(preferenze);
		FatturaPreferenzeCommessa entity = delete(delete.getId());
		PreferenzeFatturazioneJSON json = serializza(entity);
		return json;
	}

	@Override
	protected void updateValues(FatturaPreferenzeCommessa oldEntity, FatturaPreferenzeCommessa entity) {
		oldEntity.setDescrizioneFattura(entity.getDescrizioneFattura());
		oldEntity.setModalitaPagamento(entity.getModalitaPagamento());
		oldEntity.setCoordinatePagamento(entity.getCoordinatePagamento());
		oldEntity.setLayout(entity.getLayout());
	}
	
	private PreferenzeFatturazioneJSON serializza(FatturaPreferenzeCommessa entity) {
		PreferenzeFatturazioneJSON json = new PreferenzeFatturazioneJSON();
		json.setAmbito(entity.getId().getAmbito());
		json.setCommessa(entity.getId().getCommessa());
		json.setDescrizioneFattura(entity.getDescrizioneFattura());
		json.setModalitaPagamento(entity.getModalitaPagamento());
		json.setCoordinatePagamento(entity.getCoordinatePagamento());
		json.setLayout(entity.getLayout());
		return json;
	}
	
	private FatturaPreferenzeCommessa deserializza(PreferenzeFatturazioneJSON json) {
		FatturaPreferenzeCommessa entity = new FatturaPreferenzeCommessa();
		FatturaPreferenzeCommessaPK id = new FatturaPreferenzeCommessaPK();
		id.setAmbito(json.getAmbito());
		id.setCommessa(json.getCommessa());
		entity.setId(id);
		entity.setDescrizioneFattura(json.getDescrizioneFattura());
		entity.setModalitaPagamento(json.getModalitaPagamento());
		entity.setCoordinatePagamento(json.getCoordinatePagamento());
		entity.setLayout(json.getLayout());
		return entity;
	}

}
