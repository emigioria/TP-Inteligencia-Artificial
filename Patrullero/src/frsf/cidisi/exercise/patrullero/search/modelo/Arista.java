package frsf.cidisi.exercise.patrullero.search.modelo;

public class Arista extends Lugar {
	private Long id;
	private Interseccion destino;
	private Interseccion origen;
	private Calle calle;

	public Arista(Long peso, Interseccion destino, Interseccion origen, Calle calle) {
		super(peso);
		this.destino = destino;
		this.origen = origen;
		this.calle = calle;
	}

	public Interseccion getDestino() {
		return destino;
	}

	public void setDestino(Interseccion destino) {
		this.destino = destino;
	}

	public Interseccion getOrigen() {
		return origen;
	}

	public void setOrigen(Interseccion origen) {
		this.origen = origen;
	}

	public Calle getCalle() {
		return calle;
	}

	public void setCalle(Calle calle) {
		this.calle = calle;
	}

	@Override
	public boolean sosArista() {
		return true;
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
		Arista other = (Arista) obj;
		if(id != null && id.equals(other.id)){
			return true;
		}
		return false;
	}

}
