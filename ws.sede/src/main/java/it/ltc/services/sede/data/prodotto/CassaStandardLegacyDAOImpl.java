package it.ltc.services.sede.data.prodotto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.legacy.bundle.CasseStandardDao;
import it.ltc.database.model.legacy.bundle.CasseStandard;
import it.ltc.model.shared.dao.ICassaStandardDao;
import it.ltc.model.shared.json.cliente.CassaStandardJSON;
import it.ltc.model.shared.json.cliente.ElementoCassaStandardJSON;
import it.ltc.services.custom.exception.CustomException;

public class CassaStandardLegacyDAOImpl extends CasseStandardDao implements ICassaStandardDao {

	public CassaStandardLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public CassaStandardJSON salva(CassaStandardJSON cassa) {
		List<CasseStandard> composizioneCassa = deserializza(cassa);
		try {
			boolean salva = salvaCassa(composizioneCassa);
			if (!salva)
				cassa = null;
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return cassa;
	}

	@Override
	public CassaStandardJSON elimina(CassaStandardJSON cassa) {
		boolean elimina = eliminaCassa(cassa.getCodiceCassa());
		if (!elimina)
			cassa = null;
		return cassa;
	}

	@Override
	public CassaStandardJSON trovaDaCodiceCassa(String codice) {
		List<CasseStandard> composizioneCassa = trovaDaTipoCassa(codice);
		return serializza(composizioneCassa);
	}

	@Override
	public List<CassaStandardJSON> trovaTutte() {
		//Recupero tutte le casse esistenti e ne creo una mappa per ID cassa.
		List<CasseStandard> entities = trovaTutti();
		HashMap<String, List<CasseStandard>> mappaCasse = new HashMap<>();
		for (CasseStandard entity : entities) {
			//Se non ho ancora mappato la cassa provvedo a farlo
			if (!mappaCasse.containsKey(entity.getCodiceCassa())) {
				mappaCasse.put(entity.getCodiceCassa(), new LinkedList<>());
			}
			//aggiungo il prodotto alla composizione
			List<CasseStandard> lista = mappaCasse.get(entity.getCodiceCassa());
			lista.add(entity);
		}
		//Converto la mappa in una lista di json
		List<CassaStandardJSON> casse = new LinkedList<>();
		for (List<CasseStandard> lista : mappaCasse.values()) {
			CassaStandardJSON cassa = serializza(lista);
			casse.add(cassa);
		}
		return casse;
	}
	
	protected CassaStandardJSON serializza(List<CasseStandard> composizioneCassa) {
		CassaStandardJSON json = null;
		if (composizioneCassa != null && !composizioneCassa.isEmpty()) {
			json = new CassaStandardJSON();
			List<ElementoCassaStandardJSON> elementi = new LinkedList<>();
			for (CasseStandard elemento : composizioneCassa) {
				ElementoCassaStandardJSON elementoJSON = new ElementoCassaStandardJSON();
				elementoJSON.setQuantita(elemento.getQuantitaProdotto());
				elementoJSON.setTaglia(elemento.getTaglia());
				elementi.add(elementoJSON);
				json.setCodiceCassa(elemento.getCodiceCassa());
			}
			json.setElementi(elementi);
		}
		return json;
	}
	
	protected List<CasseStandard> deserializza(CassaStandardJSON json) {
		List<CasseStandard> casse;
		if (json != null && !json.getElementi().isEmpty()) {
			casse = new LinkedList<>();
			for (ElementoCassaStandardJSON elemento : json.getElementi()) {
				CasseStandard cassa = new CasseStandard();
				cassa.setCodiceCassa(json.getCodiceCassa());
				cassa.setTaglia(elemento.getTaglia());
				cassa.setQuantitaProdotto(elemento.getQuantita());
				casse.add(cassa);
			}
		} else {
			casse = null;
		}
		return casse;
	}

}
