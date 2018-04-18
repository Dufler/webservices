package it.ltc.services.logica.data.trasporti;

import java.util.List;

import it.ltc.database.model.centrale.Indirizzo;
import it.ltc.services.logica.model.trasporti.CriteriUltimaModifica;

public interface IndirizziDAO {

	public Indirizzo inserisci(Indirizzo indirizzo);
	
	public Indirizzo aggiorna(Indirizzo indirizzo);
	
	public Indirizzo elimina(Indirizzo indirizzo);
	
	public Indirizzo trovaDaID(int idIndirizzo);
	
	public List<Indirizzo> trovaTutti();

	public List<Indirizzo> trovaDaUltimaModifica(CriteriUltimaModifica criteri);
	
}
