package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.Date;

public class ObstaculoTotal extends Obstaculo {

	public ObstaculoTotal(NombreObstaculo nombre, Date tiempoInicio, Date tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		super(nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
	}

	@Override
	public Double getPeso(Double peso) {
		return -1.0;
	}
}
