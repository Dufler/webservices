package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.AziendaNote;

public interface AziendeNoteDAO {
	
	public List<AziendaNote> trovaTutti();
	
	public List<AziendaNote> trovaDaAzienda(int idAzienda);
	
	public List<AziendaNote> trovaDaContatto(int idContatto);
	
	public List<AziendaNote> trovaDaParola(String parola);
	
	public AziendaNote trova(int id);
	
	public AziendaNote inserisci(AziendaNote note);
	
	public AziendaNote aggiorna(AziendaNote note);
	
	public AziendaNote elimina(AziendaNote note);

}
