package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.GadgetInviati;

public interface GadgetInviatiDAO {
	
	public List<GadgetInviati> trovaTutti();
	
	public List<GadgetInviati> trovaDaAzienda(int idAzienda);
	
	public List<GadgetInviati> trovaDaGadget(int idGadget);
	
	public GadgetInviati trova(int id);
	
	public GadgetInviati inserisci(GadgetInviati gadgetInviati);
	
	public GadgetInviati aggiorna(GadgetInviati gadgetInviati);
	
	public GadgetInviati elimina(GadgetInviati gadgetInviati);

}
