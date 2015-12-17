package br.com.caelum.livraria.camel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Consumindo mensagens JMS com Apache Camel
 * 
 * Acessar a loja e finalizar uma compra: http://localhost:8081/fj36-livraria
 * 
 * Log: 19:15:09,541 INFO route1:96 - CAMEL Recebendo a MSG
 * ID:40c7aa6f-a503-11e5-a78f-a5c83f0e15bf
 * 
 * @author tca85
 *
 */
@Component
public class ConfiguracaoCamel {

	// Injeta o CamelContext
	@Autowired
	private CamelContext context;

	// ---------------------------------------------------------------------------------------------
	/**
	 * Configura as rotas que serão utilizadas pelo Camel
	 * 
	 * Envia somente os detalhes do item de formato E-book 7.21 - Filtro e
	 * divisão de conteúdo
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	void init() throws Exception {
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				  from("jms:topic:jms/TOPICO.LIVRARIA?username=jms&password=jms2").
				    unmarshal().string().
				    split().
				      tokenizeXML("item"). //tira cada item do pedido
				    log("Qtd de itens: ${property[CamelSplitSize]}").
				    filter().
				      xpath("/item/formato[text()='EBOOK']").
				    log("\n ${id} \n Item ebook: ${body}").
				    aggregate( //agregando itens Ebooks
				      simple("${id}"), //agrega tokens da mesma mensagens
				      new ConcatenaAggregationStrategy()). //concatena apenas
				    completionTimeout(2000l). //espera 2s
				    transform(body().prepend("<itens>").append("</itens>")).
				      log("Itens com EBooks apenas: ${body} ").
				  to("jms:queue:jms/FILA.GERADOR?username=jms&password=jms2");
			}
		});
		context.start();
	}

	// ---------------------------------------------------------------------------------------------
	/**
	 * Pára o CamelContext
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	void destroy() throws Exception {
		context.stop();
	}

	// ---------------------------------------------------------------------------------------------
	/**
	 * Classe de agregação usada no processo
	 * 
	 * https://gist.github.com/paulojribp/bcbd13b3ae9bf7a5fa67
	 * 
	 * @author tca85
	 *
	 */
	public class ConcatenaAggregationStrategy implements AggregationStrategy {
		public Exchange aggregate(Exchange msgAntiga, Exchange msgNova) {
			if (msgAntiga == null) {
				return msgNova;
			}
			String bodyAntigo = msgAntiga.getIn().getBody(String.class);
			String bodyNovo = msgNova.getIn().getBody(String.class);
			msgAntiga.getIn().setBody(bodyAntigo + bodyNovo);
			return msgAntiga;
		}
	}
	// ---------------------------------------------------------------------------------------------
}