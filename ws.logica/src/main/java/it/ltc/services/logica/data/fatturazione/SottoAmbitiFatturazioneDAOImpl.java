package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.FatturaSottoAmbito;

@Repository
public class SottoAmbitiFatturazioneDAOImpl extends CRUDDao<FatturaSottoAmbito> implements SottoAmbitiFatturazioneDAO {

	public SottoAmbitiFatturazioneDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, FatturaSottoAmbito.class);
	}

	@Override
	public List<FatturaSottoAmbito> trovaTutti() {
		List<FatturaSottoAmbito> lista = findAll();
		return lista;
	}

	@Override
	public FatturaSottoAmbito trova(int id) {
		FatturaSottoAmbito entity = findByID(id);
		return entity;
	}

	@Override
	public FatturaSottoAmbito inserisci(FatturaSottoAmbito ambito) {
		FatturaSottoAmbito entity = insert(ambito);
		return entity;
	}

	@Override
	public FatturaSottoAmbito aggiorna(FatturaSottoAmbito ambito) {
		FatturaSottoAmbito entity = update(ambito, ambito.getId());
		return entity;
	}

	@Override
	public FatturaSottoAmbito elimina(FatturaSottoAmbito ambito) {
		FatturaSottoAmbito entity = delete(ambito.getId());
		return entity;
	}

	@Override
	protected void updateValues(FatturaSottoAmbito oldEntity, FatturaSottoAmbito entity) {
		oldEntity.setCategoriaAmbito(entity.getCategoriaAmbito());
		oldEntity.setDescrizione(entity.getDescrizione());
		oldEntity.setIdAmbito(entity.getIdAmbito());
		oldEntity.setNome(entity.getNome());
		oldEntity.setValoreAmmesso(entity.getValoreAmmesso());		
	}

}
