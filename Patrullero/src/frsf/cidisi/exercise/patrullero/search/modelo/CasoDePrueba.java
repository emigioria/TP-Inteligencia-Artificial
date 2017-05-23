package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.List;

public class CasoDePrueba {

	private List<Obstaculo> obstaculos = new ArrayList<>();

	private Interseccion posicionInicialPatrullero;

	private Interseccion posicionIncidente;

	private TipoIncidente tipoIncidente;

	public List<Obstaculo> getObstaculos() {
		return obstaculos;
	}

	public void setObstaculos(List<Obstaculo> obstaculos) {
		this.obstaculos = obstaculos;
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

	public TipoIncidente getTipoIncidente() {
		return tipoIncidente;
	}

	public void setTipoIncidentes(TipoIncidente tipoIncidentes) {
		this.tipoIncidente = tipoIncidentes;
	}

}
