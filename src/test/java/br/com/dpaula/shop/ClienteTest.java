package br.com.dpaula.shop;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.dpaula.shop.modelo.Carrinho;
import br.com.dpaula.shop.modelo.Produto;
import junit.framework.Assert;

/**
 * @author ferna
 *
 */
public class ClienteTest {

	public HttpServer server;
	public WebTarget target;

	@Before
	public void criaServidor() {
		server = Servidor.inicializaServidor();

		// Criando uma configuração no meu client
		ClientConfig config = new ClientConfig();
		// registrando uma API de log na minha configuação
		config.register(new LoggingFilter());

		// O LoggingFilteré do Jersey, que é uma implementação do JAX-RS. Então, dado o
		// LoggingFilter, eu crio agora um ClientBuilder.newClient baseado nessa
		// configuração
		Client client = ClientBuilder.newClient(config);
		target = client.target("http://localhost:8585");
	}

	@After
	public void finalizaServidor() {
		server.stop();
	}

	@Test
	public void testaQueAConexaoComOServidorFunciona() {

		// Dentro desse código de teste queremos um cliente http para acessar o
		// servidor, portanto criamos um cliente novo
		Client client = ClientBuilder.newClient();
		// Agora que temos um cliente, queremos usar uma URI base,a URI do servidor,
		// para fazer várias requisições. No nosso caso é a URI do servidor que estamos
		// utilizando, o www.mocky.io, portanto dizemos ao nosso cliente que
		// trabalharemos com o alvo http://www.mocky.io:
		WebTarget target = client.target("http://www.mocky.io");
		// vamos agora dizer que queremos fazer uma requisição para uma URI específica,
		// target, por favor, para esse path '/v2/52aaf5deee7ba8c70329fb7d' faça uma
		// requisição, e a requisição que faremos é a mais básica, a que pega dados do
		// servidor, o método get
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);

		System.out.println(conteudo);

		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
	}

	@Test
	public void testaRetornoDoCarrinhoEmXMLEsperado() {

		String conteudo = target.path("/carrinhos/1").request().get(String.class);

		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);

		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());

	}

	@Test
	public void testaQueSuportaNovosCarrinhos() {

		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(315, "Celular", 2999, 1));
		carrinho.setRua("Rua 2 de Setembro 345");
		carrinho.setCidade("Blumenau");

		// converte par XML
		String xml = carrinho.toXML();
		// cria esta entidade só para explicitar que usa type xml
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

		// através do target, entra em carrinhos, pega o request e posta a entidade
		// contendo o carrinho novo
		Response resp = target.path("/carrinhos").request().post(entity);
		// verifica se a resposta é de criação 201
		Assert.assertEquals(201, resp.getStatus());

		// pegando a uri com o carrinho novo
		String location = resp.getHeaderString("Location");

		// atraves de um cliente, navega na uri do novo carrinho e pega ele como string
		String conteudo = ClientBuilder.newClient().target(location).request().get(String.class);

		Assert.assertTrue(conteudo.contains("Celular"));

	}
}
