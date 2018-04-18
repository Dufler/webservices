package it.ltc.services.logica.data.common;

import java.util.List;

import it.ltc.database.model.centrale.Operatore;

public interface OperatoriDAO {
	
	public List<Operatore> trovaTutti();
	
	public Operatore trova(String username);
	
	public Operatore inserisci(Operatore operatore);
	
	public Operatore aggiorna(Operatore operatore);
	
	public Operatore elimina(Operatore operatore);

}
