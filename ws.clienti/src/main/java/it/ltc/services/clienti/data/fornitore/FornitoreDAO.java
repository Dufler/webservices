package it.ltc.services.clienti.data.fornitore;

import java.util.List;

import it.ltc.services.clienti.model.prodotto.FornitoreJSON;

public interface FornitoreDAO<T> {
	
	public FornitoreJSON trovaDaID(int idFornitore);
	
	public List<FornitoreJSON> trovaTutti();
	
	public FornitoreJSON inserisci(FornitoreJSON fornitore);
	
	public FornitoreJSON aggiorna(FornitoreJSON fornitore);
	
	public FornitoreJSON elimina(FornitoreJSON fornitore);
	
	public T deserializza(FornitoreJSON json);
	
	public FornitoreJSON serializza(T prodotto);

}
