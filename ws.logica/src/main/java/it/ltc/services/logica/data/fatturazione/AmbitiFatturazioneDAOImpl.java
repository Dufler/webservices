package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.FatturaAmbito;

@Repository
public class AmbitiFatturazioneDAOImpl extends CRUDDao<FatturaAmbito> implements AmbitiFatturazioneDAO {

	public AmbitiFatturazioneDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, FatturaAmbito.class);
	}

	@Override
	public List<FatturaAmbito> trovaTutti() {
		List<FatturaAmbito> lista = findAll();
		return lista;
	}

	@Override
	public FatturaAmbito trova(int id) {
		FatturaAmbito entity = findByID(id);
		return entity;
	}

	@Override
	public FatturaAmbito inserisci(FatturaAmbito ambito) {
		FatturaAmbito entity = insert(ambito);
		return entity;
	}

	@Override
	public FatturaAmbito aggiorna(FatturaAmbito ambito) {
		FatturaAmbito entity = update(ambito, ambito.getId());
		return entity;
	}

	@Override
	public FatturaAmbito elimina(FatturaAmbito ambito) {
		FatturaAmbito entity = delete(ambito.getId());
		return entity;
	}

	@Override
	protected void updateValues(FatturaAmbito oldEntity, FatturaAmbito entity) {
		oldEntity.setCategoria(entity.getCategoria());
		oldEntity.setDescrizione(entity.getDescrizione());
		oldEntity.setNome(entity.getNome());		
	}

}
