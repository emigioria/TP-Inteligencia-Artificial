package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.List;

import frsf.cidisi.faia.solver.productionsystem.ProductionMemory;
import frsf.cidisi.faia.solver.productionsystem.Rule;

public class ReteProductionMemory extends Nodo implements ProductionMemory {

	private List<Rule> reglas;

	public ReteProductionMemory(ReteMatcher rm, List<Rule> reglas) {
		this.reglas = reglas;
	}

	@Override
	public List<Rule> getRules() {
		return reglas;
	}

	public void matchear() {
		super.propagarHechos(new ArrayList<>());

	}

	@Override
	public void propagarHechos(List<List<Hecho>> hechos) {
		throw new UnsupportedOperationException();
	}
}
