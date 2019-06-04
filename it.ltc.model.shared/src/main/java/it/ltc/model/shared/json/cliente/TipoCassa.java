package it.ltc.model.shared.json.cliente;

/**
 * Definisce i possibili tipi di cassa.
 * @author Damiano
 *
 */
public enum TipoCassa {
	
	NO("No"),
	STANDARD("Standard"),
	BUNDLE("Bundle");
	
	private final String descrizione;
	
	private TipoCassa(String descrizione) {
		this.descrizione = descrizione;
	}
	
	@Override
	public String toString() {
		return descrizione;
	}
	
	/**
	 * Determina il tipo di cassa in base alla stringa passata come argomento.
	 * Viene restituito <code>null</code> se non viene passato un tipo valido.
	 * @param tipo stringa che rappresenta una possibile tipologia di cassa.
	 * @return il corrispondente valore della <code>enum</code> o <code>null</code> se non trovato.
	 */
	public static TipoCassa getTipo(String tipo) {
		TipoCassa tipoCassa;
		try { tipoCassa = TipoCassa.valueOf(tipo); } catch(Exception e) { tipoCassa = null; }
		return tipoCassa;
	}

}
