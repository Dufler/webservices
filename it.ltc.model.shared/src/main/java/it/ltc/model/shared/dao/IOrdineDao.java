package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.cliente.ImballoJSON;
import it.ltc.model.shared.json.interno.ordine.DatiSpedizione;
import it.ltc.model.shared.json.interno.ordine.OperatoreOrdine;
import it.ltc.model.shared.json.interno.ordine.OrdineStato;
import it.ltc.model.shared.json.interno.ordine.OrdineTestata;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoAssegnazioneOrdine;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoFinalizzazioneOrdine;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoGenerazioneMovimenti;

/**
 * Definisce i metodi che il dao deve implemetare per poter gestire la testata di un'ordine.
 * @author Damiano
 *
 */
public interface IOrdineDao {
	
	public OrdineTestata inserisci(OrdineTestata json);
	
	public OrdineTestata aggiorna(OrdineTestata json);
	
	public OrdineTestata elimina(OrdineTestata json);
	
	//public OrdineTestata modificaStato(OrdineTestata json);
	
	public RisultatoAssegnazioneOrdine assegna(int id);
	
	public RisultatoAssegnazioneOrdine recuperaAssegnazione(int id);
	
	public RisultatoGenerazioneMovimenti generaMovimentiUscita(int id);
	
	public DatiSpedizione trovaDatiSpedizione(int id);
	
	public DatiSpedizione generaDatiSpedizione(DatiSpedizione json);
	
	public OrdineTestata trovaPerID(int id);
	
	public List<OrdineTestata> trovaCorrispondenti(OrdineTestata filtro);
	
	public List<OrdineStato> trovaStati(int idTestata);

	public List<OperatoreOrdine> trovaOperatori(int idOrdine);

	public RisultatoFinalizzazioneOrdine finalizza(int idOrdine);
	
	public List<ImballoJSON> ottieniDettagliImballo(int idOrdine);
	
	public OrdineTestata annullaImballo(int idOrdine);
	
	public OrdineTestata annullaAssegnazioneConRiposizionamento(int idOrdine);
	
	public OrdineTestata annullaAssegnazioneConNuovoCarico(int idOrdine);
	
	public OrdineTestata annullaImportazione(int idOrdine);

}
