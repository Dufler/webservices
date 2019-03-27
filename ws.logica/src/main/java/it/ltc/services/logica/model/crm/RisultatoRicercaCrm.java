package it.ltc.services.logica.model.crm;

import java.util.List;

import it.ltc.database.model.centrale.Azienda;
import it.ltc.database.model.centrale.AziendaNote;
import it.ltc.database.model.centrale.Brand;
import it.ltc.database.model.centrale.Contatto;

/**
 * Oggetto POJO che contiene i risultati della ricerca su tutte le entity crm attraverso una parola chiave.
 * @author Damiano
 *
 */
public class RisultatoRicercaCrm {
	
	private List<Azienda> aziende;
	private List<Contatto> contatti;
	private List<AziendaNote> note;
	private List<Brand> brands;
	
	public RisultatoRicercaCrm() {}

	public List<Azienda> getAziende() {
		return aziende;
	}

	public void setAziende(List<Azienda> aziende) {
		this.aziende = aziende;
	}

	public List<Contatto> getContatti() {
		return contatti;
	}

	public void setContatti(List<Contatto> contatti) {
		this.contatti = contatti;
	}

	public List<AziendaNote> getNote() {
		return note;
	}

	public void setNote(List<AziendaNote> note) {
		this.note = note;
	}

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}

}
