package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Mapa {
	private Double alto = 0.0;
	private Double ancho = 0.0;

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

	public Double getAlto() {
		return alto;
	}

	public void setAlto(Double alto) {
		this.alto = alto;
	}

	public Double getAncho() {
		return ancho;
	}

	public void setAncho(Double ancho) {
		this.ancho = ancho;
	}

	public Set<Obstaculo> getObstaculos() {
		return Stream.concat(
				esquinas.stream().map(t -> t.getObstaculos()).flatMap(List::stream),
				calles.stream().map(t -> t.getTramos()).flatMap(List::stream).map(t -> t.getObstaculos()).flatMap(List::stream))
				.collect(Collectors.toSet());
	}

	public Lugar getLugar(Lugar lugar) {
		try{
			return esquinas.stream().filter(l -> l.equals(lugar)).findFirst().get();
		} catch(NoSuchElementException e){
			return calles.stream().map(t -> t.getTramos()).flatMap(List::stream).filter(l -> l.equals(lugar)).findFirst().get();
		}
	}
}
