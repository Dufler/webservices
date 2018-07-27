package it.ltc.model.shared.json.cliente;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentoJSON {
	
	private String riferimento;
	private Date data;
	private String tipo;
	
	@JsonInclude(Include.NON_NULL)
	private byte[] documentoBase64;
	
	public DocumentoJSON() {}

	public String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public byte[] getDocumentoBase64() {
		return documentoBase64;
	}

	public void setDocumentoBase64(byte[] documentoBase64) {
		this.documentoBase64 = documentoBase64;
	}

	@Override
	public String toString() {
		return "DocumentoJSON [riferimento=" + riferimento + ", data=" + data + ", tipo=" + tipo + "]";
	}

}
