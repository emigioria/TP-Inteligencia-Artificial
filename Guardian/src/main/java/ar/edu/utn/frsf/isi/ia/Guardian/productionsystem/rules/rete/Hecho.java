package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;

public class Hecho {

	private List<Object> valores;

	public Hecho(List<Object> valores) {
		assert valores != null;

		this.valores = valores;
	}

	public Object get(Integer indice) {
		return valores.get(0);
	}
}
