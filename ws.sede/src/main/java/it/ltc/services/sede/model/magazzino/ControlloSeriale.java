package it.ltc.services.sede.model.magazzino;

/**
 * Classe che rappresenta la richiesta di eseguire controlli su un determinato seriale presente in collipack.<br>
 * Si può richiedere in maniera opzionale anche un controllo sull'impegno.
 * @author Damiano
 *
 */
public class ControlloSeriale {
	
	private String seriale;
	private String magazzino;
	private int carico;
	private boolean checkImpegno;
	
	public ControlloSeriale() {}

	public String getSeriale() {
		return seriale;
	}

	public void setSeriale(String seriale) {
		this.seriale = seriale;
	}

	public String getMagazzino() {
		return magazzino;
	}

	public void setMagazzino(String magazzino) {
		this.magazzino = magazzino;
	}

	public int getCarico() {
		return carico;
	}

	public void setCarico(int carico) {
		this.carico = carico;
	}

	public boolean isCheckImpegno() {
		return checkImpegno;
	}

	public void setCheckImpegno(boolean checkImpegno) {
		this.checkImpegno = checkImpegno;
	}

}
