package it.ltc.services.clienti.data.prodotto;

import java.util.List;

import it.ltc.model.shared.json.cliente.ProdottoJSON;

public interface ProdottoDAO {
	
	public ProdottoJSON trovaPerID(int id);
	
	public ProdottoJSON trovaPerSKU(String sku);
	
	public ProdottoJSON trovaPerBarcode(String barcode);
	
	public List<ProdottoJSON> trovaTutti();
	
	public ProdottoJSON inserisci(ProdottoJSON prodotto);
	
	public ProdottoJSON aggiorna(ProdottoJSON prodotto);
	
	public ProdottoJSON dismetti(ProdottoJSON prodotto);

}
