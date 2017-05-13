package frsf.cidisi.exercise.patrullero.search.modelo;

public class ObstaculoTotal extends Obstaculo {

	public ObstaculoTotal(NombreObstaculo nombre, Double tiempoInicio, Double tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		super(nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
	}

	@Override
	public Double getPeso(Double peso) {
		return -1.0;
	}
}
