package com.diego.cruzadas.principal;

import java.util.ArrayList;
import java.util.Random;

public class Verificacoes {
	Tabuleiro tab;
	
	public Verificacoes(Tabuleiro tab) {
		this.tab = tab;
	}
	
	public Coordenada verifDisponibilidade(String palavra, boolean isVertical) {
		ArrayList<Coordenada> possiveisPosicoes = contarPossiveisPosicoes(palavra, isVertical);
		
		if(possiveisPosicoes.isEmpty()) {
			return null;
		}
		
		possiveisPosicoes = removerPosicoesInadequadas(possiveisPosicoes, palavra);
		
		if(possiveisPosicoes.isEmpty()) {
			return null;
		}

		Random rand = new Random();
		int i = rand.nextInt(possiveisPosicoes.size());	
		return possiveisPosicoes.get(i);
	}
	
	public ArrayList<Coordenada> contarPossiveisPosicoes(String palavra, boolean isVertical){
		ArrayList<Coordenada> possiveisPosicoes = new ArrayList<Coordenada>();
		
		for(PalavraAdicionada palavraAdicionada : tab.adicionadas) {
			ArrayList<Coordenada> possiveisPosicoesDaPalavra = verifPalavra(palavra, isVertical, palavraAdicionada);
			
			if(!possiveisPosicoesDaPalavra.isEmpty()) {
				possiveisPosicoes.addAll(possiveisPosicoesDaPalavra);
			}
		}
		
		return possiveisPosicoes;
	}
	
	public ArrayList<Coordenada> verifPalavra(String palavra, boolean isVertical, PalavraAdicionada palavraAdicionada){
		ArrayList<Coordenada> possiveis = new ArrayList<Coordenada>();
		
		if(palavraAdicionada.isVertical != isVertical) { //a palavra que quero colocar estaria no sentido diferente da adicionada
			if(palavraAdicionada.isVertical) {
				for(int j = 0; j < palavra.length(); j++) {
					for(int i = palavraAdicionada.posicaoX; i < palavraAdicionada.posicaoX + palavraAdicionada.palavra.length(); i++) {
						if(!tab.tabuleiro[i][palavraAdicionada.posicaoY].isInativa && palavra.charAt(j) == palavraAdicionada.palavra.charAt(i - palavraAdicionada.posicaoX)) {
							possiveis.add(new Coordenada(i, palavraAdicionada.posicaoY-j, isVertical));
						}
					}
				}
			}else {
				for(int i = 0; i < palavra.length(); i++) {
					for(int j = palavraAdicionada.posicaoY; j < palavraAdicionada.posicaoY + palavraAdicionada.palavra.length(); j++) {
						if(!tab.tabuleiro[palavraAdicionada.posicaoX][j].isInativa && palavra.charAt(i) == palavraAdicionada.palavra.charAt(j - palavraAdicionada.posicaoY)) {
							possiveis.add(new Coordenada(palavraAdicionada.posicaoX - i, j, isVertical));
						}
					}
				}
			}
		}
		return possiveis;
	}
	
	public ArrayList<Coordenada> removerPosicoesInadequadas(ArrayList<Coordenada> posicoesPossiveis, String palavra){
		ArrayList<Coordenada> posicoesAdequadas = new ArrayList<Coordenada>();
		
		for(Coordenada coordenada : posicoesPossiveis) {
			if(verifLimites(coordenada.x, coordenada.y, palavra.length(), coordenada.isVertical) && verifCasasNoTabuleiro(coordenada, palavra)) {
				posicoesAdequadas.add(coordenada);
			}
		}
		
		return posicoesAdequadas;
	}
	
	public boolean verifCasasNoTabuleiro(Coordenada coordenada, String palavra) {
		//Verifica casa por casa se existe alguma letra em volta. se sim, ja retorna falso. se estiver tudo correto, verifica a ultima e primeira casa da palavra.
		int posInicialX = coordenada.x, posInicialY = coordenada.y;
		
		if(coordenada.isVertical) {
			for(int i = posInicialX; i < posInicialX + palavra.length(); i++) {
				
				if(tab.tabuleiro[i][posInicialY].isOcupada) {
					if(palavra.charAt(i-posInicialX) != tab.tabuleiro[i][posInicialY].getLetra()) {
						return false;
					}
				}else {
					if(posInicialY > 0) { //Verif. para Esquerda
						if(tab.tabuleiro[i][posInicialY-1].isOcupada) {
							return false;
						}
					}
						
					if(posInicialY < tab.tamanho-1) { //Verif. para Direita
						if(tab.tabuleiro[i][posInicialY+1].isOcupada) {
							return false;
						}
					}
				}
			}
			
			if(posInicialX > 0) {
				if(tab.tabuleiro[posInicialX-1][posInicialY].isOcupada) {
					return false;
				}
			}
			
			if(posInicialX + palavra.length() < tab.tamanho) {
				if(tab.tabuleiro[posInicialX + palavra.length()][posInicialY].isOcupada) {
					return false;
				}
			}
		}else {
			for(int j = posInicialY; j < posInicialY + palavra.length(); j++) {
				
				if(tab.tabuleiro[posInicialX][j].isOcupada) {
					if(palavra.charAt(j-posInicialY) != tab.tabuleiro[posInicialX][j].getLetra()) {
						return false;
					}
				}else {
					if(posInicialX > 0) { //Verif. para Cima
						if(tab.tabuleiro[posInicialX-1][j].isOcupada) {
							return false;
						}
					}
					
					if(posInicialX < tab.tamanho-1) { //Verif. para Baixo
						if(tab.tabuleiro[posInicialX+1][j].isOcupada) {
							return false;
						}
					}
				}
			}
			
			if(posInicialY > 0) {
				if(tab.tabuleiro[posInicialX][posInicialY-1].isOcupada) {
					return false;
				}
			}
			
			if(posInicialY + palavra.length() < tab.tamanho) {
				if(tab.tabuleiro[posInicialX][posInicialY + palavra.length()].isOcupada) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean verifLimites(int posInicialX, int posInicialY, int numLetras, boolean isVertical) {
		if(posInicialX < 0 || posInicialX >= tab.tamanho || posInicialY < 0 || posInicialY >= tab.tamanho) {
			return false; // fora dos limites do tabuleiro
		}
		
		if(isVertical) {
			if((posInicialX + numLetras)-1 >= tab.tamanho) {
				return false; //A palavra deveria estar mais acima para caber no tabuleiro
			}
		}else {
			if((posInicialY + numLetras)-1 >= tab.tamanho) {
				return false; //A palavra deveria estar mais para a esquerda para caber no tabuleiro
			}
		}
		return true;
	}
}