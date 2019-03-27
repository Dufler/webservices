package it.ltc.services.sede.data.magazzino;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.legacy.StagioniDao;
import it.ltc.database.model.legacy.Stagioni;
import it.ltc.model.shared.dao.IStagioneDao;
import it.ltc.model.shared.json.interno.StagioneJSON;

public class StagioneLegacyDaoImpl extends StagioniDao implements IStagioneDao {

	public StagioneLegacyDaoImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public StagioneJSON trovaDaID(String codice) {
		Stagioni entity = trovaDaCodice(codice);
		return serializza(entity);
	}

	@Override
	public List<StagioneJSON> trovaTutte() {
		List<StagioneJSON> stagioni = new LinkedList<>();
		List<Stagioni> entities = trovaTutti();
		for (Stagioni entity : entities)
			stagioni.add(serializza(entity));
		return stagioni;
	}
	
	protected StagioneJSON serializza(Stagioni stagione) {
		StagioneJSON json;
		if (stagione != null) {
			json = new StagioneJSON();
			json.setCodice(stagione.getCodice());
			json.setDescrizione(stagione.getDescrizione());
		} else {
			json = null;
		}
		return json;
	}

}
