package frsf.cidisi.exercise.patrullero.search.modelo;

public class ObstaculoParcial extends Obstaculo {
	private Integer retardoMultiplicativo;

	public ObstaculoParcial(Long id, NombreObstaculo nombre, Integer tiempoInicio, Integer tiempoFin, Visibilidad visibilidad, Lugar lugar, Integer retardoMultiplicativo) {
		super(id, nombre, tiempoInicio, tiempoFin, visibilidad, lugar);
		this.retardoMultiplicativo = retardoMultiplicativo;
	}

	@Override
	public ObstaculoParcial clone() {
		ObstaculoParcial clon = new ObstaculoParcial(getId(), getNombre(), getTiempoInicio(), getTiempoFin(), getVisibilidad(), null, retardoMultiplicativo);
		clon.setLugar(this.getLugar());
		return clon;
	}

	public Integer getRetardoMultiplicativo() {
		return retardoMultiplicativo;
	}

	public void setRetardoMultiplicativo(Integer retardoMultiplicativo) {
		this.retardoMultiplicativo = retardoMultiplicativo;
	}

	@Override
	public Integer getPeso(Integer peso) {
		return peso * retardoMultiplicativo;
	}
}
