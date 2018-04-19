package it.ltc.services.sede.controller.stampa;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public abstract class ServizioDiStampa {
	
	protected Response buildResponse(int status, String message) {
		ResponseBuilder builder = Response.status(status).entity(message);
		Response risposta = builder.build();
		return risposta;
	}
	
	protected String inviaStampa(String ip, String chiamata) {
		String esito = null;
		try {
			Socket socket = getSocket(ip);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(chiamata);
			out.flush();
			socket.close();
		} catch (IOException e) {
			esito = e.getLocalizedMessage();
		}
		return esito;
	}
	
	protected Socket getSocket(String ip) throws IOException {
		String host = ip;
		int port = 6101;
		Socket socket = new Socket(host, port);
		socket.setSoTimeout(1000);
		return socket;
	}

}
