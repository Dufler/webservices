package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.cliente.Brand;

/**
 * Interfaccia per la manipolazione dei Brand. (tabella Marchi nei sistemi legacy)
 * @author Damiano
 *
 */
public interface IBrandDao {
	
	/**
	 * Imposta l'utente che ha richiesto i servizi del dao.
	 * @param utente
	 */
	public void setUtente(String utente);
	
	public Brand trovaPerID(int id);
	
	public Brand trovaPerCodice(int codice);
	
	public List<Brand> trovaTutte();
	
	public Brand inserisci(Brand brand);
	
	public Brand aggiorna(Brand brand);
	
	public Brand elimina(Brand brand);

}
