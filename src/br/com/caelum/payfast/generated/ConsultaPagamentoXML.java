package br.com.caelum.payfast.generated;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Cliente JAX-RS com XML
 * 
 * Um arquivo XSD foi gerado no projeto fj36-webservice (GeraXSDPagamento),
 * depois o arquivo foi copiado para o fj36-livraria e 
 * as as classes baseadas nele foram geradas:
 * 
 * xjc pagamento.xsd -d src -p br.com.caelum.payfast.generated

 * 
 * @author tca85
 *
 */
public class ConsultaPagamentoXML {
	private static final String SERVER_URI = "http://localhost:8080/fj36-webservice";

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		
		Pagamento resposta = client.target(SERVER_URI + "pagamentos/1")
				                   .request()
				                   .buildGet()
				                   .invoke(Pagamento.class);
		
		System.out.printf("%d %f %s\n",
				          resposta.getId(),
				          resposta.getValor());
	}
}