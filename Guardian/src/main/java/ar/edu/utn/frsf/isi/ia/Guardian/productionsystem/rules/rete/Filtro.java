package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public abstract class Filtro extends NodoRete {

	public Filtro() {

	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		super.propagarHechos(this.filtrar(hechos));
	}

	public abstract List<Matches> filtrar(List<Matches> hechos);
}
