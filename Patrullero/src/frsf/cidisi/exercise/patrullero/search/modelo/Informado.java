package frsf.cidisi.exercise.patrullero.search.modelo;

public class Informado extends Visibilidad {

	@Override
	public Boolean soyVisible(Obstaculo obstaculo, Interseccion posicionAgente, Arista ulArista) {
		return true;
	}

	@Override
	public Boolean sosInformado() {
		return true;
	}
}
