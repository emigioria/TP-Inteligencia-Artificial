package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Fila {

	private Incidente incidente;

	private StringProperty palabra = new SimpleStringProperty();

	private StringProperty valor = new SimpleStringProperty();

	private BooleanProperty critica = new SimpleBooleanProperty();

	public Fila(Incidente incidente, String palabra, String valor, boolean critica) {
		super();
		this.incidente = incidente;
		this.palabra.set(palabra);
		this.valor.set(valor);
		this.critica.set(critica);
	}

	public Fila(Incidente incidente, String palabra, Integer valor, boolean critica) {
		super();
		this.incidente = incidente;
		this.palabra.set(palabra);
		this.valor.set(Integer.toString(valor));
		this.critica.set(critica);
	}

	public StringProperty getPalabra() {
		return palabra;
	}

	public String getPalabraStr() {
		return palabra.get();
	}

	public void setPalabra(StringProperty palabra) {
		this.palabra = palabra;
	}

	public StringProperty getValor() {
		return valor;
	}

	public String getValorStr() {
		return valor.get();
	}

	public void setValor(StringProperty valor) {
		this.valor = valor;
	}

	public BooleanProperty getCritica() {
		return critica;
	}

	public boolean getCriticaBool() {
		return critica.get();
	}

	public void setCritica(BooleanProperty critica) {
		this.critica = critica;
	}

	public void setCritica(boolean critica) {
		this.critica.set(critica);
	}

	public Incidente getIncidente() {
		return incidente;
	}

	public void setIncidente(Incidente incidente) {
		this.incidente = incidente;
	}

}
