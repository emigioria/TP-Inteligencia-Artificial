package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.List;

public class Calle {
	private List<Arista> tramos = new ArrayList<>();
	private String nombre;

	public Calle(String nombre) {
		super();
		this.nombre = nombre;
	}

	public List<Arista> getTramos() {
		return tramos;
	}

	public void setTramos(List<Arista> tramos) {
		this.tramos = tramos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
