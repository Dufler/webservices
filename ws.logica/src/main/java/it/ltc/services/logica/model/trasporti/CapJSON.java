package it.ltc.services.logica.model.trasporti;

import java.util.Date;

public class CapJSON {
	
	private String cap;
	private String localita;
	private String provincia;
	private String regione;
	private boolean tntOreDieci;
	private boolean tntOreDodici;
	private boolean brtDisagiate;
	private boolean brtIsole;
	private boolean brtZtl;
	private Date dataUltimaModifica;
	
	public CapJSON() {}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public boolean getTntOreDieci() {
		return this.tntOreDieci;
	}

	public void setTntOreDieci(boolean tntOreDieci) {
		this.tntOreDieci = tntOreDieci;
	}

	public boolean getTntOreDodici() {
		return this.tntOreDodici;
	}

	public void setTntOreDodici(boolean tntOreDodici) {
		this.tntOreDodici = tntOreDodici;
	}

	public boolean isBrtDisagiate() {
		return brtDisagiate;
	}

	public void setBrtDisagiate(boolean brtDisagiate) {
		this.brtDisagiate = brtDisagiate;
	}

	public boolean isBrtIsole() {
		return brtIsole;
	}

	public void setBrtIsole(boolean brtIsole) {
		this.brtIsole = brtIsole;
	}

	public boolean isBrtZtl() {
		return brtZtl;
	}

	public void setBrtZtl(boolean brtZtl) {
		this.brtZtl = brtZtl;
	}
	
	public Date getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(Date dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

}
