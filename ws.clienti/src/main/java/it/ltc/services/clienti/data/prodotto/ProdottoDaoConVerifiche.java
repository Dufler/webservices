package it.ltc.services.clienti.data.prodotto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jboss.logging.Logger;

import it.ltc.database.dao.Dao;
import it.ltc.database.dao.legacy.ArtibarDao;
import it.ltc.database.dao.legacy.CategoriaMerceologicaLegacyDao;
import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.RighiOrdineDao;
import it.ltc.database.model.legacy.ArtiBar;
import it.ltc.model.shared.dao.IProdottoDao;
import it.ltc.model.shared.json.cliente.ProdottoJSON;
import it.ltc.services.custom.exception.CustomException;

public abstract class ProdottoDaoConVerifiche<T> extends Dao implements ProdottoDAO {
	
	private static final Logger logger = Logger.getLogger("ProdottoLegacyDAOImpl");
	private static final SimpleDateFormat idUnivocoGenerator = new SimpleDateFormat("yyMMddHHmmssSSS");
	
	protected final CategoriaMerceologicaLegacyDao daoCategoriaMerceologica;
	protected final RighiOrdineDao daoRigheOrdini;
	protected final PakiArticoloDao daoPakiArticolo;
	protected final ColliPackDao daoColliPack;
	protected final ArtibarDao daoArtibar;
	protected final IProdottoDao<T> daoProdotti;

	public ProdottoDaoConVerifiche(String persistenceUnit) {
		super(persistenceUnit);
		daoCategoriaMerceologica = new CategoriaMerceologicaLegacyDao(persistenceUnit);
		daoRigheOrdini = new RighiOrdineDao(persistenceUnit);
		daoPakiArticolo = new PakiArticoloDao(persistenceUnit);
		daoColliPack = new ColliPackDao(persistenceUnit);
		daoArtibar = new ArtibarDao(persistenceUnit);
		daoProdotti = getDaoProdotti(persistenceUnit);
	}
	
	protected abstract IProdottoDao<T> getDaoProdotti(String persistenceUnit);
	
	protected abstract ProdottoJSON serializza(T prodotto);
	
	protected abstract T deserializza(ProdottoJSON json);
	
	protected abstract ArtiBar generaBarcodeArticolo(T articolo);
	
	protected String getIDUnivoco() {
		Date now = new Date();
		String chiave = idUnivocoGenerator.format(now);
		return chiave;
	}	
	
	@Override
	public ProdottoJSON trovaPerID(int id) {
		T articolo = daoProdotti.trovaDaID(id);
		ProdottoJSON json = articolo != null ? serializza(articolo) : null;
		return json;
	}
	
	public List<ProdottoJSON> trovaTutti() {
        List<T> articoli = daoProdotti.trovaTutti();
        List<ProdottoJSON> prodotti = new LinkedList<>();
        for (T articolo : articoli)
        	prodotti.add(serializza(articolo));
		return prodotti;
	}
	
	/**
	 * Controllo sull'univocità della chiave cliente (CodArtStr e IDUniArticolo)
	 * @param articolo l'articolo con il dato sku/codice.
	 * @throws CustomException se esiste un duplicato.
	 */
	protected void checkCodArtStrUnicity(String codArtStr) throws CustomException {
		T prodotto = daoProdotti.trovaDaSKU(codArtStr);
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
	protected void checkBarcodeUnicity(String codBarre) throws CustomException {
		T prodotto = daoProdotti.trovaDaBarcode(codBarre);
        if (prodotto != null) {
        	String message = "E' gia' presente un articolo con la stesso barcode (" + codBarre + ")";
        	logger.error(message);
        	throw new CustomException(message);
        }     
	}
	
	protected void checkCategoria(String categoria) throws CustomException {
		if (daoCategoriaMerceologica.trovaDaCodice(categoria) == null) {
			String message = "La categoria merceologica specificata non esiste. (" + categoria + ")";
			logger.error(message);
			throw new CustomException(message);
		}
	}

}
