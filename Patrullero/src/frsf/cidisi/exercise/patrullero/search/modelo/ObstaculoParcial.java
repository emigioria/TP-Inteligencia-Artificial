package frsf.cidisi.exercise.patrullero.search.modelo;

public class ObstaculoParcial extends Obstaculo {
	private Integer retardoMultiplicativo;

	public ObstaculoParcial(Long id, NombreObstaculo nombre, Long tiempoInicio, Long tiempoFin, Visibilidad visibilidad, Lugar lugar, Integer retardoMultiplicativo) {
		super(id, nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
		this.retardoMultiplicativo = retardoMultiplicativo;
	}

	@Override
	public ObstaculoParcial clone() {
		return new ObstaculoParcial(getId(), getNombre(), getTiempoInicio(), getTiempoFin(), getVisibilidad(), getLugar(), retardoMultiplicativo);
	}

	@Override
	public Integer getPeso(Integer peso) {
		return peso * retardoMultiplicativo;
	}
}
