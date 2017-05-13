package frsf.cidisi.exercise.patrullero.search.modelo;

public class Invisible extends Visibilidad {

	@Override
	public Boolean soyVisible(Obstaculo obstaculo, Interseccion posicionAgente) {
		return false;
	}

}
