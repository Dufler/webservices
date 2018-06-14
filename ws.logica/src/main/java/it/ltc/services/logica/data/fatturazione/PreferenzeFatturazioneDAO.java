package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import it.ltc.database.model.centrale.FatturaPreferenzeCommessa;

public interface PreferenzeFatturazioneDAO {
	
	public List<FatturaPreferenzeCommessa> trovaTutti();
	
	public FatturaPreferenzeCommessa trova(int ambito, int commessa);
	
	public FatturaPreferenzeCommessa inserisci(FatturaPreferenzeCommessa preferenze);
	
	public FatturaPreferenzeCommessa aggiorna(FatturaPreferenzeCommessa preferenze);
	
	public FatturaPreferenzeCommessa elimina(FatturaPreferenzeCommessa preferenze);

}
