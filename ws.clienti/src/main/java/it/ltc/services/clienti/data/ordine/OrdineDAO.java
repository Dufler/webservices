package it.ltc.services.clienti.data.ordine;

import java.util.List;

import it.ltc.model.shared.json.cliente.OrdineImballatoJSON;
import it.ltc.model.shared.json.cliente.OrdineJSON;
import it.ltc.model.shared.json.cliente.SpedizioneJSON;
import it.ltc.model.shared.json.cliente.UscitaDettaglioJSON;
import it.ltc.model.shared.json.cliente.UscitaJSON;

public interface OrdineDAO<T, U> {
	
	public OrdineJSON trovaDaID(int idOrdine);
	
	public OrdineJSON trovaDaRiferimento(String riferimento, boolean dettagliato);
	
	public List<UscitaJSON> trovaTutti();
	
	public OrdineJSON inserisci(OrdineJSON json);
	
	public UscitaDettaglioJSON inserisciDettaglio(UscitaDettaglioJSON json);
	
	public UscitaJSON aggiorna(UscitaJSON json);
	
	public UscitaDettaglioJSON aggiornaDettaglio(UscitaDettaglioJSON json);
	
	public UscitaJSON elimina(UscitaJSON json);
	
	public UscitaDettaglioJSON eliminaDettaglio(UscitaDettaglioJSON json);
	
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
