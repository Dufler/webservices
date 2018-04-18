package it.ltc.services.logica.data.listini.clienti;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCommessa;

public interface ListiniClienteDAO {
	
	public List<ListinoCommessa> trovaTutti();
	
	public ListinoCommessa trova(int id);
	
	public ListinoCommessa inserisci(ListinoCommessa listino);
	
	public ListinoCommessa aggiorna(ListinoCommessa listino);
	
	public ListinoCommessa elimina(ListinoCommessa listino);

}
