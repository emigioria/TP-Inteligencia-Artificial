package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

public class AxiomaTieneRiesgo extends Axioma {

	private String palabra;

	private String valor;

	public AxiomaTieneRiesgo(Incidente incidente, String palabra, String valor) {
		super();
		this.setIncidente(incidente);
		this.palabra = palabra;
		this.valor = valor;
	}

	public AxiomaTieneRiesgo(Incidente incidente, String palabra, int valorInt) {
		super();
		this.setIncidente(incidente);
		this.palabra = palabra;
		this.valor = Integer.toString(valorInt);
	}

	public String getPalabra() {
		return palabra;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "tieneRiesgo(" + this.getIncidente() + ", " + palabra + ", " + valor + ")";
	}
}
