package it.ltc.services.clara.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.logging.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import it.ltc.services.clara.model.Scena;
import it.ltc.services.custom.client.RestClient;

public class ClaraClient extends RestClient {
	
	private static final Logger logger = Logger.getLogger("ClaraClient");
	
	//public static final String username = "Dufler87";
	//public static final String password = "7bceaf5a-6bfa-4283-bdfc-5406f58c64fc";
	public static final String authString = "Basic RHVmbGVyODc6N2JjZWFmNWEtNmJmYS00MjgzLWJkZmMtNTQwNmY1OGM2NGZj";
	
	public static final String domain = "https://clara.io/";
	public static final String contextPath = "api/scenes/";
	
	public static final String url_AppendiceUpload = "/import?async=0";
	
	public static final String url_AppendiceDownload = "/export/threejs?zip=false";
	
	protected final SimpleDateFormat sdf;
	
	public ClaraClient() {
		super(domain, contextPath, null, null);
		sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	}
	
	protected String getAuthString() {
		return authString;
	}
	
	public Scena nuovaScena() {
		Scena nuovaScena = new Scena();
		nuovaScena.setName(sdf.format(new Date()));
		nuovaScena = post("", nuovaScena, Scena.class);
		return nuovaScena;
	}
	
	protected HttpHeaders getHeadersPerForm() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("Authorization", getAuthString());
		headers.add("Accept", "*/*");
		return headers;
	}
	
	public String scaricaJSON(String idScena) {
		String url = domain + contextPath + idScena + url_AppendiceDownload;
		String response;
		try {
			logger.info("Chiamata su URL: " + url);
	    	ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.GET, null, String.class);
	    	httpStatus = responseEntity.getStatusCode();
	    	response = responseEntity.getBody();
		} catch (HttpStatusCodeException exception) {
	    	logger.error(exception);
	    	httpStatus = exception.getStatusCode();
			error = exception.getStatusText();
			response = null;
		} catch (ResourceAccessException exception) {
			logger.error(exception);
			httpStatus = HttpStatus.NO_CONTENT;
			response = null;
		} finally {
			//DO NOTHING! for now...
		}
		return response;
	}
	
	public void caricaFileTramiteForm(String idScena, String file) {
		String url = domain + contextPath + idScena + url_AppendiceUpload;
		File temp = null;
		try {
			logger.info("Chiamata su URL: " + url);
	    	LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
	    	temp = new File("temp.STEP");
	    	if (!temp.exists())
	            temp.createNewFile();
	    	FileWriter fw = new FileWriter(temp.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(file);
	        bw.close();
		    params.add("file", new FileSystemResource(temp));
		    HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, getHeadersPerForm());
	    	ResponseEntity<?> responseEntity = rest.exchange(url, HttpMethod.POST, requestEntity, Void.class);
	    	httpStatus = responseEntity.getStatusCode();
	    } catch (HttpStatusCodeException exception) {
	    	logger.error(exception);
	    	httpStatus = exception.getStatusCode();
			error = exception.getStatusText();
		} catch (ResourceAccessException exception) {
			logger.error(exception);
			httpStatus = HttpStatus.NO_CONTENT;
		} catch (IOException exception) {
			logger.error(exception);
			error = exception.getMessage();
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} finally {
			if (temp != null)
				temp.delete();
		}
	}

}
