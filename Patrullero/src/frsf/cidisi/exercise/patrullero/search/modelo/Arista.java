package frsf.cidisi.exercise.patrullero.search.modelo;

public class Arista extends Lugar {
	private Interseccion destino;
	private Interseccion origen;
	private Calle calle;

	public Arista(Long id, Long peso, Interseccion destino, Interseccion origen, Calle calle) {
		super(id, peso);
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

}
