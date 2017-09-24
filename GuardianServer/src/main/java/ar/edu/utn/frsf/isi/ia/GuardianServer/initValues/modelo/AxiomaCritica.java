package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AxiomaCritica extends Axioma {

	private StringProperty palabra = new SimpleStringProperty();

	public AxiomaCritica(Incidente incidente, String palabra) {
		super(incidente);
		this.palabra.set(palabra);
	}

	public String getPalabra() {
		return palabra.get();
	}

	public void setPalabra(String palabra) {
		this.palabra.set(palabra);
	}

	@Override
	public String toString() {
		return "critica(" + this.getIncidente() + ", " + palabra.get() + ")";
	}

}
