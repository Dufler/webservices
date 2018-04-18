package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.AziendaContatti;

public interface AssociazioneAziendaContattiDAO {
	
	public List<AziendaContatti> trovaTutti();
	
	public List<AziendaContatti> trovaDaAzienda(int idAzienda);
	
	public List<AziendaContatti> trovaDaContatto(int idContatto);
	
	public AziendaContatti trova(int idAzienda, int idContatto);
	
	public AziendaContatti inserisci(AziendaContatti associazione);
	
	public AziendaContatti aggiorna(AziendaContatti associazione);
	
	public AziendaContatti elimina(AziendaContatti associazione);

}
