package com.diego.cruzadas.principal;

public class Comparador implements java.util.Comparator<String>{

	@Override
	public int compare(String a, String b) {
		if(a.length() > b.length()) {
			return -1;
		}else if(a.length() < b.length()) {
			return 1;
		}else {
			return 0;
		}
	}

}
