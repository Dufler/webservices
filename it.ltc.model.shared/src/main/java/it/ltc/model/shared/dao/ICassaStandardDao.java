package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.cliente.CassaStandardJSON;

public interface ICassaStandardDao {
	
	public CassaStandardJSON salva(CassaStandardJSON cassa);
	
	public CassaStandardJSON elimina(CassaStandardJSON cassa);
	
	public CassaStandardJSON trovaDaCodiceCassa(String codice);
	
	public List<CassaStandardJSON> trovaTutte();
	
	public void setUtente(String username);

}
