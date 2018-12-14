package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.Contatto;
import it.ltc.database.model.centrale.Indirizzo;

public interface ContattoDAO {
	
	public List<Contatto> trovaTutti();
	
	public List<Contatto> trovaDaAzienda(int idAzienda);
	
	public List<Contatto> trovaDaNome(String nome);
	
	public Contatto trova(int id);
	
	public Contatto inserisci(Contatto contatto);
	
	public Contatto aggiorna(Contatto contatto);
	
	public Contatto elimina(Contatto contatto);

	public Indirizzo trovaIndirizzo(int idContatto);

	public Indirizzo salvaIndirizzo(int idContatto, Indirizzo indirizzo);

}
