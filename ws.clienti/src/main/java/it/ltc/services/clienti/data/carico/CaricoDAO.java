package it.ltc.services.clienti.data.carico;

import java.util.List;

import it.ltc.model.shared.json.cliente.CaricoJSON;
import it.ltc.model.shared.json.cliente.IngressoDettaglioJSON;
import it.ltc.model.shared.json.cliente.IngressoJSON;
import it.ltc.model.shared.json.cliente.ModificaCaricoJSON;

public interface CaricoDAO<T, U> {
	
	public CaricoJSON trovaDaID(int idCarico, boolean dettagliato);
	
	public CaricoJSON trovaDaRiferimento(String riferimento, boolean dettagliato);
	
	public List<IngressoJSON> trovaTutti();
	
	public CaricoJSON inserisci(CaricoJSON carico);
	
	public IngressoDettaglioJSON inserisciDettaglio(IngressoDettaglioJSON carico);
	
	public IngressoJSON aggiorna(IngressoJSON carico);
	
	public IngressoDettaglioJSON aggiornaDettaglio(IngressoDettaglioJSON carico);
	
	public IngressoJSON elimina(IngressoJSON carico);
	
	public IngressoDettaglioJSON eliminaDettaglio(IngressoDettaglioJSON carico);
	
	public T deserializzaIngresso(CaricoJSON json);
	
	public List<U> deserializzaDettagli(CaricoJSON json);
	
	public CaricoJSON serializzaCarico(T carico, List<U> dettagli);
	
	public IngressoJSON serializzaIngresso(T carico);
	
	public IngressoDettaglioJSON serializzaDettaglio(U dettaglio);
	
	public boolean modificaCaricoDiTest(ModificaCaricoJSON modifiche);

}
