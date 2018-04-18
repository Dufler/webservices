package it.ltc.services.clienti.data.carico;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.services.clienti.data.fornitore.FornitoreLegacyDAOImpl;
import it.ltc.services.clienti.data.magazzino.MagazzinoLegacyDAOImpl;
import it.ltc.services.clienti.data.prodotto.ProdottoLegacyDAOImpl;
import it.ltc.services.clienti.model.prodotto.CaricoJSON;
import it.ltc.services.clienti.model.prodotto.DocumentoJSON;
import it.ltc.services.clienti.model.prodotto.IngressoDettaglioJSON;
import it.ltc.services.clienti.model.prodotto.IngressoJSON;
import it.ltc.services.clienti.model.prodotto.ModificaCaricoJSON;
import it.ltc.services.clienti.model.prodotto.ModificaCaricoJSON.LavorazioneSeriali;
import it.ltc.services.clienti.model.prodotto.ModificaCaricoJSON.Riscontro;
import it.ltc.services.clienti.model.prodotto.ModificaCaricoJSON.StatoCarico;
import it.ltc.services.custom.exception.CustomException;

public class CaricoLegacyDAOImpl extends Dao implements CaricoDAO<PakiTesta, PakiArticolo> {

	private static final Logger logger = Logger.getLogger("CaricoLegacyDAOImpl");

	private final FornitoreLegacyDAOImpl daoFornitore;
	private final ProdottoLegacyDAOImpl daoProdotti;
	private final MagazzinoLegacyDAOImpl daoMagazzini;
	private final Set<String> magazzini;
	private final HashMap<String, Magazzini> mappaMagazzini;

	public CaricoLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoFornitore = new FornitoreLegacyDAOImpl(persistenceUnit);
		daoProdotti = new ProdottoLegacyDAOImpl(persistenceUnit);
		daoMagazzini = new MagazzinoLegacyDAOImpl(persistenceUnit);
		magazzini = new HashSet<>();
		mappaMagazzini = new HashMap<>();
	}

	@Override
	public CaricoJSON trovaDaID(int idCarico, boolean dettagliato) {
		EntityManager em = getManager();
		PakiTesta carico = em.find(PakiTesta.class, idCarico);
		List<PakiArticolo> dettagli;
		if (dettagliato) {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PakiArticolo> criteria = cb.createQuery(PakiArticolo.class);
			Root<PakiArticolo> member = criteria.from(PakiArticolo.class);
			criteria.select(member).where(cb.equal(member.get("idPakiTesta"), idCarico));
			dettagli = em.createQuery(criteria).getResultList();
			em.close();
			//Recupero i seriali e ne faccio una mappa
			List<ColliPack> seriali = trovaSeriali(carico.getIdTestaPaki());
			HashMap<Integer, List<String>> mappaSerialiPerRiga = new HashMap<>();
			for (ColliPack seriale : seriali) {
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
			em.close();
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
				dettagli = trovaDettagli(carico.getIdTestaPaki());
				//Recupero i seriali e ne faccio una mappa
				List<ColliPack> seriali = trovaSeriali(carico.getIdTestaPaki());
				HashMap<Integer, List<String>> mappaSerialiPerRiga = new HashMap<>();
				for (ColliPack seriale : seriali) {
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

	public PakiTesta trovaDaRiferimento(String riferimento) {
		EntityManager em = getManager();
		// Cerco il carico tramite il riferimento
		CriteriaBuilder cb1 = em.getCriteriaBuilder();
		CriteriaQuery<PakiTesta> criteria1 = cb1.createQuery(PakiTesta.class);
		Root<PakiTesta> member = criteria1.from(PakiTesta.class);
		criteria1.select(member).where(cb1.equal(member.get("nrPaki"), riferimento));
		List<PakiTesta> list = em.createQuery(criteria1).setMaxResults(1).getResultList();
		em.close();
		PakiTesta carico = list.isEmpty() ? null : list.get(0);
		return carico;
	}

	public List<PakiArticolo> trovaDettagli(int idCarico) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PakiArticolo> criteria = cb.createQuery(PakiArticolo.class);
		Root<PakiArticolo> member = criteria.from(PakiArticolo.class);
		criteria.select(member).where(cb.equal(member.get("idPakiTesta"), idCarico));
		List<PakiArticolo> dettagli = em.createQuery(criteria).getResultList();
		em.close();
		return dettagli;
	}
	
	public List<ColliPack> trovaSeriali(int idCarico) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ColliPack> criteria = cb.createQuery(ColliPack.class);
		Root<ColliPack> member = criteria.from(ColliPack.class);
		criteria.select(member).where(cb.equal(member.get("idTestaPaki"), idCarico));
		List<ColliPack> dettagli = em.createQuery(criteria).getResultList();
		em.close();
		return dettagli;
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
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PakiArticolo> criteria = cb.createQuery(PakiArticolo.class);
		Root<PakiArticolo> member = criteria.from(PakiArticolo.class);
		criteria.select(member).where(cb.equal(member.get("idPakiTesta"), modifiche.getId()));
		List<PakiArticolo> dettagli = em.createQuery(criteria).getResultList();
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
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			for (PakiArticolo dettaglio : dettagli) {
				em.merge(dettaglio);
			}
			t.commit();
			logger.info("Quantita' lette aggiornate!");
		} catch (Exception e) {
			logger.error(e);
			update = -1;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return update;
	}
	
	/**
	 * Genera dei seriali univoci per ogni oggetto nel carico passato come argomento.
	 * @param idCarico l'ID del carico per cui generare i seriali.
	 * @return l'esito dell'operazione.
	 */
	private boolean generaSerialiPerCaricoDiTest(int idCarico) {
		boolean generazione;
		List<ColliPack> seriali = trovaSeriali(idCarico);
		if (seriali.isEmpty()) {
			//Preparo le informazioni necessarie alla generazione dei seriali
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
			DecimalFormat df = new DecimalFormat("000000000000000");
			double seriale = Double.parseDouble(sdf.format(now));
			//Trovo i dettagli per cui generare i seriali
			List<PakiArticolo> dettagli = trovaDettagli(idCarico);
			logger.info("Stanno per essere generati seriali per " + dettagli.size() + " righe.");
			for (PakiArticolo dettaglio : dettagli) {
				int quantita = dettaglio.getQtaVerificata();
				for (int i=0; i < quantita; i++) {
					String stringaSeriale = df.format(seriale);
					seriale += 1;
					ColliPack nuovoSeriale = new ColliPack();
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
				EntityManager em = getManager();
				EntityTransaction t = em.getTransaction();
				try {
					t.begin();
					for (ColliPack nuovoSeriale : seriali) {
						em.persist(nuovoSeriale);
					}
					t.commit();
					generazione = true;
					logger.info("Seriali randomici generati!");
				} catch (Exception e) {
					logger.error(e);
					generazione = false;
					if (t != null && t.isActive())
						t.rollback();
				} finally {
					em.close();
				}
			} else {
				generazione = false;
			}
		} else {
			generazione = false;
		}
		return generazione;
	}
	
	private boolean eliminaSerialiPerCaricoDiTest(int idCarico) {
		boolean delete;
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ColliPack> criteria = cb.createQuery(ColliPack.class);
		Root<ColliPack> member = criteria.from(ColliPack.class);
		criteria.select(member).where(cb.equal(member.get("idTestaPaki"), idCarico));
		List<ColliPack> seriali = em.createQuery(criteria).getResultList();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			for (ColliPack nuovoSeriale : seriali) {
				em.remove(nuovoSeriale);
			}
			t.commit();
			delete = true;
			logger.info("Seriali randomici generati!");
		} catch (Exception e) {
			logger.error(e);
			delete = false;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
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

	@Override
	public boolean inserisci(CaricoJSON json) {
		boolean insert;
		// Deserializzazione testata
		PakiTesta carico = deserializzaIngresso(json);
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
			insert = true;
			logger.info("Carico inserito!");
		} catch (Exception e) {
			logger.error(e);
			insert = false;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return insert;
	}
	
	@Override
	public boolean inserisciDettaglio(IngressoDettaglioJSON json) {
		boolean insert;
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
				insert = true;
			} catch (Exception e) {
				logger.error(e);
				insert = false;
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		} else {
			insert = false;
			em.close();
			throw new CustomException("La riga di carico indicata è già presente. (" + json.getRiga() + ")");
		}
		return insert;
	}

	@Override
	public boolean aggiorna(IngressoJSON json) {
		// Deserializzazione testata
		CaricoJSON c = new CaricoJSON();
		c.setIngresso(json);
		PakiTesta carico = deserializzaIngresso(c);
		// Controllo l'esistenza del riferimento al carico
		PakiTesta esistente = trovaDaRiferimento(carico.getNrPaki());
		if (esistente == null)
			throw new CustomException("Non esiste nessun carico con questo riferimento. (" + carico.getNrPaki() + ")");
		// Controllo lo stato, deve essere 'INSERITO'
		if (!"INSERITO".equals(esistente.getStato()))
			throw new CustomException("Il carico richiesto non è più modificabile.");

		// Update
		boolean update;
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
			update = true;
			logger.info("Carico modificato!");
		} catch (Exception e) {
			logger.error(e);
			update = false;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return update;
	}

	@Override
	public boolean aggiornaDettaglio(IngressoDettaglioJSON json) {
		boolean update;
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
				update = true;
			} catch (Exception e) {
				logger.error(e);
				update = false;
				if (t != null && t.isActive())
					t.rollback();
			}
		} else {
			update = false;
			em.close();
			throw new CustomException("La riga di carico indicata non esiste o non è univoca.");
		}
		return update;
	}

	@Override
	public boolean elimina(IngressoJSON json) {
		// Controllo l'esistenza del riferimento al carico
		PakiTesta esistente = trovaDaRiferimento(json.getRiferimentoCliente());
		if (esistente == null)
			throw new CustomException("Non esiste nessun carico con questo riferimento. (" + json.getRiferimentoCliente() + ")");
		// Controllo lo stato, deve essere 'INSERITO'
		if (!"INSERITO".equals(esistente.getStato()))
			throw new CustomException("Il carico richiesto non è più eliminabile.");
		// Delete
		boolean delete;
		EntityManager em = getManager();
		esistente = em.find(PakiTesta.class, esistente.getIdTestaPaki());
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(esistente);
			t.commit();
			delete = true;
			logger.info("Carico eliminato!");
		} catch (Exception e) {
			logger.error(e);
			delete = false;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return delete;
	}

	@Override
	public boolean eliminaDettaglio(IngressoDettaglioJSON json) {
		boolean delete;
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
				delete = true;
			} catch (Exception e) {
				logger.error(e);
				delete = false;
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		} else {
			delete = false;
			em.close();
			throw new CustomException("La riga di carico indicata non esiste.");
		}
		return delete;
	}

	@Override
	public PakiTesta deserializzaIngresso(CaricoJSON json) {
		PakiTesta carico = new PakiTesta();
		if (json != null) {
			IngressoJSON ingresso = json.getIngresso();
			
			Fornitori fornitore = getFornitore(ingresso.getFornitore());
			if (fornitore != null) {
				carico.setCodFornitore(fornitore.getCodiceFornitore());
				carico.setIdFornitore(fornitore.getIdFornitore());
				carico.setRagSocFor(fornitore.getRagSoc());
			} else {
				throw new CustomException("Il fornitore indicato non esiste. ( " + ingresso.getFornitore() + " )");
			}
			Date dataArrivo = ingresso.getDataArrivo() != null ? ingresso.getDataArrivo() : new Date();
			carico.setDataArrivo(new Timestamp(dataArrivo.getTime()));
			carico.setNrPaki(ingresso.getRiferimentoCliente());
			carico.setTipodocumento(ingresso.getTipo());
			carico.setQtaTotAto(ingresso.getPezziStimati());
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

	private Fornitori getFornitore(String codiceFornitore) {
		Fornitori fornitore = daoFornitore.findByCodice(codiceFornitore);
		return fornitore;
	}

	private Articoli getProdotto(String chiave) {
		Articoli prodotto = daoProdotti.findBySKU(chiave);
		return prodotto;
	}

	private boolean isMagazzinoEsistente(String codificaMagazzino) {
		// Controllo se l'ho già trovato prima, se non c'è lo cerco.
		if (!magazzini.contains(codificaMagazzino)) {
			Magazzini magazzino = daoMagazzini.findByCodificaCliente(codificaMagazzino);
			if (magazzino != null)
				magazzini.add(codificaMagazzino);
		}
		return magazzini.contains(codificaMagazzino);
	}
	
	private String trovaCodificaMagazzino(String codificaLTCMagazzino) {
		if (!mappaMagazzini.containsKey(codificaLTCMagazzino)) {
			Magazzini magazzino = daoMagazzini.findByCodiceLTC(codificaLTCMagazzino);
			if (magazzino != null)
				mappaMagazzini.put(codificaLTCMagazzino, magazzino);
		}
		Magazzini magazzino = mappaMagazzini.get(codificaLTCMagazzino);
		String codificaMagazzino = magazzino != null ? magazzino.getMagaCliente() : null;
		return codificaMagazzino;
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
		Articoli prodotto = getProdotto(item.getProdotto());
		if (prodotto == null)
			throw new CustomException("L'articolo indicato non esiste. ( " + item.getProdotto() + " )");
		if (!isMagazzinoEsistente(item.getMagazzino()))
			throw new CustomException("Il magazzino indicato non esiste. ( " + item.getMagazzino() + " )");
		PakiArticolo dettaglio = new PakiArticolo();
		dettaglio.setRigaPacki(item.getRiga());
		dettaglio.setBarcodeCollo(item.getCollo());
		dettaglio.setCodBarre(prodotto.getIdUniArticolo());
		dettaglio.setCodUnicoArt(prodotto.getIdUniArticolo());
		dettaglio.setCodArtStr(prodotto.getCodArtStr());
		dettaglio.setMagazzino(item.getMagazzino());
		dettaglio.setMagazzinoltc(item.getMagazzino());
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
		return ingresso;
	}

	@Override
	public IngressoDettaglioJSON serializzaDettaglio(PakiArticolo dettaglio) {
		IngressoDettaglioJSON item = new IngressoDettaglioJSON();
		item.setRiferimento(dettaglio.getNrOrdineFor());
		item.setRiga(dettaglio.getRigaPacki());
		item.setCollo(dettaglio.getBarcodeCollo());
		item.setMagazzino(trovaCodificaMagazzino(dettaglio.getMagazzino()));
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
