package it.ltc.services.clienti.data.carico;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.ColliPackSerialiDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.dao.legacy.PakiTestaTipoDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.seriale.ColliPackConSeriale;
import it.ltc.model.shared.json.cliente.CaricoJSON;
import it.ltc.model.shared.json.cliente.DocumentoJSON;
import it.ltc.model.shared.json.cliente.IngressoDettaglioJSON;
import it.ltc.model.shared.json.cliente.IngressoJSON;
import it.ltc.model.shared.json.cliente.ModificaCaricoJSON;
import it.ltc.model.shared.json.cliente.ModificaCaricoJSON.LavorazioneSeriali;
import it.ltc.model.shared.json.cliente.ModificaCaricoJSON.Riscontro;
import it.ltc.model.shared.json.cliente.ModificaCaricoJSON.StatoCarico;
import it.ltc.services.clienti.data.fornitore.FornitoreLegacyDAOImpl;
import it.ltc.services.custom.exception.CustomException;

public class CaricoLegacyDAOImpl extends PakiTestaDao implements CaricoDAO<PakiTesta, PakiArticolo> {

	private static final Logger logger = Logger.getLogger("CaricoLegacyDAOImpl");

	private final PakiTestaTipoDao daoTipo;
	private final FornitoreLegacyDAOImpl daoFornitore;
	private final PakiArticoloDao daoPakiArticolo;
	private final ArticoliDao daoProdotti;
	private final ColliPackSerialiDao daoColliPack;
	private final MagazzinoDao daoMagazzini;
	private final HashMap<String, Magazzini> mappaMagazzini;

	public CaricoLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoTipo = new PakiTestaTipoDao(persistenceUnit);
		daoFornitore = new FornitoreLegacyDAOImpl(persistenceUnit);
		daoPakiArticolo = new PakiArticoloDao(persistenceUnit);
		daoProdotti = new ArticoliDao(persistenceUnit);
		daoColliPack = new ColliPackSerialiDao(persistenceUnit);
		daoMagazzini = new MagazzinoDao(persistenceUnit);
		mappaMagazzini = new HashMap<>();
	}

	@Override
	public CaricoJSON trovaDaID(int idCarico, boolean dettagliato) {
		PakiTesta carico = trovaDaID(idCarico);
		List<PakiArticolo> dettagli;
		if (carico != null && dettagliato) {
			dettagli = daoPakiArticolo.trovaRigheDaCarico(idCarico);
			//Recupero i seriali e ne faccio una mappa
			List<ColliPackConSeriale> seriali = daoColliPack.trovaProdottiNelCarico(carico.getIdTestaPaki());
			HashMap<Integer, List<String>> mappaSerialiPerRiga = new HashMap<>();
			for (ColliPackConSeriale seriale : seriali) {
				//Controllo che il seriale sia stato effettivamente inserito a sistema, se non c'è lo salto.
				String rfid = seriale.getSeriale();
				if (rfid == null || rfid.isEmpty())
					continue;
				int idRiga = seriale.getIdPakiarticolo();
				//Controllo se esiste già una lista di seriali associati al pakiarticolo
				if (!mappaSerialiPerRiga.containsKey(idRiga)) {
					List<String> serialiAssociati = new LinkedList<>();
					mappaSerialiPerRiga.put(idRiga, serialiAssociati);
				}
				mappaSerialiPerRiga.get(idRiga).add(rfid);
			}
			//Per ogni riga gli associo una lista nella mappa, se presente.
			for (PakiArticolo dettaglio : dettagli) {
				dettaglio.setSeriali(mappaSerialiPerRiga.get(dettaglio.getIdPakiArticolo()));
			}
		} else {
			dettagli = null;
		}
		CaricoJSON json = serializzaCarico(carico, dettagli);
		return json;
	}

	public CaricoJSON trovaDaRiferimento(String riferimento, boolean dettagliato) {
		CaricoJSON json;
		PakiTesta carico = trovaDaRiferimento(riferimento);
		if (carico != null) {
			List<PakiArticolo> dettagli;
			//Se hanno richiesto nel dettaglio allora aggiungo info
			if (dettagliato) {
				dettagli = daoPakiArticolo.trovaRigheDaCarico(carico.getIdTestaPaki());
				//Recupero i seriali e ne faccio una mappa
				List<ColliPackConSeriale> seriali = daoColliPack.trovaProdottiNelCarico(carico.getIdTestaPaki());
				HashMap<Integer, List<String>> mappaSerialiPerRiga = new HashMap<>();
				for (ColliPackConSeriale seriale : seriali) {
					//Controllo che il seriale sia stato effettivamente inserito a sistema, se non c'è lo salto.
					String rfid = seriale.getSeriale();
					if (rfid == null || rfid.isEmpty())
						continue;
					int idRiga = seriale.getIdPakiarticolo();
					//Controllo se esiste già una lista di seriali associati al pakiarticolo
					if (!mappaSerialiPerRiga.containsKey(idRiga)) {
						List<String> serialiAssociati = new LinkedList<>();
						mappaSerialiPerRiga.put(idRiga, serialiAssociati);
					}
					mappaSerialiPerRiga.get(idRiga).add(rfid);
				}
				//Per ogni riga gli associo una lista nella mappa, se presente.
				for (PakiArticolo dettaglio : dettagli) {
					dettaglio.setSeriali(mappaSerialiPerRiga.get(dettaglio.getIdPakiArticolo()));
				}
			} else {
				dettagli = null;
			}
			json = serializzaCarico(carico, dettagli);
		} else {
			json = null;
		}
		return json;
	}
	
	/**
	 * Aggiorno lo stato del carico. Usabile solo in ambiente di test.
	 * @param idCarico il carico da aggiornare.
	 * @param stato il nuovo stato. 
	 * @return l'esito dell'operazione.
	 */
	private boolean aggiornaStatoCaricoDiTest(int idCarico, int totalePezziLetti, String stato) {
		boolean update;
		EntityManager em = getManager();
		PakiTesta carico = em.find(PakiTesta.class, idCarico);
		if (carico != null) {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				carico.setStato(stato);
				carico.setQtaTotAre(totalePezziLetti);
				t.commit();
				update = true;
				logger.info("Stato del carico aggiornato!");
			} catch (Exception e) {
				logger.error(e);
				update = false;
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		} else {
			update = false;
			em.close();
		}
		return update;
	}
	
	private int aggiornaQuantitaRiscontratePerCaricoDiTest(ModificaCaricoJSON modifiche) {
		int update = 0;
		//Trovo la lista dei paki articolo da aggiornare.
		List<PakiArticolo> dettagli = daoPakiArticolo.trovaRigheDaCarico(modifiche.getId());
		//Calcolo le nuove quantita'
		for (PakiArticolo dettaglio : dettagli) {
			int quantita;
			switch (modifiche.getRiscontro()) {
				case NESSUNO : quantita = 0; break;
				case TUTTI : quantita = dettaglio.getQtaPaki(); break;
				case RANDOM : quantita = (int) Math.random() * dettaglio.getQtaPaki(); break;
				default : quantita = dettaglio.getQtaVerificata(); break;
			}
			update += quantita;
			dettaglio.setQtaVerificata(quantita);
		}
		//Aggiorno il DB
		daoPakiArticolo.aggiorna(dettagli);
		logger.info("Quantita' lette aggiornate!");
		return update;
	}
	
	/**
	 * Genera dei seriali univoci per ogni oggetto nel carico passato come argomento.
	 * @param idCarico l'ID del carico per cui generare i seriali.
	 * @return l'esito dell'operazione.
	 */
	private boolean generaSerialiPerCaricoDiTest(int idCarico) {
		boolean generazione;
		List<ColliPackConSeriale> seriali = daoColliPack.trovaProdottiNelCarico(idCarico);
		if (seriali.isEmpty()) {
			//Preparo le informazioni necessarie alla generazione dei seriali
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
			DecimalFormat df = new DecimalFormat("000000000000000");
			double seriale = Double.parseDouble(sdf.format(now));
			//Trovo i dettagli per cui generare i seriali
			List<PakiArticolo> dettagli = daoPakiArticolo.trovaRigheDaCarico(idCarico);
			logger.info("Stanno per essere generati seriali per " + dettagli.size() + " righe.");
			for (PakiArticolo dettaglio : dettagli) {
				int quantita = dettaglio.getQtaVerificata();
				for (int i=0; i < quantita; i++) {
					String stringaSeriale = df.format(seriale);
					seriale += 1;
					ColliPackConSeriale nuovoSeriale = new ColliPackConSeriale();
					nuovoSeriale.setCodArtStr(dettaglio.getCodArtStr());
					nuovoSeriale.setCodiceArticolo(dettaglio.getCodUnicoArt());
					nuovoSeriale.setNrIdColloPk(dettaglio.getBarcodeCollo());
					nuovoSeriale.setIdPakiarticolo(dettaglio.getIdPakiArticolo());
					nuovoSeriale.setIdTestaPaki(dettaglio.getIdPakiTesta());
					nuovoSeriale.setMagazzino(dettaglio.getMagazzino());
					nuovoSeriale.setQta(1);
					nuovoSeriale.setSeriale(stringaSeriale);
					seriali.add(nuovoSeriale);
				}
			}
			logger.info("Sono stati generati " + seriali.size() + " seriali.");
			//Se ne ho generato almeno uno vado in insert.
			if (!seriali.isEmpty()) {
				seriali = daoColliPack.inserisci(seriali);
				generazione = seriali != null;
			} else {
				generazione = false;
			}
		} else {
			generazione = false;
		}
		return generazione;
	}
	
	private boolean eliminaSerialiPerCaricoDiTest(int idCarico) {
		logger.info("eliminazione seriali randomici");
		List<ColliPackConSeriale> seriali = daoColliPack.trovaProdottiNelCarico(idCarico);
		seriali = daoColliPack.elimina(seriali);
		boolean delete = seriali != null; 
		return delete;
	}
	
	@Override
	public boolean modificaCaricoDiTest(ModificaCaricoJSON modifiche) {
		boolean update;
		//In base allo stato del carico eseguo diverse azioni.
		StatoCarico stato = modifiche.getStato();
		switch (stato) {
			case INSERITO : update = modificaTestAppenaArrivato(modifiche); break;
			case ARRIVATO : update = modificaTestAppenaArrivato(modifiche); break;
			case IN_LAVORAZIONE : update = modificaTestCompleto(modifiche); break;
			case LAVORATO : update = modificaTestCompleto(modifiche); break;
			case CHIUSO : update = modificaTestCompleto(modifiche); break;
			default : update = false; break;
		}
		return update;
	}
	
	private boolean modificaTestAppenaArrivato(ModificaCaricoJSON modifiche) {
		//Imposto questi valori in maniera autonoma
		modifiche.setRiscontro(Riscontro.NESSUNO);
		//Eseguo il resto come per gli altri
		int quantitaLetta = aggiornaQuantitaRiscontratePerCaricoDiTest(modifiche);
		boolean updateStato = quantitaLetta != -1 ? aggiornaStatoCaricoDiTest(modifiche.getId(), quantitaLetta, modifiche.getStato().name()) : false;
		boolean serialiEliminati = updateStato ? eliminaSerialiPerCaricoDiTest(modifiche.getId()) : true;
		return updateStato && serialiEliminati;
	}

	private boolean modificaTestCompleto(ModificaCaricoJSON modifiche) {
		int quantitaLetta = aggiornaQuantitaRiscontratePerCaricoDiTest(modifiche);
		boolean updateStato = quantitaLetta != -1 ? aggiornaStatoCaricoDiTest(modifiche.getId(), quantitaLetta, modifiche.getStato().name()) : false;
		boolean serialiGenerati = modifiche.getSeriali() == LavorazioneSeriali.SI  && updateStato ? generaSerialiPerCaricoDiTest(modifiche.getId()) : true;
		return updateStato && serialiGenerati;
	}

	@Override
	public List<IngressoJSON> trovaTutti() {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PakiTesta> criteria = cb.createQuery(PakiTesta.class);
		Root<PakiTesta> member = criteria.from(PakiTesta.class);
		criteria.select(member).orderBy(cb.desc(member.get("idTestaPaki")));
		List<PakiTesta> list = em.createQuery(criteria).setMaxResults(100).getResultList();
		em.close();
		List<IngressoJSON> carichi = new LinkedList<>();
		for (PakiTesta entity : list) {
			IngressoJSON json = serializzaIngresso(entity);
			carichi.add(json);
		}
		return carichi;
	}
	
	private void checkTipoCarico(String codice) {
		if (daoTipo.trovaDaCodice(codice) == null)
			throw new CustomException("Il tipo di carico specificato non esiste. (" + codice + ")");
	}

	@Override
	public CaricoJSON inserisci(CaricoJSON json) {
		// Deserializzazione testata
		PakiTesta carico = deserializzaIngresso(json);
		//Controllo il tipo di carico
		checkTipoCarico(carico.getTipodocumento());
		// Controllo sull'unicità del riferimento al carico
		PakiTesta esistente = trovaDaRiferimento(carico.getNrPaki());
		if (esistente != null)
			throw new CustomException("Esiste gia' un carico con lo stesso riferimento. (" + carico.getNrPaki() + ")");
		// Deserializza e effettua controlli sull'esistenza di magazzini e
		// prodotti
		List<PakiArticolo> dettagli = deserializzaDettagli(json);
		// Vado in scrittura
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.persist(carico);
			for (PakiArticolo dettaglio : dettagli) {
				dettaglio.setIdPakiTesta(carico.getIdTestaPaki());
				em.persist(dettaglio);
			}
			t.commit();
			json = serializzaCarico(carico, dettagli);
			logger.info("Carico inserito!");
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
	public IngressoDettaglioJSON inserisciDettaglio(IngressoDettaglioJSON json) {
		//Recupero le info sul carico associato, se non lo trovo lancio l'eccezione e mi fermo qui.
		PakiTesta carico = trovaDaRiferimento(json.getRiferimento());
		if (carico == null)
			throw new CustomException("Il riferimento indicato per il carico non è valido. (" + json.getRiferimento() + ")");
		//Recupero le info da aggiungere
		PakiArticolo nuoveInfo = deserializzaDettaglio(json);
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PakiArticolo> criteria = cb.createQuery(PakiArticolo.class);
		Root<PakiArticolo> member = criteria.from(PakiArticolo.class);
		Predicate condizioneRiferimento = cb.equal(member.get("nrDispo"), json.getRiferimento());
		Predicate condizioneRiga = cb.equal(member.get("rigaPacki"), json.getRiga());
		criteria.select(member).where(cb.and(condizioneRiferimento, condizioneRiga));
		List<PakiArticolo> list = em.createQuery(criteria).getResultList();
		PakiArticolo dettaglio = list.size() == 1 ? list.get(0) : null;
		if (dettaglio == null) {
			carico = em.find(PakiTesta.class, carico.getIdTestaPaki());
			//In teoria non dovrebbe mai succedere questo!
			if (carico == null)
				throw new CustomException("Il riferimento indicato per il carico non è valido. (" + json.getRiferimento() + ")");
			int totale = carico.getQtaTotAto() + nuoveInfo.getQtaPaki();
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				//Aggiorno il totale del carico
				carico.setQtaTotAto(totale);
				em.merge(carico);
				//Inserisco la nuova riga
				nuoveInfo.setIdPakiTesta(carico.getIdTestaPaki());
				nuoveInfo.setNrDispo(carico.getNrPaki());
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
			throw new CustomException("La riga di carico indicata è già presente. (" + json.getRiga() + ")");
		}
		return json;
	}

	@Override
	public IngressoJSON aggiorna(IngressoJSON json) {
		// Deserializzazione testata
		CaricoJSON c = new CaricoJSON();
		c.setIngresso(json);
		PakiTesta carico = deserializzaIngresso(c);
		//Controllo il tipo di carico
		checkTipoCarico(carico.getTipodocumento());
		// Controllo l'esistenza del riferimento al carico
		PakiTesta esistente = trovaDaRiferimento(carico.getNrPaki());
		if (esistente == null)
			throw new CustomException("Non esiste nessun carico con questo riferimento. (" + carico.getNrPaki() + ")");
		// Controllo lo stato, deve essere 'INSERITO'
		if (!"INSERITO".equals(esistente.getStato()))
			throw new CustomException("Il carico richiesto non è più modificabile.");
		// Update
		EntityManager em = getManager();
		esistente = em.find(PakiTesta.class, esistente.getIdTestaPaki());
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			// Aggiorno le informazioni di testata, non vado a toccare la quantita' pero'!
			esistente.setCodFornitore(carico.getCodFornitore());
			esistente.setIdFornitore(carico.getIdFornitore());
			esistente.setRagSocFor(carico.getRagSocFor());
			//esistente.setDataPaki(carico.getDataPaki());
			esistente.setTipodocumento(carico.getTipodocumento());
			em.merge(esistente);
			t.commit();
			json = serializzaIngresso(esistente);
			logger.info("Carico modificato!");
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
	public IngressoDettaglioJSON aggiornaDettaglio(IngressoDettaglioJSON json) {
		//Recupero le info sul carico associato, se non lo trovo lancio l'eccezione e mi fermo qui.
		PakiTesta carico = trovaDaRiferimento(json.getRiferimento());
		if (carico == null)
			throw new CustomException("Il riferimento indicato per il carico non è valido. (" + json.getRiferimento() + ")");
		if (!"INSERITO".equals(carico.getStato()))
			throw new CustomException("Il carico richiesto non è più modificabile.");
		//Recupero la riga del dettaglio
		PakiArticolo nuoveInfo = deserializzaDettaglio(json);
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PakiArticolo> criteria = cb.createQuery(PakiArticolo.class);
		Root<PakiArticolo> member = criteria.from(PakiArticolo.class);
		Predicate condizioneRiferimento = cb.equal(member.get("nrDispo"), json.getRiferimento());
		Predicate condizioneRiga = cb.equal(member.get("rigaPacki"), json.getRiga());
		criteria.select(member).where(cb.and(condizioneRiferimento, condizioneRiga));
		List<PakiArticolo> list = em.createQuery(criteria).getResultList();
		PakiArticolo dettaglio = list.size() == 1 ? list.get(0) : null;
		if (dettaglio != null) {
			carico = em.find(PakiTesta.class, carico.getIdTestaPaki());
			//In teoria non dovrebbe mai succedere questo!
			if (carico == null)
				throw new CustomException("Il riferimento indicato per il carico non è valido. (" + json.getRiferimento() + ")");
			int differenza = nuoveInfo.getQtaPaki() - dettaglio.getQtaPaki();
			int totale = carico.getQtaTotAto() + differenza;
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				//Aggiorno il totale del carico
				carico.setQtaTotAto(totale);
				em.merge(carico);
				//Aggiorno la riga
				String collo = nuoveInfo.getBarcodeCollo();
				if (collo != null && !collo.isEmpty())
					dettaglio.setBarcodeCollo(collo);
				String madeIn = nuoveInfo.getMadeIn(); 
				if (madeIn != null && !madeIn.isEmpty())
					dettaglio.setMadeIn(madeIn);
				dettaglio.setCodArtStr(nuoveInfo.getCodArtStr());
				dettaglio.setCodBarre(nuoveInfo.getCodBarre());
				dettaglio.setCodUnicoArt(nuoveInfo.getCodUnicoArt());
				dettaglio.setMagazzino(nuoveInfo.getMagazzino());
				dettaglio.setMagazzinoltc(nuoveInfo.getMagazzinoltc());
				dettaglio.setQtaPaki(nuoveInfo.getQtaPaki());
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
			throw new CustomException("La riga di carico indicata non esiste o non è univoca.");
		}
		return json;
	}

	@Override
	public IngressoJSON elimina(IngressoJSON json) {
		// Controllo l'esistenza del riferimento al carico
		PakiTesta esistente = trovaDaRiferimento(json.getRiferimentoCliente());
		if (esistente == null)
			throw new CustomException("Non esiste nessun carico con questo riferimento. (" + json.getRiferimentoCliente() + ")");
		// Controllo lo stato, deve essere 'INSERITO'
		if (!"INSERITO".equals(esistente.getStato()))
			throw new CustomException("Il carico richiesto non è più eliminabile.");
		// Delete
		EntityManager em = getManager();
		esistente = em.find(PakiTesta.class, esistente.getIdTestaPaki());
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(esistente);
			t.commit();
			json = serializzaIngresso(esistente);
			logger.info("Carico eliminato!");
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
	public IngressoDettaglioJSON eliminaDettaglio(IngressoDettaglioJSON json) {
		//Recupero le info sul carico associato, se non lo trovo lancio l'eccezione e mi fermo qui.
		PakiTesta carico = trovaDaRiferimento(json.getRiferimento());
		if (carico == null)
			throw new CustomException("Il riferimento indicato per il carico non è valido. (" + json.getRiferimento() + ")");
		if (!"INSERITO".equals(carico.getStato()))
			throw new CustomException("Il carico richiesto non è più modificabile.");
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PakiArticolo> criteria = cb.createQuery(PakiArticolo.class);
		Root<PakiArticolo> member = criteria.from(PakiArticolo.class);
		Predicate condizioneRiferimento = cb.equal(member.get("nrDispo"), json.getRiferimento());
		Predicate condizioneRiga = cb.equal(member.get("rigaPacki"), json.getRiga());
		criteria.select(member).where(cb.and(condizioneRiferimento, condizioneRiga));
		List<PakiArticolo> list = em.createQuery(criteria).getResultList();
		PakiArticolo dettaglio = list.size() == 1 ? list.get(0) : null;
		if (dettaglio != null) {
			carico = em.find(PakiTesta.class, carico.getIdTestaPaki());
			//In teoria non dovrebbe mai succedere questo!
			if (carico == null)
				throw new CustomException("Il riferimento indicato per il carico non è valido. (" + json.getRiferimento() + ")");
			int differenza = dettaglio.getQtaPaki();
			int totale = carico.getQtaTotAto() - differenza;
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				carico.setQtaTotAto(totale);
				em.merge(carico);
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
			throw new CustomException("La riga di carico indicata non esiste.");
		}
		return json;
	}

	@Override
	public PakiTesta deserializzaIngresso(CaricoJSON json) {
		PakiTesta carico = new PakiTesta();
		if (json != null) {
			IngressoJSON ingresso = json.getIngresso();
			//Controllo sul fornitore
			Fornitori fornitore = daoFornitore.trovaDaCodice(ingresso.getFornitore());
			if (fornitore != null) {
				carico.setCodFornitore(fornitore.getCodiceFornitore());
				carico.setIdFornitore(fornitore.getIdFornitore());
				carico.setRagSocFor(fornitore.getRagSoc());
			} else {
				throw new CustomException("Il fornitore indicato non esiste. ( " + ingresso.getFornitore() + " )");
			}
			//Altre info sul carico
			Date dataArrivo = ingresso.getDataArrivo() != null ? ingresso.getDataArrivo() : new Date();
			carico.setDataArrivo(new Timestamp(dataArrivo.getTime()));
			carico.setNrPaki(ingresso.getRiferimentoCliente());
			carico.setTipodocumento(ingresso.getTipo());
			carico.setQtaTotAto(ingresso.getPezziStimati());
			carico.setNote(ingresso.getNote());
			//Campi documento - Non vengono passati in update.
			DocumentoJSON documento = json.getDocumento();
			if (documento != null) {
				carico.setDataPaki(new Timestamp(documento.getData().getTime()));
				carico.setNrDocInterno(documento.getRiferimento());
				carico.setTipoDoc(documento.getTipo());
			}
		}
		return carico;
	}
	
	private Magazzini trovaMagazzinoDaCodifica(String codifica) {
		// Controllo se l'ho già trovato prima, se non c'è lo cerco.
		if (!mappaMagazzini.containsKey(codifica)) {
			Magazzini magazzino = daoMagazzini.trovaDaCodificaCliente(codifica);
			mappaMagazzini.put(codifica, magazzino);
		}
		Magazzini magazzino = mappaMagazzini.get(codifica);
		return magazzino;
	}

	@Override
	public List<PakiArticolo> deserializzaDettagli(CaricoJSON json) {
		List<PakiArticolo> dettagli = new LinkedList<>();
		if (json != null) {
			int idCarico = json.getIngresso().getId();
			String riferimento = json.getIngresso().getRiferimentoCliente();
			List<IngressoDettaglioJSON> list = json.getDettagli();
			for (IngressoDettaglioJSON item : list) {
				PakiArticolo dettaglio = deserializzaDettaglio(item);
				dettaglio.setIdPakiTesta(idCarico);
				dettaglio.setNrOrdineFor(riferimento);
				dettagli.add(dettaglio);
			}
		}
		return dettagli;
	}

	public PakiArticolo deserializzaDettaglio(IngressoDettaglioJSON item) {
		Articoli prodotto = daoProdotti.trovaDaSKU(item.getProdotto());
		if (prodotto == null)
			throw new CustomException("L'articolo indicato non esiste. ( " + item.getProdotto() + " )");
		Magazzini magazzino = trovaMagazzinoDaCodifica(item.getMagazzino());
		if (magazzino == null)
			throw new CustomException("Il magazzino indicato non esiste. ( " + item.getMagazzino() + " )");
		PakiArticolo dettaglio = new PakiArticolo();
		dettaglio.setRigaPacki(item.getRiga());
		dettaglio.setBarcodeCollo(item.getCollo());
		dettaglio.setCodBarre(prodotto.getIdUniArticolo());
		dettaglio.setCodUnicoArt(prodotto.getIdUniArticolo());
		dettaglio.setCodArtStr(prodotto.getCodArtStr());
		dettaglio.setMagazzino(item.getMagazzino());
		//dettaglio.setMagazzinoltc(item.getMagazzino());
		dettaglio.setMagazzinoltc(magazzino.getCodiceMag());
		dettaglio.setQtaPaki(item.getQuantitaPrevista());
		dettaglio.setNrDispo(item.getNote());
		dettaglio.setMadeIn(item.getMadeIn());
		return dettaglio;
	}

	@Override
	public CaricoJSON serializzaCarico(PakiTesta carico, List<PakiArticolo> dettagli) {
		CaricoJSON json = new CaricoJSON();
		// Dettagli: controllo se mi sono stati passati dettagli ed
		// eventualmente li aggiungo
		if (dettagli != null) {
			List<IngressoDettaglioJSON> list = new LinkedList<>();
			for (PakiArticolo dettaglio : dettagli) {
				IngressoDettaglioJSON item = serializzaDettaglio(dettaglio);
				list.add(item);
			}
			json.setDettagli(list);
		}
		// Testata e Documento
		if (carico != null) {
			IngressoJSON ingresso = serializzaIngresso(carico);
			json.setIngresso(ingresso);
			DocumentoJSON documento = serializzaDocumento(carico);
			json.setDocumento(documento);
		}
		return json;
	}

	private DocumentoJSON serializzaDocumento(PakiTesta carico) {
		DocumentoJSON documento = new DocumentoJSON();
		documento.setData(carico.getDataPaki());
		documento.setRiferimento(carico.getNrDocInterno());
		documento.setTipo(carico.getTipoDoc());
		return documento;
	}

	@Override
	public IngressoJSON serializzaIngresso(PakiTesta carico) {
		IngressoJSON ingresso = new IngressoJSON();
		ingresso.setDataArrivo(carico.getDataArrivo());
		ingresso.setFornitore(carico.getCodFornitore());
		ingresso.setId(carico.getIdTestaPaki());
		ingresso.setPezziLetti(carico.getQtaTotAre());
		ingresso.setPezziStimati(carico.getQtaTotAto());
		ingresso.setRiferimentoCliente(carico.getNrPaki());
		ingresso.setTipo(carico.getTipodocumento());
		ingresso.setStato(carico.getStato());
		ingresso.setNote(carico.getNote());
		return ingresso;
	}

	@Override
	public IngressoDettaglioJSON serializzaDettaglio(PakiArticolo dettaglio) {
		IngressoDettaglioJSON item = new IngressoDettaglioJSON();
		item.setRiferimento(dettaglio.getNrOrdineFor());
		item.setRiga(dettaglio.getRigaPacki());
		item.setCollo(dettaglio.getBarcodeCollo());
		//item.setMagazzino(trovaCodificaMagazzino(dettaglio.getMagazzino()));
		item.setMagazzino(dettaglio.getMagazzino());
		item.setProdotto(dettaglio.getCodArtStr());
		item.setQuantitaVerificata(dettaglio.getQtaVerificata());
		item.setQuantitaPrevista(dettaglio.getQtaPaki());
		item.setNote(dettaglio.getNrDispo());
		item.setMadeIn(dettaglio.getMadeIn());
		//Aggiungo i seriali, se presenti
		List<String> seriali = dettaglio.getSeriali();
		if (seriali != null && !seriali.isEmpty()) {
			item.setSeriali(seriali);
		}
		return item;
	}

}
