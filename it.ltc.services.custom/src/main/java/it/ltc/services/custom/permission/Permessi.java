package it.ltc.services.custom.permission;

/**
 * Lista statica dei permessi all'interno dell'applicazione.
 * @author Damiano
 *
 */
public enum Permessi {
	
	/**
	 * Permesso speciale che garantisce la possibilità di fare tutto ma ti fa lavorare nell'ambiente di test
	 */
	TEST(1),
	
	/**
	 * Permette di usare le applicazioni per i clienti.
	 */
	CLIENTE(2),
	
	/**
	 * Permesso per poter utilizzare Logica desktop.
	 */
	LOGICA(3),
	
	/**
	 * Permette di usare la feature CDG.
	 */
	CDG(4),
	
	/**
	 * Permette di usare la feature Amministrazione.
	 */
	AMMINISTRAZIONE(5),
	
	/**
	 * Permette di usare la feature Ufficio.
	 */
	UFFICIO(6),
	
	/**
	 * Permette di usare la feature trasporti.
	 */
	TRASPORTI(7),
	
	/**
	 * Permette di usare la feature Admin.
	 */
	ADMIN(8),
	
	/**
	 * Permette di effettuare analisi nel controllo di gestione.
	 */
	CDG_ANALISI(9),
	
	/**
	 * Permette di visualizzare i costi e ricavi per pezzo nel controllo di gestione.
	 */
	CDG_COSTI_RICAVI(10),
	
	/**
	 * Permette di fare modifiche sui costi e ricavi per pezzo nel controllo di gestione.
	 */
	CDG_COSTI_RICAVI_CUD(11),
	
	/**
	 * Permette di visualizzare gli eventi nel controllo di gestione.
	 */
	CDG_EVENTI(12),
	
	/**
	 * Permette di fare modifiche sugli eventi nel controllo di gestione.
	 */
	CDG_EVENTI_CUD(13),
	
	/**
	 * Permette di visualizzare il costo del personale nel controllo di gestione.
	 */
	CDG_COSTO_PERSONALE(14),
	
	/**
	 * Permette di fare modifiche sul costo del personale nel controllo di gestione.
	 */
	CDG_COSTO_PERSONALE_CUD(15),
	
	/**
	 * Permette di visualizzare i listini cliente per la logistica.
	 */
	AMMINISTRAZIONE_LISTINI(16),
	
	/**
	 * Permette di fare modifiche ai listini cliente per la logistica.
	 */
	AMMINISTRAZIONE_LISTINI_CUD(17),
	
	/**
	 * Permette di visualizzare le preferenze sulla fatturazione.
	 */
	AMMINISTRAZIONE_PREFERENZE_FATTURAZIONE(18),
	
	/**
	 * Permette di fare modifiche alle preferenze sulla fatturazione.
	 */
	AMMINSTRAZIONE_PREFERENZE_FATTURAZIONE_CUD(19),
	
	/**
	 * Permette di visualizzare i dati sui clienti e le commesse.
	 */
	AMMINISTRAZIONE_CLIENTI_COMMESSE(20),
	
	/**
	 * Permette di fare modifiche sui dati dei clienti e delle commesse.
	 */
	AMMINISTRAZIONE_CLIENTI_COMMESSE_CUD(21),
	
	/**
	 * Permette di eseguire operazioni sugli ingressi di merce.
	 */
	UFFICIO_INGRESSI(22),
	
	/**
	 * Permette di eliminare completamente un carico.
	 */
	UFFICIO_INGRESSI_ELIMINA(56),
	
	/**
	 * Permette di eseguire operazioni sulle uscite di merce.
	 */
	UFFICIO_USCITE(23),
	
	/**
	 * Permette di visualizzare le spedizioni.
	 */
	TRASPORTI_SPEDIZIONI(24),
	
	/**
	 * Permette di fare modifiche alle spedizioni.
	 */
	TRASPORTI_SPEDIZIONI_CUD(25),
	
	/**
	 * Permette di effettuare report sui trasporti.
	 */
	TRASPORTI_REPORT(26),
	
	/**
	 * Permette di effettuare preventivi sui trasporti.
	 */
	TRASPORTI_PREVENTIVI(27),
	
	/**
	 * Permette di visualizzare i Listini Cliente relativi ai trasporti.
	 */
	TRASPORTI_LISTINI_CLIENTI(28),
	
	/**
	 * Permette di fare modifiche ai Listini Cliente relativi ai trasporti.
	 */
	TRASPORTI_LISTINI_CLIENTI_CUD(29),
	
	/**
	 * Permette di visualizzare i Listini Corriere relativi ai trasporti.
	 */
	TRASPORTI_LISTINI_CORRIERI(30),
	
	/**
	 * Permette di fare modifiche ai Listini Corriere relativi ai trasporti.
	 */
	TRASPORTI_LISTINI_CORRIERI_CUD(31),
	
	/**
	 * Permette di lavorare con i listini di simulazione.
	 */
	TRASPORTI_LISTINI_SIMULAZIONE(32),
	
	/**
	 * Permette di visualizzare le informazioni di fatturazione relative ai trasporti.
	 */
	TRASPORTI_FATTURAZIONE(33),
	
	/**
	 * Permette di fatturare le spedizioni.
	 */
	TRASPORTI_FATTURAZIONE_SPEDIZIONI(35),
	
	/**
	 * Permette di fatturare le giacenze.
	 */
	TRASPORTI_FATTURAZIONE_GIACENZE(37),
	
	/**
	 * Permette di fatturare i ritiri.
	 */
	TRASPORTI_FATTURAZIONE_RITIRI(38),
	
	/**
	 * Permette di fatturare le spedizioni ue.
	 */
	TRASPORTI_FATTURAZIONE_SPEDIZIONI_UE(39),
	
	/**
	 * Permette di fatturare le spedizioni extra ue.
	 */
	TRASPORTI_FATTURAZIONE_SPEDIZIONI_EXTRA_UE(40),
	
	/**
	 * Permette di visualizzare gli indirizzi.
	 */
	TRASPORTI_INDIRIZZI(41),
	
	/**
	 * Permette di fare modifiche agli indirizzi.
	 */
	TRASPORTI_INDIRIZZI_GESTIONE(42),
	
	/**
	 * Permette di visualizzare i cap.
	 */
	TRASPORTI_CAP(43),
	
	/**
	 * Permette di fare modifiche ai cap.
	 */
	TRASPORTI_CAP_CUD(44),
	
	/**
	 * Permette di visualizzare i codici cliente.
	 */
	TRASPORTI_CODICI_CLIENTE(45),
	
	/**
	 * Permette di fare modifiche ai codici cliente.
	 */
	TRASPORTI_CODICI_CLIENTE_GESTIONE(46),	
	
	/**
	 * Permesso per poter eliminare un documento di fatturazione
	 */
	FATTURAZIONE_ELIMINA_DOCUMENTO(47),
	
	/**
	 * Permette di inserire e fare modifiche agli utenti presenti a sistema.
	 */
	ADMIN_GESTIONE_UTENTE(48),
	
	/**
	 * Permette di gestire i permessi esistenti all'interno dell'applicazione.
	 */
	ADMIN_GESTIONE_PERMESSI(49),
	
	/**
	 * Permette di gestire gli ambiti e sottoambiti di fatturazione.
	 */
	ADMIN_GESTIONE_FATTURAZIONE(50),
	
	/**
	 * Permette di usare i web services per la gestione dei prodotti.
	 */
	CLIENTE_PRODOTTI(51),
	
	/**
	 * Permette di usare i web services per la gestione dei carichi.
	 */
	CLIENTE_CARICHI(52),
	
	/**
	 * Permette di usare i web services per la gestione dei fornitori.
	 */
	CLIENTE_FORNITORI(53),
	
	/**
	 * Permette di usare i web services per la gestione dei ordini.
	 */
	CLIENTE_ORDINI(54),
	
	/**
	 * Permette di usare i web services per la visione del tracking.
	 */
	CLIENTE_TRACKING(55);
	
	private final int id;
	
	private Permessi(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public String toString() {
		return name() + " ID: " + id;
	}

}
