package it.ltc.model.shared.json.cliente;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	private int pezziEffettivi;
	
	@JsonInclude(Include.NON_NULL) //Solo alcuni clienti hanno questa feature.
	private String particolarita;
	
	@JsonInclude(Include.NON_NULL) //Usato solo dai dao e controller di sede, non viene inserito per i clienti.
	private Integer commessa;

	@JsonInclude(Include.NON_NULL)
	private Date dataUltimaModifica;

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

	public String getParticolarita() {
		return particolarita;
	}

	public void setParticolarita(String particolarita) {
		this.particolarita = particolarita;
	}
	
	public Integer getCommessa() {
		return commessa;
	}

	public void setCommessa(Integer commessa) {
		this.commessa = commessa;
	}

	public Date getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(Date dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public int getPezziEffettivi() {
		return pezziEffettivi;
	}

	public void setPezziEffettivi(int pezziEffettivi) {
		this.pezziEffettivi = pezziEffettivi;
	}

	@Override
	public String toString() {
		return "ProdottoJSON [id=" + id + ", cassa=" + cassa + ", chiaveCliente=" + chiaveCliente + ", codiceModello=" + codiceModello + ", barcode=" + barcode + ", taglia=" + taglia + ", colore=" + colore + ", descrizione=" + descrizione + ", descrizioneAggiuntiva="
				+ descrizioneAggiuntiva + ", composizione=" + composizione + ", brand=" + brand + ", categoria=" + categoria + ", madeIn=" + madeIn + ", sottoCategoria=" + sottoCategoria + ", stagione=" + stagione + ", valore=" + valore + ", h=" + h + ", l=" + l + ", z=" + z
				+ ", peso=" + peso + ", skuFornitore=" + skuFornitore + ", barcodeFornitore=" + barcodeFornitore + ", note=" + note + ", particolarita=" + particolarita + ", commessa=" + commessa + ", dataUltimaModifica=" + dataUltimaModifica + "]";
	}

}
