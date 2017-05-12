package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.List;

public class Mapa {
	private List<Interseccion> esquinas = new ArrayList<>();
	private List<Calle> calles = new ArrayList<>();

	public Mapa() {
		super();
	}

	public List<Interseccion> getEsquinas() {
		return esquinas;
	}

	public void setEsquinas(List<Interseccion> esquinas) {
		this.esquinas = esquinas;
	}

	public List<Calle> getCalles() {
		return calles;
	}

	public void setCalles(List<Calle> calles) {
		this.calles = calles;
	}
}
