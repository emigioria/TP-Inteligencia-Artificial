package frsf.cidisi.exercise.patrullero.search.modelo;

public abstract class Visibilidad {

	public abstract Boolean soyVisible(Obstaculo obstaculo, Interseccion posicionAgente, Arista ulArista);

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		return true;
	}

	public Boolean sosInformado() {
		return false;
	}

	public Boolean sosInvisible() {
		return false;
	}

	public Boolean sosVisible() {
		return false;
	}

}
