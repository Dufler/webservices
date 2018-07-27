package it.ltc.services.clienti.data.ordine;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import it.ltc.database.dao.Dao;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.ColliImballoDao;
import it.ltc.database.dao.legacy.ImballoDao;
import it.ltc.database.dao.legacy.MagaSdDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.dao.legacy.RighiOrdineDao;
import it.ltc.database.dao.legacy.TestaCorrDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.dao.legacy.TestataOrdiniTipoDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.ColliImballo;
import it.ltc.database.model.legacy.ColliPreleva;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.Imballi;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.MittentiOrdine;
import it.ltc.database.model.legacy.RigaCorr;
import it.ltc.database.model.legacy.RighiImballoLight;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.TestaCorr;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.TestataOrdiniTipo;
import it.ltc.database.model.legacy.model.StatoOrdine;
import it.ltc.model.shared.json.cliente.ContrassegnoJSON;
import it.ltc.model.shared.json.cliente.DocumentoJSON;
import it.ltc.model.shared.json.cliente.ImballoJSON;
import it.ltc.model.shared.json.cliente.IndirizzoJSON;
import it.ltc.model.shared.json.cliente.OrdineImballatoJSON;
import it.ltc.model.shared.json.cliente.OrdineJSON;
import it.ltc.model.shared.json.cliente.ProdottoImballatoJSON;
import it.ltc.model.shared.json.cliente.SpedizioneJSON;
import it.ltc.model.shared.json.cliente.UscitaDettaglioJSON;
import it.ltc.model.shared.json.cliente.UscitaJSON;
import it.ltc.services.clienti.data.indirizzo.IndirizziLegacyDAOImpl;
import it.ltc.services.custom.exception.CustomErrorCause;
import it.ltc.services.custom.exception.CustomException;

public class OrdineLegacyDAOImpl extends Dao implements OrdineDAO<TestataOrdini, RighiOrdine> {

	private static final Logger logger = Logger.getLogger("OrdineLegacyDAOImpl");

	protected final IndirizziLegacyDAOImpl daoIndirizzi;
	protected final TestaCorrDao daoTestaCorr;
	protected final ArticoliDao daoProdotti;
	protected final MagazzinoDao daoMagazzini;
	protected final ImballoDao daoImballi;
	protected final TestataOrdiniDao daoTestataOrdine;
	protected final RighiOrdineDao daoRigheOrdine;
	protected final TestataOrdiniTipoDao daoTipiOrdine;
	protected final MagaSdDao daoSaldi;
	protected final ColliImballoDao daoColliImballati;
	
	private final Set<String> tipiRiga;
	private final HashMap<String, String> magazzini;
	private final HashMap<String, Imballi> imballi;
	
	protected final DecimalFormat df;
	protected final SimpleDateFormat meseGiorno;

	public OrdineLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoIndirizzi = new IndirizziLegacyDAOImpl(persistenceUnit);
		daoTestaCorr = new TestaCorrDao(persistenceUnit);
		daoProdotti = new ArticoliDao(persistenceUnit);
		daoMagazzini = new MagazzinoDao(persistenceUnit);
		daoImballi = new ImballoDao(persistenceUnit);
		daoTestataOrdine = new TestataOrdiniDao(persistenceUnit);
		daoRigheOrdine = new RighiOrdineDao(persistenceUnit);
		daoTipiOrdine = new TestataOrdiniTipoDao(persistenceUnit);
		daoSaldi = new MagaSdDao(persistenceUnit);
		daoColliImballati = new ColliImballoDao(persistenceUnit);
		tipiRiga = new HashSet<>();
		magazzini = new HashMap<>();
		imballi = new HashMap<>();
		df = new DecimalFormat("0000000");
		meseGiorno = new SimpleDateFormat("MMdd");
	}
	
	@Override
	public OrdineJSON trovaDaID(int idOrdine) {
		OrdineJSON json;
		TestataOrdini ordine = daoTestataOrdine.trovaDaID(idOrdine);
		if (ordine != null) {
			List<RighiOrdine> dettagli = daoRigheOrdine.trovaRigheDaIDOrdine(idOrdine);
			json = serializzaOrdine(ordine, dettagli);
		} else {
			json = null;
		}		
		return json;
	}

	@Override
	public OrdineJSON trovaDaRiferimento(String riferimento, boolean dettagliato) {
		OrdineJSON json;
		TestataOrdini ordine = daoTestataOrdine.trovaDaRiferimento(riferimento);
		if (ordine != null) {
			List<RighiOrdine> dettagli;
			// Se hanno richiesto nel dettaglio e l'ordine è in uno stato congruo allora aggiungo info.
			StatoOrdine stato = getStatoOrdine(ordine);
			boolean statoOk = (stato == StatoOrdine.DIIB || stato == StatoOrdine.COIB || stato == StatoOrdine.ELAB || stato == StatoOrdine.INSP || stato == StatoOrdine.SPED);
			if (dettagliato && statoOk) {
				dettagli = daoRigheOrdine.trovaRigheDaIDOrdine(ordine.getIdTestaSped());
				// Recupero i seriali e ne faccio una mappa
				List<RighiImballoLight> seriali = trovaSeriali(ordine.getNrLista());
				HashMap<Integer, List<String>> mappaSerialiPerRiga = new HashMap<>();
				for (RighiImballoLight seriale : seriali) {
					// Controllo che il seriale sia stato effettivamente
					// inserito a sistema, se non c'è lo salto.
					String rfid = seriale.getSeriale();
					if (rfid == null || rfid.isEmpty())
						continue;
					int idRiga = seriale.getNrRigoOrdine();
					// Controllo se esiste già una lista di seriali associati al
					// pakiarticolo
					if (!mappaSerialiPerRiga.containsKey(idRiga)) {
						List<String> serialiAssociati = new LinkedList<>();
						mappaSerialiPerRiga.put(idRiga, serialiAssociati);
					}
					mappaSerialiPerRiga.get(idRiga).add(rfid);
				}
				// Per ogni riga gli associo una lista nella mappa, se presente.
				for (RighiOrdine dettaglio : dettagli) {
					dettaglio.setSeriali(mappaSerialiPerRiga.get(dettaglio.getNrRigo()));
				}
			} else {
				dettagli = null;
			}
			json = serializzaOrdine(ordine, dettagli);
		} else {
			json = null;
		}
		return json;
	}

	public List<RighiImballoLight> trovaSeriali(String nrLista) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RighiImballoLight> criteria = cb.createQuery(RighiImballoLight.class);
		Root<RighiImballoLight> member = criteria.from(RighiImballoLight.class);
		criteria.select(member).where(cb.equal(member.get("nrLista"), nrLista));
		List<RighiImballoLight> dettagli = em.createQuery(criteria).getResultList();
		em.close();
		return dettagli;
	}

	@Override
	public List<UscitaJSON> trovaTutti() {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestataOrdini> criteria = cb.createQuery(TestataOrdini.class);
		Root<TestataOrdini> member = criteria.from(TestataOrdini.class);
		criteria.select(member);
		List<TestataOrdini> list = em.createQuery(criteria).getResultList();
		em.close();
		List<UscitaJSON> ordini = new LinkedList<>();
		for (TestataOrdini entity : list) {
			UscitaJSON json = serializzaUscita(entity, false);
			ordini.add(json);
		}
		return ordini;
	}
	
	private void checkTipi(OrdineJSON json) {
		//Controllo il tipo dell'ordine
		checkTipoOrdine(json.getOrdine());
		//Controllo i tipi di riga, se presenti.
		for (UscitaDettaglioJSON dettaglio : json.getDettagli()) {
			checkTipoRiga(dettaglio);			
		}
	}
	
	private void checkTipoOrdine(UscitaJSON json) {
		String tipoOrdine = json.getTipo();
		TestataOrdiniTipo tipo = daoTipiOrdine.trovaTipoDaCodice(tipoOrdine);
		if (tipo == null || !tipo.getTipo().equals("TESTATA")) {
			throw new CustomException("Il tipo indicato per l'ordine non esiste. (" + tipoOrdine + ")");
		}
	}
	
	private void checkTipoRiga(UscitaDettaglioJSON dettaglio) {
		//Controllo se è stata inserita una tipologia.
		String tipoRiga = dettaglio.getTipo();
		if (tipoRiga != null && !tipoRiga.isEmpty()) {
			//Se è presente vado a verificare che esiste e sia valida.
			if (!tipiRiga.contains(tipoRiga)) {
				TestataOrdiniTipo tipoRigaOrdine = daoTipiOrdine.trovaTipoDaCodice(tipoRiga);
				if (tipoRigaOrdine == null || !tipoRigaOrdine.getTipo().equals("RIGA")) {
					throw new CustomException("Il tipo indicato per la riga non esiste. (" + tipoRiga + ")");
				} else {
					tipiRiga.add(tipoRiga);
				}
			}
		}
	}

	@Override
	public OrdineJSON inserisci(OrdineJSON json) {
		//Controllo il tipo d'ordine specificato
		checkTipi(json);
		// Deserializzazione testata
		TestataOrdini ordine = deserializzaUscita(json);
		// Controllo sull'unicità del riferimento al carico
		TestataOrdini esistente = daoTestataOrdine.trovaDaRiferimento(ordine.getRifOrdineCli());
		if (esistente != null)
			throw new CustomException("Esiste gia' un ordine con lo stesso riferimento. (" + ordine.getRifOrdineCli() + ")");
		// Deserializza destinatario e inserisci le info nell'ordine
		Destinatari destinatario = daoIndirizzi.ottieniDestinatario(json.getOrdine().getDestinatario());
		ordine.setCodCliente(destinatario.getCodDestina());
		ordine.setIdDestina(destinatario.getIdDestina());
		logger.info("Ottenuto e impostato il destinatario, ID: '" + destinatario.getIdDestina() + "'");
		// Deserializza mittente e inserisci le info nell'ordine
		MittentiOrdine mittente = daoIndirizzi.ottieniMittente(json.getOrdine().getMittente());
		ordine.setIdMittente(mittente.getIdMittente());
		logger.info("Ottenuto e impostato il mittente, ID: '" + mittente.getIdMittente() + "'");
		// Deserializza e effettua controlli sull'esistenza di magazzini e
		// prodotti
		List<RighiOrdine> dettagli = new LinkedList<>();
		for (UscitaDettaglioJSON dettaglioJson : json.getDettagli()) {
			RighiOrdine dettaglio = deserializzaDettaglio(dettaglioJson);
			dettagli.add(dettaglio);
		}
		logger.info("Deserializzati e pronti per l'inserimento " + dettagli.size() + " righe.");
		// Lo stato dell'ordine sarà 'INSE' all'inizio.
		ordine.setStato("INSE");
		// Vado in scrittura
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			logger.info("Inizio della transazione - inserimento ordine");
			t.begin();
			em.persist(ordine);
			for (RighiOrdine dettaglio : dettagli) {
				dettaglio.setIdTestataOrdine(ordine.getIdTestaSped());
				dettaglio.setNrLista(ordine.getNrLista());
				dettaglio.setRagstampe1(ordine.getNrLista());
				dettaglio.setNrOrdine(ordine.getNrLista());
				dettaglio.setDataOrdine(ordine.getDataOrdine());
				dettaglio.setIdDestina(ordine.getIdDestina());
				em.persist(dettaglio);
			}
			t.commit();
			json = serializzaOrdine(ordine, dettagli);
			logger.info("Ordine inserito!");
		} catch (Exception e) {
			logger.error(e);
			json = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return json;
	}

	@Override
	public UscitaDettaglioJSON inserisciDettaglio(UscitaDettaglioJSON json) {
		//Controllo il tipo, se presente
		checkTipoRiga(json);
		// Recupero le info sull'ordine associato, se non lo trovo lancio l'eccezione e mi fermo qui.
		TestataOrdini ordine = daoTestataOrdine.trovaDaRiferimento(json.getRiferimento());
		if (ordine == null)
			throw new CustomException("Il riferimento indicato per l'ordine non e' valido. (" + json.getRiferimento() + ")");
		// Controllo lo stato
		checkOrdineModificabileOAssegnabile(ordine.getStato());
		// Recupero le info da aggiungere
		RighiOrdine nuoveInfo = deserializzaDettaglio(json);
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RighiOrdine> criteria = cb.createQuery(RighiOrdine.class);
		Root<RighiOrdine> member = criteria.from(RighiOrdine.class);
		Predicate condizioneRiferimento = cb.equal(member.get("idTestataOrdine"), ordine.getIdTestaSped());
		Predicate condizioneRiga = cb.equal(member.get("nrRigo"), json.getRiga());
		criteria.select(member).where(cb.and(condizioneRiferimento, condizioneRiga));
		List<RighiOrdine> list = em.createQuery(criteria).getResultList();
		RighiOrdine dettaglio = list.size() == 1 ? list.get(0) : null;
		if (dettaglio == null) {
			ordine = em.find(TestataOrdini.class, ordine.getIdTestaSped());
			// In teoria non dovrebbe mai succedere questo!
			if (ordine == null)
				throw new CustomException("Il riferimento indicato per l'ordine non e' valido. (" + json.getRiferimento() + ")");
			int totale = ordine.getQtaTotaleSpedire() + nuoveInfo.getQtaSpedizione();
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				// Aggiorno il totale del carico
				ordine.setQtaTotaleSpedire(totale);
				em.merge(ordine);
				// Inserisco la nuova riga
				nuoveInfo.setIdTestataOrdine(ordine.getIdTestaSped());
				nuoveInfo.setNrLista(ordine.getNrLista());
				nuoveInfo.setRagstampe1(ordine.getNrLista());
				em.persist(nuoveInfo);
				t.commit();
				json = serializzaDettaglio(nuoveInfo);
			} catch (Exception e) {
				logger.error(e);
				json = null;
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		} else {
			em.close();
			throw new CustomException("La riga d'ordine indicata è già presente. (" + json.getRiga() + ")");
		}
		return json;
	}

	@Override
	public UscitaJSON aggiorna(UscitaJSON json) {
		//Controllo il tipo d'ordine specificato
		checkTipoOrdine(json);
		// Controllo l'esistenza del riferimento al carico
		TestataOrdini esistente = daoTestataOrdine.trovaDaRiferimento(json.getRiferimentoOrdine()); //trovaDaRiferimento(json.getRiferimentoOrdine());
		if (esistente == null)
			throw new CustomException("Non esiste nessun ordine con questo riferimento. (" + json.getRiferimentoOrdine() + ")");
		// Controllo lo stato
		checkOrdineModificabileOAssegnabile(esistente.getStato());
		// Controllo destinatario e mittente, forse sono cambiati
		Destinatari destinatario = daoIndirizzi.ottieniDestinatario(json.getDestinatario());
		MittentiOrdine mittente = daoIndirizzi.ottieniMittente(json.getMittente());
		// Update
		EntityManager em = getManager();
		esistente = em.find(TestataOrdini.class, esistente.getIdTestaSped());
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			// Aggiorno le informazioni di testata, non vado a toccare la quantita' pero'!
			if (json.getDataConsegna() != null)
				esistente.setDataConsegna(new Timestamp(json.getDataConsegna().getTime()));
			esistente.setDataOrdine(new Timestamp(json.getDataOrdine().getTime()));
			esistente.setNote(json.getNote());
			esistente.setPriorita(json.getPriorita());
			// Aggiorno il destinatario
			esistente.setCodCliente(destinatario.getCodDestina());
			esistente.setIdDestina(destinatario.getIdDestina());
			// Aggiorno il mittente
			esistente.setIdMittente(mittente.getIdMittente());
			em.merge(esistente);
			t.commit();
			json = serializzaUscita(esistente, false);
			logger.info("Ordine modificato!");
		} catch (Exception e) {
			logger.error(e);
			json = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return json;
	}

	@Override
	public UscitaDettaglioJSON aggiornaDettaglio(UscitaDettaglioJSON json) {
		//Controllo il tipo, se presente
		checkTipoRiga(json);
		// Recupero le info sul carico associato, se non lo trovo lancio l'eccezione e mi fermo qui.
		TestataOrdini ordine = daoTestataOrdine.trovaDaRiferimento(json.getRiferimento()); //trovaDaRiferimento(json.getRiferimento());
		if (ordine == null)
			throw new CustomException("Il riferimento indicato per l'ordine non è valido. (" + json.getRiferimento() + ")");
		// Controllo lo stato
		checkOrdineModificabileOAssegnabile(ordine.getStato());
		// Recupero la riga del dettaglio
		RighiOrdine nuoveInfo = deserializzaDettaglio(json);
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RighiOrdine> criteria = cb.createQuery(RighiOrdine.class);
		Root<RighiOrdine> member = criteria.from(RighiOrdine.class);
		Predicate condizioneRiferimento = cb.equal(member.get("idTestataOrdine"), ordine.getIdTestaSped());
		Predicate condizioneRiga = cb.equal(member.get("nrRigo"), json.getRiga());
		criteria.select(member).where(cb.and(condizioneRiferimento, condizioneRiga));
		List<RighiOrdine> list = em.createQuery(criteria).getResultList();
		RighiOrdine dettaglio = list.size() == 1 ? list.get(0) : null;
		if (dettaglio != null) {
			ordine = em.find(TestataOrdini.class, ordine.getIdTestaSped());
			// In teoria non dovrebbe mai succedere questo!
			if (ordine == null)
				throw new CustomException("Il riferimento indicato per l'ordine non è valido. (" + json.getRiferimento() + ")");
			int differenza = nuoveInfo.getQtaSpedizione() - dettaglio.getQtaSpedizione();
			int totale = ordine.getQtaTotaleSpedire() + differenza;
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				// Aggiorno il totale del carico
				ordine.setQtaTotaleSpedire(totale);
				em.merge(ordine);
				// Aggiorno la riga
				dettaglio.setBarraEAN(nuoveInfo.getBarraEAN());
				dettaglio.setBarraUPC(nuoveInfo.getBarraUPC());
				dettaglio.setCodiceArticolo(nuoveInfo.getCodiceArticolo());
				dettaglio.setColore(nuoveInfo.getColore());
				dettaglio.setComposizione(nuoveInfo.getComposizione());
				dettaglio.setDescrizione(nuoveInfo.getDescrizione());
				dettaglio.setIdUnicoArt(nuoveInfo.getIdUnicoArt());
				dettaglio.setMagazzino(nuoveInfo.getMagazzino());
				dettaglio.setQtaSpedizione(nuoveInfo.getQtaSpedizione());
				dettaglio.setTaglia(nuoveInfo.getTaglia());
				em.merge(dettaglio);
				t.commit();
				json = serializzaDettaglio(dettaglio);
			} catch (Exception e) {
				logger.error(e);
				json = null;
				if (t != null && t.isActive())
					t.rollback();
			}
		} else {
			em.close();
			throw new CustomException("La riga d'ordine indicata non esiste oppure non è univoca.");
		}
		return json;
	}

	@Override
	public UscitaJSON elimina(UscitaJSON json) {
		// Controllo l'esistenza del riferimento al carico
		TestataOrdini esistente = daoTestataOrdine.trovaDaRiferimento(json.getRiferimentoOrdine()); //trovaDaRiferimento(json.getRiferimentoOrdine());
		if (esistente == null)
			throw new CustomException("Non esiste nessun ordine con questo riferimento. (" + json.getRiferimentoOrdine() + ")");
		// Controllo lo stato, deve essere 'INSERITO'
		checkOrdineModificabileOAssegnabile(esistente.getStato());
		// Delete
		EntityManager em = getManager();
		esistente = em.find(TestataOrdini.class, esistente.getIdTestaSped());
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(esistente);
			t.commit();
			json = serializzaUscita(esistente, false);
			logger.info("Ordine eliminato!");
		} catch (Exception e) {
			logger.error(e);
			json = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return json;
	}

	@Override
	public UscitaDettaglioJSON eliminaDettaglio(UscitaDettaglioJSON json) {
		// Recupero le info sul carico associato, se non lo trovo lancio l'eccezione e mi fermo qui.
		TestataOrdini ordine = daoTestataOrdine.trovaDaRiferimento(json.getRiferimento()); //trovaDaRiferimento(json.getRiferimento());
		if (ordine == null)
			throw new CustomException("Il riferimento indicato per l'ordine non è valido. (" + json.getRiferimento() + ")");
		// Controllo lo stato
		checkOrdineModificabileOAssegnabile(ordine.getStato());
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RighiOrdine> criteria = cb.createQuery(RighiOrdine.class);
		Root<RighiOrdine> member = criteria.from(RighiOrdine.class);
		Predicate condizioneRiferimento = cb.equal(member.get("idTestataOrdine"), ordine.getIdTestaSped());
		Predicate condizioneRiga = cb.equal(member.get("nrRigo"), json.getRiga());
		criteria.select(member).where(cb.and(condizioneRiferimento, condizioneRiga));
		List<RighiOrdine> list = em.createQuery(criteria).getResultList();
		RighiOrdine dettaglio = list.size() == 1 ? list.get(0) : null;
		if (dettaglio != null) {
			ordine = em.find(TestataOrdini.class, ordine.getIdTestaSped());
			// In teoria non dovrebbe mai succedere questo!
			if (ordine == null)
				throw new CustomException("Il riferimento indicato per l'ordine non è valido. (" + json.getRiferimento() + ")");
			int differenza = dettaglio.getQtaSpedizione();
			int totale = ordine.getQtaTotaleSpedire() - differenza;
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				ordine.setQtaTotaleSpedire(totale);
				em.merge(ordine);
				em.remove(dettaglio);
				t.commit();
				json = serializzaDettaglio(dettaglio);
			} catch (Exception e) {
				logger.error(e);
				json = null;
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		} else {
			em.close();
			throw new CustomException("La riga d'ordine indicata non esiste.");
		}
		return json;
	}

	/**
	 * Controlla che lo stato dell'ordine sia congruo per poter essere
	 * assegnato.<br>
	 * I stati validi sono "INSE" e "ERRO"
	 * 
	 * @param stato
	 */
	private void checkOrdineModificabileOAssegnabile(String stato) {
		if (!(StatoOrdine.INSE.name().equals(stato) || StatoOrdine.ERRO.name().equals(stato)))
			throw new CustomException("L'ordine richiesto non è più modificabile.");
	}

	private boolean finalizza(TestataOrdini ordine) {
		// Controllo lo stato dell'ordine
		checkOrdineModificabileOAssegnabile(ordine.getStato());
		// Recupero la lista di prodotti richiesti e controllo la disponibilita'
		List<RighiOrdine> prodotti = daoRigheOrdine.trovaRigheDaIDOrdine(ordine.getIdTestaSped());
		logger.info("Verranno ora verificate " + prodotti.size() + " righe.");
		//EntityManager em = getManager();
		boolean finalizza;
		List<CustomErrorCause> righeNonValide = new LinkedList<>();
		HashMap<String, MagaSd> saldi = new HashMap<>();
		//List<MagaSd> saldi = new LinkedList<>();
		List<MagaMov> movimenti = new LinkedList<>();
		for (RighiOrdine prodotto : prodotti) {
			String keySaldo = prodotto.getIdUnicoArt() + "-" +  prodotto.getMagazzino();
			if (!saldi.containsKey(keySaldo)) {
				MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(prodotto.getIdUnicoArt(), prodotto.getMagazzino());
				saldi.put(keySaldo, saldo);
			}
			MagaSd saldo = saldi.get(keySaldo);
			// Controllo ciò che ho trovato:
			// - se la lista è vuota oppure se non ho disponibilita' aggiungo il numero di riga a quelle non valide e boccio la transazione.
			// - altrimenti aggiorno la disponibilita' e l'impegno e genero il movimento di magazzino.
			if (saldo == null || saldo.getDisponibile() < prodotto.getQtaSpedizione()) {
				String message = "La riga " + prodotto.getNrRigo() + " non e' valida. (SKU: '" + prodotto.getCodiceArticolo() + "')";
				String reason = saldo != null ? "la quantita' disponibile è insufficiente (saldo disponibile: " + saldo.getDisponibile() + ", quantità richiesta: " + prodotto.getQtaSpedizione() + ")" : "Non esiste a magazzino.";
				CustomErrorCause cause = new CustomErrorCause(message, reason);
				righeNonValide.add(cause);
				logger.warn(message + reason);
			} else {
				int disponibile = saldo.getDisponibile() - prodotto.getQtaSpedizione();
				int impegnato = saldo.getImpegnato() + prodotto.getQtaSpedizione();
				saldo.setDisponibile(disponibile);
				saldo.setImpegnato(impegnato);
				//saldi.add(saldo);
				MagaMov movimento = getMovimento(saldo, prodotto);
				movimenti.add(movimento);
			}
		}
		// Se non ho trovato problemi aggiorno tutti i saldi e inserisco i movimenti di magazzino
		// Altrimenti chiudo l'entity manager e restituisco la lista delle righe non valide.
		if (!righeNonValide.isEmpty()) {
			EntityManager em = getManager();
			ordine = em.find(TestataOrdini.class, ordine.getIdTestaSped());
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				ordine.setStato("ERRO");
				em.merge(ordine);
				t.commit();
			} catch (Exception e) {
				logger.error(e);
				t.rollback();
			} finally {
				em.close();
			}
			throw new CustomException("Assegnazione fallita!", 400, righeNonValide);
		} else {
			EntityManager em = getManager();
			ordine = em.find(TestataOrdini.class, ordine.getIdTestaSped());
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				ordine.setStato("IMPO");
				em.merge(ordine);
				for (MagaSd saldo : saldi.values()) {
					em.merge(saldo);
				}
				for (MagaMov movimento : movimenti) {
					em.persist(movimento);
				}
				t.commit();
				finalizza = true;
			} catch (Exception e) {
				finalizza = false;
				righeNonValide = null;
				logger.error(e);
				t.rollback();
			} finally {
				em.close();
			}
		}
		return finalizza;
	}

	private MagaMov getMovimento(MagaSd saldo, RighiOrdine prodotto) {
		MagaMov movimento = new MagaMov();
		movimento.setCausale("IOS");
		movimento.setDocNr(prodotto.getNrLista());
		movimento.setDocData(prodotto.getDataOrdine());
		movimento.setDocCat("O");
		movimento.setDocNote("Impegnato da ordine cliente.");
		movimento.setDocTipo("ORD");
		movimento.setCodMaga(prodotto.getMagazzino());
		movimento.setDisponibilemov(saldo.getDisponibile());
		movimento.setEsistenzamov(saldo.getEsistenza());
		movimento.setImpegnatomov(saldo.getImpegnato());
		movimento.setSegno("+");
		movimento.setSegnoDis("-");
		movimento.setSegnoEsi("N");
		movimento.setSegnoImp("+");
		movimento.setIdUniArticolo(prodotto.getIdUnicoArt());
		movimento.setIncTotali("NO");
		movimento.setTipo("IP");
		movimento.setQuantita(prodotto.getQtaSpedizione());
		return movimento;
	}

	@Override
	public TestataOrdini deserializzaUscita(OrdineJSON jsonOrdine) {
		UscitaJSON json = jsonOrdine.getOrdine();
		TestataOrdini ordine = new TestataOrdini();
		// Riferimento
		ordine.setRifOrdineCli(json.getRiferimentoOrdine());
		ordine.setNrOrdine(json.getRiferimentoOrdine());
		// Dati sull'ordine
		ordine.setQtaTotaleSpedire(json.getPezziOrdinati());
		ordine.setTipoOrdine(json.getTipo());
		ordine.setDataOrdine(new Timestamp(json.getDataOrdine().getTime()));
		// Opzionali
		ordine.setNote(json.getNote());
		ordine.setPriorita(json.getPriorita());
		// Data di consegna
		if (json.getDataConsegna() != null)
			ordine.setDataConsegna(new Timestamp(json.getDataConsegna().getTime()));
		// Documento
		DocumentoJSON documento = jsonOrdine.getDocumento();
		if (documento != null) {
			ordine.setNrDoc(documento.getRiferimento());
			ordine.setTipoDoc(documento.getTipo());
			ordine.setDataDoc(new Timestamp(documento.getData().getTime()));
		}
		return ordine;
	}

	@Override
	public RighiOrdine deserializzaDettaglio(UscitaDettaglioJSON json) {
		// Verifico che il prodotto esiste ed inietto dentro il rigo ordine le info del prodotto trovato
		Articoli prodotto = getProdotto(json.getProdotto());
		if (prodotto == null)
			throw new CustomException("L'articolo indicato non esiste. ( " + json.getProdotto() + " )");
		if (!isMagazzinoEsistente(json.getMagazzino()))
			throw new CustomException("Il magazzino indicato non esiste. ( " + json.getMagazzino() + " )");
		RighiOrdine dettaglio = new RighiOrdine();
		dettaglio.setCodBarre(prodotto.getCodBarre());
		dettaglio.setBarraEAN(prodotto.getBarraEAN());
		dettaglio.setBarraUPC(prodotto.getBarraUPC());
		dettaglio.setCodiceArticolo(prodotto.getCodArtStr());
		dettaglio.setColore(prodotto.getColore());
		dettaglio.setComposizione(prodotto.getComposizione());
		dettaglio.setDescrizione(prodotto.getDescrizione());
		dettaglio.setIdUnicoArt(prodotto.getIdUniArticolo());
		dettaglio.setMagazzino(magazzini.get(json.getMagazzino()));
		dettaglio.setNrRigo(json.getRiga());
		dettaglio.setQtaSpedizione(json.getQuantitaOrdinata());
		dettaglio.setQtadaubicare(json.getQuantitaOrdinata());
		dettaglio.setTaglia(prodotto.getTaglia());
		dettaglio.setNoteCliente(json.getNote());
		dettaglio.setTipoord(json.getTipo());
		if (json.getSeriali() != null && !json.getSeriali().isEmpty())
			dettaglio.setSeriali(json.getSeriali());
		return dettaglio;
	}

	private Articoli getProdotto(String chiave) {
		Articoli prodotto = daoProdotti.trovaDaSKU(chiave);
		return prodotto;
	}

	private boolean isMagazzinoEsistente(String codificaMagazzino) {
		// Controllo se l'ho già trovato prima, se non c'è lo cerco.
		if (!magazzini.containsKey(codificaMagazzino)) {
			Magazzini magazzino = daoMagazzini.trovaDaCodificaCliente(codificaMagazzino);
			if (magazzino != null)
				magazzini.put(codificaMagazzino, magazzino.getCodiceMag());
		}
		return magazzini.containsKey(codificaMagazzino);
	}

	private String getCodificaClientePerMagazzino(String magazzino) {
		// null check
		if (magazzino == null)
			magazzino = "";
		String codifica = null;
		for (String key : magazzini.keySet()) {
			String value = magazzini.get(key);
			if (magazzino.equals(value)) {
				codifica = key;
				break;
			}
		}
		return codifica;
	}

	@Override
	public OrdineJSON serializzaOrdine(TestataOrdini uscita, List<RighiOrdine> dettagli) {
		OrdineJSON json = new OrdineJSON();
		//Testata
		UscitaJSON ordine = serializzaUscita(uscita, true);
		json.setOrdine(ordine);
		// Dettagli, se presenti
		if (dettagli != null) {
			List<UscitaDettaglioJSON> prodotti = new LinkedList<>();
			for (RighiOrdine dettaglio : dettagli) {
				UscitaDettaglioJSON prodotto = serializzaDettaglio(dettaglio);
				prodotti.add(prodotto);
			}
			json.setDettagli(prodotti);
		}
		// Documento
		DocumentoJSON documento = new DocumentoJSON();
		if (documento != null) {
			documento.setRiferimento(uscita.getNrDoc());
			documento.setTipo(uscita.getTipoDoc());
			documento.setData(uscita.getDataDoc());
		}
		json.setDocumento(documento);
		return json;
	}

	@Override
	public UscitaJSON serializzaUscita(TestataOrdini uscita, boolean dettagliato) {
		UscitaJSON json = new UscitaJSON();
		// Tracking
		json.setCodiceTracking(uscita.getNrLetteraVettura());
		// Dati ordine
		json.setId(uscita.getIdTestaSped());
		StatoOrdine statoOrdine = getStatoOrdine(uscita);
		json.setStato(statoOrdine.getNome());
		json.setTipo(uscita.getTipoOrdine());
		json.setDataOrdine(uscita.getDataOrdine());
		json.setPezziImballati(uscita.getQtaimballata());
		json.setPezziOrdinati(uscita.getQtaTotaleSpedire());
		json.setRiferimentoOrdine(uscita.getRifOrdineCli());
		// Opzionali
		json.setDataConsegna(uscita.getDataConsegna());
		json.setPriorita(uscita.getPriorita());
		json.setNote(uscita.getNote());
		if (dettagliato) {
			// Destinatario
			Destinatari d = daoIndirizzi.trovaDestinatario(uscita.getIdDestina());
			if (d != null) {
				IndirizzoJSON destinatario = new IndirizzoJSON();
				destinatario.setCap(d.getCap());
				destinatario.setEmail(d.getEmail());
				destinatario.setIndirizzo(d.getIndirizzo());
				destinatario.setLocalita(d.getLocalita());
				destinatario.setNazione(d.getCodIso());
				destinatario.setProvincia(d.getProvincia());
				destinatario.setRagioneSociale(d.getRagSoc1());
				destinatario.setTelefono(d.getTel());
				json.setDestinatario(destinatario);
			}
			// Mittente
			MittentiOrdine m = daoIndirizzi.trovaMittente(uscita.getIdMittente());
			if (m != null) {
				IndirizzoJSON mittente = new IndirizzoJSON();
				mittente.setCap(m.getCap());
				mittente.setEmail(m.getEmail());
				mittente.setIndirizzo(m.getIndirizzo());
				mittente.setLocalita(m.getLocalita());
				mittente.setNazione(m.getNazione());
				mittente.setProvincia(m.getProvincia());
				mittente.setRagioneSociale(m.getRagioneSociale());
				mittente.setTelefono(m.getTelefono());
				json.setDestinatario(mittente);
			}
		}
		return json;
	}

	@Override
	public UscitaDettaglioJSON serializzaDettaglio(RighiOrdine dettaglio) {
		UscitaDettaglioJSON json = new UscitaDettaglioJSON();
		json.setMagazzino(getCodificaClientePerMagazzino(dettaglio.getMagazzino()));
		json.setProdotto(dettaglio.getCodiceArticolo());
		json.setQuantitaOrdinata(dettaglio.getQtaSpedizione());
		json.setQuantitaImballata(dettaglio.getQtaImballata());
		json.setRiga(dettaglio.getNrRigo());
		json.setNote(dettaglio.getNoteCliente());
		json.setTipo(dettaglio.getTipoord());
		if (dettaglio.getSeriali() != null && !dettaglio.getSeriali().isEmpty())
			json.setSeriali(dettaglio.getSeriali());
		return json;
	}

	@Override
	public boolean assegna(String riferimentoOrdine) {
		// Recupero le info sul carico associato, se non lo trovo lancio l'eccezione e mi fermo qui.
		TestataOrdini ordine = daoTestataOrdine.trovaDaRiferimento(riferimentoOrdine); //trovaDaRiferimento(riferimentoOrdine);
		if (ordine == null)
			throw new CustomException("Il riferimento indicato per l'ordine non è valido. (" + riferimentoOrdine + ")");
		boolean finalizza = finalizza(ordine);
		return finalizza;
	}

	@Override
	public boolean assegna(int idOrdine) {
		// Recupero le info sul carico associato, se non lo trovo lancio l'eccezione e mi fermo qui.
		EntityManager em = getManager();
		TestataOrdini ordine = em.find(TestataOrdini.class, idOrdine);
		em.close();
		if (ordine == null)
			throw new CustomException("L'ID indicato per l'ordine non è valido. (" + idOrdine + ")");
		boolean finalizza = finalizza(ordine);
		return finalizza;
	}
	
	@Override
	public OrdineImballatoJSON ottieniDettagliImballo(String riferimento) {
		// Recupero le info sul carico associato, se non lo trovo lancio l'eccezione e mi fermo qui.
		TestataOrdini ordine = daoTestataOrdine.trovaDaRiferimento(riferimento); //trovaDaRiferimento(riferimento);
		if (ordine == null)
			throw new CustomException("Il riferimento indicato per l'ordine non è valido. (" + riferimento + ")");
		OrdineImballatoJSON ordineImballato = ottieniDettagliImballo(ordine);
		return ordineImballato;
	}

	@Override
	public OrdineImballatoJSON ottieniDettagliImballo(int idOrdine) {
		EntityManager em = getManager();
		TestataOrdini ordine = em.find(TestataOrdini.class, idOrdine);
		em.close();
		if (ordine == null)
			throw new CustomException("L'ID indicato per l'ordine non è valido. (" + idOrdine + ")");
		OrdineImballatoJSON ordineImballato = ottieniDettagliImballo(ordine);
		return ordineImballato;
	}
	
	private Imballi getFormatoImballo(String codice) {
		if (!imballi.containsKey(codice)) {
			Imballi imballo = daoImballi.trovaDaCodice(codice);
			imballi.put(codice, imballo);
		}
		return imballi.get(codice);
	}
	
	private OrdineImballatoJSON ottieniDettagliImballo(TestataOrdini ordine) {
		//Controllo lo stato dell'ordine, se non va bene lancio un'eccezione
		StatoOrdine stato = getStatoOrdine(ordine);
		boolean statoOk = (stato == StatoOrdine.DIIB || stato == StatoOrdine.COIB || stato == StatoOrdine.ELAB || stato == StatoOrdine.INSP || stato == StatoOrdine.SPED);
		if (!statoOk)
			throw new CustomException("L'ordine non è ancora stato finito di imballare.");
		OrdineImballatoJSON ordineImballato = new OrdineImballatoJSON();
		//Recupero e inserisco le informazioni generali sull'ordine.
		UscitaJSON infoGenerali = serializzaUscita(ordine, false);
		ordineImballato.setOrdine(infoGenerali);
		//Recupero e inserisco le informazioni sugli imballi.
		List<ColliImballo> colliImballati = recuperaImballiDaOrdine(ordine);
		List<RighiImballoLight> righe = trovaSeriali(ordine.getNrLista());
		List<ImballoJSON> imballi = new LinkedList<>();
		for (ColliImballo collo : colliImballati) {
			ImballoJSON imballo = new ImballoJSON();
			//Info generali sul collo
			imballo.setRiferimento(collo.getKeyColloSpe());
			imballo.setBarcode(collo.getBarCodeImb());
			imballo.setPeso(collo.getPesoKg() != null ? collo.getPesoKg() : 0.0);
			double volume = ((double) collo.getVolume()) / 1000000.0;
			imballo.setVolume(volume);
			//formato dell'imballo, se presente.
			Imballi formatoImballo = getFormatoImballo(collo.getCodFormato());
			if (formatoImballo != null) {
				imballo.setH(formatoImballo.getH());
				imballo.setL(formatoImballo.getL());
				imballo.setZ(formatoImballo.getZ());
			}
			//Prodotti e seriali
			List<ProdottoImballatoJSON> prodotti = new LinkedList<>();
			List<String> seriali = new LinkedList<>();
			HashMap<String, Integer> mappaProdottiQuantita = new HashMap<>();
			for (RighiImballoLight riga : righe) {
				//Controllo se appartiene a quel collo
				if (riga.getKeyColloSpe().equals(collo.getKeyColloSpe())) {
					//Aggiorno la giusta quantita per lo SKU
					String sku = riga.getCodiceArticolo();
					int quantita = mappaProdottiQuantita.containsKey(sku) ? mappaProdottiQuantita.get(sku) : 0;
					quantita += riga.getQtaImballata();
					mappaProdottiQuantita.put(sku, quantita);
					//Aggiungo il seriale alla lista
					seriali.add(riga.getSeriale());
				}
			}
			//Aggiungo ogni SKU trovato e la relativa quantita alla lista dei prodotti.
			for (String sku : mappaProdottiQuantita.keySet()) {
				ProdottoImballatoJSON prodottoImballato = new ProdottoImballatoJSON();
				prodottoImballato.setProdotto(sku);
				prodottoImballato.setQuantitaImballata(mappaProdottiQuantita.get(sku));
				prodotti.add(prodottoImballato);
			}
			imballo.setProdotti(prodotti);
			imballo.setSeriali(seriali);
			imballi.add(imballo);
		}
		ordineImballato.setImballi(imballi);
		return ordineImballato;
	}

	@Override
	public boolean spedisci(SpedizioneJSON spedizione) {
		boolean spedisci;
		// Recupero la lista degli ordini ed eseguo i controlli necessari.
		List<TestataOrdini> ordiniDaSpedire = checkOrdiniDaSpedire(spedizione);
		// Controllo se esiste se ho già inserito le informazioni sulla spedizione.
		// Durante il check ho già controllato che IdTestaCorr sia identico per tutte quindi mi basta controllare il primo
		// Se è uguale a 0, il valore di default, allora non ho ancora inserito un testacorr corrispondente.
		boolean primoInserimento = ordiniDaSpedire.get(0).getIdTestaCorr() == 0;
		// La prima volta creo colliPreleva e un testacorr in cui combino le info delle testateordini.
		// la prima volta aggiorno i testaordini con stato = "INSP" e campo = ID di testacorr appena creato
		// Le successive vado in update su testacorr
		if (primoInserimento) {
			spedisci = inserisciInfoSpedizione(ordiniDaSpedire, spedizione);
		} else {
			spedisci = aggiornaInfoSpedizione(ordiniDaSpedire, spedizione);
		}
		return spedisci;
	}

	protected boolean aggiornaInfoSpedizione(List<TestataOrdini> ordiniDaSpedire, SpedizioneJSON infoSpedizione) {
		boolean aggiornamento;
		// Utility
		SimpleDateFormat meseGiorno = new SimpleDateFormat("MMdd");
		// Recupero la spedizione dal primo ordine, ho già controllato che siano tutti uguali
		TestataOrdini primoOrdine = ordiniDaSpedire.get(0);
		EntityManager em = getManager();
		TestaCorr spedizione = em.find(TestaCorr.class, primoOrdine.getIdTestaCorr());
		// Aggiorno i campi necessari
		spedizione.setCorriere(infoSpedizione.getCorriere());
		int dataConsegna = infoSpedizione.getDataConsegna() != null ? Integer.parseInt(meseGiorno.format(infoSpedizione.getDataConsegna())) : 0;
		spedizione.setDataConsegna(dataConsegna);
		String note = infoSpedizione.getNote() != null ? infoSpedizione.getNote() : "";
		String note1 = note.length() > 35 ? note.substring(0, 35) : note;
		String note2 = note.length() > 35 ? note.substring(35, note.length()) : "";
		spedizione.setNote1(note1);
		spedizione.setNote2(note2);
		spedizione.setServizio(infoSpedizione.getServizioCorriere());
		spedizione.setValoreMerce(infoSpedizione.getValoreDoganale());
		
		//Documento - Non lo faccio nella versione base
//		DocumentoJSON documento = infoSpedizione.getDocumentoFiscale();
//		if (documento != null) {
//			spedizione.setDocumentoBase64(documento.getDocumentoBase64());
//			//FIXME - Salvare questi dati su TestaCorr o sulle TestataOrdini
//			documento.getData();
//			documento.getRiferimento();
//			documento.getTipo();
//		}

		// Contrassegno
		ContrassegnoJSON infoContrassegno = infoSpedizione.getContrassegno();
		if (infoContrassegno != null) {
			spedizione.setCodBolla("4 ");
			spedizione.setContrassegno(infoContrassegno.getValore());
			spedizione.setTipoIncasso(infoContrassegno.getTipo());
			spedizione.setValutaIncasso(infoContrassegno.getValuta());
		} else {
			spedizione.setCodBolla("1 ");
			spedizione.setContrassegno(0.0);
			spedizione.setTipoIncasso("  ");
			spedizione.setValutaIncasso("EUR");
		}

		// Vado in scrittura in maniera transazionale.
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(spedizione);
			transaction.commit();
			aggiornamento = true;
		} catch (Exception e) {
			logger.error(e);
			transaction.rollback();
			aggiornamento = false;
		} finally {
			em.close();
		}
		return aggiornamento;
	}
	
	protected ColliPreleva creaColloDaPrelevare(TestataOrdini ordine, ColliImballo colloImballato, SpedizioneJSON infoSpedizione) {
		ColliPreleva colloDaPrelevare = new ColliPreleva();
		colloDaPrelevare.setNrLista(ordine.getNrLista());
		colloDaPrelevare.setBarcodeCorriere(colloImballato.getBarCodeImb());
		colloDaPrelevare.setCodiceCorriere(infoSpedizione.getCorriere());
		colloDaPrelevare.setKeyColloPre(colloImballato.getKeyColloSpe());
		colloDaPrelevare.setNrColloCliente(colloImballato.getNrIdCollo());
		return colloDaPrelevare;
	}
	
	protected RigaCorr creaRigaSpedizione(TestataOrdini ordine, ColliImballo colloImballato, SpedizioneJSON infoSpedizione, int progressivoSpedizione) {
		//Calcolo il volume corretto: secondo Antonio va diviso per 1 milione
		double volumeCollo = ((double) colloImballato.getVolume()) / 1000000.0;
		RigaCorr rigaSpedizione = new RigaCorr();
		rigaSpedizione.setCodRaggruppamento(progressivoSpedizione);
		rigaSpedizione.setFormato(colloImballato.getCodFormato());
		rigaSpedizione.setNrCollo(colloImballato.getNrIdCollo());
		rigaSpedizione.setNrColloStr(df.format(progressivoSpedizione));
		rigaSpedizione.setNrLista(ordine.getNrLista());
		rigaSpedizione.setNrSpedi(progressivoSpedizione);
		rigaSpedizione.setPeso(colloImballato.getPesoKg());
		rigaSpedizione.setPezzi(colloImballato.getPezziCollo());
		rigaSpedizione.setVolume(volumeCollo);
		rigaSpedizione.setCodMittente(infoSpedizione.getCodiceCorriere());
		return rigaSpedizione;
	}

	protected boolean inserisciInfoSpedizione(List<TestataOrdini> ordiniDaSpedire, SpedizioneJSON infoSpedizione) {
		boolean inserimento;
		// Preparo le variabili che mi serviranno per andare in insert.
		TestaCorr spedizione = new TestaCorr();
		List<ColliPreleva> colliDaPrelevare = new LinkedList<>();
		List<RigaCorr> righeSpedizione = new LinkedList<>();
		int pezzi = 0;
		int colli = 0;
		double peso = 0.0;
		double volume = 0.0;
		// Recupero il mittente e il destinatario dal primo ordine, ho già
		// controllato che siano tutti uguali
		TestataOrdini primoOrdine = ordiniDaSpedire.get(0);
		Destinatari destinatario = daoIndirizzi.trovaDestinatario(primoOrdine.getIdDestina());
		MittentiOrdine mittente = daoIndirizzi.trovaMittente(primoOrdine.getIdMittente());
		// Imposto alcune info di testacorr
		spedizione.setCorriere(infoSpedizione.getCorriere());
		spedizione.setCodMittente(infoSpedizione.getCodiceCorriere());
		int dataConsegna = infoSpedizione.getDataConsegna() != null ? Integer.parseInt(meseGiorno.format(infoSpedizione.getDataConsegna())) : 0;
		spedizione.setDataConsegna(dataConsegna);
		spedizione.setMittenteAlfa(primoOrdine.getNrOrdine());
		int progressivoSpedizione = daoTestaCorr.getProgressivoSpedizioneTestaCorr(); //getProgressivoSpedizioneTestaCorr();
		spedizione.setMittenteNum(progressivoSpedizione);
		String note = infoSpedizione.getNote() != null ? infoSpedizione.getNote() : ""; //Ho già verificato che sia <= 70 caratteri.
		String note1 = note.length() > 35 ? note.substring(0, 35) : note;
		String note2 = note.length() > 35 ? note.substring(35, note.length()) : "";
		spedizione.setNote1(note1);
		spedizione.setNote2(note2);
		spedizione.setNrSpedi(progressivoSpedizione);
		spedizione.setServizio(infoSpedizione.getServizioCorriere());
		spedizione.setValoreMerce(infoSpedizione.getValoreDoganale());
		
		//Documento - Questa parte la faccio solo per quelli che hanno un documento da gestire.
//		DocumentoJSON documento = infoSpedizione.getDocumentoFiscale();
//		if (documento != null) {
//			spedizione.setDocumentoData(new Timestamp(documento.getData().getTime()));
//			spedizione.setDocumentoRiferimento(documento.getRiferimento());
//			spedizione.setDocumentoTipo(documento.getTipo());
//		}

		// Contrassegno
		ContrassegnoJSON infoContrassegno = infoSpedizione.getContrassegno();
		if (infoContrassegno != null) {
			spedizione.setCodBolla("4 ");
			spedizione.setContrassegno(infoContrassegno.getValore());
			spedizione.setTipoIncasso(infoContrassegno.getTipo());
			spedizione.setValutaIncasso(infoContrassegno.getValuta());
		} else {
			spedizione.setCodBolla("1 ");
			spedizione.setContrassegno(0.0);
			spedizione.setTipoIncasso("  ");
			spedizione.setValutaIncasso("EUR");
		}

		spedizione.setCap(destinatario.getCap());
		spedizione.setIndirizzo(destinatario.getIndirizzo());
		spedizione.setLocalita(destinatario.getLocalita());
		spedizione.setNazione(destinatario.getNazione());
		spedizione.setProvincia(destinatario.getProvincia());
		spedizione.setRagSocDest(destinatario.getRagSoc1());
		spedizione.setRagSocEst(destinatario.getRagSoc2());
		//spedizione.setTelefono(destinatario.getTel());

		spedizione.setCapMitt(mittente.getCap());
		spedizione.setNazioneMitt(mittente.getNazione());
		spedizione.setRagSocMitt(mittente.getRagioneSociale());

		// Recupero le info necessarie da ogni ordine e aggiorno quelle che dovranno essere salvate
		for (TestataOrdini ordine : ordiniDaSpedire) {
			//Fix, secondo Antonio e Andrea il campo nr lista di testaCorr andrebbe riempito con uno qualsiasi dei nr lista delle testate.
			spedizione.setNrLista(ordine.getNrLista());
			//Aggiorno le info dell'ordine necessarie
			ordine.setStato("INSP");
			ordine.setCodCorriere(infoSpedizione.getCorriere());
			ordine.setCodCorriere(infoSpedizione.getCorriere());
			ordine.setCodiceClienteCorriere(infoSpedizione.getCodiceCorriere());
			if (infoContrassegno != null) {
				ordine.setTipoIncasso(infoContrassegno.getTipo());
				ordine.setValContrassegno(infoContrassegno.getValore());
			}
			// Recupero i ColliImballo corrispondenti
			List<ColliImballo> colliImballati = recuperaImballiDaOrdine(ordine);
			for (ColliImballo colloImballato : colliImballati) {
				// Creo il ColliPreleva corrispondente
				ColliPreleva colloDaPrelevare = creaColloDaPrelevare(ordine, colloImballato, infoSpedizione);
				colliDaPrelevare.add(colloDaPrelevare);
				// Creo la RigaCorr corrispondente
				RigaCorr rigaSpedizione = creaRigaSpedizione(ordine, colloImballato, infoSpedizione, progressivoSpedizione);
				righeSpedizione.add(rigaSpedizione);
				// Aggiungo per avere i totali di pezzi, peso e volume
				colli += 1;
				pezzi += colloImballato.getPezziCollo();
				peso += colloImballato.getPesoKg();
				volume += rigaSpedizione.getVolume();
			}
		}
		// Aggiorno le info su testacorr
		spedizione.setPeso(peso);
		spedizione.setVolume(volume);
		spedizione.setNrColli(colli);
		spedizione.setPezzi(pezzi);
		// Vado in scrittura in maniera transazionale.
		EntityManager em = getManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			// Inserisco TestaCorr, RigaCorr e ColliPreleva
			em.persist(spedizione);
			for (RigaCorr riga : righeSpedizione) {
				em.persist(riga);
			}
			for (ColliPreleva collo : colliDaPrelevare) {
				em.persist(collo);
			}
			// Aggiorno le TestataOrdini con il testacorr creato e le altre info.
			for (TestataOrdini ordine : ordiniDaSpedire) {
				ordine.setIdTestaCorr(spedizione.getIdTestaCor());
				em.merge(ordine);
			}
			transaction.commit();
			inserimento = true;
		} catch (Exception e) {
			logger.error(e);
			transaction.rollback();
			inserimento = false;
		} finally {
			em.close();
		}
		return inserimento;
	}

	protected List<ColliImballo> recuperaImballiDaOrdine(TestataOrdini ordine) {
		List<ColliImballo> dettagli = daoColliImballati.trovaDaNumeroLista(ordine.getNrLista());
		return dettagli;
	}
	
	/**
	 * Controlla lo stato dell'ordine e lo converte in un valore della enum.<br>
	 * Nel caso in cui qualcosa sia andato storto restituisce lo stato "NONE".
	 * @param uscita la testata dell'ordine.
	 * @return lo stato dell'ordine come enum.
	 */
	protected StatoOrdine getStatoOrdine(TestataOrdini uscita) {
		StatoOrdine stato;
		try {
			stato = StatoOrdine.valueOf(uscita.getStato());
		} catch (Exception e) {
			stato = StatoOrdine.NONE;
		}
		return stato;
	}

	private List<TestataOrdini> checkOrdiniDaSpedire(SpedizioneJSON spedizione) {
		List<TestataOrdini> ordiniDaSpedire = new LinkedList<>();
		List<CustomErrorCause> problemiRiscontrati = new LinkedList<>();
		List<String> riferimenti = spedizione.getRiferimenti();
		int idTestaCorr = -1;
		int idDestina = -1;
		// Recupero tutti gli ordini
		for (String riferimento : riferimenti) {
			TestataOrdini ordine = daoTestataOrdine.trovaDaRiferimento(riferimento);
			// Se non trovo corrispondenza o non sono in uno stato congruo
			// lancio un'eccezione.
			if (ordine == null) {
				CustomErrorCause problema = new CustomErrorCause("Non esiste alcun ordine a sistema con il riferimento indicato.", riferimento);
				problemiRiscontrati.add(problema);
				continue; //Passo al riferimento successivo.
			} else {
				ordiniDaSpedire.add(ordine);
				StatoOrdine stato = getStatoOrdine(ordine);
				if (!(stato == StatoOrdine.ELAB || stato == StatoOrdine.INSP)) {
					CustomErrorCause problema = new CustomErrorCause("Non è possibile indicare informazioni per la spedizione dell'ordine indicato.", riferimento + " (Stato: " + stato.getNome() + ")");
					problemiRiscontrati.add(problema);
				}
			}
			// Controllo che il campo che le lega a testacorr sia vuoto per
			// tutti o identico per tutti
			if (idTestaCorr == -1) {
				idTestaCorr = ordine.getIdTestaCorr();
			} else if (idTestaCorr != ordine.getIdTestaCorr()) {
				CustomErrorCause problema = new CustomErrorCause("Non è possibile raggruppare ordini per cui sono già state indicate spedizioni diverse.", riferimento);
				problemiRiscontrati.add(problema);
			}
			// Controllo che il destinatario sia lo stesso per tutti
			if (spedizione.isForzaAccoppiamentoDestinatari()) {
				if (idDestina == -1) {
					idDestina = ordine.getIdDestina();
				} else if (idDestina != ordine.getIdDestina()) {
					CustomErrorCause problema = new CustomErrorCause("Non è possibile raggruppare in una sola spedizione ordini con destinatari diversi.", riferimento);
					problemiRiscontrati.add(problema);
				}
			}
			//Controllo che lo stato del TestaCorr presente sia 0, passa a 1 quando vengono stampati i documenti
			if (idTestaCorr > 0) {
				TestaCorr testataSpedizione = daoTestaCorr.trovaDaID(idTestaCorr);
				if (testataSpedizione != null && testataSpedizione.getTrasmesso() != 0) {
					CustomErrorCause problema = new CustomErrorCause("I documenti relativi alla spedizione sono stati già stampati e allegati ai colli. Non è possibile aggiornare le informazioni sulla spedizione.", riferimento);
					problemiRiscontrati.add(problema);
				}
			}
		}
		// Se ho trovato dei problemi sollevo l'eccezione altrimenti restituisco
		// le testate ordini trovate e proseguo con l'inserimento.
		if (!problemiRiscontrati.isEmpty())
			throw new CustomException("Sono stati riscontrati problemi con gli ordini indicati", 400, problemiRiscontrati);
		return ordiniDaSpedire;
	}

	@Override
	public SpedizioneJSON getDocumentoDiTrasporto(int idSpedizione) {
		EntityManager em = getManager();
		TestaCorr spedizione = em.find(TestaCorr.class, idSpedizione);
		em.close();
		//String documento = spedizione != null? spedizione.getDocumentoBase64() : null;
		SpedizioneJSON json;
		if (spedizione != null) {
			json = new SpedizioneJSON();
			DocumentoJSON documento = new DocumentoJSON();
			documento.setData(null);
			documento.setRiferimento(spedizione.getMittenteAlfa());
			documento.setTipo("ORDINE");
			documento.setDocumentoBase64(null);
			json.setDocumentoFiscale(documento);
			json.setCorriere(spedizione.getCorriere());
		} else {
			json = null;
		}
		return json;
	}

}
