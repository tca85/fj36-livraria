package br.com.caelum.estoque.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * Serviço de estoque que fornece a quantidade de itens pelo código do produto
 * utilizando RMI
 * 
 * @author tca85
 *
 */
public interface EstoqueRMI extends Remote{
	
	public ItemEstoque getItemEstoque(String codigoProduto) throws RemoteException;
}