package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AxiomaFrase {

	private StringProperty palabra1 = new SimpleStringProperty();

	private StringProperty palabra2 = new SimpleStringProperty();

	public AxiomaFrase(String palabra1, String palabra2) {
		super();
		this.palabra1.set(palabra1);
		this.palabra2.set(palabra2);
	}

	public String getPalabra1() {
		return palabra1.get();
	}

	public void setPalabra1(String palabra) {
		this.palabra1.set(palabra);
	}

	public String getPalabra2() {
		return palabra2.get();
	}

	public void setPalabra2(String palabra) {
		this.palabra2.set(palabra);
	}

	@Override
	public String toString() {
		return "frase(" + palabra1.get() + ", " + palabra2.get() + ")";
	}
}
