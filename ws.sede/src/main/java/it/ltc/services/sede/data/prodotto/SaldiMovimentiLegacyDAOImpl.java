package it.ltc.services.sede.data.prodotto;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;

import it.ltc.database.dao.Dao;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.MagaMovDao;
import it.ltc.database.dao.legacy.MagaSdDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.model.CausaliMovimento;
import it.ltc.model.shared.json.interno.MovimentoProdotto;
import it.ltc.model.shared.json.interno.SaldoProdotto;
import it.ltc.services.custom.exception.CustomException;

public class SaldiMovimentiLegacyDAOImpl extends Dao implements SaldiMovimentiDAO {
	
	private static final Logger logger = Logger.getLogger("SaldiMovimentiLegacyDAOImpl");
	
	protected final ArticoliDao daoArticoli;
	protected final MagaSdDao daoSaldi;
	protected final MagaMovDao daoMovimenti;
	
	protected String utente;
	
	public SaldiMovimentiLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoArticoli = new ArticoliDao(persistenceUnit);
		daoSaldi = new MagaSdDao(persistenceUnit);
		daoMovimenti = new MagaMovDao(persistenceUnit);
		daoMovimenti.setUtente(utente);
	}
	
	@Override
	public void setUtente(String utente) {
		this.utente = utente;		
	}

	@Override
	public List<MovimentoProdotto> trovaMovimenti(int idProdotto) {
		List<MovimentoProdotto> movimenti = new LinkedList<>();
		Articoli articolo = daoArticoli.trovaDaID(idProdotto);
		if (articolo != null) {
			List<MagaMov> entities = daoMovimenti.trovaMovimentiProdotto(articolo.getIdUniArticolo());
			for (MagaMov entity : entities) {
				MovimentoProdotto movimento = new MovimentoProdotto();
				movimento.setId(entity.getIdMagaMov());
				movimento.setDocumentoRiferimento(entity.getDocNr());
				movimento.setDataMovimento(entity.getDataMovMag());
				movimento.setDocumentoID(entity.getIDdocum());
				movimento.setIdProdotto(idProdotto);
				movimento.setSkuProdotto(articolo.getCodArtStr());
//				movimento.setCausale(entity.getCausale());
				movimento.setCausale(CausaliMovimento.getCausaleCorrispondente(entity.getSegnoEsi(), entity.getSegnoDis(), entity.getSegnoImp()).name());
				movimento.setCausaleLegacy(entity.getCausale());
				movimento.setMagazzino(entity.getCodMaga());
				movimento.setQuantita(entity.getQuantita());
				movimento.setNote(entity.getDocNote());
				movimenti.add(movimento);
			}
		} else {
			throw new CustomException("Articolo non esistente!");
		}
		return movimenti;
	}

	@Override
	public List<SaldoProdotto> trovaSaldi(int idProdotto) {
		List<SaldoProdotto> saldi = new LinkedList<>();
		Articoli articolo = daoArticoli.trovaDaID(idProdotto);
		if (articolo != null) {
			List<MagaSd> entities = daoSaldi.trovaTuttiPerArticolo(articolo.getIdUniArticolo());
			for (MagaSd entity : entities) {
				SaldoProdotto saldo = new SaldoProdotto();
				saldo.setIdProdotto(idProdotto);
				saldo.setSkuProdotto(articolo.getCodArtStr());
				saldo.setDisponibile(entity.getDisponibile());
				saldo.setImpegnato(entity.getImpegnato());
				saldo.setEsistente(entity.getEsistenza());
				saldo.setMagazzino(entity.getCodMaga());
				saldo.setTotaleEntrato(entity.getTotIn());
				saldo.setTotaleUscito(entity.getTotOut());
				saldi.add(saldo);
			}
		} else {
			throw new CustomException("Articolo non esistente!");
		}
		return saldi;
	}
	
	protected CausaliMovimento checkCausale(MovimentoProdotto movimento) {
		CausaliMovimento causale = null;
		try {
			causale = CausaliMovimento.valueOf(movimento.getCausale());
			if (causale == CausaliMovimento.ALTRO)
				throw new RuntimeException();
		} catch (Exception e) { throw new CustomException("La causale indicata per il movimento non è valida. (" + movimento.getCausale() + ")"); }
		return causale;
	}
	
	protected Articoli checkArticolo(MovimentoProdotto movimento) {
		Articoli articolo =  daoArticoli.trovaDaID(movimento.getIdProdotto());
		if (articolo == null)
			throw new CustomException("Articolo non trovato! (ID: " + movimento.getIdProdotto() + ")");
		return articolo;
	}
	
//	protected MagaMov getMovimento(MagaSd saldo, MovimentoProdotto datiMovimento, Articoli articolo, CausaliMovimento causale) {
//		MagaMov movimento = new MagaMov();
//		movimento.setCausale(causale.name());
//		movimento.setDocNr(datiMovimento.getDocumentoRiferimento());
//		movimento.setDocData(new Timestamp(datiMovimento.getDataMovimento().getTime()));
//		movimento.setIDdocum(datiMovimento.getDocumentoID());
//		movimento.setDocCat("_");
//		movimento.setDocNote(causale.getDescrizione());
//		movimento.setDocTipo("___");
//		movimento.setCodMaga(datiMovimento.getMagazzino());
//		movimento.setDisponibilemov(saldo.getDisponibile());
//		movimento.setEsistenzamov(saldo.getEsistenza());
//		movimento.setImpegnatomov(saldo.getImpegnato());
//		movimento.setSegno(causale.getSegnoEsistenza());
//		movimento.setSegnoDis(causale.getSegnoDisponibile());
//		movimento.setSegnoEsi(causale.getSegnoEsistenza());
//		movimento.setSegnoImp(causale.getSegnoImpegnato());
//		movimento.setIdUniArticolo(articolo.getIdUniArticolo());
//		movimento.setIncTotali(causale.getIncrementoTotali());
//		movimento.setTipo("__");
//		movimento.setQuantita(datiMovimento.getQuantita());
//		movimento.setUtente(utente);
//		return movimento;
//	}

	@Override
	public MovimentoProdotto inserisci(MovimentoProdotto movimento) {		
		CausaliMovimento causale = checkCausale(movimento);
		Articoli articolo = checkArticolo(movimento);
		//Recupero l'attuale saldo, se non c'è lo inizializzo e lo inserisco.
		MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(articolo.getIdUniArticolo(), movimento.getMagazzino());
		if (saldo == null) {
			saldo = new MagaSd();
			saldo.setCodMaga(movimento.getMagazzino());
			saldo.setIdUniArticolo(articolo.getIdUniArticolo());
		}
		saldo.setEsistenza(saldo.getEsistenza() + movimento.getQuantita() * causale.getMoltiplicatoreEsistenza());
		saldo.setDisponibile(saldo.getDisponibile() + movimento.getQuantita() * causale.getMoltiplicatoreDisponibile());
		saldo.setImpegnato(saldo.getImpegnato() + movimento.getQuantita() * causale.getMoltiplicatoreImpegnato());
		saldo.setTotIn(saldo.getTotIn() + movimento.getQuantita() * causale.getTotaleIn());
		saldo.setTotOut(saldo.getTotOut() + movimento.getQuantita() * causale.getTotaleOut());
		//Creo il movimento
//		MagaMov nuovoMovimento = getMovimento(saldo, movimento, articolo, causale);
		MagaMov nuovoMovimento = daoMovimenti.getNuovoMovimento(causale, movimento.getDocumentoRiferimento(), movimento.getDocumentoID(), movimento.getDataMovimento(), saldo, movimento.getQuantita(), movimento.getNote());
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			if (saldo.getIdMagaSd() == 0) {
				em.persist(saldo);
			} else {
				em.merge(saldo);
			}
			em.persist(nuovoMovimento);
			movimento.setId(nuovoMovimento.getIdMagaMov());
			t.commit();
		} catch (Exception e) {
			movimento = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return movimento;
	}

	@Override
	public MovimentoProdotto elimina(MovimentoProdotto movimento) {
		CausaliMovimento causale = checkCausale(movimento);
		Articoli articolo = checkArticolo(movimento);
		//Recupero l'attuale saldo, se non c'è lo inizializzo e lo inserisco.
		MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(articolo.getIdUniArticolo(), movimento.getMagazzino());
		if (saldo == null)
			throw new CustomException("Saldo non trovato. (articolo: " + articolo.getIdUniArticolo() + ", magazzino:" + movimento.getMagazzino() + ")");
		MagaMov eliminare = daoMovimenti.trovaDaID(movimento.getId());
		if (eliminare == null)
			throw new CustomException("Movimento non trovato. (ID: " + movimento.getId() + ")");
		saldo.setEsistenza(saldo.getEsistenza() - movimento.getQuantita() * causale.getMoltiplicatoreEsistenza());
		saldo.setDisponibile(saldo.getDisponibile() - movimento.getQuantita() * causale.getMoltiplicatoreDisponibile());
		saldo.setImpegnato(saldo.getImpegnato() - movimento.getQuantita() * causale.getMoltiplicatoreImpegnato());
		saldo.setTotIn(saldo.getTotIn() - movimento.getQuantita() * causale.getTotaleIn());
		saldo.setTotOut(saldo.getTotOut() - movimento.getQuantita() * causale.getTotaleOut());
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(saldo);
			em.remove(em.contains(eliminare) ? eliminare : em.find(MagaMov.class, eliminare.getIdMagaMov()));
			t.commit();
		} catch (Exception e) {
			movimento = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return movimento;
	}

}
