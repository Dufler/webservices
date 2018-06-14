package it.ltc.services.logica.data.trasporti;

import java.util.List;

import it.ltc.database.dao.common.model.CriteriUltimaModifica;
import it.ltc.database.model.centrale.SpedizioneContrassegno;

public interface ContrassegniDAO {
	
	public List<SpedizioneContrassegno> trovaTutti();
	
	public SpedizioneContrassegno trova(int id);
	
	public SpedizioneContrassegno inserisci(SpedizioneContrassegno contrassegno);
	
	public SpedizioneContrassegno aggiorna(SpedizioneContrassegno contrassegno);
	
	public SpedizioneContrassegno elimina(SpedizioneContrassegno contrassegno);

	public List<SpedizioneContrassegno> trovaDaUltimaModifica(CriteriUltimaModifica criteri);

}
