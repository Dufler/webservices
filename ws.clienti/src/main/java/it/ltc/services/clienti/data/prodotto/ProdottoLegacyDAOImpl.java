package it.ltc.services.clienti.data.prodotto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.legacy.ArtiBar;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.CatMercGruppi;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.services.clienti.model.prodotto.ProdottoJSON;
import it.ltc.services.custom.exception.CustomException;

//@Repository("ProdottoLegacyDAO")
public class ProdottoLegacyDAOImpl extends Dao implements ProdottoDAO<Articoli> {
	
	private static final Logger logger = Logger.getLogger("ProdottoLegacyDAOImpl");
	private static final SimpleDateFormat idUnivocoGenerator = new SimpleDateFormat("yyMMddHHmmssSSS");

	public ProdottoLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	@Override
	public ProdottoJSON trovaDaID(int id) {
		EntityManager em = getManager();
		Articoli articolo = em.find(Articoli.class, id);
		em.close();
		ProdottoJSON json = articolo != null ? serializza(articolo) : null;
		return json;
	}

	@Override
	public List<ProdottoJSON> trovaTutti() {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Articoli> criteria = cb.createQuery(Articoli.class);
        Root<Articoli> member = criteria.from(Articoli.class);
        criteria.select(member);
        List<Articoli> articoli = em.createQuery(criteria).getResultList();
        em.close();
        List<ProdottoJSON> prodotti = new LinkedList<>();
        for (Articoli articolo : articoli)
        	prodotti.add(serializza(articolo));
		return prodotti;
	}

	@Override
	public boolean inserisci(ProdottoJSON prodotto) {
		//Deserializzo
		Articoli articolo = deserializza(prodotto);
		ArtiBar barcodeArticolo = generaBarcodeArticolo(articolo);
		//Eseguo i controlli
		checkCodArtStrUnicity(articolo.getCodArtStr());
		checkBarcodeUnicity(articolo.getCodBarre());
		checkCategoria(articolo.getCategoria());
		//Genero l'ID univoco dell'articolo
		String idUnivoco = getIDUnivoco();
		articolo.setIdUniArticolo(idUnivoco);
		barcodeArticolo.setIdUniArticolo(idUnivoco);
		logger.info("nuovo ID univoco per l'articolo: '" + idUnivoco + "'");
		//Inserisco
		boolean insert;
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.persist(articolo);
			em.persist(barcodeArticolo);
			t.commit();
			insert = true;
			logger.info("(Legacy SQL) Prodotto inserito!");
		} catch (Exception e) {
			logger.error(e);
			insert = false;
			t.rollback();
		} finally {
			em.close();
		}
		return insert;
	}

	private ArtiBar generaBarcodeArticolo(Articoli articolo) {
		ArtiBar barcode = new ArtiBar();
		barcode.setBarraEAN(articolo.getCodBarre());
		barcode.setBarraUPC(articolo.getBarraEAN());
		barcode.setCodiceArticolo(articolo.getCodArtStr());
		barcode.setIdUniArticolo(articolo.getIdUniArticolo());
		barcode.setTaglia(articolo.getTaglia());
		return barcode;
	}

	/**
	 * Controllo sull'univocità della chiave cliente (CodArtStr e IDUniArticolo)
	 * @param articolo l'articolo con il dato sku/codice.
	 * @throws CustomException se esiste un duplicato.
	 */
	private void checkCodArtStrUnicity(String codArtStr) throws CustomException {
		Articoli prodotto = findBySKU(codArtStr);
        if (prodotto != null) {
        	String message = "E' gia' presente un articolo con la stessa chiaveCliente (" + codArtStr + ")";
        	logger.error(message);
        	throw new CustomException(message);
        } 
	}
	
	/**
	 * Controllo sull'univocità del barcode (CodBarre e BarraEAN)
	 * @param articolo l'articolo con il dato barcode.
	 * @throws CustomException se esiste un duplicato.
	 */
	private void checkBarcodeUnicity(String codBarre) throws CustomException {
		Articoli prodotto = findByBarcode(codBarre);
        if (prodotto != null) {
        	String message = "E' gia' presente un articolo con la stesso barcode (" + codBarre + ")";
        	logger.error(message);
        	throw new CustomException(message);
        }     
	}
	
	private void checkCategoria(String categoria) throws CustomException {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CatMercGruppi> criteria = cb.createQuery(CatMercGruppi.class);
        Root<CatMercGruppi> member = criteria.from(CatMercGruppi.class);
        criteria.select(member).where(cb.equal(member.get("categoria"), categoria));
        List<CatMercGruppi> list = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
        if (list.isEmpty()) {
			String message = "La categoria merceologica specificata non esiste. (" + categoria + ")";
			logger.error(message);
			throw new CustomException(message);
		}
	}

	@Override
	//@Transactional
	public boolean aggiorna(ProdottoJSON prodotto) {
		//Controllo di avere già l'anagrafica basandomi sullo SKU
		Articoli articolo = findBySKU(prodotto.getChiaveCliente());
		ArtiBar barcode = findBarcodeBySKU(prodotto.getChiaveCliente());
		if (articolo == null || barcode == null)
			throw new CustomException("L'articolo che si sta tentando di aggiornare non esiste. Controlla la chiave cliente!");
		//Se esiste controllo che non sia già arrivato in magazzino
		//nel caso in cui sia gia' presente eseguo l'aggiornamento solo per alcuni campi.
		boolean presente = isPresenteInMagazzino(prodotto); 
		//Recupero la entity
		EntityManager em = getManager();
		articolo = em.find(Articoli.class, articolo.getIdArticolo());
		barcode = em.find(ArtiBar.class, barcode.getIdArtiBar());
		if (!presente) {
			//update sul barcode
			articolo.setCodBarre(prodotto.getBarcode());
			barcode.setBarraEAN(prodotto.getBarcode());
			//update sulla taglia
			articolo.setTaglia(prodotto.getTaglia());
			barcode.setTaglia(prodotto.getTaglia());
			//update su tutto il resto
			articolo.setCategoria(prodotto.getCategoria());
			articolo.setCatMercGruppo(prodotto.getCategoria());
			articolo.setLinea(prodotto.getBrand());
			//null check sugli opzionali
			if (prodotto.getBarcodeFornitore() != null && !prodotto.getBarcodeFornitore().isEmpty())
				articolo.setBarraEAN(prodotto.getBarcodeFornitore());
			if (prodotto.getSkuFornitore() != null && !prodotto.getSkuFornitore().isEmpty())
				articolo.setCodArtOld(prodotto.getSkuFornitore());
			if (prodotto.getMadeIn() != null && !prodotto.getMadeIn().isEmpty())
				articolo.setMadeIn(prodotto.getMadeIn());
			if (prodotto.getStagione() != null && !prodotto.getStagione().isEmpty())
				articolo.setStagione(prodotto.getStagione());
		}
		//Aggiorno tutto il resto
		//null check sugli opzionali.
		if (prodotto.getComposizione() != null && !prodotto.getComposizione().isEmpty())
			articolo.setComposizione(prodotto.getComposizione());
		if (prodotto.getColore() != null && !prodotto.getColore().isEmpty())
			articolo.setColore(prodotto.getColore());
		if (prodotto.getDescrizione() != null && !prodotto.getDescrizione().isEmpty())
			articolo.setDescrizione(prodotto.getDescrizione());
		if (prodotto.getDescrizioneAggiuntiva() != null && !prodotto.getDescrizioneAggiuntiva().isEmpty())
			articolo.setDescAggiuntiva(prodotto.getDescrizioneAggiuntiva());
		if (prodotto.getNote() != null && !prodotto.getNote().isEmpty())
			articolo.setNote(prodotto.getNote());
		if (prodotto.getH() != null)
			articolo.setArtH(prodotto.getH());
		if (prodotto.getL() != null)
			articolo.setArtL(prodotto.getL());
		if (prodotto.getZ() != null)
			articolo.setArtZ(prodotto.getZ());
		if (prodotto.getPeso() != null)
			articolo.setArtPeso(new BigDecimal(prodotto.getPeso()));
		if (prodotto.getValore() != null)
			articolo.setValVen(new BigDecimal(prodotto.getValore()));
		//Update
		boolean update;
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(articolo);
			t.commit();
			update = true;
		} catch (Exception e) {
			logger.error(e);
			update = false;
			t.rollback();
		} finally {
			em.close();
		}
		return update;
	}

	@Override
	public boolean dismetti(ProdottoJSON prodotto) {
		if (prodotto == null)
			throw new CustomException("L'articolo che si sta tentando di aggiornare non esiste. Controlla la chiave cliente!");
		Articoli articolo = findBySKU(prodotto.getChiaveCliente());
		if (articolo == null)
			throw new CustomException("L'articolo che si sta tentando di aggiornare non esiste. Controlla la chiave cliente!");
		//Se esiste controllo che non sia presente su carichi o ordini attualmente in lavorazione.
		if (isPresenteInCarichi(prodotto))
			throw new CustomException("Non e' possibile dismettere il prodotto, è attualmente presente in uno o più carichi.");
		if (isPresenteInOrdini(prodotto))
			throw new CustomException("Non e' possibile dismettere il prodotto, è attualmente presente in uno o più ordini.");
		//Se esiste controllo che non sia già arrivato in magazzino
		if (isPresenteInMagazzino(prodotto)) {
			throw new CustomException("Non e' possibile dismettere il prodotto, è attualmente presente in magazzino.");
		}
		//Elimino anche il barcode ad esso collegato.
		ArtiBar barcode = findBarcodeBySKU(prodotto.getChiaveCliente());
		//Delete
		boolean delete;
		EntityManager em = getManager();
		articolo = em.find(Articoli.class, articolo.getIdArticolo());
		barcode = em.find(ArtiBar.class, barcode.getIdArtiBar());
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(articolo);
			em.remove(barcode);
			t.commit();
			delete = true;
		} catch(Exception e) {
			logger.error(e);
			delete = false;
			t.rollback();
		} finally {
			em.close();
		}		
		return delete;
	}

	private boolean isPresenteInOrdini(ProdottoJSON prodotto) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RighiOrdine> criteria = cb.createQuery(RighiOrdine.class);
        Root<RighiOrdine> member = criteria.from(RighiOrdine.class);
        criteria.select(member).where(cb.equal(member.get("codiceArticolo"), prodotto.getChiaveCliente()));
        List<RighiOrdine> articoli = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        boolean presente = !articoli.isEmpty();
		return presente;
	}

	private boolean isPresenteInCarichi(ProdottoJSON prodotto) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PakiArticolo> criteria = cb.createQuery(PakiArticolo.class);
        Root<PakiArticolo> member = criteria.from(PakiArticolo.class);
        criteria.select(member).where(cb.equal(member.get("codArtStr"), prodotto.getChiaveCliente()));
        List<PakiArticolo> articoli = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        boolean presente = !articoli.isEmpty();
		return presente;
	}

	@Override
	public Articoli deserializza(ProdottoJSON json) {
		Articoli articolo = new Articoli();
		if (json != null) {
			articolo.setCodArtStr(json.getChiaveCliente());
			articolo.setModello(json.getCodiceModello());
			articolo.setCodBarre(json.getBarcode());
			articolo.setTaglia(json.getTaglia());
			articolo.setColore(json.getColore());
			articolo.setDescrizione(json.getDescrizione());
			articolo.setComposizione(json.getComposizione());
			articolo.setLinea(json.getBrand());
			articolo.setCategoria(json.getCategoria());
			articolo.setCatMercGruppo(json.getCategoria());
			articolo.setCatMercDett(json.getSottoCategoria());
			articolo.setMadeIn(json.getMadeIn());
			articolo.setStagione(json.getStagione());
			if (json.getPeso() != null) //null check
				articolo.setArtPeso(new BigDecimal(json.getPeso()));
			articolo.setArtH(json.getH());
			articolo.setArtL(json.getL());
			articolo.setArtZ(json.getZ());
			if (json.getValore() != null) //null check
				articolo.setValVen(new BigDecimal(json.getValore()));
			articolo.setBarraEAN(json.getBarcodeFornitore());
			articolo.setCodArtOld(json.getSkuFornitore());
			articolo.setDescAggiuntiva(json.getDescrizioneAggiuntiva());
			articolo.setNote(json.getNote());
			articolo.setTipoCassa(json.getCassa());
			logger.info("Articolo deserializzato: " + articolo);
		}
		return articolo;
	}
	
	private String getIDUnivoco() {
		Date now = new Date();
		String chiave = idUnivocoGenerator.format(now);
		return chiave;
	}
	
	private boolean isPresenteInMagazzino(ProdottoJSON prodotto) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ColliPack> criteria = cb.createQuery(ColliPack.class);
        Root<ColliPack> member = criteria.from(ColliPack.class);
        criteria.select(member).where(cb.equal(member.get("codArtStr"), prodotto.getChiaveCliente()));
        List<ColliPack> articoli = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        boolean presente = !articoli.isEmpty();
		return presente;
	}

	@Override
	public ProdottoJSON serializza(Articoli articolo) {
		ProdottoJSON json = new ProdottoJSON();
		if (articolo != null) {
			json.setId(articolo.getIdArticolo());
			//Controllo sul valore per la cassa.
			String cassa = articolo.getTipoCassa();
			if (cassa == null || cassa.isEmpty())
				cassa = "NO";
			json.setCassa(cassa);
			json.setChiaveCliente(articolo.getCodArtStr());
			json.setCodiceModello(articolo.getModello());
			json.setBarcode(articolo.getCodBarre());
			json.setTaglia(articolo.getTaglia());
			json.setColore(articolo.getColore());
			json.setDescrizione(articolo.getDescrizione());
			json.setComposizione(articolo.getComposizione());
			json.setBrand(articolo.getLinea());
			json.setCategoria(articolo.getCatMercGruppo());
			json.setSottoCategoria(articolo.getCatMercDett());
			json.setMadeIn(articolo.getMadeIn());
			json.setStagione(articolo.getStagione());
			if (articolo.getArtPeso() != null) //null check
				json.setPeso(articolo.getArtPeso().intValue());
			json.setH(articolo.getArtH());
			json.setL(articolo.getArtL());
			json.setZ(articolo.getArtZ());
			if (articolo.getValVen() != null)
				json.setValore(articolo.getValVen().doubleValue());
			json.setBarcodeFornitore(articolo.getBarraEAN());
			json.setSkuFornitore(articolo.getCodArtOld());
			json.setDescrizioneAggiuntiva(articolo.getDescAggiuntiva());
			json.setNote(articolo.getNote());
		}
		return json;
	}

	@Override
	public ProdottoJSON trovaDaSKU(String sku) {
		Articoli articolo = findBySKU(sku);
        ProdottoJSON prodotto = articolo == null ? null : serializza(articolo);
		return prodotto;
	}
	
	public Articoli findByIDUnivoco(String idUnivoco) {
		logger.info("Ricerca articolo tramite ID Univoco: '" + idUnivoco + "'");
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Articoli> criteria = cb.createQuery(Articoli.class);
        Root<Articoli> member = criteria.from(Articoli.class);
        criteria.select(member).where(cb.equal(member.get("idUniArticolo"), idUnivoco));
        List<Articoli> articoli = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        Articoli articolo = articoli.isEmpty() ? null : articoli.get(0);
        logger.info("Articolo trovato: " + articolo);
		return articolo;
	}
	
	public Articoli findBySKU(String sku) {
		logger.info("Ricerca articolo tramite SKU: '" + sku + "'");
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Articoli> criteria = cb.createQuery(Articoli.class);
        Root<Articoli> member = criteria.from(Articoli.class);
        criteria.select(member).where(cb.equal(member.get("codArtStr"), sku));
        List<Articoli> articoli = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        Articoli articolo = articoli.isEmpty() ? null : articoli.get(0);
        logger.info("Articolo trovato: " + articolo);
		return articolo;
	}
	
	public ArtiBar findBarcodeBySKU(String sku) {
		logger.info("Ricerca barcode tramite SKU: '" + sku + "'");
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ArtiBar> criteria = cb.createQuery(ArtiBar.class);
        Root<ArtiBar> member = criteria.from(ArtiBar.class);
        criteria.select(member).where(cb.equal(member.get("codiceArticolo"), sku));
        List<ArtiBar> articoli = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        ArtiBar articolo = articoli.isEmpty() ? null : articoli.get(0);
        logger.info("Barcode trovato: " + articolo);
		return articolo;
	}

	@Override
	public ProdottoJSON trovaDaBarcode(String barcode) {
		Articoli articolo = findByBarcode(barcode);
        ProdottoJSON prodotto = articolo == null ? null : serializza(articolo);
        logger.info("Articolo trovato: " + prodotto);
		return prodotto;
	}
	
	public Articoli findByBarcode(String barcode) {
		logger.info("Ricerca tramite barcode: '" + barcode + "'");
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Articoli> criteria = cb.createQuery(Articoli.class);
        Root<Articoli> member = criteria.from(Articoli.class);
        criteria.select(member).where(cb.or(cb.equal(member.get("codBarre"), barcode), cb.equal(member.get("barraEAN"), barcode)));
        List<Articoli> articoli = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        Articoli articolo = articoli.isEmpty() ? null : articoli.get(0);
        logger.info("Articolo trovato: " + articolo);
		return articolo;
	}

}
