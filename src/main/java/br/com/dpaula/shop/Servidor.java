package br.com.dpaula.shop;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.ProcessingException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Servidor {

	public static void main(String[] args) throws IOException {

		HttpServer server = inicializaServidor();

		System.out.println("Servidor rodando");
		System.in.read();
		server.stop();
	}

	/**
	 * @return
	 * @throws ProcessingException
	 */
	public static HttpServer inicializaServidor() throws ProcessingException {
		URI uri = URI.create("http://localhost:8585/");
		ResourceConfig config = new ResourceConfig().packages("br.com.dpaula.shop");
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, config);
		return server;
	}

}
