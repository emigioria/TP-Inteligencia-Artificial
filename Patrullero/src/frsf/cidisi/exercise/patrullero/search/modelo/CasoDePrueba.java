package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.List;

public class CasoDePrueba {

	private List<Obstaculo> obstaculos = new ArrayList<>();

	private Interseccion posicionInicialPatrullero;

	private Interseccion posicionIncidente;

	public List<Obstaculo> getObstaculos() {
		return obstaculos;
	}

	public Interseccion getPosicionInicialPatrullero() {
		return posicionInicialPatrullero;
	}

	public void setPosicionInicialPatrullero(Interseccion posicionInicialPatrullero) {
		this.posicionInicialPatrullero = posicionInicialPatrullero;
	}

	public Interseccion getPosicionIncidente() {
		return posicionIncidente;
	}

	public void setPosicionIncidente(Interseccion posicionIncidente) {
		this.posicionIncidente = posicionIncidente;
	}
}
