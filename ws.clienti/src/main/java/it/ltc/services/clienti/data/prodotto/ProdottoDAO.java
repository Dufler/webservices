package it.ltc.services.clienti.data.prodotto;

import java.util.List;

import it.ltc.services.clienti.model.prodotto.ProdottoJSON;

public interface ProdottoDAO<T> {
	
	public ProdottoJSON trovaDaID(int id);
	
	public ProdottoJSON trovaDaSKU(String sku);
	
	public ProdottoJSON trovaDaBarcode(String barcode);
	
	public List<ProdottoJSON> trovaTutti();
	
	public boolean inserisci(ProdottoJSON prodotto);
	
	public boolean aggiorna(ProdottoJSON prodotto);
	
	public boolean dismetti(ProdottoJSON prodotto);
	
	public T deserializza(ProdottoJSON json);
	
	public ProdottoJSON serializza(T prodotto);

}
