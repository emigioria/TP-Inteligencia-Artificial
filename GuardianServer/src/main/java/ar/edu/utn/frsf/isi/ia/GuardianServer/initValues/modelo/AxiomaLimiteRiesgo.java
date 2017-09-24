package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

public class AxiomaLimiteRiesgo extends Axioma {

	private String valor;

	public AxiomaLimiteRiesgo(Incidente incidente, String valor) {
		super();
		this.setIncidente(incidente);
		this.valor = valor;
	}

	public AxiomaLimiteRiesgo(Incidente incidente, int valorInt) {
		super();
		this.setIncidente(incidente);
		this.valor = Integer.toString(valorInt);
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "limiteRiesgo(" + this.getIncidente() + ", " + valor + ")";
	}
}
