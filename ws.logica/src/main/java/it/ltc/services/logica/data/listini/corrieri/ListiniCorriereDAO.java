package it.ltc.services.logica.data.listini.corrieri;

import java.util.List;

import it.ltc.database.model.centrale.ListinoCorriere;

public interface ListiniCorriereDAO {
	
	public List<ListinoCorriere> trovaTutti();
	
	public ListinoCorriere trova(int id);
	
	public ListinoCorriere inserisci(ListinoCorriere listino);
	
	public ListinoCorriere aggiorna(ListinoCorriere listino);
	
	public ListinoCorriere elimina(ListinoCorriere listino);

}
