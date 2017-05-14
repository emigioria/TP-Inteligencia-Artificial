package frsf.cidisi.exercise.patrullero.search.modelo;

public enum Visibilidad {
	INFORMADO {
		@Override
		public Boolean soyVisible(Obstaculo obstaculo, Interseccion posicionAgente, Arista ulArista) {
			return true;
		}
	},
	VISIBLE {
		@Override
		public Boolean soyVisible(Obstaculo obstaculo, Interseccion posicionAgente, Arista ulArista) {
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
	},
	INVISIBLE {
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
	};

	public abstract Boolean soyVisible(Obstaculo obstaculo, Interseccion posicionAgente, Arista ulArista);

}
