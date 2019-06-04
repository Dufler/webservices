package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.interno.ordine.DatiSpedizione;
import it.ltc.model.shared.json.interno.ordine.DeliveryJSON;

public interface ISpedizioneDao {
	
	/**
	 * Trova la spedizione con il determinato ID.
	 */
	public DatiSpedizione trovaPerID(int id);
	
	/**
	 * Trova le spedizioni che corrispondono ai criteri specificati.
	 */
	public List<DatiSpedizione> trovaCorrispondenti(DatiSpedizione filtro);
	
	/**
	 * Abilita o disabilita la partenza di una spedizione.
	 */
	public DatiSpedizione abilita(DatiSpedizione spedizione, boolean abilita);
	
	/**
	 * Elimina i dati su una spedizione e resetta lo stato dell'ordine per poterle reinserire.
	 */
	public DatiSpedizione elimina(DatiSpedizione spedizione);
	
	public DeliveryJSON inserisciDelivery(DeliveryJSON delivery);
	
	public DeliveryJSON aggiornaDelivery(DeliveryJSON delivery);
	
	public DeliveryJSON eliminaDelivery(DeliveryJSON delivery);
	
	public DeliveryJSON trovaDettagliDelivery(int id);
	
	public List<DeliveryJSON> trovaDelivery(DeliveryJSON filtro);

}
