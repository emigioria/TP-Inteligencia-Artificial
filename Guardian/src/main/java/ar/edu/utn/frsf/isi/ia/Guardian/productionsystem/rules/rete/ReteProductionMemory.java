package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.List;

import frsf.cidisi.faia.solver.productionsystem.Matches;
import frsf.cidisi.faia.solver.productionsystem.ProductionMemory;
import frsf.cidisi.faia.solver.productionsystem.Rule;

public class ReteProductionMemory extends NodoRete implements ProductionMemory {

	private List<Rule> reglas;

	public ReteProductionMemory(List<Rule> reglas) {
		this.reglas = reglas;
	}

	public void inicializar() {
		super.propagarHechos(new ArrayList<>());
	}

	@Override
	public List<Rule> getRules() {
		return reglas;
	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		throw new UnsupportedOperationException();
	}
}
