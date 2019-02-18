package com.diego.cruzadas.principal;

public class Casa {
	private char letra;
	public boolean isOcupada;
	public boolean isInativa; //se nao tiver como fazer letras em comum com esta casa
	
	public Casa(char letra, boolean isOcupada) {
		this.letra = letra;
		this.isOcupada = isOcupada;
	}
	
	public char getLetra() {
		return this.letra;
	}
	
	public void setLetra(char letra) {
		this.letra = letra;
	}
}
