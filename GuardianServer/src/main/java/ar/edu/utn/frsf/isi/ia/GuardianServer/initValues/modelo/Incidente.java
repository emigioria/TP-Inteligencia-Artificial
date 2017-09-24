package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

public class Incidente {

	private String nombre;

	public Incidente(String nombre) {
		super();
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Incidente other = (Incidente) obj;
		if(nombre == null) {
			if(other.nombre != null)
				return false;
		} else if(!nombre.equals(other.nombre))
			return false;
		return true;
	}

}
