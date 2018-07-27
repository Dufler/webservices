package it.ltc.services.sede.data.carico;

import java.util.List;

import it.ltc.services.sede.model.carico.ProdottoCaricoJSON;

public interface RiscontroProdottiDAO {
	
	public ProdottoCaricoJSON nuovoProdotto(ProdottoCaricoJSON prodotto);
	
	public ProdottoCaricoJSON aggiornaProdotto(ProdottoCaricoJSON prodotto);
	
	public ProdottoCaricoJSON eliminaProdotto(ProdottoCaricoJSON prodotto);

	public List<ProdottoCaricoJSON> trovaProdotti(ProdottoCaricoJSON prodotto);

}
