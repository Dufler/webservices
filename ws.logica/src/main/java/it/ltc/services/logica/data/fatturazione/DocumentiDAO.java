package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import it.ltc.database.model.centrale.Documento;

public interface DocumentiDAO {
	
	public List<Documento> trovaTutti();
	
	public Documento trova(int id);
	
	public Documento inserisci(Documento documento);
	
	public Documento aggiorna(Documento documento);
	
	public Documento elimina(Documento documento);

}
