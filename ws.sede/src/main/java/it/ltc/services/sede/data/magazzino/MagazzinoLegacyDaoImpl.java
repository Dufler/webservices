package it.ltc.services.sede.data.magazzino;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.model.shared.dao.IMagazzinoDao;
import it.ltc.model.shared.json.interno.MagazzinoJSON;

public class MagazzinoLegacyDaoImpl extends MagazzinoDao implements IMagazzinoDao {

	public MagazzinoLegacyDaoImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public MagazzinoJSON trovaDaLTC(String codifica) {
		Magazzini entity = trovaDaCodiceLTC(codifica);
		MagazzinoJSON json = serializza(entity);
		return json;
	}

	@Override
	public MagazzinoJSON trovaDaCliente(String codifica) {
		Magazzini entity = trovaDaCodificaCliente(codifica);
		MagazzinoJSON json = serializza(entity);
		return json;
	}

	@Override
	public List<MagazzinoJSON> trovaliTutti() {
		List<Magazzini> entities = trovaTutti();
		List<MagazzinoJSON> jsons = new LinkedList<>();
		for (Magazzini entity : entities)
			jsons.add(serializza(entity));
		return jsons;
	}
	
	protected MagazzinoJSON serializza(Magazzini entity) {
		MagazzinoJSON json;
		if (entity != null) {
			json = new MagazzinoJSON();
			json.setCodiceLTC(entity.getCodiceMag());
			json.setCodiceCliente(entity.getMagaCliente());
			json.setDescrizione(entity.getDesMag());
		} else {
			json = null;
		}
		return json;
	}

}
