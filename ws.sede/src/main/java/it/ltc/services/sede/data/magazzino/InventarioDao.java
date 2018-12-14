package it.ltc.services.sede.data.magazzino;

import it.ltc.services.sede.model.magazzino.ColloInventarioConSeriali;
import it.ltc.services.sede.model.magazzino.ControlloSeriale;

public interface InventarioDao {
	
	/**
	 * Crea un nuovo collo a sistema contenente la lista dei seriali indicati nell'argomento.<br>
	 * Viene aggiornato di conseguenza il packing list dell'inventario indicato con i prodotti indicati.<br>
	 * Viene restituita un'etichetta EPL/ZPL da stampare.
	 * @param collo il contenuto del collo da inserire a sistema. 
	 * @return il collo effettivamente inserito a sistema.
	 */
	public ColloInventarioConSeriali nuovoCollo(ColloInventarioConSeriali collo);
	
	/**
	 * Crea un nuovo collo a sistema contenente la lista dei seriali indicati come argomento.<br>
	 * Viene aggiornato di conseguenza il packing list degli scarichi indicato con i prodotti indicati.<br>
	 * Vengono impegnati i pezzi indicati.<br>
	 * Vengono fatti movimenti di scarico e carico opportuni.<br>
	 * Viene restituita un'etichetta EPL/ZPL da stampare.
	 * @param collo
	 * @return
	 */
	public ColloInventarioConSeriali nuovoColloDiScarico(ColloInventarioConSeriali collo);
	
	/**
	 * Controlla se il seriale indicato è effettivamente impegnato.<br>
	 * L'implementazione del controllo dipende dal cliente.
	 * @param seriale
	 * @return l'esisto dell'operazione
	 */
	public boolean checkImpegnoSeriale(ControlloSeriale seriale);
	
	/**
	 * Controlla se il collo non contiene nulla e se è effettivamente vuoto lo segna come eliminato.
	 * @param collo nei sistemi legacy è KeyColloCar
	 * @return l'esisto dell'operazione
	 */
	public boolean distruggiCollo(ColloInventarioConSeriali colloDaDistruggere);

}
