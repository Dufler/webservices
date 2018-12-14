package it.ltc.services.sede.model.magazzino;

import java.util.List;

public class ColloInventarioConSeriali {
	
	private int carico;
	private String magazzino;
	private String ubicazione;
	private List<String> seriali;
	
	//Queste variabili vengono valorizzate dal server nella risposta ad un inserimento avvenuto con successo
	private int idCollo;
	private String collo;
	private String etichetta;
	
	private boolean forzaDistruzione;
	
	public ColloInventarioConSeriali() {}

	public int getCarico() {
		return carico;
	}

	public void setCarico(int carico) {
		this.carico = carico;
	}

	public String getMagazzino() {
		return magazzino;
	}

	public void setMagazzino(String magazzino) {
		this.magazzino = magazzino;
	}

	public String getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

	public List<String> getSeriali() {
		return seriali;
	}

	public void setSeriali(List<String> seriali) {
		this.seriali = seriali;
	}

	public int getIdCollo() {
		return idCollo;
	}

	public void setIdCollo(int idCollo) {
		this.idCollo = idCollo;
	}

	public String getCollo() {
		return collo;
	}

	public void setCollo(String collo) {
		this.collo = collo;
	}

	public String getEtichetta() {
		return etichetta;
	}

	public void setEtichetta(String etichetta) {
		this.etichetta = etichetta;
	}

	public boolean isForzaDistruzione() {
		return forzaDistruzione;
	}

	public void setForzaDistruzione(boolean forzaDistruzione) {
		this.forzaDistruzione = forzaDistruzione;
	}
	

}
