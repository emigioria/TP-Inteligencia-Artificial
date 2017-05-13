package frsf.cidisi.exercise.patrullero.search.modelo;

public class Informado extends Visibilidad {

	@Override
	public Boolean soyVisible(Obstaculo obstaculo, Interseccion posicionAgente) {
		return true;
	}

}
