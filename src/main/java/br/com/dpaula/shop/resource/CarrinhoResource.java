package br.com.dpaula.shop.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import br.com.dpaula.shop.dao.CarrinhoDAO;
import br.com.dpaula.shop.modelo.Carrinho;

@Path("carrinhos")
public class CarrinhoResource {

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@QueryParam("id") long id) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);

		return carrinho.toXML();
	}

}
