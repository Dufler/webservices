package it.ltc.services.sede.data.operatore;

import java.util.List;

import it.ltc.database.model.sede.Attivita;

public interface AttivitaDAO {
	
	public List<Attivita> trovaTutte();
	
	public List<Attivita> trovaTutteDaOperatore(String operatore);
	
	public Attivita trova(int id);
	
	public Attivita inserisci(Attivita attivita);
	
	public Attivita aggiorna(Attivita attivita);
	
	public Attivita elimina(Attivita attivita);

}
