package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.cliente.CassaJSON;

public interface ICassaDao {
	
	public CassaJSON salva(CassaJSON cassa);
	
	public CassaJSON elimina(CassaJSON cassa);
	
	public CassaJSON trovaDaIDCassa(int idCassa);
	
	public List<CassaJSON> trovaTutte();

	public void setUtente(String username);
	
}
