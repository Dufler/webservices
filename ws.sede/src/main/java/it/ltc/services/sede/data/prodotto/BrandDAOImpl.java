package it.ltc.services.sede.data.prodotto;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.legacy.MarchiDao;
import it.ltc.database.model.legacy.Marchi;
import it.ltc.model.shared.dao.IBrandDao;
import it.ltc.model.shared.json.cliente.Brand;

public class BrandDAOImpl extends MarchiDao implements IBrandDao {

	public BrandDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public Brand trovaPerID(int id) {
		Marchi entity = trovaDaID(id);
		return serializza(entity);
	}

	@Override
	public Brand trovaPerCodice(int codice) {
		Marchi entity = trovaDaCodice(codice);
		return serializza(entity);
	}
	
	@Override
	public List<Brand> trovaTutte() {
		List<Marchi> entities = trovaTutti();
		List<Brand> jsons = new LinkedList<>();
		for (Marchi entity : entities)
			jsons.add(serializza(entity));
		return jsons;
	}

	@Override
	public Brand inserisci(Brand brand) {
		Marchi entity = deserializza(brand);
		entity = insert(entity);
		return serializza(entity);
	}

	@Override
	public Brand aggiorna(Brand brand) {
		Marchi entity = deserializza(brand);
		entity = update(entity, entity.getIdMarchio());
		return serializza(entity);
	}

	@Override
	public Brand elimina(Brand brand) {
		Marchi entity = deserializza(brand);
		entity = delete(entity.getIdMarchio());
		return serializza(entity);
	}
	
	private Brand serializza(Marchi entity) {
		Brand brand = null;
		if (entity != null) {
			brand = new Brand();
			brand.setId(entity.getIdMarchio());
			brand.setCodice(entity.getCodice());
			brand.setDescrizione(entity.getDescrizione());
		}
		return brand;
	}
	
	private Marchi deserializza(Brand json) {
		Marchi brand = null;
		if (json != null) {
			brand = new Marchi();
			brand.setIdMarchio(json.getId());
			brand.setCodice(json.getCodice());
			brand.setDescrizione(json.getDescrizione());
		}
		return brand;
	}

}
