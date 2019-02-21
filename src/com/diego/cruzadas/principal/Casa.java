package com.diego.cruzadas.principal;

public class Casa {
	private char letra;
	public boolean isOcupada;
	public boolean isInativa; //se nao tiver como fazer letras em comum com esta casa
	public boolean isPrincipal;
	
	public Casa(char letra, boolean isOcupada, boolean isPrincipal) {
		this.letra = letra;
		this.isOcupada = isOcupada;
		this.isPrincipal = isPrincipal;
	}
	
	public char getLetra() {
		return this.letra;
	}
	
	public void setLetra(char letra) {
		this.letra = letra;
	}
}
