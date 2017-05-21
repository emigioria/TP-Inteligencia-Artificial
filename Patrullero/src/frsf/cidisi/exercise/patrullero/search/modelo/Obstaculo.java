package frsf.cidisi.exercise.patrullero.search.modelo;

public abstract class Obstaculo implements Cloneable {
	private Long id;
	private NombreObstaculo nombre;
	private Integer tiempoInicio;
	private Integer tiempoFin;
	private Visibilidad visibilidad;
	private Lugar lugar;

	public Obstaculo(Long id, NombreObstaculo nombre, Integer tiempoInicio, Integer tiempoFin, Visibilidad visibilidad, Lugar lugar) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tiempoInicio = tiempoInicio;
		this.tiempoFin = tiempoFin;
		this.visibilidad = visibilidad;
		this.lugar = lugar;
		if(lugar != null){
			lugar.getObstaculos().add(this);
		}
	}

	public NombreObstaculo getNombre() {
		return nombre;
	}

	public void setNombre(NombreObstaculo nombre) {
		this.nombre = nombre;
	}

	public Integer getTiempoInicio() {
		return tiempoInicio;
	}

	public void setTiempoInicio(Integer tiempoInicio) {
		this.tiempoInicio = tiempoInicio;
	}

	public Integer getTiempoFin() {
		return tiempoFin;
	}

	public void setTiempoFin(Integer tiempoFin) {
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

	@Override
	public String toString() {
		return "Nombre: " + this.getNombre() + ". Lugar: " + this.getLugar() + ".";
	}

	public abstract Integer getPeso(Integer peso);

	@Override
	public abstract Obstaculo clone();

	public Boolean sosVisible(Interseccion posicionAgente, Arista ulArista) {
		return visibilidad.soyVisible(this, posicionAgente, ulArista);
	}
}
