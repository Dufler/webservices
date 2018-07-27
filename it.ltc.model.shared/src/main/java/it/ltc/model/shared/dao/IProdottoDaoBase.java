package it.ltc.model.shared.dao;

import java.util.List;

public interface IProdottoDaoBase<T> {
	
	T trovaDaID(int id);

	T trovaDaSKU(String sku);
	
	T trovaDaBarcode(String barcode);

	List<T> trovaTutti();
	
}
