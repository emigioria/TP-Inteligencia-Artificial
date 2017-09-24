package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AxiomaTieneRiesgo extends Axioma {

	private StringProperty palabra = new SimpleStringProperty();

	private StringProperty valor = new SimpleStringProperty();

	public AxiomaTieneRiesgo(Incidente incidente, String palabra, String valor) {
		super(incidente);
		this.palabra.set(palabra);
		this.valor.set(valor);
	}

	public AxiomaTieneRiesgo(Incidente incidente, String palabra, int valorInt) {
		super(incidente);
		this.palabra.set(palabra);
		this.valor.set(Integer.toString(valorInt));
	}

	public String getPalabra() {
		return palabra.get();
	}

	public void setPalabra(String palabra) {
		this.palabra.set(palabra);
	}

	public String getValor() {
		return valor.get();
	}

	public void setValor(String valor) {
		this.valor.set(valor);
	}

	@Override
	public String toString() {
		return "tieneRiesgo(" + this.getIncidente() + ", " + palabra.get() + ", " + valor.get() + ")";
	}
}
