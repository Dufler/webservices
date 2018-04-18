package it.ltc.services.logica.data.trasporti;

import java.util.List;

import it.ltc.database.model.centrale.Cap;
import it.ltc.services.logica.model.trasporti.CapJSON;
import it.ltc.services.logica.model.trasporti.CriteriUltimaModifica;

public interface CapDAO {
	
	public boolean insert(CapJSON cap);
	
	public boolean update(CapJSON cap);
	
	public boolean delete(CapJSON cap);
	
	public List<CapJSON> findAll();
	
	public CapJSON findByCap(String cap);
	
	public CapJSON findByCapAndTown(String cap, String town);
	
	public Cap deserializza(CapJSON json);
	
	public CapJSON serializza(Cap cap);

	public List<CapJSON> trovaDaUltimaModifica(CriteriUltimaModifica criteri);

}
