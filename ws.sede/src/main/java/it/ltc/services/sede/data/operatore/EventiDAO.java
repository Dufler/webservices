package it.ltc.services.sede.data.operatore;

import java.util.Date;
import java.util.List;

import it.ltc.services.sede.model.operatore.EventoJSON;

public interface EventiDAO<T> {
	
	public EventoJSON trovaEventoAperto(String operatore);
	
	public List<EventoJSON> trovaEventi(String operatore, Date inizio, Date fine);
	
	public EventoJSON inserisci(EventoJSON evento);
	
	public EventoJSON aggiorna(EventoJSON evento);
	
	public EventoJSON elimina(EventoJSON evento);
	
	public T deserializza(EventoJSON json);
	
	public EventoJSON serializza(T evento);

}
