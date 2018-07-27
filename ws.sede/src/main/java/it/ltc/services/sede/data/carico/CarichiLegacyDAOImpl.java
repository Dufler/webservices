package it.ltc.services.sede.data.carico;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;

import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.FornitoreDao;
import it.ltc.database.dao.legacy.MagaMovDao;
import it.ltc.database.dao.legacy.MagaSdDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.model.shared.dao.ICaricoDao;
import it.ltc.model.shared.json.interno.CaricoTestata;
import it.ltc.services.custom.exception.CustomException;

public class CarichiLegacyDAOImpl extends PakiTestaDao implements ICaricoDao {
	
	private static final Logger logger = Logger.getLogger("CarichiLegacyDAOImpl");
	
	protected final FornitoreDao daoFornitori;
	protected final ColliPackDao daoProdotti;
	protected final MagaSdDao daoSaldi;
	protected final MagaMovDao daoMovimenti;

	public CarichiLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoFornitori = new FornitoreDao(persistenceUnit);
		daoProdotti = new ColliPackDao(persistenceUnit);
		daoSaldi = new MagaSdDao(persistenceUnit);
		daoMovimenti = new MagaMovDao(persistenceUnit);
	}

	@Override
	public CaricoTestata inserisci(CaricoTestata json) {
		PakiTesta testata = deserializza(json);
		PakiTesta entity = insert(testata);
		return serializza(entity);
	}

	@Override
	public CaricoTestata aggiorna(CaricoTestata json) {
		PakiTesta testata = deserializza(json);
		PakiTesta entity = update(testata, testata.getIdTestaPaki());
		return serializza(entity);
	}

	@Override
	public CaricoTestata elimina(CaricoTestata json) {
		PakiTesta testata = deserializza(json);
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
			CondizioneWhere condizione = new CondizioneWhere("nrPaki", riferimento, Operatore.LIKE);
			condizioni.add(condizione);
		}
		//Fornitore 
		int fornitore = filtro.getFornitore();
		if (fornitore > 0) {
			CondizioneWhere condizione = new CondizioneWhere("idFornitore", fornitore);
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
	
	protected PakiTesta deserializza(CaricoTestata json) {
		PakiTesta testata;
		if (json != null) {
			testata = new PakiTesta();
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
	
	public MagaMov getNuovoMovimento(PakiTesta carico, MagaSd saldo, String codiceUnivocoArticolo, String magazzino, int quantità) {
		MagaMov movimento = new MagaMov();
		movimento.setCausale("CPK");
		movimento.setIdUniArticolo(codiceUnivocoArticolo);
		movimento.setDocData(carico.getDataPaki());
		movimento.setDocCat("E");
		movimento.setDocTipo("CAR");
		movimento.setDocNr(carico.getNrPaki());
		movimento.setDocNote("");
		movimento.setQuantita(quantità);
		movimento.setSegno("+");
		movimento.setTipo("CR");
		movimento.setSegnoEsi("+");
		movimento.setSegnoImp("N");
		movimento.setSegnoDis("+");
		movimento.setIncTotali("SI");
		movimento.setCancellato("NO");
		movimento.setCodMaga(magazzino);
		movimento.setEsistenzamov(saldo.getEsistenza());
		movimento.setDisponibilemov(saldo.getDisponibile());
		movimento.setImpegnatomov(saldo.getImpegnato());
		return movimento;
	}

	@Override
	public CaricoTestata modificaStato(CaricoTestata json) {
		PakiTesta entity = trovaDaID(json.getId());
		if (entity != null) {
			String statoPrecedente = entity.getStato();
			String statoNuovo = json.getStato();
			Timestamp stamp = new Timestamp(new Date().getTime());
			entity.setStato(statoNuovo);
			//Considerazioni ulteriori sui movimenti
			List<MagaSd> saldiDaAggiornare = new LinkedList<>();
			List<MagaSd> saldiDaInserire = new LinkedList<>();
			List<MagaMov> movimentiDaInserire = new LinkedList<>();
			List<MagaMov> movimentiDaEliminare = new LinkedList<>();
			List<ColliPack> prodottiDisponibili = new LinkedList<>();
			if (statoNuovo.equals("ARRIVATO") && !statoPrecedente.equals("ARRIVATO")) {
				entity.setDataArrivo(stamp);
			} else if (statoNuovo.equals("IN_LAVORAZIONE") && !statoPrecedente.equals("IN_LAVORAZIONE")) {
				entity.setDataOraInizio(stamp);
			} else if (statoNuovo.equals("CHIUSO") && !statoPrecedente.equals("CHIUSO")) {
				entity.setDataOraGenerazione(stamp);
				entity.setGeneratoMov("SI");
				entity.setGeneratoFile("SI");
				HashMap<String, Integer> mappaProdotti = new HashMap<>();
				//Trovo i prodotti nel carico da rendere disponibili
				List<ColliPack> prodottiCarico = daoProdotti.trovaProdottiNelCarico(entity.getIdTestaPaki());
				for (ColliPack prodotto : prodottiCarico) {
					prodotto.setFlagtc(1);
					prodottiDisponibili.add(prodotto);
					String key = prodotto.getCodiceArticolo() + "@" + prodotto.getMagazzino();
					int disponibile = mappaProdotti.containsKey(key) ? mappaProdotti.get(key) : 0;
					disponibile += prodotto.getQta();
					mappaProdotti.put(key, disponibile);
				}
				//Aggiorno i saldi e inserisco i movimenti
				for (String key : mappaProdotti.keySet()) {
					int quantità = mappaProdotti.get(key);
					String[] dati = key.split("@");
					if (dati.length != 2)
						throw new CustomException("Impossibile dividere codice univoco articolo e magazzino con il simbolo @");
					MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(dati[0], dati[1]);
					if (saldo == null) {
						saldo = new MagaSd();
						saldo.setIdUniArticolo(dati[0]);
						saldo.setCodMaga(dati[1]);
						saldo.setDisponibile(quantità);
						saldo.setEsistenza(quantità);
						saldo.setImpegnato(0);
						saldiDaInserire.add(saldo);
					} else {
						saldo.setDisponibile(saldo.getDisponibile() + quantità);
						saldo.setEsistenza(saldo.getEsistenza() + quantità);
						saldiDaAggiornare.add(saldo);
					}
					MagaMov movimento = getNuovoMovimento(entity, saldo, dati[0], dati[1], quantità);
					movimentiDaInserire.add(movimento);
				}				
			} else if (!statoNuovo.equals("CHIUSO") && statoPrecedente.equals("CHIUSO")) {
				entity.setGeneratoMov("NO");
				entity.setGeneratoFile("NO");
				HashMap<String, Integer> mappaProdotti = new HashMap<>();
				//Trovo i prodotti nel carico da rendere non disponibili
				List<ColliPack> prodottiCarico = daoProdotti.trovaProdottiNelCarico(entity.getIdTestaPaki());
				for (ColliPack prodotto : prodottiCarico) {
					prodotto.setFlagtc(0);
					prodottiDisponibili.add(prodotto);
					String key = prodotto.getCodiceArticolo() + "@" + prodotto.getMagazzino();
					int disponibile = mappaProdotti.containsKey(key) ? mappaProdotti.get(key) : 0;
					disponibile += prodotto.getQta();
					mappaProdotti.put(key, disponibile);
				}
				//Aggiorno i saldi e annullo i movimenti
				for (String key : mappaProdotti.keySet()) {
					int quantità = mappaProdotti.get(key);
					String[] dati = key.split("@");
					if (dati.length != 2)
						throw new CustomException("Impossibile dividere codice univoco articolo e magazzino con il simbolo @");
					MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(dati[0], dati[1]);
					if (saldo != null) {
						saldo.setDisponibile(saldo.getDisponibile() - quantità);
						saldo.setEsistenza(saldo.getEsistenza() - quantità);
						saldiDaAggiornare.add(saldo);
					}
				}
				movimentiDaEliminare = daoMovimenti.trovaMovimentiCarico(entity.getNrPaki());
			}
			//Vado in scrittura
			EntityManager em = getManager();
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				//Aggiorno lo stato del carico
				em.merge(entity);
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
				for (MagaMov movimento : movimentiDaEliminare) {
					em.remove(em.contains(movimento) ? movimento : em.find(MagaMov.class, movimento.getIdMagaMov()));
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
		}
		return serializza(entity);
	}

}
