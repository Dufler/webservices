package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.Azienda;
import it.ltc.database.model.centrale.Indirizzo;

public interface AziendaDAO {
	
	public List<Azienda> trovaTutte();
	
	public List<Azienda> trovaDaContatto(int idContatto);
	
	public List<Azienda> trovaDaBrand(int idBrand);
	
	public List<Azienda> trovaDaNome(String nome);
	
	public Indirizzo trovaIndirizzo(int idAzienda);
	
	public Indirizzo salvaIndirizzo(int idAzienda, Indirizzo indirizzo);
	
	public Azienda trovaDaID(int id);
	
	public Azienda inserisci(Azienda azienda);
	
	public Azienda aggiorna(Azienda azienda);
	
	public Azienda elimina(Azienda azienda);

}
