package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;

public abstract class Predicado extends Nodo {

	private ReteWorkingMemory rwm;

	private String nombre;

	public Predicado(String nombre, Integer cantidadParamentros) {
		nombre = Character.toLowerCase(nombre.charAt(0)) + nombre.substring(1, nombre.length());
		this.nombre = nombre + getStringParametros(cantidadParamentros);
	}

	@Override
	public void propagarHechos(List<List<Hecho>> hechos) {
		rwm.query(nombre).stream().forEach(map -> {

		});

		super.propagarHechos(hechos);
	}

	private String getStringParametros(Integer cantidadParamentros) {
		StringBuilder retorno = new StringBuilder("(");
		for(int i = 1; i < cantidadParamentros; i++){
			retorno.append(alpha(i)).append(",");
		}
		retorno.append(alpha(cantidadParamentros)).append(")");
		return retorno.toString();
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
}
