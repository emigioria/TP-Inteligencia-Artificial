package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class Axioma {

	private ObjectProperty<Incidente> incidente = new SimpleObjectProperty<>();

	public Axioma(Incidente incidente) {
		this.incidente.set(incidente);
	}

	public Incidente getIncidente() {
		return incidente.get();
	}

	public void setIncidente(Incidente incidente) {
		this.incidente.set(incidente);
	}

}
