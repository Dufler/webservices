package it.ltc.services.sede.data.cdg;

import java.util.Date;
import java.util.List;

import it.ltc.database.model.sede.json.CdgEventoRiepilogoJSON;
import it.ltc.services.sede.model.cdg.FiltroEventoRiepilogo;

public interface EventoRiepilogoDAO {
	
	public List<CdgEventoRiepilogoJSON> trovaTutti();
	
	public CdgEventoRiepilogoJSON trova(int evento, int commessa, Date data);
	
	public CdgEventoRiepilogoJSON inserisci(CdgEventoRiepilogoJSON evento);
	
	public CdgEventoRiepilogoJSON aggiorna(CdgEventoRiepilogoJSON evento);
	
	public CdgEventoRiepilogoJSON elimina(CdgEventoRiepilogoJSON evento);

	public List<CdgEventoRiepilogoJSON> trovaPerCommessaEData(FiltroEventoRiepilogo filtro);

}
