package it.ltc.services.logica.data.trasporti;

import java.util.List;

import it.ltc.database.model.centrale.SpedizioneGiacenza;
import it.ltc.services.logica.model.trasporti.CriteriUltimaModifica;

public interface GiacenzeDAO {

	public List<SpedizioneGiacenza> trovaTutte();
	
	public SpedizioneGiacenza trova(int id);
	
	public SpedizioneGiacenza inserisci(SpedizioneGiacenza giacenza);
	
	public SpedizioneGiacenza aggiorna(SpedizioneGiacenza giacenza);
	
	public SpedizioneGiacenza elimina(SpedizioneGiacenza giacenza);

	public List<SpedizioneGiacenza> trovaDaUltimaModifica(CriteriUltimaModifica criteri);
}
