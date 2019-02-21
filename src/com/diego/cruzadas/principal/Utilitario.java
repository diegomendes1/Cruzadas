package com.diego.cruzadas.principal;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTable.XWPFBorderType;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;

public class Utilitario {
	public static String caminhoParaPasta;
	public static int idAtual;
	
	public Utilitario() {
		
	}
	
	public static ArrayList<String> carregarPalavras(int maxNumLetras, int numPalavras, boolean isPalavras){
		idAtual++;
		salvarDados(caminhoParaPasta, idAtual);
		
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
				s = s.replaceAll("[^A-Za-z0-9]", "");
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
	
	public static String encontrarDesktopParaSalvar() {
		String machine_name;
		String path_to_desktop;
		try {
			machine_name = InetAddress.getLocalHost().getHostName();
			path_to_desktop = "C:/Documents and Settings/"+machine_name+"/Desktop/Palavras Cruzadas";
			
			(new File(path_to_desktop)).mkdirs();
			
			return path_to_desktop;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String encontrarNomeParaSalvar() {
		return "Cruzadinha" + idAtual;
	}
	
	public static XWPFDocument gerarTabelaComum(Tabuleiro tab) {
		XWPFDocument doc = new XWPFDocument();
		
		XWPFTable tabela = doc.createTable(tab.tamanho, tab.tamanho);
		tabela.setWidth(8000);
		int alturaCasa = tabela.getWidth()/tab.tamanho;
		
		setTableAlign(tabela, ParagraphAlignment.CENTER);
		
		 tabela.setBottomBorder(XWPFBorderType.SINGLE, 15, 0, "FFFFFF");
		    tabela.setTopBorder(XWPFBorderType.SINGLE, 15, 0, "FFFFFF");
		   tabela.setLeftBorder(XWPFBorderType.SINGLE, 15, 0, "FFFFFF");
		  tabela.setRightBorder(XWPFBorderType.SINGLE, 15, 0, "FFFFFF");
		tabela.setInsideHBorder(XWPFBorderType.SINGLE, 15, 0, "FFFFFF");
		tabela.setInsideVBorder(XWPFBorderType.SINGLE, 15, 0, "FFFFFF");

		for(int i = 0; i < tab.tamanho; i++) {
			tabela.getRow(i).setHeight(alturaCasa);
			for(int j = 0; j < tab.tamanho; j++) {
				tabela.getRow(i).getCell(j).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
				if(tab.tabuleiro[i][j].isOcupada) {
					tabela.getRow(i).getCell(j).setColor("c4c4c4");
					
					if(tab.tabuleiro[i][j].isPrincipal) {
						tabela.getRow(i).getCell(j).removeParagraph(0);
						XWPFParagraph paragrafo = tabela.getRow(i).getCell(j).addParagraph();
						paragrafo.setAlignment(ParagraphAlignment.CENTER);
						XWPFRun run = paragrafo.createRun();
						run.setText((tab.tabuleiro[i][j].getLetra() + "").toUpperCase());
						run.setFontSize(12);
						tabela.getRow(i).getCell(j).setVerticalAlignment(XWPFVertAlign.CENTER);
					}
				}else {
					tabela.getRow(i).getCell(j).setColor("FFFFFF");
					tabela.getRow(i).getCell(j).setVerticalAlignment(XWPFVertAlign.CENTER);
				}
			}
		}
		
		XWPFParagraph paragrafo = doc.createParagraph();
		XWPFRun run = paragrafo.createRun();
		run.addBreak();
		
		return mostrarPalavras(doc, tab);
	}
	
	public static XWPFDocument mostrarPalavras(XWPFDocument documento, Tabuleiro tab) {
		XWPFDocument doc = documento;
		
		XWPFTable tabelaPalavras;
		
		ArrayList<ListaPalavras> palavrasOrganizadas = separarPalavrasTamanho(tab.adicionadas);
		
		tabelaPalavras = doc.createTable(8, 8);
				
		tabelaPalavras.setCellMargins(0, 50, 0, 50);
		tabelaPalavras.setWidth(8000);
		setTableAlign(tabelaPalavras, ParagraphAlignment.CENTER);
		tabelaPalavras.getCTTbl().getTblPr().unsetTblBorders();
		
		int x = 0, y = 0;
		for(ListaPalavras lista: palavrasOrganizadas) {
			if(x > 5) {
				x = 0;
				y++;
			}
					
			XWPFParagraph paragrafo = tabelaPalavras.getRow(x).getCell(y).addParagraph();
			XWPFRun runParagrafo = paragrafo.createRun();
			runParagrafo.setText(lista.tamanho +  " Letras");
			runParagrafo.setBold(true);
			runParagrafo.setFontSize(14);
						
			tabelaPalavras.getRow(x).getCell(y).removeParagraph(0);
					
			x++;
			for(PalavraAdicionada palavra : lista.lista) {
				tabelaPalavras.getRow(x).getCell(y).removeParagraph(0);
					
				XWPFParagraph paragrafoPalavra = tabelaPalavras.getRow(x).getCell(y).addParagraph();
				XWPFRun runPalavra = paragrafoPalavra.createRun();
				runPalavra.setText(palavra.palavra.toUpperCase());
				runPalavra.setFontSize(11);
				
				
				if(palavra.isPrincipal) {
					runPalavra.setStrikeThrough(true);
				}
					
				if(x == 7) {
					x = 0;
					y++;
				}else {
					x++;
				}
			}
				
			if(x>0) {
				x++;
			}
		}
				
		return doc;
	}
	
	public static void salvarArquivoNormal(Tabuleiro tab){
		XWPFDocument doc = gerarTabelaComum(tab);
		
		try {
			FileOutputStream output = new FileOutputStream(caminhoParaPasta + "/" + encontrarNomeParaSalvar() + ".docx");
			System.out.println("Salvando arquivo: " + caminhoParaPasta + "/" + encontrarNomeParaSalvar() + ".docx");
			doc.write(output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void salvarArquivoRespondido(Tabuleiro tab) {
		XWPFDocument doc = gerarTabelaComum(tab);
		
		List<XWPFTable> tabelas = doc.getTables();
		
		for(XWPFTable tabela : tabelas) {
			for(int i = 0; i < tab.tamanho; i++) {
				for(int j = 0; j < tab.tamanho; j++) {
					if(tab.tabuleiro[i][j].isOcupada) {
						tabela.getRow(i).getCell(j).removeParagraph(0);
						XWPFParagraph paragrafo = tabela.getRow(i).getCell(j).addParagraph();
						paragrafo.setAlignment(ParagraphAlignment.CENTER);
						paragrafo.setVerticalAlignment(TextAlignment.CENTER);
						XWPFRun run = paragrafo.createRun();
						run.setText((tab.tabuleiro[i][j].getLetra() + "").toUpperCase());
						tabela.getRow(i).getCell(j).setVerticalAlignment(XWPFVertAlign.CENTER);
					}
				}
			}
			break;
		}
		
		try {
			FileOutputStream output = new FileOutputStream(caminhoParaPasta + "/" + encontrarNomeParaSalvar() + "_respondido.docx");
			System.out.println("Salvando arquivo: " + caminhoParaPasta + "/" + encontrarNomeParaSalvar() + "_respondido.docx");
			doc.write(output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<ListaPalavras> separarPalavrasTamanho(ArrayList<PalavraAdicionada> adicionadas){
		ArrayList<ListaPalavras> resultado = new ArrayList<ListaPalavras>();
		
		for(PalavraAdicionada palavraAdc : adicionadas) {
			int tamanho = palavraAdc.palavra.length();
			boolean adicionou = false;
			for(ListaPalavras lista : resultado) {
				if(lista.tamanho == tamanho) {
					lista.lista.add(palavraAdc);
					adicionou = true;
					break;
				}
			}
			
			if(!adicionou) {
				ListaPalavras novaLista = new ListaPalavras(tamanho);
				novaLista.lista.add(palavraAdc);
				resultado.add(novaLista);
			}
		}
		
		return resultado;
	}
	
	public static void setTableAlign(XWPFTable table,ParagraphAlignment align) {
	    CTTblPr tblPr = table.getCTTbl().getTblPr();
	    CTJc jc = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
	    STJc.Enum en = STJc.Enum.forInt(align.getValue());
	    jc.setVal(en);
	}
	
	public static void escolherInformacoesIniciais() {
		Properties prop = new Properties();
		
		try {
			FileInputStream input = new FileInputStream("config.properties");
			prop.load(input);
			
			idAtual = Integer.parseInt(prop.getProperty("idAtual", "-1"));
			caminhoParaPasta = prop.getProperty("caminho", encontrarDesktopParaSalvar());
			
			if(idAtual == -1) {
				idAtual = 0;
				salvarDados(caminhoParaPasta, idAtual);
			}
			
			input.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void salvarDados(String caminho, int idAtual) {
		Properties prop = new Properties();
		OutputStream output = null;
		
		try {
			output = new FileOutputStream("config.properties");
			
			prop.setProperty("idAtual", idAtual + "");
			prop.setProperty("caminho", caminho);
			caminhoParaPasta = caminho;
			
			prop.store(output, "Projeto Cruzadas, feito inteiramente por: Diego Mendes");
			
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
