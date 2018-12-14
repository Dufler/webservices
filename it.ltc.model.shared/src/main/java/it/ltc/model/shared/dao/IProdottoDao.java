package it.ltc.model.shared.dao;

import java.util.Date;
import java.util.List;

import it.ltc.model.shared.json.cliente.ProdottoJSON;

public interface IProdottoDao {
	
	/**
	 * Imposta l'utente che ha richiesto i servizi del dao.
	 * @param utente
	 */
	public void setUtente(String utente);
	
	public ProdottoJSON trovaPerID(int id);
	
	public ProdottoJSON trovaPerSKU(String sku);
	
	public ProdottoJSON trovaPerBarcode(String barcode);
	
	public List<ProdottoJSON> trova(ProdottoJSON filtro);
	
	public List<ProdottoJSON> trovaTutti();
	
	public List<ProdottoJSON> trovaDaUltimaModifica(Date ultimaModifica);
	
	public ProdottoJSON inserisci(ProdottoJSON prodotto);
	
	public ProdottoJSON aggiorna(ProdottoJSON prodotto);
	
	public ProdottoJSON dismetti(ProdottoJSON prodotto);

}
