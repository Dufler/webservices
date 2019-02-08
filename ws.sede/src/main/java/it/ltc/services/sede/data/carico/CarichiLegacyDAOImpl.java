package it.ltc.services.sede.data.carico;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;

import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Condizione;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.dao.legacy.ColliCaricoDao;
import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.FornitoreDao;
import it.ltc.database.dao.legacy.MagaMovDao;
import it.ltc.database.dao.legacy.MagaSdDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.dao.legacy.PakiTestaLogStatoDao;
import it.ltc.database.model.legacy.ColliCarico;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.PakiTestaLogStato;
import it.ltc.database.model.legacy.model.CausaliMovimento;
import it.ltc.database.model.legacy.model.PakiTestaTotali;
import it.ltc.model.shared.dao.ICaricoDao;
import it.ltc.model.shared.json.interno.CaricoStato;
import it.ltc.model.shared.json.interno.CaricoTestata;
import it.ltc.services.custom.exception.CustomErrorCause;
import it.ltc.services.custom.exception.CustomException;

public class CarichiLegacyDAOImpl extends PakiTestaDao implements ICaricoDao {
	
	private static final Logger logger = Logger.getLogger("CarichiLegacyDAOImpl");
	
	protected static final String SPLIT_CHAR = "@";
	
	protected final FornitoreDao daoFornitori;
	protected final ColliPackDao daoProdotti;
	protected final ColliCaricoDao daoColli;
	protected final MagaSdDao daoSaldi;
	protected final MagaMovDao daoMovimenti;
	protected final PakiTestaLogStatoDao daoStati;
	protected final PakiArticoloDao daoRighe;

	public CarichiLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoFornitori = new FornitoreDao(persistenceUnit);
		daoProdotti = new ColliPackDao(persistenceUnit);
		daoColli = new ColliCaricoDao(persistenceUnit);
		daoSaldi = new MagaSdDao(persistenceUnit);
		daoMovimenti = new MagaMovDao(persistenceUnit);
		daoStati = new PakiTestaLogStatoDao(persistenceUnit);
		daoRighe = new PakiArticoloDao(persistenceUnit);
	}
	
	protected PakiTesta checkInserimento(CaricoTestata json) {
		PakiTesta testata = deserializza(json, new PakiTesta());
		//Controllo il riferimento
		PakiTesta esistente = trovaDaRiferimento(testata.getNrPaki());
		if (esistente != null)
			throw new CustomException("Il riferimento indicato per il carico è già esistente. (" + testata.getNrPaki() + ")");
		return testata;
	}
	
	protected PakiTesta checkAggiornamento(CaricoTestata json) {
		PakiTesta testata =	trovaDaID(json.getId());
		//Controllo l'esistenza
		if (testata == null)
			throw new CustomException("Il riferimento indicato per il carico non esiste. (" + json.getId() + ")");
		if (!"INSERITO".equals(testata.getStato()) && !"ARRIVATO".equals(testata.getStato()))
			throw new CustomException("Il carico richiesto non è più modificabile.");
		testata = deserializza(json, testata);
		return testata;
	}

	@Override
	public CaricoTestata inserisci(CaricoTestata json) {
		PakiTesta testata = checkInserimento(json);
		PakiTesta entity = insert(testata);
		return serializza(entity);
	}

	@Override
	public CaricoTestata aggiorna(CaricoTestata json) {
		PakiTesta testata = checkAggiornamento(json);
		PakiTesta entity = update(testata, testata.getIdTestaPaki());
		return serializza(entity);
	}

	@Override
	public CaricoTestata elimina(CaricoTestata json) {
		PakiTesta testata = checkEliminazioneCarico(json);		
		PakiTesta entity = delete(testata.getIdTestaPaki());
		return serializza(entity);
	}

	@Override
	public List<CaricoTestata> trovaCorrispondenti(CaricoTestata filtro) {
		List<CondizioneWhere> condizioni = new LinkedList<>();
		//Tipo di carico
		String tipo = filtro.getTipo();
		if (tipo != null && !tipo.isEmpty()) {
			CondizioneWhere condizione = new CondizioneWhere("tipodocumento", tipo);
			condizioni.add(condizione);
		}
		//Stato del carico
		String stato = filtro.getStato();
		if (stato != null && !stato.isEmpty()) {
			CondizioneWhere condizione = new CondizioneWhere("stato", stato);
			condizioni.add(condizione);
		}
		//Riferimento del cliente, ricerca parziale
		String riferimento = filtro.getRiferimento();
		if (riferimento != null && !riferimento.isEmpty()) {
			CondizioneWhere condizione = new CondizioneWhere("nrPaki", riferimento, Operatore.LIKE, Condizione.AND);
			condizioni.add(condizione);
		}
		//Fornitore 
		int fornitore = filtro.getFornitore();
		if (fornitore > 0) {
			CondizioneWhere condizione = new CondizioneWhere("idFornitore", fornitore);
			condizioni.add(condizione);
		}
		//Data creazione da
		Date da = filtro.getDa();
		if (da != null) {
			CondizioneWhere condizione = new CondizioneWhere("creazione", da, Operatore.GREATER_OR_EQUAL, Condizione.AND);
			condizioni.add(condizione);
		}
		//Data creazione fino a
		Date a = filtro.getA();
		if (a != null) {
			CondizioneWhere condizione = new CondizioneWhere("creazione", a, Operatore.LESSER_OR_EQUAL, Condizione.AND);
			condizioni.add(condizione);
		}		
		List<PakiTesta> entities = findAll(condizioni, 100);
		List<CaricoTestata> carichi = new LinkedList<>();
		for (PakiTesta entity : entities) {
			CaricoTestata json = serializza(entity);
			carichi.add(json);
		}
		return carichi;
	}
	
	protected CaricoTestata serializza(PakiTesta testata) {
		CaricoTestata json;
		if (testata != null) {
			json = new CaricoTestata();
			json.setDataArrivo(testata.getDataInizio());
			json.setDataArrivoPresunto(testata.getDataArrivo());
			json.setDocumentoData(testata.getDataPaki());
			json.setDocumentoRiferimento(testata.getNrDocInterno());
			json.setDocumentoTipo(testata.getTipoDoc());
			json.setFornitore(testata.getIdFornitore());
			json.setId(testata.getIdTestaPaki());
			json.setNote(testata.getNote());
			json.setProdottiEccedenti(testata.getAbilitaEccedenze().equals("SI"));
			json.setProdottiNonDichiarati(testata.getTipoPack().equals("INS"));
			json.setQuantitaDichiarataTotale(testata.getQtaTotAto());
			json.setQuantitaRiscontrataTotale(testata.getQtaTotAre());
			json.setRiferimento(testata.getNrPaki());
			json.setStagione(testata.getStagione());
			json.setStato(testata.getStato());
			json.setTipo(testata.getTipodocumento());
		} else {
			json = null;
		}
		return json;
	}
	
	@Override
	public List<CaricoStato> trovaStati(int idCarico) {
		List<CaricoStato> stati = new LinkedList<>();
		List<PakiTestaLogStato> entities = daoStati.trovaStati(idCarico);
		for (PakiTestaLogStato entity : entities) {
			CaricoStato stato = new CaricoStato();
			stato.setCarico(entity.getIdTestaPaki());
			stato.setStato(entity.getStato());
			stato.setData(entity.getDataOra());
			stati.add(stato);
		}
		return stati;
	}
	
	protected PakiTesta deserializza(CaricoTestata json, PakiTesta testata) {
		if (json != null) {
			testata.setDataInizio(new Timestamp(json.getDataArrivo() != null ? json.getDataArrivo().getTime() : new Date().getTime()));
			testata.setDataArrivo(new Timestamp(json.getDataArrivoPresunto() != null ? json.getDataArrivoPresunto().getTime() : new Date().getTime()));
			testata.setDataPaki(new Timestamp(json.getDocumentoData() != null ? json.getDocumentoData().getTime() : new Date().getTime()));
			testata.setNrDocInterno(json.getDocumentoRiferimento());
			testata.setTipoDoc(json.getDocumentoTipo());
			testata.setIdTestaPaki(json.getId());
			testata.setNote(json.getNote());
			testata.setAbilitaEccedenze(json.isProdottiEccedenti() ? "SI" : "NO");
			testata.setTipoPack(json.isProdottiNonDichiarati() ? "INS" : "ART");
			testata.setQtaTotAto(json.getQuantitaDichiarataTotale());
			testata.setQtaTotAre(json.getQuantitaRiscontrataTotale());
			testata.setNrPaki(json.getRiferimento());
			testata.setStagione(json.getStagione());
			testata.setStato(json.getStato());
			testata.setTipoCarico(json.getTipo());
			testata.setTipodocumento(json.getTipo());
			//Fornitore
			Fornitori fornitore = daoFornitori.trovaPerID(json.getFornitore());
			if (fornitore != null) {
				testata.setIdFornitore(json.getFornitore());
				testata.setCodFornitore(fornitore.getCodiceFornitore());
				testata.setRagSocFor(fornitore.getRagSoc());
			}
		} else {
			testata = null;
		}
		return testata;
	}

	@Override
	public CaricoTestata trovaPerID(int id) {
		CaricoTestata json = serializza(trovaDaID(id));
		return json;
	}
	
	protected void checkPakiArticoloVsColliPackPerGenerazioneCarico(PakiTesta entity, List<ColliPack> prodottiCarico) {
		//Faccio una mappa delle righe per prodotto e relativa quantità.
		List<PakiArticolo> righe = daoRighe.trovaRigheDaCarico(entity.getIdTestaPaki());
		HashMap<String, Integer> mappaQuantitàRighe = new HashMap<>();
		for (PakiArticolo riga : righe) {
			String key = riga.getCodUnicoArt().trim();
			int q = mappaQuantitàRighe.containsKey(key) ? mappaQuantitàRighe.get(key) : 0;
			q += riga.getQtaVerificata();
			mappaQuantitàRighe.put(key, q); 
		}
		//Faccio una mappa dei collipack per prodotto e relativa quantità.
		HashMap<String, Integer> mappaQuantitàProdotti = new HashMap<>();
		for (ColliPack prodotto : prodottiCarico) {
			String key = prodotto.getCodiceArticolo().trim();
			int q = mappaQuantitàProdotti.containsKey(key) ? mappaQuantitàProdotti.get(key) : 0;
			q += prodotto.getQta();
			mappaQuantitàProdotti.put(key, q); 
		}
		//Se il numero di elementi nelle righe è minore lancio subito un'eccezione, se è uguale o maggiore potrebbe andare bene.
		if (mappaQuantitàRighe.keySet().size() < mappaQuantitàProdotti.keySet().size())
			throw new CustomException("Il numero di prodotti effettivamente riscontrati è maggiore del numero di prodotti vericati dichiarato nella packing list.");
		//Verifico le quantità per ogni prodotto riscontrato.
		List<CustomErrorCause> erroriQuantità = new LinkedList<>();
		for (String key : mappaQuantitàRighe.keySet()) {
			int quantitàRighe = mappaQuantitàRighe.get(key);
			int quantitàProdotti = mappaQuantitàProdotti.containsKey(key) ? mappaQuantitàProdotti.get(key) : 0;
			if (quantitàRighe != quantitàProdotti) {
				String product = "ID univoco: '" + key;
				String cause = "Quantità effettivamente riscontrata: " + quantitàProdotti + ", Quantità verificata: " + quantitàRighe;
				CustomErrorCause errorCause = new CustomErrorCause(product, cause);
				erroriQuantità.add(errorCause);
			}
		}
		if (!erroriQuantità.isEmpty()) {
			String message = "La quantità effettivamente riscontrata è diversa da quella verificata nella packing list per i seguenti prodotti";
			throw new CustomException(message, 400, erroriQuantità);
		}
	}
	
	protected PakiTesta checkEliminazioneCarico(CaricoTestata json) {
		// Controllo l'esistenza del riferimento al carico
		PakiTesta carico = trovaDaID(json.getId());
		if (carico == null)
			throw new CustomException("Non esiste nessun carico con questo ID. (" + json.getId() + ")");
		// Controllo lo stato, deve essere 'INSERITO'
		if (!"INSERITO".equals(carico.getStato()))
			throw new CustomException("Il carico richiesto non è più eliminabile.");
		return carico;
	}
	
	protected List<ColliPack> checkProdottiAnnullamentoCarico(PakiTesta carico) {
		List<ColliPack> prodotti = daoProdotti.trovaProdottiNelCarico(carico.getIdTestaPaki());
		for (ColliPack prodotto : prodotti) {
			if (!prodotto.getFlagimp().equals("N") || prodotto.getQtaimpegnata() != 0) {
				throw new CustomException("Il carico contiene merce già impegnata, non è possibile annullarlo.");
			} else if (prodotto.getFlagtc() != 0) {
				throw new CustomException("Il carico contiene merce già generata e caricata a sistema, non è possibile annullarlo.");
			}
		}
		return prodotti;
	}
	
	protected List<ColliCarico> checkColliAnnullamentoCarico(PakiTesta carico) {
		List<ColliCarico> colli = daoColli.trovaColliNelCarico(carico.getIdTestaPaki());
		for (ColliCarico collo : colli) {
			if (!collo.getFlagtc().equals("0") || collo.getUbicato().equals("SI")) {
				throw new CustomException("Il carico contiene colli già ubicati o caricati a sistema, non è possibile annullarlo.");
			}
		}
		return colli;
	}
	
	protected List<PakiArticolo> checkRigheAnnullamentoCarico(PakiTesta carico) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String data = sdf.format(new Date());
		List<PakiArticolo> righe = daoRighe.trovaRigheDaCarico(carico.getIdTestaPaki());
		for (PakiArticolo riga : righe) {
			riga.setNote("Carico annullato da " + utente + " il " + data);
		}
		return righe;
	}
	
	
	protected PakiTesta modificaStatoInAnnullato(PakiTesta carico) {
		List<PakiArticolo> righe = checkRigheAnnullamentoCarico(carico);
		List<ColliPack> prodotti = checkProdottiAnnullamentoCarico(carico);
		List<ColliCarico> colli = checkColliAnnullamentoCarico(carico);
		carico.setStato("ANNULLATO");
		//Vado in scrittura
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(carico);
			for (PakiArticolo riga : righe) {
				em.merge(riga);
			}
			for (ColliPack prodotto : prodotti) {
				em.remove(em.contains(prodotto) ? prodotto : em.find(ColliPack.class, prodotto.getIdColliPack()));
			}
			for (ColliCarico collo : colli) {
				em.remove(em.contains(collo) ? collo : em.find(ColliCarico.class, collo.getIdCollo()));
			}
			t.commit();
		} catch (Exception e) {
			carico = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return carico;
	}
	
	protected PakiTesta modificaStatoInInserito(PakiTesta carico) {
		//Aggiungere controlli, se necessario
		//Aggiorno solo lo stato? Forse potrei togliere qualche data per esempio.
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(carico);
			t.commit();
		} catch (Exception e) {
			carico = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return carico;
	}
	
	protected PakiTesta modificaStatoInArrivato(PakiTesta carico) {
		//Aggiungere controllo se sono state fatte letture.
		//Aggiorno i totali, sul vecchio spesso non vengono aggiornati. Un giorno sarà possibile rimuover questa riga.
		PakiTestaTotali totali = daoRighe.calcolaTotaliPerCarico(carico.getIdTestaPaki());
		carico.setQtaTotAre(totali.getTotaleRiscontrato());
		carico.setQtaTotAto(totali.getTotaleDichiarato());
		carico.setDataArrivo(new Timestamp(new Date().getTime()));
		carico.setLetto("SI");
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(carico);
			t.commit();
		} catch (Exception e) {
			carico = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return carico;
	}
	
	protected PakiTesta modificaStatoInInLavorazione(PakiTesta carico) {
		carico.setDataOraInizio(new Timestamp(new Date().getTime()));
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(carico);
			t.commit();
		} catch (Exception e) {
			carico = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return carico;
	}
	
	protected PakiTesta modificaStatoInLavorato(PakiTesta carico) {
		//Aggiorno i totali, sul vecchio spesso non vengono aggiornati. Un giorno sarà possibile rimuover questa riga.
		PakiTestaTotali totali = daoRighe.calcolaTotaliPerCarico(carico.getIdTestaPaki());
		carico.setQtaTotAre(totali.getTotaleRiscontrato());
		carico.setQtaTotAto(totali.getTotaleDichiarato());
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(carico);
			t.commit();
		} catch (Exception e) {
			carico = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return carico;
	}
	
	/**
	 * Annulla la chiusura del carico eliminando i movimenti e correggendo i saldi.
	 */
	protected PakiTesta modificaStatoAnnullaChiuso(PakiTesta carico) {
		List<ColliPack> prodottiDisponibili = new LinkedList<>();
		List<MagaSd> saldiDaAggiornare = new LinkedList<>();
		List<MagaMov> movimentiDaEliminare = new LinkedList<>();
		//Modifico il carico
		carico.setGeneratoMov("NO");
		carico.setGeneratoFile("NO");
		HashMap<String, Integer> mappaProdotti = new HashMap<>();
		//Trovo i colli nel carico e li flaggo come non disponibili
		List<ColliCarico> colliDisponibili = daoColli.trovaColliNelCarico(carico.getIdTestaPaki());
		for (ColliCarico collo : colliDisponibili) {
			collo.setFlagtc("0");
		}
		//Trovo i prodotti nel carico da rendere non disponibili
		List<ColliPack> prodottiCarico = daoProdotti.trovaProdottiNelCarico(carico.getIdTestaPaki());
		for (ColliPack prodotto : prodottiCarico) {
			//Controllo che non sia stato impegnato da qualche lista, se si gli do picche.
			if (prodotto.getFlagimp().equals("S") || prodotto.getQtaimpegnata() > 0)
				throw new CustomException("Impossibile annullare la generazione del carico.\r\nIl prodotto " + prodotto.getCodiceArticolo() + " nel collo " + prodotto.getNrIdColloPk() +  " è già stato impegnato.");
			//Altrimenti vado ad impostare la generazione a 0 e lo metto nella mappa.
			prodotto.setFlagtc(0);
			prodottiDisponibili.add(prodotto);
			String key = prodotto.getCodiceArticolo() + SPLIT_CHAR + prodotto.getMagazzino();
			int disponibile = mappaProdotti.containsKey(key) ? mappaProdotti.get(key) : 0;
			disponibile += prodotto.getQta();
			mappaProdotti.put(key, disponibile);
		}
		//Aggiorno i saldi e annullo i movimenti
		for (String key : mappaProdotti.keySet()) {
			int quantità = mappaProdotti.get(key);
			String[] dati = key.split(SPLIT_CHAR);
			if (dati.length != 2)
				throw new CustomException("Impossibile dividere codice univoco articolo e magazzino con il simbolo " + SPLIT_CHAR);
			MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(dati[0], dati[1]);
			if (saldo != null) {
				saldo.setDisponibile(saldo.getDisponibile() - quantità);
				saldo.setEsistenza(saldo.getEsistenza() - quantità);
				saldiDaAggiornare.add(saldo);
			}
		}
		movimentiDaEliminare = daoMovimenti.trovaMovimentiCarico(carico);
		//Vado in scrittura
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			//Aggiorno lo stato del carico
			em.merge(carico);
			//Aggiorno lo stato dei colli, se necessario
			for (ColliCarico collo : colliDisponibili) {
				em.merge(collo);
			}
			//Aggiorno lo stato dei prodotti, se necessario
			for (ColliPack prodotto : prodottiDisponibili) {
				em.merge(prodotto);
			}
			//Aggiusto i saldi e i movimenti
			for (MagaSd saldo : saldiDaAggiornare) {
				em.merge(saldo);
			}
			for (MagaMov movimento : movimentiDaEliminare) {
				em.remove(em.contains(movimento) ? movimento : em.find(MagaMov.class, movimento.getIdMagaMov()));
			}
			t.commit();
		} catch (Exception e) {
			carico = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return carico;
	}
	
	protected PakiTesta modificaStatoInChiuso(PakiTesta entity) {
		//Considerazioni ulteriori sui movimenti
		List<MagaSd> saldiDaAggiornare = new LinkedList<>();
		List<MagaSd> saldiDaInserire = new LinkedList<>();
		List<MagaMov> movimentiDaInserire = new LinkedList<>();
		List<ColliPack> prodottiDisponibili = new LinkedList<>();
		List<ColliCarico> colliDisponibili = new LinkedList<>();
		//Modifico i dati sul carico
		entity.setDataOraGenerazione(new Timestamp(new Date().getTime()));
		entity.setGeneratoMov("SI");
		entity.setGeneratoFile("SI");
		entity.setFlagTra("F");
		HashMap<String, Integer> mappaProdotti = new HashMap<>();
		//Trovo i colli nel carico e li flaggo come disponibili
		colliDisponibili = daoColli.trovaColliNelCarico(entity.getIdTestaPaki());
		for (ColliCarico collo : colliDisponibili) {
			collo.setFlagtc("1");
		}
		//Trovo i prodotti nel carico
		List<ColliPack> prodottiCarico = daoProdotti.trovaProdottiNelCarico(entity.getIdTestaPaki());
		//Faccio una verifica su quanto è presente come riscontro nella packing list e quanto effettivamente riscontrato.
		checkPakiArticoloVsColliPackPerGenerazioneCarico(entity, prodottiCarico);
		//Li rendo disponibili
		for (ColliPack prodotto : prodottiCarico) {
			prodotto.setFlagtc(1);
			prodottiDisponibili.add(prodotto);
			String key = prodotto.getCodiceArticolo() + SPLIT_CHAR + prodotto.getMagazzino();
			int disponibile = mappaProdotti.containsKey(key) ? mappaProdotti.get(key) : 0;
			disponibile += prodotto.getQta();
			mappaProdotti.put(key, disponibile);
		}
		//Aggiorno i saldi e inserisco i movimenti
		for (String key : mappaProdotti.keySet()) {
			int quantità = mappaProdotti.get(key);
			String[] dati = key.split(SPLIT_CHAR);
			if (dati.length != 2)
				throw new CustomException("Impossibile dividere codice univoco articolo e magazzino con il simbolo " + SPLIT_CHAR);
			MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(dati[0], dati[1]);
			if (saldo == null) {
				saldo = new MagaSd();
				saldo.setIdUniArticolo(dati[0]);
				saldo.setCodMaga(dati[1]);
				saldo.setDisponibile(quantità);
				saldo.setEsistenza(quantità);
				saldo.setImpegnato(0);
				saldo.setTotIn(quantità);
				saldo.setTotOut(0);
				saldiDaInserire.add(saldo);
			} else {
				saldo.setDisponibile(saldo.getDisponibile() + quantità);
				saldo.setEsistenza(saldo.getEsistenza() + quantità);
				saldo.setTotIn(saldo.getTotIn() + quantità);
				saldiDaAggiornare.add(saldo);
			}
			MagaMov movimento = daoMovimenti.getNuovoMovimento(CausaliMovimento.CPK, entity.getNrPaki(), entity.getIdTestaPaki(), entity.getDataPaki(), saldo, dati[0], dati[1], quantità);
			movimentiDaInserire.add(movimento);
		}
		//Vado in scrittura
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			//Aggiorno lo stato del carico
			em.merge(entity);
			//Aggiorno lo stato dei colli, se necessario
			for (ColliCarico collo : colliDisponibili) {
				em.merge(collo);
			}
			//Aggiorno lo stato dei prodotti, se necessario
			for (ColliPack prodotto : prodottiDisponibili) {
				em.merge(prodotto);
			}
			//Aggiusto i saldi
			for (MagaSd saldo : saldiDaInserire) {
				em.persist(saldo);
			}
			for (MagaSd saldo : saldiDaAggiornare) {
				em.merge(saldo);
			}
			//Aggiusto i movimenti
			for (MagaMov movimento : movimentiDaInserire) {
				em.persist(movimento);
			}
			t.commit();
		} catch (Exception e) {
			entity = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return entity;
	}
	
	@Override
	public CaricoTestata modificaStato(CaricoTestata json) {
		PakiTesta carico = trovaDaID(json.getId());
		if (carico == null) throw new CustomException("L'ID indicato per il carico non esiste. (" + json.getId() + ")");
		String statoPrecedente = carico.getStato();
		String statoNuovo = json.getStato();
		if (statoNuovo == null || statoNuovo.isEmpty()) throw new CustomException("Bisogna indicare un nuovo stato per il carico.");
		//Vado avanti solo se lo stato cambia.
		if (!statoPrecedente.equals(statoNuovo)) {
			//Controllo speciale, se ero nello stato "CHIUSO" e passo a qualsiasi altro devo annullare i movimenti
			if (statoPrecedente.equals("CHIUSO")) {
				modificaStatoAnnullaChiuso(carico);
			}
			//Passo alla parte specifica
			carico.setStato(statoNuovo);
			switch (statoNuovo) {
				case "INSERITO" : carico = modificaStatoInInserito(carico); break;
				case "ARRIVATO" : carico = modificaStatoInArrivato(carico); break;
				case "IN_LAVORAZIONE" : carico = modificaStatoInInLavorazione(carico); break;
				case "LAVORATO" : carico = modificaStatoInLavorato(carico); break;
				case "CHIUSO" : carico = modificaStatoInChiuso(carico); break;
				case "ANNULLATO" : carico = modificaStatoInAnnullato(carico); break;
			}
		}
		return serializza(carico);
	}

//	@Override
//	public CaricoTestata modificaStato(CaricoTestata json) {
//		PakiTesta entity = trovaDaID(json.getId());
//		if (entity != null) {
//			String statoPrecedente = entity.getStato();
//			String statoNuovo = json.getStato();
//			Timestamp stamp = new Timestamp(new Date().getTime());
//			entity.setStato(statoNuovo);
//			//Considerazioni ulteriori sui movimenti
//			List<MagaSd> saldiDaAggiornare = new LinkedList<>();
//			List<MagaSd> saldiDaInserire = new LinkedList<>();
//			List<MagaMov> movimentiDaInserire = new LinkedList<>();
//			List<MagaMov> movimentiDaEliminare = new LinkedList<>();
//			List<ColliPack> prodottiDisponibili = new LinkedList<>();
//			List<ColliCarico> colliDisponibili = new LinkedList<>();
//			if (statoNuovo.equals("ARRIVATO") && !statoPrecedente.equals("ARRIVATO")) {
//				entity.setDataArrivo(stamp);
//				entity.setLetto("SI");
//			} else if (statoNuovo.equals("IN_LAVORAZIONE") && !statoPrecedente.equals("IN_LAVORAZIONE")) {
//				entity.setDataOraInizio(stamp);
//			} else if (statoNuovo.equals("CHIUSO") && !statoPrecedente.equals("CHIUSO")) {
//				entity.setDataOraGenerazione(stamp);
//				entity.setGeneratoMov("SI");
//				entity.setGeneratoFile("SI");
//				entity.setFlagTra("F");
//				HashMap<String, Integer> mappaProdotti = new HashMap<>();
//				//Trovo i colli nel carico e li flaggo come disponibili
//				colliDisponibili = daoColli.trovaColliNelCarico(entity.getIdTestaPaki());
//				for (ColliCarico collo : colliDisponibili) {
//					collo.setFlagtc("1");
//				}
//				//Trovo i prodotti nel carico
//				List<ColliPack> prodottiCarico = daoProdotti.trovaProdottiNelCarico(entity.getIdTestaPaki());
//				//Faccio una verifica su quanto è presente come riscontro nella packing list e quanto effettivamente riscontrato.
//				checkPakiArticoloVsColliPackPerGenerazioneCarico(entity, prodottiCarico);
//				//Li rendo disponibili
//				for (ColliPack prodotto : prodottiCarico) {
//					prodotto.setFlagtc(1);
//					prodottiDisponibili.add(prodotto);
//					String key = prodotto.getCodiceArticolo() + SPLIT_CHAR + prodotto.getMagazzino();
//					int disponibile = mappaProdotti.containsKey(key) ? mappaProdotti.get(key) : 0;
//					disponibile += prodotto.getQta();
//					mappaProdotti.put(key, disponibile);
//				}
//				//Aggiorno i saldi e inserisco i movimenti
//				for (String key : mappaProdotti.keySet()) {
//					int quantità = mappaProdotti.get(key);
//					String[] dati = key.split(SPLIT_CHAR);
//					if (dati.length != 2)
//						throw new CustomException("Impossibile dividere codice univoco articolo e magazzino con il simbolo " + SPLIT_CHAR);
//					MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(dati[0], dati[1]);
//					if (saldo == null) {
//						saldo = new MagaSd();
//						saldo.setIdUniArticolo(dati[0]);
//						saldo.setCodMaga(dati[1]);
//						saldo.setDisponibile(quantità);
//						saldo.setEsistenza(quantità);
//						saldo.setImpegnato(0);
//						saldo.setTotIn(quantità);
//						saldo.setTotOut(0);
//						saldiDaInserire.add(saldo);
//					} else {
//						saldo.setDisponibile(saldo.getDisponibile() + quantità);
//						saldo.setEsistenza(saldo.getEsistenza() + quantità);
//						saldo.setTotIn(saldo.getTotIn() + quantità);
//						saldiDaAggiornare.add(saldo);
//					}
//					MagaMov movimento = daoMovimenti.getNuovoMovimento(CausaliMovimento.CPK, entity.getNrPaki(), entity.getIdTestaPaki(), entity.getDataPaki(), saldo, dati[0], dati[1], quantità);
//					movimentiDaInserire.add(movimento);
//				}				
//			} else if (!statoNuovo.equals("CHIUSO") && statoPrecedente.equals("CHIUSO")) {
//				entity.setGeneratoMov("NO");
//				entity.setGeneratoFile("NO");
//				HashMap<String, Integer> mappaProdotti = new HashMap<>();
//				//Trovo i colli nel carico e li flaggo come non disponibili
//				colliDisponibili = daoColli.trovaColliNelCarico(entity.getIdTestaPaki());
//				for (ColliCarico collo : colliDisponibili) {
//					collo.setFlagtc("0");
//				}
//				//Trovo i prodotti nel carico da rendere non disponibili
//				List<ColliPack> prodottiCarico = daoProdotti.trovaProdottiNelCarico(entity.getIdTestaPaki());
//				for (ColliPack prodotto : prodottiCarico) {
//					//Controllo che non sia stato impegnato da qualche lista, se si gli do picche.
//					if (prodotto.getFlagimp().equals("S") || prodotto.getQtaimpegnata() > 0)
//						throw new CustomException("Impossibile annullare la generazione del carico.\r\nIl prodotto " + prodotto.getCodiceArticolo() + " nel collo " + prodotto.getNrIdColloPk() +  " è già stato impegnato.");
//					//Altrimenti vado ad impostare la generazione a 0 e lo metto nella mappa.
//					prodotto.setFlagtc(0);
//					prodottiDisponibili.add(prodotto);
//					String key = prodotto.getCodiceArticolo() + SPLIT_CHAR + prodotto.getMagazzino();
//					int disponibile = mappaProdotti.containsKey(key) ? mappaProdotti.get(key) : 0;
//					disponibile += prodotto.getQta();
//					mappaProdotti.put(key, disponibile);
//				}
//				//Aggiorno i saldi e annullo i movimenti
//				for (String key : mappaProdotti.keySet()) {
//					int quantità = mappaProdotti.get(key);
//					String[] dati = key.split(SPLIT_CHAR);
//					if (dati.length != 2)
//						throw new CustomException("Impossibile dividere codice univoco articolo e magazzino con il simbolo " + SPLIT_CHAR);
//					MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(dati[0], dati[1]);
//					if (saldo != null) {
//						saldo.setDisponibile(saldo.getDisponibile() - quantità);
//						saldo.setEsistenza(saldo.getEsistenza() - quantità);
//						saldiDaAggiornare.add(saldo);
//					}
//				}
//				movimentiDaEliminare = daoMovimenti.trovaMovimentiCarico(entity);
//			}
//			//Vado in scrittura
//			EntityManager em = getManager();
//			EntityTransaction t = em.getTransaction();
//			try {
//				t.begin();
//				//Aggiorno lo stato del carico
//				em.merge(entity);
//				//Aggiorno lo stato dei colli, se necessario
//				for (ColliCarico collo : colliDisponibili) {
//					em.merge(collo);
//				}
//				//Aggiorno lo stato dei prodotti, se necessario
//				for (ColliPack prodotto : prodottiDisponibili) {
//					em.merge(prodotto);
//				}
//				//Aggiusto i saldi
//				for (MagaSd saldo : saldiDaInserire) {
//					em.persist(saldo);
//				}
//				for (MagaSd saldo : saldiDaAggiornare) {
//					em.merge(saldo);
//				}
//				//Aggiusto i movimenti
//				for (MagaMov movimento : movimentiDaInserire) {
//					em.persist(movimento);
//				}
//				for (MagaMov movimento : movimentiDaEliminare) {
//					em.remove(em.contains(movimento) ? movimento : em.find(MagaMov.class, movimento.getIdMagaMov()));
//				}
//				t.commit();
//			} catch (Exception e) {
//				entity = null;
//				logger.error(e);
//				if (t != null && t.isActive())
//					t.rollback();
//			} finally {
//				em.close();
//			}
//		}
//		return serializza(entity);
//	}

}
