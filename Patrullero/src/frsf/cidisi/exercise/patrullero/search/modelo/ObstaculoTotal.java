package frsf.cidisi.exercise.patrullero.search.modelo;

public class ObstaculoTotal extends Obstaculo {

	public ObstaculoTotal(Long id, NombreObstaculo nombre, Long tiempoInicio, Long tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		super(id, nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
	}

	@Override
	public Long getPeso(Long peso) {
		if(peso > 0){
			return -1 * peso;
		}
		return peso;
	}

	@Override
	public ObstaculoTotal clone() {
		return new ObstaculoTotal(getId(), getNombre(), getTiempoInicio(), getTiempoFin(), getVisibilidad(), getLugar());
	}

}
