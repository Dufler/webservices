package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.Gadget;

public interface GadgetDAO {
	
	public List<Gadget> trovaTutti();
	
	public List<Gadget> trovaDaNome(String testo);
	
	public Gadget trova(int id);
	
	public Gadget inserisci(Gadget gadget);
	
	public Gadget aggiorna(Gadget gadget);
	
	public Gadget elimina(Gadget gadget);

}
