package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import it.ltc.database.model.centrale.CoordinateBancarie;

public interface CoordinateBancarieDAO {
	
	public List<CoordinateBancarie> trovaTutti();
	
	public CoordinateBancarie trova(int id);
	
	public CoordinateBancarie inserisci(CoordinateBancarie coordinate);
	
	public CoordinateBancarie aggiorna(CoordinateBancarie coordinate);
	
	public CoordinateBancarie elimina(CoordinateBancarie coordinate);

}
