package it.ltc.services.clienti.model.prodotto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ProdottoJSON {
	
	private int id;
	private String cassa;
	private String chiaveCliente;
	private String codiceModello;
	private String barcode;
	private String taglia;
	private String colore;
	private String descrizione;
	private String descrizioneAggiuntiva;
	private String composizione;
	private String brand;
	private String categoria;
	private String madeIn;
	private String sottoCategoria;
	private String stagione;
	private Double valore;
	private Integer h;
	private Integer l;
	private Integer z;
	private Integer peso;
	private String skuFornitore;
	private String barcodeFornitore;
	private String note;

	public ProdottoJSON() {}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBarcode() {
		return this.barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCassa() {
		return this.cassa;
	}

	public void setCassa(String cassa) {
		this.cassa = cassa;
	}

	public String getCodiceModello() {
		return this.codiceModello;
	}

	public void setCodiceModello(String codiceModello) {
		this.codiceModello = codiceModello;
	}

	public String getColore() {
		return this.colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public String getComposizione() {
		return this.composizione;
	}

	public void setComposizione(String composizione) {
		this.composizione = composizione;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDescrizioneAggiuntiva() {
		return descrizioneAggiuntiva;
	}

	public void setDescrizioneAggiuntiva(String descrizioneAggiuntiva) {
		this.descrizioneAggiuntiva = descrizioneAggiuntiva;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getMadeIn() {
		return this.madeIn;
	}

	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}

	public String getSottoCategoria() {
		return this.sottoCategoria;
	}

	public void setSottoCategoria(String sottoCategoria) {
		this.sottoCategoria = sottoCategoria;
	}

	public String getTaglia() {
		return this.taglia;
	}

	public void setTaglia(String taglia) {
		this.taglia = taglia;
	}

	public Double getValore() {
		return this.valore;
	}

	public void setValore(Double valore) {
		this.valore = valore;
	}

	public String getSkuFornitore() {
		return skuFornitore;
	}

	public void setSkuFornitore(String skuFornitore) {
		this.skuFornitore = skuFornitore;
	}

	public String getBarcodeFornitore() {
		return barcodeFornitore;
	}

	public void setBarcodeFornitore(String barcodeFornitore) {
		this.barcodeFornitore = barcodeFornitore;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getChiaveCliente() {
		return chiaveCliente;
	}

	public void setChiaveCliente(String chiaveCliente) {
		this.chiaveCliente = chiaveCliente;
	}

	public String getStagione() {
		return stagione;
	}

	public void setStagione(String stagione) {
		this.stagione = stagione;
	}
	
	public Integer getH() {
		return this.h;
	}

	public void setH(Integer h) {
		this.h = h;
	}
	
	public Integer getL() {
		return this.l;
	}

	public void setL(Integer l) {
		this.l = l;
	}
	
	public Integer getZ() {
		return this.z;
	}

	public void setZ(Integer z) {
		this.z = z;
	}
	
	public Integer getPeso() {
		return this.peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "ProdottoJSON [id=" + id + ", chiaveCliente=" + chiaveCliente + ", barcode=" + barcode + ", descrizione=" + descrizione + "]";
	}

}
