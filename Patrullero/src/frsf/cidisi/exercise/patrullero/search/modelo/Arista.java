package frsf.cidisi.exercise.patrullero.search.modelo;

public class Arista extends Lugar {
	private Double peso;
	private Interseccion destino;
	private Interseccion origen;
	private Calle calle;

	public Arista(Double peso, Interseccion destino, Interseccion origen, Calle calle) {
		super();
		this.peso = peso;
		this.destino = destino;
		this.origen = origen;
		this.calle = calle;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
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
