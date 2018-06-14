package it.ltc.services.clienti.data.prodotto;

import java.math.BigDecimal;
import java.util.List;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.commessa.Prodotto;
import it.ltc.model.shared.json.cliente.ProdottoJSON;

//@Repository("ProdottoDAO")
public class ProdottoDAOImpl extends Dao implements ProdottoDAO {
	
	public ProdottoDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public ProdottoJSON trovaPerID(int id) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Non ancora implementato!");
		//return null;
	}
	
	@Override
	public ProdottoJSON trovaPerSKU(String sku) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProdottoJSON trovaPerBarcode(String barcode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProdottoJSON> trovaTutti() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Non ancora implementato!");
		//return null;
	}

	@Override
	public ProdottoJSON inserisci(ProdottoJSON prodotto) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Non ancora implementato!");
	}

	@Override
	public ProdottoJSON aggiorna(ProdottoJSON prodotto) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Non ancora implementato!");
		//return false;
	}

	@Override
	public ProdottoJSON dismetti(ProdottoJSON prodotto) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Non ancora implementato!");
		//return false;
	}

	public Prodotto deserializza(ProdottoJSON json) {
		Prodotto prodotto = new Prodotto();
		prodotto.setCodificaCliente(json.getChiaveCliente());
		prodotto.setCodiceModello(json.getCodiceModello());
		prodotto.setBarcode(json.getBarcode());
		prodotto.setTaglia(json.getTaglia());
		prodotto.setColore(json.getColore());
		prodotto.setDescrizione(json.getDescrizione());
		prodotto.setComposizione(json.getComposizione());
		prodotto.setBrand(json.getBrand());
		prodotto.setCategoria(json.getCategoria());
		prodotto.setSottoCategoria(json.getSottoCategoria());
		prodotto.setMadeIn(json.getMadeIn());
		prodotto.setPeso(json.getPeso());
		prodotto.setH(json.getH());
		prodotto.setL(json.getL());
		prodotto.setZ(json.getZ());
		prodotto.setValore(new BigDecimal(json.getValore()));
		return prodotto;
	}

	public ProdottoJSON serializza(Prodotto prodotto) {
		// TODO Auto-generated method stub
		return null;
	}

}
