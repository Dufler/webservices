package it.ltc.services.sede.data.carico;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;

import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.ColliCaricoDao;
import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.ColliCarico;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.model.StatoCarico;
import it.ltc.services.custom.exception.CustomException;
import it.ltc.services.sede.model.carico.ProdottoCaricoJSON;

public class RiscontroProdottiLegacyDAOImpl extends ColliPackDao implements RiscontroProdottiDAO {
	
	private static final Logger logger = Logger.getLogger("RiscontroProdottiLegacyDAOImpl");

	private final PakiTestaDao daoTestata;
	protected final PakiArticoloDao daoRighe;
	private final ArticoliDao daoArticoli;
	protected final ColliCaricoDao daoColli;
	
	public RiscontroProdottiLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoTestata = new PakiTestaDao(persistenceUnit);
		daoRighe = new PakiArticoloDao(persistenceUnit);
		daoArticoli = new ArticoliDao(persistenceUnit);
		daoColli = new ColliCaricoDao(persistenceUnit);
	}
	
	protected PakiTesta checkCarico(int idCarico) {
		PakiTesta carico = daoTestata.trovaDaID(idCarico);
		if (carico == null)
			throw new CustomException("Il carico indicato non esiste (ID: " + idCarico + ")");
		else if (!carico.getStato().equals(StatoCarico.IN_LAVORAZIONE.getNome()))
			throw new CustomException("Il carico specificato non e' lavorabile. (Stato: " + carico.getStato() + ")");
		return carico;
	}
	
	protected PakiArticolo checkRiga(PakiTesta testata, Articoli articolo, ColliCarico collo, ProdottoCaricoJSON prodotto) {
		PakiArticolo riga = prodotto.getRiga() == 0 ? creaPakiArticolo(testata, articolo, collo, prodotto.getQuantita()) : daoRighe.trovaDaID(prodotto.getRiga());
		if (riga == null)
			throw new CustomException("La riga del dichiarato indicata non esiste (ID: " + prodotto.getRiga() + ")");
		return riga;
	}
	
	protected Articoli checkArticolo(int idProdotto) {
		Articoli articolo = daoArticoli.trovaDaID(idProdotto);
		if (articolo == null)
			throw new CustomException("L'articolo indicato non esiste (ID: " + idProdotto + ")");
		return articolo;
	}
	
	protected ColliCarico checkCollo(int idCollo) {
		ColliCarico collo = daoColli.trovaDaID(idCollo);
		if (collo == null)
			throw new CustomException("Il collo indicato non esiste (ID: " + idCollo + ")");
		return collo;
	}
	
	protected ColliPack checkProdotto(int idProdotto) {
		ColliPack prodotto = trovaDaID(idProdotto);
		if (prodotto == null)
			throw new CustomException("Il prodotto già verificato che hai indicato non esiste (ID: " + idProdotto + ")");
		return prodotto;
	}
	
	protected void checkArticoloRiga(PakiArticolo riga, Articoli articolo) {
		if (!riga.getCodUnicoArt().equals(articolo.getIdUniArticolo()))
			throw new CustomException("L'articolo indicato non è presente nella riga indicata (ID Prodotto: " + articolo.getIdArticolo() + ", ID Riga: " + riga.getIdPakiArticolo() + ")");
	}
	
	protected void checkCaricoRiga(PakiTesta carico, PakiArticolo riga, ProdottoCaricoJSON prodotto) {
		//Nel caso in cui è una nuova riga controllo se è possibile inserirle
		if (prodotto.getRiga() == 0) {
			if (!carico.getTipoPack().equals("INS"))
				throw new CustomException("Non è possibile inserire prodotti non dichiarati nel carico indicato.");
		} else {
			//Controllo se la riga è presente nel carico
			if (carico.getIdTestaPaki() != riga.getIdPakiTesta())
				throw new CustomException("Il carico indicato non contiene la riga indicata (ID carico: " + carico.getIdTestaPaki() + ", ID Riga: " + riga.getIdPakiArticolo() + ")");
			//Controllo la quantità, se è maggiore di quella disponibile verifico se posso andare in eccedenza.
			if (prodotto.isForzaEccedenza() && riga.getQtaPaki() - riga.getQtaVerificata() < prodotto.getQuantita() && carico.getAbilitaEccedenze().equals("NO"))
				throw new CustomException("Non è possibile inserire prodotti eccedenti le quantità dichiarate nel carico indicato.");
		}
	}
	
	protected void checkColloProdotto(ColliCarico collo, ColliPack prodotto) {
		if (!collo.getKeyColloCar().equals(prodotto.getNrIdColloPk()))
			throw new CustomException("Il prodotto verificato indicato non è presente nel collo indicato.");
	}
	
	protected PakiArticolo creaPakiArticolo(PakiTesta testata, Articoli articolo, ColliCarico collo, int quantita) {
		PakiArticolo nuovaRiga = new PakiArticolo();
		nuovaRiga.setIdPakiTesta(testata.getIdTestaPaki());
		nuovaRiga.setNrDispo(testata.getNrPaki());
		nuovaRiga.setRigaPacki(0); //FIXME: Questo non va bene forse.
		nuovaRiga.setCodBarre(articolo.getIdUniArticolo());
		nuovaRiga.setCodUnicoArt(articolo.getIdUniArticolo());
		nuovaRiga.setCodArtStr(articolo.getCodArtStr());
		nuovaRiga.setMagazzino(collo.getMagazzino()); //FIXME: Questo forse dovrebbe essere il magazzino del cliente.
		nuovaRiga.setMagazzinoltc(collo.getMagazzino());
		nuovaRiga.setQtaPaki(0);
		nuovaRiga.setQtaVerificata(quantita);
		nuovaRiga.setNrDispo(testata.getNrPaki());
		nuovaRiga.setMadeIn("");
		nuovaRiga.setCodMotivo("INSE");
		return nuovaRiga;
	}
	
	protected ColliPack creaColliPack(PakiTesta testata, PakiArticolo riga, Articoli articolo, ColliCarico collo, int quantitaDaAggiungere) {
		ColliPack prodotto = new ColliPack();
		//Inserisco le informazioni necessarie
		prodotto.setIdTestaPaki(testata.getIdTestaPaki());
		prodotto.setFlagimp("N");
		prodotto.setCodArtStr(riga.getCodArtStr());
		prodotto.setCodiceArticolo(riga.getCodUnicoArt());
		prodotto.setIdPakiarticolo(riga.getIdPakiArticolo());
		prodotto.setMagazzino(collo.getMagazzino()); //Lo prendo da cosa è stato verificato dall'operatore
		prodotto.setNrIdColloPk(collo.getKeyColloCar());
		prodotto.setQta(quantitaDaAggiungere);
		prodotto.setQtaimpegnata(0);
		prodotto.setTaglia(articolo.getTaglia());
		prodotto.setDescrizione(articolo.getDescrizione());
		return prodotto;
	}
	
	@Override
	public ProdottoCaricoJSON nuovoProdotto(ProdottoCaricoJSON prodotto) {
		//Recupero le info ed eseguo i controlli necessari
		PakiTesta carico = checkCarico(prodotto.getCarico());
		Articoli articolo = checkArticolo(prodotto.getProdotto());
		ColliCarico collo = checkCollo(prodotto.getCollo());
		PakiArticolo riga = checkRiga(carico, articolo, collo, prodotto);
		checkCaricoRiga(carico, riga, prodotto);
		checkArticoloRiga(riga, articolo);
		//Creo il collipack e aggiorno le quantità
		int quantita = prodotto.isForzaEccedenza() || riga.getQtaPaki() == 0 ? prodotto.getQuantita() : Math.min(riga.getQtaPaki() - riga.getQtaVerificata(), prodotto.getQuantita());
		if (quantita < 1)
			throw new CustomException("Impossibile aggiungere il prodotto richiesto sulla riga specificata.");
		ColliPack nuovoProdotto = creaColliPack(carico, riga, articolo, collo, quantita);
		riga.setQtaVerificata(riga.getQtaVerificata() + quantita);
		carico.setQtaTotAre(carico.getQtaTotAre() + quantita);
		//Salvo i dati a sistema
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.persist(nuovoProdotto);
			em.merge(carico);
			em.merge(riga);
			t.commit();
			//aggiorno le info nel json
			prodotto.setQuantita(quantita);
			prodotto.setId(nuovoProdotto.getIdColliPack());
		} catch (Exception e) {
			logger.error(e);
			prodotto = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return prodotto;
	}

	@Override
	public ProdottoCaricoJSON aggiornaProdotto(ProdottoCaricoJSON prodotto) {
		//Recupero le info ed eseguo i controlli necessari
		PakiTesta carico = checkCarico(prodotto.getCarico());
		Articoli articolo = checkArticolo(prodotto.getProdotto());
		ColliCarico collo = checkCollo(prodotto.getCollo());
		PakiArticolo riga = checkRiga(carico, articolo, collo, prodotto);
		ColliPack prodottoEsistente = checkProdotto(prodotto.getId());
		checkCaricoRiga(carico, riga, prodotto);
		checkArticoloRiga(riga, articolo);
		checkColloProdotto(collo, prodottoEsistente);
		//Aggiorno le quantità
		int nuovaQuantita = prodotto.getQuantita();
		int quantitaPrecedente = prodottoEsistente.getQta();
		//Se è uguale o minore vado a decrementare le quantità nel collipack, la riga e la testata
		//altrimenti verifico la capienza massima e aumento il possibile
		if (nuovaQuantita <= quantitaPrecedente) {
			int differenza = quantitaPrecedente - nuovaQuantita;
			prodottoEsistente.setQta(nuovaQuantita);
			riga.setQtaVerificata(riga.getQtaVerificata() - differenza);
			carico.setQtaTotAre(carico.getQtaTotAre() - differenza);
		} else {
			int quantitaDaAggiungere = prodotto.isForzaEccedenza() || riga.getQtaPaki() == 0 ? nuovaQuantita - quantitaPrecedente : Math.min(riga.getQtaPaki() - riga.getQtaVerificata(), nuovaQuantita - quantitaPrecedente);
			prodotto.setQuantita(quantitaPrecedente + quantitaDaAggiungere);
			prodottoEsistente.setQta(quantitaPrecedente + quantitaDaAggiungere);
			riga.setQtaVerificata(riga.getQtaVerificata()  + quantitaDaAggiungere);
			carico.setQtaTotAre(carico.getQtaTotAre() + quantitaDaAggiungere);
		}				
		//Salvo i dati a sistema
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(prodottoEsistente);
			em.merge(carico);
			em.merge(riga);
			t.commit();
		} catch (Exception e) {
			logger.error(e);
			prodotto = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return prodotto;
	}

	@Override
	public ProdottoCaricoJSON eliminaProdotto(ProdottoCaricoJSON prodotto) {
		// Recupero le info ed eseguo i controlli necessari
		PakiTesta carico = checkCarico(prodotto.getCarico());
		Articoli articolo = checkArticolo(prodotto.getProdotto());
		ColliCarico collo = checkCollo(prodotto.getCollo());
		PakiArticolo riga = checkRiga(carico, articolo, collo, prodotto);
		ColliPack prodottoEsistente = checkProdotto(prodotto.getId());
		checkCaricoRiga(carico, riga, prodotto);
		checkArticoloRiga(riga, articolo);
		checkColloProdotto(collo, prodottoEsistente);
		//Aggiorno le quantità
		int quantitaPrecedente = prodottoEsistente.getQta();
		riga.setQtaVerificata(riga.getQtaVerificata() - quantitaPrecedente);
		carico.setQtaTotAre(carico.getQtaTotAre() - quantitaPrecedente);		
		//Elimino il collipack e salvo i dati a sistema
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(em.contains(prodottoEsistente) ? prodottoEsistente : em.find(ColliPack.class, prodottoEsistente.getIdColliPack()));
			em.merge(carico);
			em.merge(riga);
			t.commit();
		} catch (Exception e) {
			logger.error(e);
			prodotto = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return prodotto;
	}

}
