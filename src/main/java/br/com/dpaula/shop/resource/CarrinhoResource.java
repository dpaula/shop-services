package br.com.dpaula.shop.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.dpaula.shop.dao.CarrinhoDAO;
import br.com.dpaula.shop.modelo.Carrinho;

@Path("carrinhos")
public class CarrinhoResource {

	/**
	 * no navegador: http://localhost:8585/carrinhos/1
	 * 
	 * @param id
	 * @return
	 */
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@PathParam("id") long id) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);

		return carrinho.toXML();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response adiciona(String conteudo) {
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		new CarrinhoDAO().adiciona(carrinho);

		URI uri = URI.create("/carrinhos/" + carrinho.getId());
		return Response.created(uri).build();

	}

}
