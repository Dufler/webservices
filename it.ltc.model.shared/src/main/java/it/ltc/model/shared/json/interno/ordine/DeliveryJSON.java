package it.ltc.model.shared.json.interno.ordine;

import java.util.Date;
import java.util.Set;

/**
 * Classe che mappa il JSON che rappresenta una deliry da stampare per il corriere.
 * @author Damiano
 *
 */
public class DeliveryJSON {
	
	private int id;
	private Date dataGenerazione;
	private String utente;
	private String corriere;
	private int totaleSpedizioni;
	private int totaleColli;
	private double totalePeso;
	private double totaleVolume;
	
	/**
	 * Viene valorizzato solo in inserimento e nella richiesta di dettaglio.
	 */
	private Set<Integer> spedizioni;
	
	//Vengono usate per filtrare
	private Date da;
	private Date a;
	
	public DeliveryJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataGenerazione() {
		return dataGenerazione;
	}

	public void setDataGenerazione(Date dataGenerazione) {
		this.dataGenerazione = dataGenerazione;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getCorriere() {
		return corriere;
	}

	public void setCorriere(String corriere) {
		this.corriere = corriere;
	}

	public int getTotaleSpedizioni() {
		return totaleSpedizioni;
	}

	public void setTotaleSpedizioni(int totaleSpedizioni) {
		this.totaleSpedizioni = totaleSpedizioni;
	}

	public int getTotaleColli() {
		return totaleColli;
	}

	public void setTotaleColli(int totaleColli) {
		this.totaleColli = totaleColli;
	}

	public double getTotalePeso() {
		return totalePeso;
	}

	public void setTotalePeso(double totalePeso) {
		this.totalePeso = totalePeso;
	}

	public double getTotaleVolume() {
		return totaleVolume;
	}

	public void setTotaleVolume(double totaleVolume) {
		this.totaleVolume = totaleVolume;
	}

	public Set<Integer> getSpedizioni() {
		return spedizioni;
	}

	public void setSpedizioni(Set<Integer> spedizioni) {
		this.spedizioni = spedizioni;
	}

	public Date getDa() {
		return da;
	}

	public void setDa(Date da) {
		this.da = da;
	}

	public Date getA() {
		return a;
	}

	public void setA(Date a) {
		this.a = a;
	}

}
