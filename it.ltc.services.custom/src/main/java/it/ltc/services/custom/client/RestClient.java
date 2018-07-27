package it.ltc.services.custom.client;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.jboss.logging.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {
	
	private static final Logger logger = Logger.getLogger("RestClient");
	
	public static final String DOMAIN_TEST = "http://test.services.ltc-logistics.it";
	public static final String DOMAIN_PRODUZIONE = "http://ws.services.ltc-logistics.it";
	
	public static final String CONTEXT_PATH_CENTRALE = "/logica/rest/";
	public static final String CONTEXT_PATH_SEDE = "/sede/rest/";
	
	protected final RestTemplate rest;
	protected final HttpHeaders headers;
	
	protected final String domain;
	protected final String contextPath;
	
	protected HttpStatus httpStatus;
	protected String error;
	
	/**
	 * Costruttore di default, prende il dominio in base a se si sta facendo test o meno e il context path del centrale.
	 */
	public RestClient() {
		this(null, null, null, null);
	}
	
	public RestClient(String contextPath, String risorsaCommessa) {
		this(null, contextPath, null, risorsaCommessa);
	}

	public RestClient(String domain, String contextPath, String dateFormat, String risorsaCommessa) {
		this.domain = domain != null ? domain : getDomain();
		this.contextPath = contextPath != null ? contextPath : CONTEXT_PATH_CENTRALE;
		this.rest = getClient(dateFormat);
		this.headers = getHeaders(risorsaCommessa);
	}

	protected String getDomain() {
		String domain = DOMAIN_TEST;
		return domain;
	}
	
	protected RestTemplate getClient(String dateFormat) {
		RestTemplate rest = new RestTemplate();
		rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		//Form
		FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
		formConverter.setCharset(Charset.forName("UTF8"));
		//XML
		Jaxb2RootElementHttpMessageConverter xmlConverter = new Jaxb2RootElementHttpMessageConverter();
		xmlConverter.setSupportDtd(true);
		//JSON
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		if (dateFormat != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
			//objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));
			jsonConverter.setObjectMapper(objectMapper);
		}
		//Forzo la timezone a UTC.
		jsonConverter.getObjectMapper().setTimeZone(TimeZone.getTimeZone("NET")); //Europe/Rome
		//Aggiungo tutti i converter
		messageConverters.add(jsonConverter);
		messageConverters.add(xmlConverter);
		messageConverters.add(formConverter);
		rest.setMessageConverters(messageConverters);
		return rest;
	}
	
	protected HttpHeaders getHeaders(String risorsaCommessa) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", getAuthString());
		headers.add("Accept", "*/*");
		if (risorsaCommessa != null)
			headers.add("commessa", risorsaCommessa);
		return headers;
	}
	
	protected String getAuthString() {
		String authstring = "Basic ";
		return authstring;
	}
	
	protected <T> T call(HttpMethod method, String completeURI, Object body, Class<T> c) {
		HttpEntity<Object> requestEntity = body != null ? new HttpEntity<Object>(body, headers) : new HttpEntity<Object>(headers);
		T result;
		try {
			ResponseEntity<T> responseEntity = rest.exchange(completeURI, method, requestEntity, c);
			httpStatus = responseEntity.getStatusCode();
			result = responseEntity.getBody();
		} catch (ResourceAccessException exception) {
			logger.error(exception);
			result = null;
			httpStatus = HttpStatus.NO_CONTENT;
			error = exception.getLocalizedMessage();
		} catch (HttpStatusCodeException exception) {
			logger.error(exception);
			result = null;
			httpStatus = exception.getStatusCode();
			error = httpStatus == HttpStatus.BAD_REQUEST ? exception.getResponseBodyAsString() : exception.getStatusText();
		}
		return result;
	}
	
	protected void call(HttpMethod method, String completeURI, Object body) {
		HttpEntity<Object> requestEntity = body != null ? new HttpEntity<Object>(body, headers) : new HttpEntity<Object>(headers);
		try {
			Class<?> nullClass = null;
			ResponseEntity<?> responseEntity = rest.exchange(completeURI, method, requestEntity, nullClass);
			httpStatus = responseEntity.getStatusCode();
		} catch (ResourceAccessException exception) {
			logger.error(exception);
			httpStatus = HttpStatus.NO_CONTENT;
		} catch (HttpStatusCodeException exception) {
			logger.error(exception);
			httpStatus = exception.getStatusCode();
			error = httpStatus == HttpStatus.BAD_REQUEST ? exception.getResponseBodyAsString() : exception.getStatusText();
		}
	}
	
	public <T> T get(String uri, Class<T> c) {
		T entity = call(HttpMethod.GET, domain + contextPath + uri, null, c);
		return entity;
	}

	public <T> T post(String uri, Object json, Class<T> c) {
		T entity = call(HttpMethod.POST, domain + contextPath + uri, json, c);
		return entity;
	}
	
	public void post(String uri, Object json) {
		call(HttpMethod.POST, domain + contextPath + uri, json);
	}

	public <T> T  put(String uri, Object json, Class<T> c) {
		T entity = call(HttpMethod.PUT, domain + contextPath + uri, json, c);
		return entity;
	}
	
	public void put(String uri, Object json) {
		call(HttpMethod.PUT, domain + contextPath + uri, json);
	}

	public <T> T delete(String uri, Object json, Class<T> c) {
		T entity = call(HttpMethod.DELETE, domain + contextPath + uri, json, c);
		return entity;
	}
	
	public void delete(String uri, Object json) {
		call(HttpMethod.DELETE, domain + contextPath + uri, json);
	}

	public int getStatus() {
		int status = httpStatus != null ? httpStatus.value() : -1; 
		return status;
	}

	public String getError() {
		return error;
	}
	
//	public WSError getJSONError() {
//		WSError message;
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			message = objectMapper.readValue(error, WSError.class);
//		} catch (Exception e) {
//			message = new WSError();
//			message.setMessage("Impossibile leggere la risposta: " + e.getLocalizedMessage());
//			e.printStackTrace();
//		}
//		return message;
//	}

}
