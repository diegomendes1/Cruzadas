package com.diego.cruzadas.principal;

public class PalavraAdicionada {
	//Coordenadas da primeira letra
	public int posicaoX, posicaoY;
	public String palavra;
	public boolean isVertical;
	public Casa[] casas;
	public boolean isPrincipal;
	
	public PalavraAdicionada(int posicaoX, int posicaoY, String palavra, boolean isVertical, boolean isPrincipal) {
		this.posicaoX = posicaoX;
		this.posicaoY = posicaoY;
		this.palavra = palavra;
		this.isVertical = isVertical;
		this.isPrincipal = isPrincipal;
		casas = new Casa[palavra.length()];
	}
	
	public int getXPosicaoLetra(int posicaoLetra) {
		if(isVertical) {
			return posicaoX + posicaoLetra;
		}else {
			return posicaoX;
		}
	}
	
	public int getYPosicaoLetra(int posicaoLetra) {
		if(isVertical) {
			return posicaoY;
		}else {
			return posicaoY + posicaoLetra;
		}
	}
}
