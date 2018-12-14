package it.ltc.services.sede.model.magazzino;

/**
 * Classe che rappresenta un punto di ubicazione a magazzino e riporta i suoi elementi base;
 * @author Damiano
 *
 */
public class UbicazioneJSON {
	
	public enum TipoUbicazione { 
		
		PRELIEVO("PR"),
		SCORTA("SC");
		
		private final String valoreLegacy;
		
		private TipoUbicazione(String valoreLegacy) {
			this.valoreLegacy = valoreLegacy;
		}
		
		public String getValoreLegacy() {
			return valoreLegacy;
		}
		
		public static TipoUbicazione trovaDaValoreLegacy(String valoreLegacy) {
			TipoUbicazione tipo;
			switch (valoreLegacy) {
				case "PR" : tipo = PRELIEVO; break;
				case "SC" : tipo = SCORTA; break;
				default : tipo = null;
			}
			return tipo;
		}
	}
	
	private String ubicazione;
	private TipoUbicazione tipo;
	
	public UbicazioneJSON() {}

	public String getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

	public TipoUbicazione getTipo() {
		return tipo;
	}

	public void setTipo(TipoUbicazione tipo) {
		this.tipo = tipo;
	}

}
