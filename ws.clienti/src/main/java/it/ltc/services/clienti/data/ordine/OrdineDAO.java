package it.ltc.services.clienti.data.ordine;

import java.util.List;

import it.ltc.services.clienti.model.prodotto.UscitaJSON;
import it.ltc.services.clienti.model.prodotto.OrdineImballatoJSON;
import it.ltc.services.clienti.model.prodotto.OrdineJSON;
import it.ltc.services.clienti.model.prodotto.SpedizioneJSON;
import it.ltc.services.clienti.model.prodotto.UscitaDettaglioJSON;

public interface OrdineDAO<T, U> {
	
	public OrdineJSON trovaDaID(int idOrdine);
	
	public OrdineJSON trovaDaRiferimento(String riferimento, boolean dettagliato);
	
	public List<UscitaJSON> trovaTutti();
	
	public boolean inserisci(OrdineJSON json);
	
	public boolean inserisciDettaglio(UscitaDettaglioJSON json);
	
	public boolean aggiorna(UscitaJSON json);
	
	public boolean aggiornaDettaglio(UscitaDettaglioJSON json);
	
	public boolean elimina(UscitaJSON json);
	
	public boolean eliminaDettaglio(UscitaDettaglioJSON json);
	
	public boolean assegna(String riferimento);
	
	public boolean assegna(int idOrdine);
	
	public OrdineImballatoJSON ottieniDettagliImballo(String riferimento);
	
	public OrdineImballatoJSON ottieniDettagliImballo(int idOrdine);
	
	public boolean spedisci(SpedizioneJSON spedizione);
	
	public SpedizioneJSON getDocumentoDiTrasporto(int idSpedizione);
	
	public T deserializzaUscita(OrdineJSON json);
	
	public U deserializzaDettaglio(UscitaDettaglioJSON json);
	
	public OrdineJSON serializzaOrdine(T uscita, List<U> dettagli);
	
	public UscitaJSON serializzaUscita(T uscita, boolean dettagliato);
	
	public UscitaDettaglioJSON serializzaDettaglio(U dettaglio);

}
