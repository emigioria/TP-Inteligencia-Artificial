package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import java.util.NoSuchElementException;

import frsf.cidisi.exercise.patrullero.search.modelo.CasoDePrueba;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.NombreObstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.ObstaculoParcial;
import frsf.cidisi.exercise.patrullero.search.modelo.ObstaculoTotal;
import frsf.cidisi.exercise.patrullero.search.modelo.Visibilidad;

public class CasoDePruebaGUI {

	protected static Long ultimoIdAsignadoObstaculo = 0L;

	private CasoDePrueba casoDePrueba;

	public CasoDePruebaGUI() {
		super();

		//Resetear IDs
		CasoDePruebaGUI.ultimoIdAsignadoObstaculo = 0L;
	}

	public CasoDePruebaGUI(CasoDePrueba casoDePrueba) {
		this();
		this.casoDePrueba = casoDePrueba;

		//Setear IDs
		try{
			CasoDePruebaGUI.ultimoIdAsignadoObstaculo = casoDePrueba.getObstaculos().stream().max((x, y) -> x.getId().compareTo(y.getId())).get().getId();
		} catch(NoSuchElementException e){

		}
	}

	public CasoDePrueba getCasoDePrueba() {
		return casoDePrueba;
	}

	public void setCasoDePrueba(CasoDePrueba casoDePrueba) {
		this.casoDePrueba = casoDePrueba;
	}

	public static ObstaculoParcial crearObstaculoParcial(NombreObstaculo nombre, Integer tiempoInicio, Integer tiempoFin, Visibilidad visibilidad, Lugar lugar, Integer retardoMultiplicativo) {
		return new ObstaculoParcial(++ultimoIdAsignadoObstaculo, nombre, tiempoInicio, tiempoFin, visibilidad, lugar, retardoMultiplicativo);
	}

	public static ObstaculoTotal crearObstaculoTotal(NombreObstaculo nombre, Integer tiempoInicio, Integer tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		return new ObstaculoTotal(++ultimoIdAsignadoObstaculo, nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
	}
}
