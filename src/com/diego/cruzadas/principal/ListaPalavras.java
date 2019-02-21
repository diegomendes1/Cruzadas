package com.diego.cruzadas.principal;

import java.util.ArrayList;

public class ListaPalavras {
	public ArrayList<PalavraAdicionada> lista;
	public int tamanho;
	
	public ListaPalavras(int tamanho) {
		lista = new ArrayList<PalavraAdicionada>();
		this.tamanho = tamanho;
	}

}
