package com.diego.cruzadas.principal;

import javafx.fxml.FXML;

public class MenuController {
	
	@FXML
	public void iniciarProcesso() {
		Tabuleiro tabuleiro = new Tabuleiro(15, 20, true);
		tabuleiro.gerarTabuleiro();
		tabuleiro.imprimirTabuleiro();
	}
}