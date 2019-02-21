package com.diego.cruzadas.ui;

import java.io.File;
import java.io.IOException;

import com.diego.cruzadas.principal.Tabuleiro;
import com.diego.cruzadas.principal.Utilitario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ResultadosController {
	public Tabuleiro tabuleiro;
	
	@FXML
	public GridPane gridPane; // (y, x), ao contrario dos outros 
	@FXML
	public TextField textoPasta;
	
	public ResultadosController(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}
	
	@FXML
	public void initialize() {
		mostrarTabuleiro(tabuleiro.tamanho);
		Utilitario.escolherInformacoesIniciais();
		textoPasta.setText(Utilitario.caminhoParaPasta);
	}
	
	@FXML
	public void voltarMenu() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
		
		Parent root;
		try {
			root = loader.load();
			Stage atual = (Stage)gridPane.getScene().getWindow();
	        atual.setScene(new Scene(root, 800, 600));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void recriarTabuleiro() {
		tabuleiro.gerarTabuleiro();
		tabuleiro.imprimirTabuleiro();
		mostrarTabuleiro(tabuleiro.tamanho);
	}
	@FXML
	public void salvarPDFNormal() {
		Utilitario.salvarArquivoNormal(tabuleiro);
	}
	
	@FXML
	public void salvarPDFRespondido() {
		Utilitario.salvarArquivoRespondido(tabuleiro);
	}
	
	@FXML
	public void escolherNovaPasta() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("JavaFX Projects");
		File selectedDirectory = chooser.showDialog((Stage)gridPane.getScene().getWindow());
		
		if(selectedDirectory != null) {
			Utilitario.salvarDados(selectedDirectory.getAbsolutePath(), Utilitario.idAtual);
			textoPasta.setText(selectedDirectory.getAbsolutePath());
		}
	}
	
	public void mostrarTabuleiro(int tamanho) {
		while(gridPane.getRowConstraints().size() > 0){
			gridPane.getRowConstraints().remove(0);
		}

		while(gridPane.getColumnConstraints().size() > 0){
			gridPane.getColumnConstraints().remove(0);
		}
		
		for(int i = 0; i < tamanho; i++) {
			for(int j = 0; j < tamanho; j++) {
				Button btn = new Button();
				btn.setPrefHeight(500/tamanho);
				btn.setPrefWidth(500/tamanho);
				
				if(tabuleiro.tabuleiro[i][j].isOcupada) {
					btn.setText((tabuleiro.tabuleiro[i][j].getLetra() + "").toUpperCase());
					
					Font font = new Font(200/tamanho);
					btn.setFont(font);
					btn.setBackground(new Background(new BackgroundFill(Color.web("c4c4c4"), new CornerRadii(0), new Insets(1,1,1,1))));
				}else {
					btn.setBackground(new Background(new BackgroundFill(Color.web("FFFFFF"), new CornerRadii(0), Insets.EMPTY)));
				}
				
				gridPane.add(btn, j, i);
			}
		}
	}
}
