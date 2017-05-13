package frsf.cidisi.exercise.patrullero.search.modelo;

public abstract class Obstaculo {
	private NombreObstaculo nombre;
	private Double tiempoInicio;
	private Double tiempoFin;
	private Visibilidad visibilidad;
	private Lugar lugar;

	public Obstaculo(NombreObstaculo nombre, Double tiempoInicio, Double tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		super();
		this.nombre = nombre;
		this.tiempoInicio = tiempoInicio;
		this.tiempoFin = tiempoFin;
		this.visibilidad = visibilidad;
		this.lugar = lugar;
	}

	public NombreObstaculo getNombre() {
		return nombre;
	}

	public void setNombre(NombreObstaculo nombre) {
		this.nombre = nombre;
	}

	public Double getTiempoInicio() {
		return tiempoInicio;
	}

	public void setTiempoInicio(Double tiempoInicio) {
		this.tiempoInicio = tiempoInicio;
	}

	public Double getTiempoFin() {
		return tiempoFin;
	}

	public void setTiempoFin(Double tiempoFin) {
		this.tiempoFin = tiempoFin;
	}

	public Visibilidad getVisibilidad() {
		return visibilidad;
	}

	public void setVisibilidad(Visibilidad visibilidad) {
		this.visibilidad = visibilidad;
	}

	public Lugar getLugar() {
		return lugar;
	}

	public void setLugar(Lugar lugar) {
		this.lugar = lugar;
	}

	public Double getPeso(Double peso) {
		// TODO Auto-generated method stub
		if(peso < 0){
			return peso;
		}
		return null;
	}

	public Boolean sosVisible(Interseccion posicionAgente) {
		return visibilidad.soyVisible(this, posicionAgente);
	}
}
