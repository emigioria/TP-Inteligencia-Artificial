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

}
