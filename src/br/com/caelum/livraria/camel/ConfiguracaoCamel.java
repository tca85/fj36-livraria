package br.com.caelum.livraria.camel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Consumindo mensagens JMS com Apache Camel
 * 
 * Acessar a loja e finalizar uma compra:
 * http://localhost:8081/fj36-livraria
 * 
 * Log:
 * 19:15:09,541  INFO route1:96 - CAMEL Recebendo a MSG ID:40c7aa6f-a503-11e5-a78f-a5c83f0e15bf
 * 
 * @author tca85
 *
 */
@Component
public class ConfiguracaoCamel {

	// Injeta o CamelContext
	@Autowired
	private CamelContext context;

	//---------------------------------------------------------------------------------------------
	/**
	 * Configura as rotas que serão utilizadas pelo Camel
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	void init() throws Exception {
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("jms:topic:jms/TOPICO.LIVRARIA?username=jms&password=jms2")
				   .log(LoggingLevel.INFO, "CAMEL Recebendo a MSG ${id}")
				   .to("jms:queue:jms/FILA.GERADOR?username=jms&password=jms2");
			}
		});
		context.start();
	}
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Pára o CamelContext
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	void destroy() throws Exception {
		context.stop();
	}
	//---------------------------------------------------------------------------------------------
}