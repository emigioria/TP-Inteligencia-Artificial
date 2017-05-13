package frsf.cidisi.exercise.patrullero.search.modelo;

public class Visible extends Visibilidad {

	@Override
	public Boolean soyVisible(Obstaculo obstaculo, Interseccion posicionAgente) {
		if(obstaculo.getLugar().sosArista()){
			Arista aristaObstaculo = (Arista) obstaculo.getLugar();
			return posicionAgente.equals(aristaObstaculo.getDestino()) || posicionAgente.equals(aristaObstaculo.getOrigen());
		}
		else if(obstaculo.getLugar().sosInterseccion()){
			Interseccion interseccionObstaculo = (Interseccion) obstaculo.getLugar();
			return posicionAgente.getSalientes().stream().anyMatch(salida -> salida.getDestino().equals(interseccionObstaculo)) ||
					posicionAgente.getEntrantes().stream().anyMatch(salida -> salida.getOrigen().equals(interseccionObstaculo));
		}
		return false;
	}

}
