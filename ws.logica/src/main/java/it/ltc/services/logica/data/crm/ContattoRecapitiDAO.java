package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.CrmContattoRecapiti;

public interface ContattoRecapitiDAO {
	
	public List<CrmContattoRecapiti> trovaTuttiDaContatto(int idContatto);
	
	public CrmContattoRecapiti trovaDaID(int idRecapito);
	
	public CrmContattoRecapiti inserisci(CrmContattoRecapiti recapito);
	
	public CrmContattoRecapiti aggiorna(CrmContattoRecapiti recapito);
	
	public CrmContattoRecapiti elimina(CrmContattoRecapiti recapito);

}
