package frsf.cidisi.exercise.patrullero.search.modelo;

public class ObstaculoParcial extends Obstaculo {
	private Long retardoMultiplicativo;

	public ObstaculoParcial(Long id, NombreObstaculo nombre, Long tiempoInicio, Long tiempoFin, Visibilidad visibilidad, Lugar lugar, Long retardoMultiplicativo) {
		super(id, nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
		this.retardoMultiplicativo = retardoMultiplicativo;
	}

	@Override
	public ObstaculoParcial clone() {
		return new ObstaculoParcial(getId(), getNombre(), getTiempoInicio(), getTiempoFin(), getVisibilidad(), getLugar(), retardoMultiplicativo);
	}

	@Override
	public Long getPeso(Long peso) {
		return peso * retardoMultiplicativo;
	}
}
