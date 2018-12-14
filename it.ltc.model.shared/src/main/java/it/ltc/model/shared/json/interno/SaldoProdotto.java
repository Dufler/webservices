package it.ltc.model.shared.json.interno;

/**
 * Rappresenta il saldo di un prodotto in un magazzino.
 * @author Damiano
 *
 */
public class SaldoProdotto {
	
	private int idProdotto;
	private String skuProdotto;
	
	private int esistente;
	private int disponibile;
	private int impegnato;
	private int totaleEntrato;
	private int totaleUscito;
	
	private String magazzino;
	
	public SaldoProdotto() {}

	public int getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(int idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getSkuProdotto() {
		return skuProdotto;
	}

	public void setSkuProdotto(String skuProdotto) {
		this.skuProdotto = skuProdotto;
	}

	public int getEsistente() {
		return esistente;
	}

	public void setEsistente(int esistente) {
		this.esistente = esistente;
	}

	public int getDisponibile() {
		return disponibile;
	}

	public void setDisponibile(int disponibile) {
		this.disponibile = disponibile;
	}

	public int getImpegnato() {
		return impegnato;
	}

	public void setImpegnato(int impegnato) {
		this.impegnato = impegnato;
	}

	public int getTotaleEntrato() {
		return totaleEntrato;
	}

	public void setTotaleEntrato(int totaleEntrato) {
		this.totaleEntrato = totaleEntrato;
	}

	public int getTotaleUscito() {
		return totaleUscito;
	}

	public void setTotaleUscito(int totaleUscito) {
		this.totaleUscito = totaleUscito;
	}

	public String getMagazzino() {
		return magazzino;
	}

	public void setMagazzino(String magazzino) {
		this.magazzino = magazzino;
	}

}
