package frsf.cidisi.exercise.patrullero.search.modelo;

public class Arista extends Lugar {
	private Interseccion origen;
	private Interseccion destino;
	private Calle calle;

	public Arista(Long id, Long peso, Interseccion origen, Interseccion destino, Calle calle) {
		super(id, peso);
		this.origen = origen;
		if(origen != null){
			origen.getSalientes().add(this);
		}
		this.destino = destino;
		if(origen != null){
			destino.getEntrantes().add(this);
		}
		this.calle = calle;
		if(calle != null){
			calle.getTramos().add(this);
		}
	}

	public Interseccion getOrigen() {
		return origen;
	}

	public void setOrigen(Interseccion origen) {
		this.origen = origen;
	}

	public Interseccion getDestino() {
		return destino;
	}

	public void setDestino(Interseccion destino) {
		this.destino = destino;
	}

	public Calle getCalle() {
		return calle;
	}

	public void setCalle(Calle calle) {
		this.calle = calle;
	}

	@Override
	public String toString() {
		return calle.getNombre();
	}

	@Override
	public boolean sosArista() {
		return true;
	}

}
