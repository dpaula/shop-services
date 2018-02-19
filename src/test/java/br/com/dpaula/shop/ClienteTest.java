package br.com.dpaula.shop;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.dpaula.shop.modelo.Carrinho;
import junit.framework.Assert;

/**
 * @author ferna
 *
 */
public class ClienteTest {

	public HttpServer server;

	@Before
	public void criaServidor() {
		server = Servidor.inicializaServidor();
	}

	@After
	public void finalizaServidor() {
		server.stop();
	}

	@Test
	public void testaQueAConexaoComOServidorFunciona() {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://www.mocky.io");
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);

		System.out.println(conteudo);

		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
	}

	@Test
	public void testaRetornoDoCarrinhoEmXMLEsperado() {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8787");
		String conteudo = target.path("/carrinhos").request().get(String.class);

		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);

		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());

	}
}
