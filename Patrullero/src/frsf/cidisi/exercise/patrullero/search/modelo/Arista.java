package frsf.cidisi.exercise.patrullero.search.modelo;

public class Arista extends Lugar {
	private Interseccion origen;
	private Interseccion destino;
	private Calle calle;

	public Arista(Long id, Integer peso, Interseccion origen, Interseccion destino, Calle calle) throws Exception {
		super(id, peso);
		this.origen = origen;
		this.destino = destino;
		this.calle = calle;
		if(calle != null){
			calle.getTramos().add(this);
		}
		this.comprobarRepeticion();
		if(origen != null){
			origen.getSalientes().add(this);
		}
		if(destino != null){
			destino.getEntrantes().add(this);
		}
	}

	private void comprobarRepeticion() throws Exception {
		if(origen != null && destino != null){
			if(origen.getSalientes().stream().anyMatch(a -> a.getDestino().equals(destino))){
				throw new Exception("Ya hay una arista entre origen y destino!");
			}
		}
	}

	public Interseccion getOrigen() {
		return origen;
	}

	public void setOrigen(Interseccion origen) throws Exception {
		Interseccion origenViejo = origen;
		this.origen = origen;
		try{
			comprobarRepeticion();
		} catch(Exception e){
			this.origen = origenViejo;
			throw e;
		}
	}

	public Interseccion getDestino() {
		return destino;
	}

	public void setDestino(Interseccion destino) throws Exception {
		Interseccion destinoViejo = destino;
		this.destino = destino;
		try{
			comprobarRepeticion();
		} catch(Exception e){
			this.destino = destinoViejo;
			throw e;
		}
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
	public Boolean sosArista() {
		return true;
	}

}
