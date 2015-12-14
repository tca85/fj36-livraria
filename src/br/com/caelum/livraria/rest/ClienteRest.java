package br.com.caelum.livraria.rest;

import java.io.Serializable;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.caelum.livraria.modelo.Link;
import br.com.caelum.livraria.modelo.Pagamento;
import br.com.caelum.livraria.modelo.Transacao;

/**
 * http://localhost:8081/fj36-livraria
 * 
 * Escolha um livro, adicione no carrinho e insira os dados do cartão
 * Pague com payfast. Finalize o pedido, e depois teste a url do serviço
 * 
 * http://localhost:8080/fj36-webservice/pagamentos/2
 * 
 * 
 * @author tca85
 *
 */
@Component
@Scope("request")
public class ClienteRest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String SERVER_URI = "http://localhost:8080/fj36-webservice";
	private static final String ENTRY_POINT = "/pagamentos/";

	//---------------------------------------------------------------------------------------------
	/**
	 * Faz a chamada do serviço REST
	 * 
	 * @param transacao
	 * @return
	 */
	public Pagamento criarPagamento(Transacao transacao) {
		Client cliente = ClientBuilder.newClient();
		
		Pagamento resposta = cliente.target(SERVER_URI + ENTRY_POINT)
		                            .request()
		                            .buildPost(Entity.json(transacao))
	                                .invoke(Pagamento.class);
		
		System.out.println("Pagamento criado com o id " + resposta.getId());
		
		return resposta;
	}

	//---------------------------------------------------------------------------------------------
	/**
	 * Faz uma chamada remota utilizando os dados do pagamento criado
	 * 
	 * @param pagamento
	 * @return
	 */
	public Pagamento confirmarPagamento(Pagamento pagamento) {
		Link linkConfirmar = pagamento.getLinkPeloRel("confirmar");
		
		Client cliente = ClientBuilder.newClient();
		
		Pagamento resposta = cliente.target(SERVER_URI + linkConfirmar.getUri())
                                    .request()
                                    .build(linkConfirmar.getMethod())
                                    .invoke(Pagamento.class);
		
		System.out.println("Pagamento confirmado com o id " + resposta.getId());
		
		return resposta;
	}
	//---------------------------------------------------------------------------------------------
}
