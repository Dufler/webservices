package it.ltc.services.clienti.model.prodotto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ImballoJSON {
	
	private String riferimento;
	private String barcode;
	private double peso;
	private double volume;
	private Double h;
	private Double l;
	private Double z;
	
	@JsonInclude(value=Include.NON_EMPTY)
	private List<ProdottoImballatoJSON> prodotti;
	
	@JsonInclude(value=Include.NON_EMPTY)
	private List<String> seriali;
	
	public ImballoJSON() {}

	public String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public Double getH() {
		return h;
	}

	public void setH(Double h) {
		this.h = h;
	}

	public Double getL() {
		return l;
	}

	public void setL(Double l) {
		this.l = l;
	}

	public Double getZ() {
		return z;
	}

	public void setZ(Double z) {
		this.z = z;
	}

	public List<ProdottoImballatoJSON> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<ProdottoImballatoJSON> prodotti) {
		this.prodotti = prodotti;
	}

	public List<String> getSeriali() {
		return seriali;
	}

	public void setSeriali(List<String> seriali) {
		this.seriali = seriali;
	}

}
