package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.cliente.FornitoreJSON;

public interface IFornitoreDao {
	
	public FornitoreJSON trovaDaID(int idFornitore);
	
	public List<FornitoreJSON> trovaTutti();
	
	public FornitoreJSON inserisci(FornitoreJSON fornitore);
	
	public FornitoreJSON aggiorna(FornitoreJSON fornitore);
	
	public FornitoreJSON elimina(FornitoreJSON fornitore);

}
