package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public abstract class Predicado extends Nodo {

	private ReteWorkingMemory rwm;

	private String nombre;

	public Predicado(String nombre, Integer cantidadParamentros) {
		nombre = Character.toLowerCase(nombre.charAt(0)) + nombre.substring(1, nombre.length());
		this.nombre = nombre + getStringParametros(cantidadParamentros);
	}

	@Override
	public void propagarHechos(List<List<Hecho>> hechos) {
		List<Hecho> hs = new ArrayList<>();
		rwm.query(nombre).stream().forEach(map -> {
			List<Object> valores = new ArrayList<>();
			for(Entry<String, String> hecho: map.entrySet()){
				valores.set(this.number(hecho.getKey()), hecho.getValue());
			}
			hs.add(new Hecho(valores));
		});
		hechos.add(hs);

		super.propagarHechos(hechos);
	}

	private String getStringParametros(Integer cantidadParamentros) {
		StringBuilder retorno = new StringBuilder("(");
		for(int i = 1; i < cantidadParamentros; i++){
			retorno.append(alpha(i)).append(",");
		}
		if(cantidadParamentros > 0){
			retorno.append(alpha(cantidadParamentros));
		}
		return retorno.append(")").toString();
	}

	private static char[] letras;
	static{
		letras = new char['Z' - 'A' + 1];
		for(char i = 'A'; i <= 'Z'; i++){
			letras[i - 'A'] = i;
		}
	}

	private StringBuilder alpha(int i) {
		char r = letras[--i % letras.length];
		int n = i / letras.length;
		return n == 0 ? new StringBuilder().append(r) : alpha(n).append(r);
	}

	private Integer number(String s) {
		return numberRec(s.toCharArray(), s.length() - 1);
	}

	private Integer numberRec(char[] s, int i) {
		if(i < 0){
			return 0;
		}
		char letra = s[i];
		int n = letra - 'A' + 1;
		return numberRec(s, i - 1) * letras.length + n;
	}
}
