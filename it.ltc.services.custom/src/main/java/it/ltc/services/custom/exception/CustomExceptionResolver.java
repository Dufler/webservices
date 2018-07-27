package it.ltc.services.custom.exception;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

public class CustomExceptionResolver extends DefaultHandlerExceptionResolver {

	private static final Logger logger = Logger.getLogger("ExceptionResolver");
	
	private final boolean intercept;
	
	/**
	 * Costruisce il bean specificando che deve intervenire per primo in caso di errore.
	 */
	public CustomExceptionResolver() {
		setOrder(Ordered.HIGHEST_PRECEDENCE);
		intercept = true;
	}
	
	private void sendErrorJSON(HttpServletResponse response, CustomErrorMessage message) throws IOException {
		String errorMessage = message.toString();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(message.getErrorCode());
		response.getWriter().append(errorMessage);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,	Object handler, Exception ex) {
		if (intercept) 
		try {
			if (ex instanceof CustomException) {
				return handleCustomException((CustomException) ex, request, response, handler);
			} else if (ex instanceof BadCredentialsException) {
				return handleAuthException((BadCredentialsException) ex, request, response, handler);
			}
//			else if (ex instanceof NoSuchRequestHandlingMethodException) {
//				return handleNoSuchRequestHandlingMethod((NoSuchRequestHandlingMethodException) ex, request, response, handler);
//			}
			else if (ex instanceof HttpRequestMethodNotSupportedException) {
				return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, request, response, handler);
			}
			else if (ex instanceof HttpMediaTypeNotSupportedException) {
				return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, request, response, handler);
			}
			else if (ex instanceof HttpMediaTypeNotAcceptableException) {
				return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, request, response, handler);
			}
			else if (ex instanceof MissingServletRequestParameterException) {
				return handleMissingServletRequestParameter((MissingServletRequestParameterException) ex, request, response, handler);
			}
			else if (ex instanceof ServletRequestBindingException) {
				return handleServletRequestBindingException((ServletRequestBindingException) ex, request, response, handler);
			}
			else if (ex instanceof ConversionNotSupportedException) {
				return handleConversionNotSupported((ConversionNotSupportedException) ex, request, response, handler);
			}
			else if (ex instanceof TypeMismatchException) {
				return handleTypeMismatch((TypeMismatchException) ex, request, response, handler);
			}
			else if (ex instanceof HttpMessageNotReadableException) {
				return handleHttpMessageNotReadable((HttpMessageNotReadableException) ex, request, response, handler);
			}
			else if (ex instanceof HttpMessageNotWritableException) {
				return handleHttpMessageNotWritable((HttpMessageNotWritableException) ex, request, response, handler);
			}
			else if (ex instanceof MethodArgumentNotValidException) {
				return handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex, request, response, handler);
			}
			else if (ex instanceof MissingServletRequestPartException) {
				return handleMissingServletRequestPartException((MissingServletRequestPartException) ex, request, response, handler);
			}
			else if (ex instanceof BindException) {
				return handleBindException((BindException) ex, request, response, handler);
			}
			else if (ex instanceof NoHandlerFoundException) {
				return handleNoHandlerFoundException((NoHandlerFoundException) ex, request, response, handler);
			} 
			else if (ex instanceof UnexpectedRollbackException) {
				return handleRollBackException((UnexpectedRollbackException) ex, request, response, handler);
			}
			else {
				handleNonDefaultException(ex, request, response, handler);
			}
		}
		catch (Exception handlerException) {
			logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
		}
		return null;
	}
	
	@Override
	protected ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		List<CustomErrorCause> errors = new LinkedList<>();
		for (ObjectError  error : ex.getBindingResult().getAllErrors()) {
			CustomErrorCause cause = new CustomErrorCause(error.getObjectName(), error.getDefaultMessage());
			errors.add(cause);
		}
		CustomErrorMessage message = new CustomErrorMessage(400, "Validazione fallita", errors);
		sendErrorJSON(response, message);
		return new ModelAndView();
	}
	
	/**
	 * Metodo per la gestione degli errori durante la scrittura su DB.
	 * @param ex
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws IOException
	 */
	protected ModelAndView handleRollBackException(UnexpectedRollbackException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {		
		CustomErrorMessage message = new CustomErrorMessage(500, "Errore durante l'accesso al DB.", null);
		sendErrorJSON(response, message);
		return new ModelAndView();
	}
	
	/**
	 * Metodo generico per la gestione di tutti gli errori non contemplati precedentemente.
	 * @param ex
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws IOException
	 */
	protected ModelAndView handleNonDefaultException(Exception ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		String errorMessage = "Errore: " + ex.getLocalizedMessage();
		CustomErrorMessage message = new CustomErrorMessage(500, errorMessage, null);
		sendErrorJSON(response, message);
		return new ModelAndView();
	}
	
	/**
	 * Metodo generico per la gestione di tutti gli errori non contemplati precedentemente.
	 * @param ex
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws IOException
	 */
	protected ModelAndView handleCustomException(CustomException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		CustomErrorMessage message = new CustomErrorMessage(ex);
		sendErrorJSON(response, message);
		return new ModelAndView();
	}
	
	/**
	 * Metodo per la gestione degli errori di autenticazione.
	 * @param ex
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws IOException
	 */
	protected ModelAndView handleAuthException(BadCredentialsException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		CustomErrorMessage message = new CustomErrorMessage(401, ex.getMessage(), null);
		sendErrorJSON(response, message);
		return new ModelAndView();
	}
	
	/**
	 * Invoked to send a server error. Sets the status to 500 and also sets the
	 * request attribute "javax.servlet.error.exception" to the Exception.
	 */
	@Override
	protected void sendServerError(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.error(request.getMethod() + request.getRequestURI() + ": " + ex);
		request.setAttribute("javax.servlet.error.exception", ex);
		String errorMessage = "Errore: " + ex.getLocalizedMessage();
		CustomErrorMessage message = new CustomErrorMessage(500, errorMessage, null);
		sendErrorJSON(response, message);
	}

}
