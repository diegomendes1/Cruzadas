package com.diego.cruzadas.principal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Utilitario {
	public Utilitario() {
		
	}
	
	public static ArrayList<String> carregarPalavras(int maxNumLetras, int numPalavras, boolean isPalavras){
		if(isPalavras) {
			return carregarPalavrasDoArquivo(maxNumLetras, numPalavras);
		}else {
			return gerarListaNumeros(maxNumLetras, numPalavras);
		}
	}
	
	public static ArrayList<String> gerarListaNumeros(int maxNumAlgarismos, int qtdNumeros){
		ArrayList<String> numerosGerados = new ArrayList<String>();
		
		Random random = new Random();
		for(int i = 0; i < qtdNumeros; i++) {
			String novoNumero = "";
			int tamanho = random.nextInt(maxNumAlgarismos-2) + 2;
			for(int j = 0; j < tamanho; j++) {
				novoNumero += random.nextInt(10);
			}
			numerosGerados.add(novoNumero);
		}
		
		Collections.sort(numerosGerados, new Comparador());
		return numerosGerados;
	}
	
	public static ArrayList<String> carregarPalavrasDoArquivo(int maxNumLetras, int numPalavras){
		ArrayList<String> palavrasCarregadas = carregarArquivos(maxNumLetras, numPalavras);
		Collections.sort(palavrasCarregadas, new Comparador());
		return palavrasCarregadas;
	}
	
	public static ArrayList<String> carregarArquivos(int maxNumLetras, int numPalavras) {
		Map<String,Integer> map = new HashMap<String,Integer>();
	     while(map.size() < numPalavras){
	        String s;
			try {
				s = choose(new File("src/com/diego/cruzadas/resources/palavras.txt"));
				if(s.length() < maxNumLetras) {
					if(!map.containsKey(s)) {
						map.put(s, s.length());
					} 
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	        
	     }
	     
	     ArrayList<String> palavrasSelecionadas = new ArrayList<String>();

	     System.out.println(map);
	     map.forEach((k,v) -> palavrasSelecionadas.add(k));
	     
	     return palavrasSelecionadas;
	}
	
	public static String choose(File f) throws FileNotFoundException
	  {
	     String result = null;
	     Random rand = new Random();
	     int n = 0;
	     for(Scanner sc = new Scanner(f); sc.hasNext(); )
	     {
	        ++n;
	        String line = sc.nextLine();
	        if(rand.nextInt(n) == 0)
	           result = line;         
	     }

	     return result;      
	  }
}
