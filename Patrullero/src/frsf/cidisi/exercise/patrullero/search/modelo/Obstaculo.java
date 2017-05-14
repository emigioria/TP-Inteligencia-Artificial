package frsf.cidisi.exercise.patrullero.search.modelo;

public abstract class Obstaculo implements Cloneable {
	private Long id;
	private NombreObstaculo nombre;
	private Long tiempoInicio;
	private Long tiempoFin;
	private Visibilidad visibilidad;
	private Lugar lugar;

	public Obstaculo(Long id, NombreObstaculo nombre, Long tiempoInicio, Long tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		super();
		this.id = id;
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

	public Long getTiempoInicio() {
		return tiempoInicio;
	}

	public void setTiempoInicio(Long tiempoInicio) {
		this.tiempoInicio = tiempoInicio;
	}

	public Long getTiempoFin() {
		return tiempoFin;
	}

	public void setTiempoFin(Long tiempoFin) {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Obstaculo other = (Obstaculo) obj;
		if(id != null && id.equals(other.id)){
			return true;
		}
		return false;
	}

	public abstract Long getPeso(Long peso);

	@Override
	public abstract Obstaculo clone();

	public Boolean sosVisible(Interseccion posicionAgente, Arista ulArista) {
		return visibilidad.soyVisible(this, posicionAgente, ulArista);
	}
}
