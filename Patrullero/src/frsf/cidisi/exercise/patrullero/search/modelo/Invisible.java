package frsf.cidisi.exercise.patrullero.search.modelo;

public class Invisible extends Visibilidad {

	@Override
	public Boolean soyVisible(Obstaculo obstaculo, Interseccion posicionAgente, Arista ulArista) {
		if(obstaculo.getLugar().sosArista()){
			Arista aristaObstaculo = (Arista) obstaculo.getLugar();
			return ulArista.equals(aristaObstaculo);
		}
		else if(obstaculo.getLugar().sosInterseccion()){
			Interseccion interseccionObstaculo = (Interseccion) obstaculo.getLugar();
			return posicionAgente.equals(interseccionObstaculo);
		}
		return false;
	}

	@Override
	public Boolean sosInvisible() {
		return true;
	}
}
