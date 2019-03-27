package it.ltc.services.clienti.data.magazzino;

import it.ltc.database.dao.shared.magazzino.GiacenzeDao;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.model.shared.json.cliente.InfoProdotto;

public class SaldiMagazzinoDAOCiesseImpl extends GiacenzeDao implements SaldiMagazzinoDAO {

	public SaldiMagazzinoDAOCiesseImpl(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	protected String getQueryBase() {
		String query = "SELECT a.Modello AS modello, a.UmPos AS posizionetaglia, a.CodBarre AS barcode, m.disponibile as disponibilità, m.esistenza as giacenza, m.codMaga as magazzino FROM MagaSd m JOIN Articoli a ON m.idUniArticolo = a.idUniArticolo";
		return query;
	}
	
	protected String getQueryPerMagazzino(Magazzini magazzino) {
		String query = "SELECT a.Modello AS modello, a.UmPos AS posizionetaglia, a.CodBarre AS barcode, m.disponibile as disponibilità, m.esistenza as giacenza, m.codMaga as magazzino FROM MagaSd m JOIN Articoli a ON m.idUniArticolo = a.idUniArticolo WHERE m.codMaga = '" + magazzino.getCodiceMag() + "'";
		return query;
	}
	
	protected InfoProdotto getInfoProdotto(Object[] arrayInfo) {
		InfoProdotto info = new InfoProdotto();
		String modello = (String) arrayInfo[0];
		int posizione = (int) arrayInfo[1];
		String barcode = (String) arrayInfo[2];
		String codiceArticolo = modello + "|" + posizione + "|" + barcode;
		info.setCodiceArticolo(codiceArticolo);
		info.setDisponibilità((int) arrayInfo[3]);
		info.setGiacenza((int) arrayInfo[4]);
		info.setMagazzino((String) arrayInfo[5]);
		return info;
	}

}
