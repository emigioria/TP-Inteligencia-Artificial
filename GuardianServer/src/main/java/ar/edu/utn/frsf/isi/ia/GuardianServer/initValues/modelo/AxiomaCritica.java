package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

public class AxiomaCritica extends Axioma {

	private String palabra;

	public AxiomaCritica(Incidente incidente, String palabra) {
		super();
		this.setIncidente(incidente);
		this.palabra = palabra;
	}

	public String getPalabra() {
		return palabra;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

	@Override
	public String toString() {
		return "critica(" + this.getIncidente() + ", " + palabra + ")";
	}

}
