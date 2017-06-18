/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.productionsystem.ProductionMemory;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemAction;
import frsf.cidisi.faia.agent.productionsystem.Rule;
import frsf.cidisi.faia.agent.productionsystem.WorkingMemory;
import frsf.cidisi.faia.solver.Solve;
import frsf.cidisi.faia.solver.SolveParam;
import javafx.util.Pair;

/**
 * Clase que implementa el solver del sistema de producciï¿½n.
 */
public abstract class InferenceEngine extends Solve {

	private Pair<Rule, Matches> r;

	private List<Criteria> criterias;

	/**
	 * Constructor.
	 * Recibe las estrategias en el orden a ser aplicadas.
	 */
	public InferenceEngine(List<Criteria> criterias) {
		this.criterias = criterias;
	}

	@Override
	public void showSolution() {
		if(r == null){
			System.out.println("\nRule to execute: None");
		}
		else{
			System.out.println("\nRule to execute: " + r.getKey().getId());
		}
	}

	@Override
	public Action solve(SolveParam params) throws Exception {
		ProductionSystemSolveParam param = (ProductionSystemSolveParam) params;

		ProductionMemory productionMemory = param.getProductionMemory();
		WorkingMemory workingMemory = param.getWorkingMemory();

		//Se obtienen las reglas activas
		List<Pair<Rule, Matches>> activeRules = this.match(productionMemory, workingMemory);

		//Si no hay reglas activas se termina.
		if(activeRules.isEmpty()){
			return null;
		}
		else if(activeRules.size() != 1){
			//Se resuelven los conflictos.
			Iterator<Criteria> it = criterias.iterator();
			while(it.hasNext() && activeRules.size() > 1){
				List<Pair<Rule, Matches>> finalRules = it.next().apply(activeRules);
				if(finalRules.size() != 0){
					activeRules = finalRules;
				}
			}
		}

		//Se obtiene la regla elegida
		r = activeRules.get(0);

		return new ProductionSystemAction(r);
	}

	/**
	 * Naive algorithm for matching. It can be overrited to a better solution, like a rete algorithm.
	 *
	 * @param productionMemory
	 *            Production memory with the rules
	 * @param workingMemory
	 *            Working memory with the data
	 * @return List of pair of rules with their matches
	 */
	public List<Pair<Rule, Matches>> match(ProductionMemory productionMemory, WorkingMemory workingMemory) {
		return productionMemory.getRules().parallelStream().map(r -> {
			List<Matches> unificaciones = new ArrayList<>();
			if(r.isActive(unificaciones)){
				return unificaciones.parallelStream().map(m -> new Pair<>(r, m)).collect(Collectors.toList());
			}

			ArrayList<Pair<Rule, Matches>> retorno = new ArrayList<>();
			return retorno;
		}).flatMap(List::stream).collect(Collectors.toList());
	};
}
