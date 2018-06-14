package it.ltc.services.sede.data.cdg;

import java.util.Date;
import java.util.List;

import it.ltc.database.model.sede.CdgEventoRiepilogo;
import it.ltc.services.sede.model.cdg.FiltroEventoRiepilogo;

public interface EventoRiepilogoDAO {
	
	public List<CdgEventoRiepilogo> trovaTutti();
	
	public CdgEventoRiepilogo trova(int evento, int commessa, Date data);
	
	public CdgEventoRiepilogo inserisci(CdgEventoRiepilogo evento);
	
	public CdgEventoRiepilogo aggiorna(CdgEventoRiepilogo evento);
	
	public CdgEventoRiepilogo elimina(CdgEventoRiepilogo evento);

	public List<CdgEventoRiepilogo> trovaPerCommessaEData(FiltroEventoRiepilogo filtro);

}
