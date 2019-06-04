package it.ltc.services.sede.data.prodotto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.bundle.CasseDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.bundle.Casse;
import it.ltc.model.shared.dao.ICassaDao;
import it.ltc.model.shared.json.cliente.CassaJSON;
import it.ltc.model.shared.json.cliente.ElementoCassaJSON;
import it.ltc.model.shared.json.cliente.TipoCassa;
import it.ltc.services.custom.exception.CustomException;

public class CassaLegacyDAOImpl extends CasseDao implements ICassaDao {
	
	protected final ArticoliDao daoArticoli;

	public CassaLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoArticoli = new ArticoliDao(persistenceUnit);
	}

	@Override
	public CassaJSON salva(CassaJSON json) {
		//elimino la composizione esistente e salvo la nuova
		List<Casse> ListaProdottiCassa = new LinkedList<>();
		for (ElementoCassaJSON elemento : json.getProdotti()) {
			//verifico che la cassa e il prodotto singolo esistano
			Articoli prodotto = daoArticoli.trovaDaID(elemento.getIdProdotto());
			if (prodotto == null) {
				throw new CustomException("Il prodotto contenuto nella cassa non esiste. (ID: " + elemento.getIdProdotto() + ")");
			}
			Articoli cassa = daoArticoli.trovaDaID(json.getIdCassa());
			if (cassa == null) {
				throw new CustomException("La cassa indicata non esiste. (ID: " + json.getIdCassa() + ")");
			} else if (cassa.getCassa().equals("NO")) {
				throw new CustomException("Il prodotto indicato come cassa non è una cassa. (ID: " + json.getIdCassa() + ")");
			}
			//Compongo le info sull'elemento della cassa
			Casse elementoCassa = new Casse();
			elementoCassa.setCodiceCassa(json.getCodiceCassa());
			elementoCassa.setModello(json.getModello());
			elementoCassa.setTipo(json.getTipo().name());
			elementoCassa.setIdCassa(json.getIdCassa());
			elementoCassa.setIdProdotto(elemento.getIdProdotto());
			elementoCassa.setQuantitaProdotto(elemento.getQuantita());
			elementoCassa.setIdUnivocoCassa(cassa.getIdUniArticolo());
			elementoCassa.setIdUnivocoProdotto(prodotto.getIdUniArticolo());
			ListaProdottiCassa.add(elementoCassa);
		}
		try {
			boolean salva = salvaCassa(ListaProdottiCassa);
			if (!salva)
				json = null;
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return json;
	}

	@Override
	public CassaJSON elimina(CassaJSON cassa) {
		boolean elimina = eliminaCassa(cassa.getIdCassa());
		if (!elimina)
			cassa = null;
		return cassa;
	}

	@Override
	public CassaJSON trovaDaIDCassa(int idCassa) {
		List<Casse> composizione = trovaDaCassa(idCassa);
		CassaJSON json = serializza(composizione);
		return json;
	}

	@Override
	public List<CassaJSON> trovaTutte() {
		//Recupero tutte le casse esistenti e ne creo una mappa per ID cassa.
		List<Casse> entities = trovaTutti();
		HashMap<Integer, List<Casse>> mappaCasse = new HashMap<>();
		for (Casse entity : entities) {
			//Se non ho ancora mappato la cassa provvedo a farlo
			if (!mappaCasse.containsKey(entity.getIdCassa())) {
				mappaCasse.put(entity.getIdCassa(), new LinkedList<>());
			}
			//aggiungo il prodotto alla composizione
			List<Casse> lista = mappaCasse.get(entity.getIdCassa());
			lista.add(entity);
		}
		//Converto la mappa in una lista di json
		List<CassaJSON> casse = new LinkedList<>();
		for (List<Casse> lista : mappaCasse.values()) {
			CassaJSON cassa = serializza(lista);
			casse.add(cassa);
		}
		return casse;
	}
	
	protected CassaJSON serializza(List<Casse> composizione) {
		CassaJSON json;
		if (composizione == null || composizione.isEmpty()) {
			json = null;
		} else {
			json = new CassaJSON();
			//Imposto i dati comuni
			Casse cassa = composizione.get(0);
			json.setCodiceCassa(cassa.getCodiceCassa());
			json.setModello(cassa.getModello());
			json.setTipo(TipoCassa.getTipo(cassa.getTipo()));
			json.setIdCassa(cassa.getIdCassa());
			//Imposto la composizione
			List<ElementoCassaJSON> prodotti = new LinkedList<>();
			for (Casse elemento : composizione) {
				ElementoCassaJSON prodotto = new ElementoCassaJSON();
				prodotto.setIdProdotto(elemento.getIdProdotto());
				prodotto.setQuantita(elemento.getQuantitaProdotto());
				prodotti.add(prodotto);
			}
			json.setProdotti(prodotti);
		}
		return json;
	}

}
