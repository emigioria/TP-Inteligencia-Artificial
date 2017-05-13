package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.List;

public class Interseccion extends Lugar {
	private Double peso;
	private List<Arista> entrantes = new ArrayList<>();
	private List<Arista> salientes = new ArrayList<>();

	public Interseccion(Double peso) {
		super();
		this.peso = peso;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public List<Arista> getEntrantes() {
		return entrantes;
	}

	public void setEntrantes(List<Arista> entrantes) {
		this.entrantes = entrantes;
	}

	public List<Arista> getSalientes() {
		return salientes;
	}

	public void setSalientes(List<Arista> salientes) {
		this.salientes = salientes;
	}

	@Override
	public boolean sosInterseccion() {
		return true;
	}
}
