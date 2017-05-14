package frsf.cidisi.exercise.patrullero.search.modelo;

public class ObstaculoParcial extends Obstaculo {

	public ObstaculoParcial(Long id, NombreObstaculo nombre, Long tiempoInicio, Long tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		super(id, nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
	}

	@Override
	public ObstaculoParcial clone() {
		return new ObstaculoParcial(getId(), getNombre(), getTiempoInicio(), getTiempoFin(), getVisibilidad(), getLugar());
	}
}
