package br.com.caelum.livraria.jms;

import java.io.Serializable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import br.com.caelum.livraria.jaxb.SerializadorXml;
import br.com.caelum.livraria.modelo.Pedido;

/**
 * Para testar:
 * http://localhost:8081/fj36-livraria
 * - Escolha um livro e finalize a compra
 * 
 * @author tca85
 *
 */
@Component
@Lazy(true)
public class EnviadorMensagemJms implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ConnectionFactory factory;
	
	@Autowired
	private Topic topico;

	//---------------------------------------------------------------------------------------------
	/**
	 * Envia o pedido para o tópico jms/TOPICO.LIVRARIA
	 * 
	 * @param pedido
	 */
	public void enviar(Pedido pedido) {
		System.out.println("JMS: Enviando pedido: " + pedido);
		
		try(JMSContext context = factory.createContext("jms", "jms2")){
			JMSProducer producer = context.createProducer();
			producer.setProperty("formato", pedido.getFormato());
			
			//producer.send(topico, pedido.toString());
			String xml = new SerializadorXml().toXml(pedido);
			System.out.println(xml);
			producer.send(topico, xml);
		}
	} 
	//---------------------------------------------------------------------------------------------
}