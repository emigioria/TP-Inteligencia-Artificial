package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import java.util.List;

import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.NombreObstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.ObstaculoParcial;
import frsf.cidisi.exercise.patrullero.search.modelo.ObstaculoTotal;
import frsf.cidisi.exercise.patrullero.search.modelo.Visibilidad;

public class CasoDePrueba {

	protected static Long ultimoIdAsignadoObstaculo = 0L;

	private List<Obstaculo> obstaculos;

	private Interseccion posicionInicialPatrullero;

	private Interseccion posicionIncidente;

	public static ObstaculoParcial crearObstaculoParcial(NombreObstaculo nombre, Integer tiempoInicio, Integer tiempoFin, Visibilidad visibilidad, Lugar lugar, Integer retardoMultiplicativo) {
		return new ObstaculoParcial(++ultimoIdAsignadoObstaculo, nombre, tiempoInicio, tiempoFin, visibilidad, lugar, retardoMultiplicativo);
	}

	public static ObstaculoTotal crearObstaculoTotal(NombreObstaculo nombre, Integer tiempoInicio, Integer tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		return new ObstaculoTotal(++ultimoIdAsignadoObstaculo, nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
	}
}
