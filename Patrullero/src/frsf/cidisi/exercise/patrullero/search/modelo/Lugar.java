package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Lugar {
	private List<Obstaculo> obstaculos = new ArrayList<>();
	private Long peso;

	public Lugar(Long peso) {
		this.peso = peso;
	}

	public Long getPeso() {
		return peso;
	}

	public void setPeso(Long peso) {
		this.peso = peso;
	}

	public List<Obstaculo> getObstaculos() {
		return obstaculos;
	}

	public void setObstaculos(List<Obstaculo> obstaculos) {
		this.obstaculos = obstaculos;
	}

	public boolean sosArista() {
		return false;
	}

	public boolean sosInterseccion() {
		return false;
	}
}
