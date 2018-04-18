package it.ltc.services.clienti.data.magazzino;

import java.util.List;

import it.ltc.database.model.legacy.Imballi;

public interface ImballoDAO {
	
	Imballi trovaDaID(int id);
	
	Imballi trovaDaCodice(String codice);
	
	List<Imballi> trovaTutti();

}
