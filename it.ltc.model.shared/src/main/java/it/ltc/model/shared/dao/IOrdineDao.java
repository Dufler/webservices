package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.interno.OperatoreOrdine;
import it.ltc.model.shared.json.interno.OrdineStato;
import it.ltc.model.shared.json.interno.OrdineTestata;
import it.ltc.model.shared.json.interno.RisultatoAssegnazioneOrdine;
import it.ltc.model.shared.json.interno.RisultatoFinalizzazioneOrdine;

/**
 * Definisce i metodi che il dao deve implemetare per poter gestire la testata di un'ordine.
 * @author Damiano
 *
 */
public interface IOrdineDao {
	
	public OrdineTestata inserisci(OrdineTestata json);
	
	public OrdineTestata aggiorna(OrdineTestata json);
	
	public OrdineTestata elimina(OrdineTestata json);
	
	public OrdineTestata modificaStato(OrdineTestata json);
	
	public RisultatoAssegnazioneOrdine assegna(int id);
	
	public RisultatoAssegnazioneOrdine recuperaAssegnazione(int id);
	
	public OrdineTestata trovaPerID(int id);
	
	public List<OrdineTestata> trovaCorrispondenti(OrdineTestata filtro);
	
	public List<OrdineStato> trovaStati(int idTestata);

	public List<OperatoreOrdine> trovaOperatori(int idOrdine);

	public RisultatoFinalizzazioneOrdine finalizza(int idOrdine);

}
