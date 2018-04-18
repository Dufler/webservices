package it.ltc.services.logica.model.fatturazione;

import java.util.List;

import it.ltc.database.model.centrale.FatturaDocumento;

public class RisultatoFatturazioneJSON {
	
	private FatturaDocumento documento;
	private List<ElementoFatturazioneJSON> elementi;
	
	public RisultatoFatturazioneJSON() {}

	public FatturaDocumento getDocumento() {
		return documento;
	}

	public void setDocumento(FatturaDocumento documento) {
		this.documento = documento;
	}

	public List<ElementoFatturazioneJSON> getElementi() {
		return elementi;
	}

	public void setElementi(List<ElementoFatturazioneJSON> elementi) {
		this.elementi = elementi;
	}

}
