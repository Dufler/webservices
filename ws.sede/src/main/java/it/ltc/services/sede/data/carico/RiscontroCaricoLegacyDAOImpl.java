package it.ltc.services.sede.data.carico;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;

import it.ltc.database.dao.Dao;
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
import it.ltc.services.sede.model.carico.ColloInRiscontroJSON;
import it.ltc.services.sede.model.carico.ProdottoInRiscontroJSON;

public class RiscontroCaricoLegacyDAOImpl extends Dao implements RiscontroCaricoDAO {
	
	private static final Logger logger = Logger.getLogger("RiscontroCaricoLegacyDAOImpl");
	
	private final PakiTestaDao daoTestata;
	private final PakiArticoloDao daoRighe;
	private final ArticoliDao daoArticoli;
	private final ColliCaricoDao daoColliCarico;
	private final ColliPackDao daoColliPack;
	
	public RiscontroCaricoLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoTestata = new PakiTestaDao(persistenceUnit);
		daoRighe = new PakiArticoloDao(persistenceUnit);
		daoArticoli = new ArticoliDao(persistenceUnit);
		daoColliCarico = new ColliCaricoDao(persistenceUnit);
		daoColliPack = new ColliPackDao(persistenceUnit);
	}

	@Override
	public boolean cambiaStatoCarico(int idCarico, StatoCarico stato) {
		boolean update;
		PakiTesta testata = daoTestata.trovaDaID(idCarico);
		if (testata == null) {
			throw new CustomException("Nessun carico trovato con ID: " + idCarico);
		} else {
			testata.setStato(stato.name());
			PakiTesta entity = daoTestata.aggiorna(testata);
			update = entity != null;
		}
		return update;
	}
	
	protected PakiTesta trovaEControllaCarico(int idCarico) {
		PakiTesta testata = daoTestata.trovaDaID(idCarico);
		if (testata == null)
			throw new CustomException("Nessun carico trovato con ID: " + idCarico);
		else if (!testata.getStato().equals(StatoCarico.IN_LAVORAZIONE.getNome()))
			throw new CustomException("Il carico specificato non e' lavorabile. (Stato: " + testata.getStato() + ")");
		return testata;
	}
	
	protected ColliCarico creaNuovoCollo(ColloInRiscontroJSON collo) {
		ColliCarico nuovoCollo = new ColliCarico();
		nuovoCollo.setId_Box(collo.getBarcodeCliente());
		nuovoCollo.setIdDocumento(collo.getCarico());
		nuovoCollo.setMagazzino(collo.getMagazzino());
		nuovoCollo.setNrCollo(daoColliCarico.getProgressivoNrCollo());
		return nuovoCollo;
	}
	
	protected PakiArticolo creaNuovaRiga(PakiTesta testata, Articoli articolo, String magazzino, int numeroRiga, int quantita) {
		PakiArticolo nuovaRiga = new PakiArticolo();
		nuovaRiga.setIdPakiTesta(testata.getIdTestaPaki());
		nuovaRiga.setNrDispo(testata.getNrPaki());
		nuovaRiga.setRigaPacki(numeroRiga);
		nuovaRiga.setCodBarre(articolo.getIdUniArticolo());
		nuovaRiga.setCodUnicoArt(articolo.getIdUniArticolo());
		nuovaRiga.setCodArtStr(articolo.getCodArtStr());
		nuovaRiga.setMagazzino(magazzino); //FIXME: Questo forse dovrebbe essere il magazzino del cliente.
		nuovaRiga.setMagazzinoltc(magazzino);
		nuovaRiga.setQtaPaki(0);
		nuovaRiga.setQtaVerificata(quantita);
		nuovaRiga.setNrDispo(testata.getNrPaki());
		nuovaRiga.setMadeIn("");
		return nuovaRiga;
	}
	
	protected ColliPack creaColliPack(PakiTesta testata, PakiArticolo riga, Articoli articolo, String magazzino, int quantitaDaAggiungere) {
		ColliPack dettaglioNuovoCollo = new ColliPack();
		//Inserisco le informazioni necessarie
		dettaglioNuovoCollo.setIdTestaPaki(testata.getIdTestaPaki());
		dettaglioNuovoCollo.setFlagimp("N");
		dettaglioNuovoCollo.setCodArtStr(riga.getCodArtStr());
		dettaglioNuovoCollo.setCodiceArticolo(riga.getCodUnicoArt());
		dettaglioNuovoCollo.setIdPakiarticolo(riga.getIdPakiArticolo());
		dettaglioNuovoCollo.setMagazzino(magazzino); //Lo prendo da cosa è stato verificato dall'operatore
		dettaglioNuovoCollo.setQta(quantitaDaAggiungere);
		dettaglioNuovoCollo.setQtaimpegnata(0);
		dettaglioNuovoCollo.setTaglia(articolo.getTaglia());
		dettaglioNuovoCollo.setDescrizione(articolo.getDescrizione());
		return dettaglioNuovoCollo;
	}
	
	protected void inserisciContenutoCollo(PakiTesta testata, List<PakiArticolo> righe, Set<PakiArticolo> righeDaAggiornare, Set<PakiArticolo> righeDaInserire, List<ColliPack> dettagliNuovoCollo, ColloInRiscontroJSON collo) {
		//Verifico che ogni prodotto esista e sia presente nella packing list. Nel caso in cui non sia presente vedo se è consentito andare in eccedenza.
		for (ProdottoInRiscontroJSON prodotto : collo.getProdotti()) {
			//Memorizzo le quantità necessarie.
			int quantitaDaAggiungere = prodotto.getQuantita();
			int quantitaIniziale = prodotto.getQuantita(); //Mi serve di memorizzarla, alla fine la uso per aggiornare la qt. totale del carico e inserire un nuovo collipack
			//Verifico e recupero le informazioni sull'articolo interessato.
			Articoli articolo = daoArticoli.trovaDaID(prodotto.getId());
			if (articolo == null)
				throw new CustomException("Nessun articolo trovato con ID: " + prodotto.getId());
			//Scandisco tutte le righe d'ordine alla ricerca di quelle che riguardano questo articolo.
			for (PakiArticolo riga : righe) {
				//Verifico la quantità rimasta da aggiungere al riscontro, se è inferiore ad 1 procedo con la prossimo prodotto.
				if (quantitaDaAggiungere < 1)
					break;
				//Controllo se nella riga gestisco questo articolo
				if (riga.getCodUnicoArt().equals(articolo.getIdUniArticolo())) {
					//Calcolo la quantità ancora registrabile come: Qt. Dichiarata - Qt. precedentemente riscontrata e caricata a magazzino - Qt. letta finora nella sessione.
					int quantitaVerificata = riga.getQtaVerificata();
					int quantitaDisponibile = riga.getQtaPaki() - riga.getQtaPreDoc() - quantitaVerificata;
					//Se è sufficiente marco questa riga di riscontro come ok e la riga del carico come da aggiornare.
					if (quantitaDisponibile > 0) {
						int quantitaDaInserire = 0;
						//Controllo quanto spazio rimane
						if (quantitaDisponibile >= quantitaDaAggiungere) {
							quantitaVerificata += quantitaDaAggiungere;
							quantitaDaInserire = quantitaDaAggiungere;
							quantitaDaAggiungere = 0;
						} else {
							quantitaVerificata += quantitaDisponibile;
							quantitaDaInserire = quantitaDisponibile;
							quantitaDaAggiungere -= quantitaDisponibile;
						}
						//Aggiorno i totali nella riga del dichiarato e nella riga del collo da registrare.
						prodotto.setQuantita(quantitaDaAggiungere);
						riga.setQtaVerificata(quantitaVerificata);
						//La segno come da aggiornare
						righeDaAggiornare.add(riga);
						//Infine creo il collipack o gli N collipack corrispondenti.
						if (prodotto.getSeriali() != null && !prodotto.getSeriali().isEmpty()) {
							for (int index = 0; index < quantitaDaInserire; index++) {
								ColliPack dettaglioNuovoCollo = creaColliPack(testata, riga, articolo, collo.getMagazzino(), 1);
								//dettaglioNuovoCollo.setSeriale(prodotto.getSeriali().remove(0)); //Rimuovo il primo ogni volta, è stato fatto nel caso in cui venga distribuito su più pakiarticolo diversi.
								dettagliNuovoCollo.add(dettaglioNuovoCollo);
							}
						} else {
							ColliPack dettaglioNuovoCollo = creaColliPack(testata, riga, articolo, collo.getMagazzino(), quantitaDaInserire);
							dettagliNuovoCollo.add(dettaglioNuovoCollo);
						}
					}
				}
			}
			//Controllo se, dopo che ho scandito tutte le righe del dichiarato, ho ancora del prodotto nel collo che ecceda le quantità.
			if (quantitaDaAggiungere > 0) {
				//Controllo se è possibile eccedere: se si può inserisco una nuova riga nel dichiarato altrimenti lancio un'eccezione.
				if (testata.getAbilitaEccedenze().equals("SI")) {
					//Inserisco le info sulla nuova riga
					int numeroRiga = righe.size() + righeDaInserire.size();
					PakiArticolo nuovaRiga = creaNuovaRiga(testata, articolo, collo.getMagazzino(), numeroRiga, prodotto.getQuantita());
					righeDaInserire.add(nuovaRiga);
				} else {
					throw new CustomException("Il carico non prevede la possibilità di andare in eccedenza. Prodotto in eccesso: '" + articolo.getCodArtStr() + "', ID: " + prodotto.getId());
				}
			}
			//A meno che non sia stata lanciata un eccezione aumento la quantità riscontrata totale del carico
			testata.setQtaTotAre(testata.getQtaTotAre() + quantitaIniziale);
			//Riporto la quantità del prodotto a quella originale così la restituisco correttamente.
			prodotto.setQuantita(quantitaIniziale);
		}
	}
	
	protected void rimuoviContenutoCollo(PakiTesta testata, ColliCarico colloCarico, List<ColliPack> prodotti, HashMap<Integer, PakiArticolo> righe) {
		//Per ogni prodotto trovato vado ad aggiustare le righe
		for (ColliPack prodotto : prodotti) {
			//Se non ho ancora recuperato il paki articolo corrispondente lo trovo.
			int idRiga = prodotto.getIdPakiarticolo();
			if (!righe.containsKey(idRiga)) {
				PakiArticolo riga = daoRighe.trovaDaID(idRiga);
				if (riga == null) throw new CustomException("Errore fatale: non ho trovato la riga del dichiarato corrispondente al collo! (ID: " + idRiga + ")");
				righe.put(idRiga, riga);
			}
			PakiArticolo riga = righe.get(idRiga);
			int quantitaVerificata = riga.getQtaVerificata() - prodotto.getQta();
			riga.setQtaVerificata(quantitaVerificata);
			int quantitaTotale = testata.getQtaTotAre() - prodotto.getQta();
			testata.setQtaTotAre(quantitaTotale);
		}
	}
	
	@Override
	public ColloInRiscontroJSON nuovoCollo(ColloInRiscontroJSON collo) {
		//Trovo il carico e le righe corrispondenti alla merce del collo.
		PakiTesta testata = trovaEControllaCarico(collo.getCarico());
		List<PakiArticolo> righe = daoRighe.trovaRigheDaCarico(collo.getCarico());
		Set<PakiArticolo> righeDaAggiornare = new HashSet<>();
		Set<PakiArticolo> righeDaInserire = new HashSet<>(); 
		//Creo una base per il collicarico e i collipack da inserire
		ColliCarico nuovoCollo = creaNuovoCollo(collo);
		List<ColliPack> dettagliNuovoCollo = new LinkedList<>();
		//Verifico e inserisco tutto.
		inserisciContenutoCollo(testata, righe, righeDaAggiornare, righeDaInserire, dettagliNuovoCollo, collo);
		//Se le verifiche sono andate tutte a buon fine aggiorno la testata, le righe, inserisco un nuovo collicarico e nuovi collipack.
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.persist(nuovoCollo);
			for (ColliPack dettaglioNuovoCollo : dettagliNuovoCollo) {
				dettaglioNuovoCollo.setNrIdColloPk(nuovoCollo.getKeyColloCar());
				em.persist(dettaglioNuovoCollo);
			}
			em.merge(testata); //Devo recuperarla dal context prima?
			for (PakiArticolo riga : righeDaAggiornare) {
				em.merge(riga);
			}
			for (PakiArticolo riga : righeDaInserire) {
				em.persist(riga);
			}
			t.commit();
		} catch (Exception e) {
			collo = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		//Se tutto è andato bene valorizzo il JSON con le info sul collicarico appena creato.
		if (collo != null) {
			collo.setEtichetta(nuovoCollo.getKeyColloCar());
			collo.setId(nuovoCollo.getIdCollo());
		}
		return collo;
	}

	@Override
	public ColloInRiscontroJSON aggiornaCollo(ColloInRiscontroJSON collo) {
		//Trovo il carico, le righe, il collicarico e tutti i collipack corrispondenti, se qualcosa non esiste lancio un'eccezione
		PakiTesta testata = trovaEControllaCarico(collo.getCarico());
		ColliCarico colloCarico = daoColliCarico.trovaDaID(collo.getId());
		if (colloCarico == null)
			throw new CustomException("Nessun collo trovato con ID: " + collo.getId());
		HashMap<Integer, PakiArticolo> righe = new HashMap<>();
		List<ColliPack> prodotti = daoColliPack.trovaProdottiNelCollo(colloCarico.getKeyColloCar());
		//Rimuovo l'attuale contenuto del collo
		rimuoviContenutoCollo(testata, colloCarico, prodotti, righe);
		//Inserisco ciò che mi è stato passato
		List<PakiArticolo> righeCarico = daoRighe.trovaRigheDaCarico(collo.getCarico());
		Set<PakiArticolo> righeDaAggiornare = new HashSet<>();
		Set<PakiArticolo> righeDaInserire = new HashSet<>(); 
		List<ColliPack> dettagliNuovoCollo = new LinkedList<>();
		righeDaAggiornare.addAll(righe.values());
		inserisciContenutoCollo(testata, righeCarico, righeDaAggiornare, righeDaInserire, dettagliNuovoCollo, collo);
		//Salvo le modifiche
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			for (ColliPack prodotto : prodotti) {
				em.remove(em.contains(prodotto) ? prodotto : em.find(ColliPack.class, prodotto.getIdColliPack()));
			}
			for (ColliPack dettaglioNuovoCollo : dettagliNuovoCollo) {
				dettaglioNuovoCollo.setNrIdColloPk(colloCarico.getKeyColloCar());
				em.persist(dettaglioNuovoCollo);
			}
			em.merge(testata);
			for (PakiArticolo riga : righeDaAggiornare) {
				if (riga.getQtaPaki() == 0 && riga.getQtaVerificata() == 0) { //Se era una riga aggiunta fuori dal dichiarato la elimino.
					em.remove(em.contains(riga) ? riga : em.find(PakiArticolo.class, riga.getIdPakiArticolo()));
				} else {
					em.merge(riga);
				}
			}
			for (PakiArticolo riga : righeDaInserire) {
				em.persist(riga);
			}
			t.commit();
		} catch (Exception e) {
			collo = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return collo;
	}

	@Override
	public ColloInRiscontroJSON eliminaCollo(ColloInRiscontroJSON collo) {
		//Trovo il carico, le righe, il collicarico e tutti i collipack corrispondenti, se qualcosa non esiste lancio un'eccezione
		PakiTesta testata = trovaEControllaCarico(collo.getCarico());
		ColliCarico colloCarico = daoColliCarico.trovaDaID(collo.getId());
		if (colloCarico == null)
			throw new CustomException("Nessun collo trovato con ID: " + collo.getId());
		HashMap<Integer, PakiArticolo> righe = new HashMap<>();
		List<ColliPack> prodotti = daoColliPack.trovaProdottiNelCollo(colloCarico.getKeyColloCar());
		rimuoviContenutoCollo(testata, colloCarico, prodotti, righe);
		//Se le verifiche sono andate tutte a buon fine aggiorno la testata, le righe, elimino il collicarico e i collipack.
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(em.contains(colloCarico) ? colloCarico : em.find(ColliCarico.class, colloCarico.getIdCollo()));
			for (ColliPack prodotto : prodotti) {
				em.remove(em.contains(prodotto) ? prodotto : em.find(ColliPack.class, prodotto.getIdColliPack()));
			}
			em.merge(testata); //Devo recuperarla dal context prima?
			for (PakiArticolo riga : righe.values()) {
				if (riga.getQtaPaki() == 0 && riga.getQtaVerificata() == 0) { //Se era una riga aggiunta fuori dal dichiarato la elimino.
					em.remove(em.contains(riga) ? riga : em.find(PakiArticolo.class, riga.getIdPakiArticolo()));
				} else {
					em.merge(riga);
				}
			}
			t.commit();
		} catch (Exception e) {
			collo = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return collo;
	}

}
