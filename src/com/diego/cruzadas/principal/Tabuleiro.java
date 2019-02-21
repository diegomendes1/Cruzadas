package com.diego.cruzadas.principal;

import java.util.ArrayList;
import java.util.Random;

public class Tabuleiro {
	public int tamanho, numPalavras;
	public ArrayList<String> faltam = new ArrayList<String>();
	public ArrayList<PalavraAdicionada> adicionadas = new ArrayList<PalavraAdicionada>();
	public ArrayList<String> puladas = new ArrayList<String>();
	public Casa[][] tabuleiro;
	public boolean isPalavras;
	public boolean palavrasJaTrocadas = false;
	
	public Verificacoes verif = new Verificacoes(this);
	
	public Tabuleiro(int tamanho, int numPalavras, boolean isPalavras) {
		this.tamanho = tamanho;
		this.isPalavras = isPalavras;
		this.numPalavras = numPalavras;
		tabuleiro = new Casa[tamanho][tamanho];
	}
	
	public void gerarTabuleiro() {
		gerarTabuleiroVazio();
		
		faltam.clear();
		adicionadas.clear();
		puladas.clear();
		palavrasJaTrocadas = false;
		
		faltam = Utilitario.carregarPalavras(tamanho, numPalavras, isPalavras);
		posicionarPalavraInicial();
		posicionarPalavras();
	}
	
	public void posicionarPalavraInicial() {
		String palavraInicial = escolherUmaPalavra();
		int tamanhoPalavra = palavraInicial.length();
		if(tamanhoPalavra > tamanho) {
			return;
		}
		
		//Para a palavra inicial ser horizontal, basta trocar Y por X, e mudar o boolean para false
		int espacoDisponivelY = tamanho/2;
		int espacoDisponivelX = tamanho-tamanhoPalavra;
		espacoDisponivelX /=2;
		Coordenada posicao = new Coordenada(espacoDisponivelX, espacoDisponivelY, true);
		adicionarPalavraTabuleiro(palavraInicial, posicao);
		adicionadas.get(0).isPrincipal = true;
	}
	
	public String escolherUmaPalavra() {
		if(faltam.size() > 4) {
			Random random = new Random();
			int num = random.nextInt(faltam.size()/2);
			return faltam.get(num);
		}else {
			return faltam.get(0);
		}
		
	}
	
	public void posicionarPalavras() {
		boolean isVertical = true;
		
		boolean segundaTentativa = false;
		
		String palavra = "";
		
		while(!faltam.isEmpty()){
			if(adicionadas.size() == numPalavras/2) {
				return;
			}
			
			if(!segundaTentativa) {
				palavra = escolherUmaPalavra();
			}
			
			isVertical = !isVertical;
			
			Coordenada posicaoPossivel = verif.verifDisponibilidade(palavra, isVertical);
			
			if(posicaoPossivel == null) {
				if(!segundaTentativa) {
					segundaTentativa = true;
				}else {
					segundaTentativa = false;
					atualizarListasFaltamParaPuladas(palavra);
				}
			}else {
				segundaTentativa = false;
				adicionarPalavraTabuleiro(palavra, posicaoPossivel);
				
				//Este if pode ser revisto, caso possa flexibilizar a quantidade de palavras
				if(faltam.isEmpty()) {
					if(!palavrasJaTrocadas) {
						trocarPalavrasPuladas();
					}
				}else if(!puladas.isEmpty()){
					tentarPosicionarPuladas(isVertical);
				}
			}
		}
	}
	
	//Procura outras palavras para adicionar no lugar das puladas. Pode ser retirada no futuro
	public void trocarPalavrasPuladas() {
		if(puladas.isEmpty()) {
			return;
		}else {
			int palavrasQueFaltam = numPalavras - adicionadas.size();
			//faltam esta vazia, adiciona a mesma quantidade de palavras que foram puladas
			faltam.addAll(Utilitario.carregarPalavras(tamanho, palavrasQueFaltam, isPalavras));
			puladas.clear();
			palavrasJaTrocadas = true;
		}
	}

	public void tentarPosicionarPuladas(boolean isVertical) {
		ArrayList<String> palavrasAdicionadas = new ArrayList<String>();
		for(String palavra : puladas) {
			Coordenada posicaoPossivel = verif.verifDisponibilidade(palavra, isVertical);
			if(posicaoPossivel != null) {
				adicionarPalavraTabuleiro(palavra, posicaoPossivel);
				palavrasAdicionadas.add(palavra);
			}
		}
		
		puladas.removeAll(palavrasAdicionadas);
	}
	
	public void adicionarPalavraTabuleiro(String palavra, Coordenada posicao) {
		int posicaoX = posicao.x;
		int posicaoY = posicao.y;
		int letraAtual = 0;  
		if(posicao.isVertical) {
			for(int i = posicaoX; i < posicaoX+palavra.length(); i++) {
				if(adicionadas.isEmpty()) {
					tabuleiro[i][posicaoY].isPrincipal = true;
				}
				
				if(!tabuleiro[i][posicaoY].isOcupada) {
					tabuleiro[i][posicaoY].setLetra(palavra.charAt(letraAtual));
					tabuleiro[i][posicaoY].isOcupada = true;
					letraAtual++;
				}else if(tabuleiro[i][posicaoY].getLetra() == palavra.charAt(letraAtual)){
					letraAtual++;
				}
			}
		}else {
			for(int j = posicaoY; j < posicaoY+palavra.length();j++) {
				if(adicionadas.isEmpty()) {
					tabuleiro[posicaoX][j].isPrincipal = true;
				}
				
				if(!tabuleiro[posicaoX][j].isOcupada) {
					tabuleiro[posicaoX][j].setLetra(palavra.charAt(letraAtual));
					tabuleiro[posicaoX][j].isOcupada = true;
					letraAtual++;
				}else if(tabuleiro[posicaoX][j].getLetra() == palavra.charAt(letraAtual)) {
					letraAtual++;
				}
			}
		}
		atualizarListasParaAdicionadas(posicao.x, posicao.y, palavra, posicao.isVertical);
	}
	
	public void atualizarListasParaAdicionadas(int x, int y, String palavra, boolean isVertical) {
		if(faltam.contains(palavra)) {
			faltam.remove(palavra);
		}
		
		PalavraAdicionada novaPalavra = new PalavraAdicionada(x, y, palavra, isVertical, false);
		
		if(isVertical) {
			
			for(int i = x; i < x+palavra.length(); i++) {
				novaPalavra.casas[i-x] = tabuleiro[i][y];
			}
		}else {
			for(int j = y; j < y+palavra.length(); j++) {
				novaPalavra.casas[j-y] = tabuleiro[x][j];
			}
		}
		
		adicionadas.add(novaPalavra);
	}
	
	public void atualizarListasFaltamParaPuladas(String palavra) {
		faltam.remove(palavra);
		puladas.add(palavra);
	}
	
	public void gerarTabuleiroVazio() {
		for(int i = 0; i < tamanho; i++) {
			for(int j = 0; j < tamanho; j++) {
				tabuleiro[i][j] = new Casa(' ', false, false);
			}
		}
	}
	
	public void imprimirTabuleiro() {
		System.out.println("------------------------------");
		System.out.println("Palavras Adicionadas: ");
		for(PalavraAdicionada palavra : adicionadas) {
			System.out.println(palavra.palavra);
		}
		System.out.println("   ---   ");
		
		System.out.println("Palavras Puladas");
		for(String palavra : puladas) {
			System.out.println(palavra);
		}
		System.out.println("   ---   ");
		
		for(int i = 0; i < tamanho; i++) {
			for(int j = 0; j < tamanho; j++) {
				System.out.print(tabuleiro[i][j].getLetra() + " ");
			}
			System.out.println();
		}
		
		System.out.println("------------------------------");
	}
}