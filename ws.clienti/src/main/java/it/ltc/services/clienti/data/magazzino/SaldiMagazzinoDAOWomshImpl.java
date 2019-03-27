package it.ltc.services.clienti.data.magazzino;

import it.ltc.database.dao.shared.magazzino.GiacenzeDao;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.model.shared.json.cliente.InfoProdotto;

public class SaldiMagazzinoDAOWomshImpl extends GiacenzeDao implements SaldiMagazzinoDAO {

	public SaldiMagazzinoDAOWomshImpl(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	protected String getQueryBase() {
		String query = "SELECT a.codArtStr AS codiceArticolo, a.taglia AS taglia, m.disponibile as disponibilità, m.esistenza as giacenza, m.codMaga as magazzino FROM MagaSd m JOIN Articoli a ON m.idUniArticolo = a.idUniArticolo";
		return query;
	}
	
	protected String getQueryPerMagazzino(Magazzini magazzino) {
		String query = "SELECT a.codArtStr AS codiceArticolo, a.taglia AS taglia, m.disponibile as disponibilità, m.esistenza as giacenza, m.codMaga as magazzino FROM MagaSd m JOIN Articoli a ON m.idUniArticolo = a.idUniArticolo WHERE m.codMaga = '" + magazzino.getCodiceMag() + "'";
		return query;
	}
	
	protected InfoProdotto getInfoProdotto(Object[] arrayInfo) {
		InfoProdotto info = new InfoProdotto();
		String sku = (String) arrayInfo[0];
		String taglia = (String) arrayInfo[1];
		String codiceArticolo = taglia != null && !taglia.isEmpty() ? sku + "|" + taglia : sku;
		info.setCodiceArticolo(codiceArticolo);
		info.setDisponibilità((int) arrayInfo[2]);
		info.setGiacenza((int) arrayInfo[3]);
		info.setMagazzino((String) arrayInfo[4]);
		return info;
	}

}
