package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AxiomaLimiteRiesgo extends Axioma {

	private StringProperty valor = new SimpleStringProperty();

	public AxiomaLimiteRiesgo(Incidente incidente, String valor) {
		super(incidente);
		this.valor.set(valor);
	}

	public AxiomaLimiteRiesgo(Incidente incidente, int valorInt) {
		super(incidente);
		this.setIncidente(incidente);
		this.valor.set(Integer.toString(valorInt));
	}

	public String getValor() {
		return valor.get();
	}

	public void setValor(String valor) {
		this.valor.set(valor);
	}

	@Override
	public String toString() {
		return "limiteRiesgo(" + this.getIncidente() + ", " + valor.get() + ")";
	}
}
