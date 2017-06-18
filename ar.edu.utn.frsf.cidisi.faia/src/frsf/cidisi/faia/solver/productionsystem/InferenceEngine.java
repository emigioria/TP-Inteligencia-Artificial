/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem;

import java.util.Iterator;
import java.util.List;

import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemAction;
import frsf.cidisi.faia.solver.Solve;
import frsf.cidisi.faia.solver.SolveParam;
import javafx.util.Pair;

/**
 * Clase que implementa el solver del sistema de producciï¿½n.
 */
public abstract class InferenceEngine extends Solve {

	private Pair<Rule, Matches> r;

	private List<Criteria> criterias;

	private Matcher matcher;

	/**
	 * Constructor.
	 * Recibe las estrategias en el orden a ser aplicadas.
	 */
	public InferenceEngine(List<Criteria> criterias, Matcher matcher) {
		this.criterias = criterias;
		this.matcher = matcher;
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
		List<Pair<Rule, Matches>> activeRules = matcher.match(productionMemory, workingMemory);

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

}
