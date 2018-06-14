package it.ltc.services.logica.data.trasporti;

import java.util.Date;
import java.util.List;

import it.ltc.database.dao.common.model.CriteriUltimaModifica;
import it.ltc.database.model.centrale.Spedizione;
import it.ltc.database.model.centrale.SpedizioneLight;
import it.ltc.services.logica.model.trasporti.CriteriRicercaSpedizioniLight;
import it.ltc.services.logica.model.trasporti.SpedizioneCompletaJSON;
import it.ltc.services.logica.model.trasporti.TrackingJSON;

public interface SpedizioneDAO {
	
	public List<Spedizione> trovaTutte();
	
	public List<Spedizione> trovaDaUltimaModifica(CriteriUltimaModifica criteri);
	
	public List<Spedizione> trovaSpedizioniFatturabili(int idCommessa, Date start, Date end);
	
	public List<SpedizioneLight> trovaSpedizioni(CriteriRicercaSpedizioniLight criteri);
	
	public Spedizione trovaDaID(int id);
	
	public Spedizione inserisci(Spedizione spedizione);
	
	public Spedizione aggiorna(Spedizione spedizione);

	public SpedizioneCompletaJSON trovaDettagli(int id);
	
	public List<TrackingJSON> getTracking(int id);
	
	public boolean archiviaPerTracking(int id);
	
	public boolean eliminaPerTracking(int id);

}
