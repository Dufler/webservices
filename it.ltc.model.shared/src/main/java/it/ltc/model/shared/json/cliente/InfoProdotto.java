package it.ltc.model.shared.json.cliente;

import java.io.Serializable;

public class InfoProdotto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String codiceArticolo;
	private int giacenza;
	private int disponibilità;
	private String magazzino;
	
	public InfoProdotto() {}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}
	
	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}
	
	public int getGiacenza() {
		return giacenza;
	}
	
	public void setGiacenza(int giacenza) {
		this.giacenza = giacenza;
	}
	
	public int getDisponibilità() {
		return disponibilità;
	}
	
	public void setDisponibilità(int disponibilità) {
		this.disponibilità = disponibilità;
	}
	
	public String getMagazzino() {
		return magazzino;
	}
	
	public void setMagazzino(String magazzino) {
		this.magazzino = magazzino;
	}

}
