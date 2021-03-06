package frsf.cidisi.exercise.patrullero.search.modelo;

public class ObstaculoTotal extends Obstaculo {

	public ObstaculoTotal(Long id, NombreObstaculo nombre, Integer tiempoInicio, Integer tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		super(id, nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
	}

	@Override
	public Integer getPeso(Integer peso) {
		if(peso > 0){
			return -1 * peso;
		}
		return peso;
	}

	@Override
	public ObstaculoTotal clone() {
		ObstaculoTotal clon = new ObstaculoTotal(getId(), getNombre(), null, null, getVisibilidad(), null);
		clon.setLugar(this.getLugar());
		return clon;
	}

	@Override
	public String toString() {
		return "Tipo: Total. " + super.toString();
	}
}
