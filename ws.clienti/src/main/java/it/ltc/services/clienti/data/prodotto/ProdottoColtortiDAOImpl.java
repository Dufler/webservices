package it.ltc.services.clienti.data.prodotto;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;

import it.ltc.database.dao.legacy.coltorti.ArticoliColtortiDao;
import it.ltc.database.model.legacy.ArtiBar;
import it.ltc.database.model.legacy.coltorti.ArticoliColtorti;
import it.ltc.model.shared.dao.IProdottoDao;
import it.ltc.model.shared.json.cliente.ProdottoJSON;
import it.ltc.services.custom.exception.CustomException;

public class ProdottoColtortiDAOImpl extends ProdottoDaoConVerifiche<ArticoliColtorti> implements ProdottoDAO {
	
	private static final Logger logger = Logger.getLogger("ProdottoColtortiDAOImpl");

	public ProdottoColtortiDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	protected IProdottoDao<ArticoliColtorti> getDaoProdotti(String persistenceUnit) {
		return new ArticoliColtortiDao(persistenceUnit);
	}

	@Override
	public ProdottoJSON inserisci(ProdottoJSON prodotto) {
		//Deserializzo
		ArticoliColtorti articolo = deserializza(prodotto);
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
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.persist(articolo);
			em.persist(barcodeArticolo);
			t.commit();
			logger.info("(Legacy SQL) Prodotto inserito!");
		} catch (Exception e) {
			logger.error(e);
			articolo = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return serializza(articolo);
	}

	@Override
	protected ArtiBar generaBarcodeArticolo(ArticoliColtorti articolo) {
		ArtiBar barcode = new ArtiBar();
		barcode.setBarraEAN(articolo.getCodBarre());
		barcode.setBarraUPC(articolo.getBarraEAN());
		barcode.setCodiceArticolo(articolo.getCodArtStr());
		barcode.setIdUniArticolo(articolo.getIdUniArticolo());
		barcode.setTaglia(articolo.getTaglia());
		return barcode;
	}

	@Override
	public ProdottoJSON aggiorna(ProdottoJSON prodotto) {
		//Controllo di avere già l'anagrafica basandomi sullo SKU
		ArticoliColtorti articolo = daoProdotti.trovaDaSKU(prodotto.getChiaveCliente());
		ArtiBar barcode = daoArtibar.trovaDaSKU(prodotto.getChiaveCliente());
		if (articolo == null || barcode == null)
			throw new CustomException("L'articolo che si sta tentando di aggiornare non esiste. Controlla la chiave cliente!");
		//Se esiste controllo che non sia già arrivato in magazzino
		//nel caso in cui sia gia' presente eseguo l'aggiornamento solo per alcuni campi.
		boolean presente = daoColliPack.isProdottoPresenteInMagazzino(prodotto.getChiaveCliente());
		//Recupero la entity
		EntityManager em = getManager();
		articolo = em.find(ArticoliColtorti.class, articolo.getIdArticolo());
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
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(articolo);
			t.commit();
		} catch (Exception e) {
			logger.error(e);
			articolo = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return serializza(articolo);
	}

	@Override
	public ProdottoJSON dismetti(ProdottoJSON prodotto) {
		if (prodotto == null)
			throw new CustomException("L'articolo che si sta tentando di aggiornare non esiste. Controlla la chiave cliente!");
		ArticoliColtorti articolo = daoProdotti.trovaDaSKU(prodotto.getChiaveCliente());
		if (articolo == null)
			throw new CustomException("L'articolo che si sta tentando di aggiornare non esiste. Controlla la chiave cliente!");
		//Se esiste controllo che non sia presente su carichi o ordini attualmente in lavorazione.
		if (daoPakiArticolo.isProdottoPresenteInCarichi(prodotto.getChiaveCliente()))
			throw new CustomException("Non e' possibile dismettere il prodotto, è attualmente presente in uno o più carichi.");
		if (daoRigheOrdini.isProdottoPresente(prodotto.getChiaveCliente()))
			throw new CustomException("Non e' possibile dismettere il prodotto, è attualmente presente in uno o più ordini.");
		//Se esiste controllo che non sia già arrivato in magazzino
		if (daoColliPack.isProdottoPresenteInMagazzino(prodotto.getChiaveCliente())) {
			throw new CustomException("Non e' possibile dismettere il prodotto, è attualmente presente in magazzino.");
		}
		//Elimino anche il barcode ad esso collegato.
		ArtiBar barcode = daoArtibar.trovaDaSKU(prodotto.getChiaveCliente());
		//Delete
		EntityManager em = getManager();
		articolo = em.find(ArticoliColtorti.class, articolo.getIdArticolo());
		barcode = em.find(ArtiBar.class, barcode.getIdArtiBar());
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(articolo);
			em.remove(barcode);
			t.commit();
		} catch(Exception e) {
			logger.error(e);
			articolo = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}		
		return serializza(articolo);
	}

	public ArticoliColtorti deserializza(ProdottoJSON json) {
		ArticoliColtorti articolo = new ArticoliColtorti();
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

	public ProdottoJSON serializza(ArticoliColtorti articolo) {
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
			json.setParticolarita(articolo.getParticolarita());
		}
		return json;
	}

	@Override
	public ProdottoJSON trovaPerSKU(String sku) {
		ArticoliColtorti articolo = daoProdotti.trovaDaSKU(sku);
        ProdottoJSON prodotto = articolo == null ? null : serializza(articolo);
		return prodotto;
	}

	@Override
	public ProdottoJSON trovaPerBarcode(String barcode) {
		ArticoliColtorti articolo = daoProdotti.trovaDaBarcode(barcode);
        ProdottoJSON prodotto = articolo == null ? null : serializza(articolo);
        logger.info("Articolo trovato: " + prodotto);
		return prodotto;
	}

}
