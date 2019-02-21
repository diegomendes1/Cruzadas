package com.diego.cruzadas.ui;

import java.io.IOException;

import com.diego.cruzadas.principal.Tabuleiro;
import com.diego.cruzadas.principal.Utilitario;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class MenuController {
	public int tamanhoTabuleiro;
	public int numPalavras;
	public boolean usandoPalavras;
	
	@FXML
	public Slider sliderTamanhoTab;
	@FXML
	public Slider sliderNumPalavras;
	@FXML
	public CheckBox checkUsandoPalavras;
	
	@FXML
	public void initialize() {
		tamanhoTabuleiro = 15;
		numPalavras = 20;
		usandoPalavras = true;
		
		sliderTamanhoTab.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	tamanhoTabuleiro = newValue.intValue();
		    }
		});
		
		sliderNumPalavras.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	numPalavras = newValue.intValue();
		    }
		});
	}
	
	@FXML
	public void iniciarProcesso() {
		Utilitario.escolherInformacoesIniciais();
		
		Tabuleiro tabuleiro = new Tabuleiro(tamanhoTabuleiro, numPalavras*2, usandoPalavras);
		tabuleiro.gerarTabuleiro();
		tabuleiro.imprimirTabuleiro();
		carregarResultados(tabuleiro);
	}
	
	public void carregarResultados(Tabuleiro tabuleiro) {
		//Parent root = FXMLLoader.load(getClass().getResource("../ui/Resultados.fxml"));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/Resultados.fxml"));
		ResultadosController resController = new ResultadosController(tabuleiro);
		loader.setController(resController);
		
		Parent root;
		try {
			root = loader.load();
			Stage atual = (Stage)checkUsandoPalavras.getScene().getWindow();
	        atual.setScene(new Scene(root, 800, 600));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void atualizarUsandoPalavras() {
		usandoPalavras = checkUsandoPalavras.isSelected();
	}
}