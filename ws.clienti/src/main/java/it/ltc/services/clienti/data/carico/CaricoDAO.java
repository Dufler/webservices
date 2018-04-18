package it.ltc.services.clienti.data.carico;

import java.util.List;

import it.ltc.services.clienti.model.prodotto.CaricoJSON;
import it.ltc.services.clienti.model.prodotto.IngressoDettaglioJSON;
import it.ltc.services.clienti.model.prodotto.IngressoJSON;
import it.ltc.services.clienti.model.prodotto.ModificaCaricoJSON;

public interface CaricoDAO<T, U> {
	
	public CaricoJSON trovaDaID(int idCarico, boolean dettagliato);
	
	public CaricoJSON trovaDaRiferimento(String riferimento, boolean dettagliato);
	
	public List<IngressoJSON> trovaTutti();
	
	public boolean inserisci(CaricoJSON carico);
	
	public boolean inserisciDettaglio(IngressoDettaglioJSON carico);
	
	public boolean aggiorna(IngressoJSON carico);
	
	public boolean aggiornaDettaglio(IngressoDettaglioJSON carico);
	
	public boolean elimina(IngressoJSON carico);
	
	public boolean eliminaDettaglio(IngressoDettaglioJSON carico);
	
	public T deserializzaIngresso(CaricoJSON json);
	
	public List<U> deserializzaDettagli(CaricoJSON json);
	
	public CaricoJSON serializzaCarico(T carico, List<U> dettagli);
	
	public IngressoJSON serializzaIngresso(T carico);
	
	public IngressoDettaglioJSON serializzaDettaglio(U dettaglio);
	
	public boolean modificaCaricoDiTest(ModificaCaricoJSON modifiche);

}
