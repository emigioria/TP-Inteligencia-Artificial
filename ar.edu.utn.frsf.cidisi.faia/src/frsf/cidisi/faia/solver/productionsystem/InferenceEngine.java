/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemAction;
import frsf.cidisi.faia.solver.Solve;
import frsf.cidisi.faia.solver.SolveParam;

/**
 * Clase que implementa el solver del sistema de producciï¿½n.
 */
public class InferenceEngine extends Solve {

	private RuleMatchesPair r;

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
		List<RuleMatchesPair> activeRules = matcher.match(productionMemory, workingMemory);
		imprimirReglas(activeRules);

		//Si no hay reglas activas se termina.
		if(activeRules.isEmpty()){
			return null;
		}
		else if(activeRules.size() != 1){
			//Se resuelven los conflictos.
			Iterator<Criteria> it = criterias.iterator();
			while(it.hasNext() && activeRules.size() > 1){
				Criteria currentCriteria = it.next();
				System.out.println("\nCriterio:" + currentCriteria.toString());

				List<RuleMatchesPair> finalRules = currentCriteria.apply(activeRules);
				if(finalRules.size() != 0){
					System.out.print("Conflict set: ");
					imprimirReglas(finalRules);

					activeRules = finalRules;
				}
				else{
					System.out.print("Conflict set: - ");
				}
			}
		}

		//Se obtiene la regla elegida
		r = activeRules.get(0);

		return new ProductionSystemAction(r);
	}

	private void imprimirReglas(List<RuleMatchesPair> activeRules) {
		if(!activeRules.isEmpty()){
			Map<Rule, List<RuleMatchesPair>> map = activeRules.stream().collect(Collectors.groupingBy(RuleMatchesPair::getKey));
			map.keySet().forEach(r -> {
				System.out.println("\nRegla: " + r.getId());
				System.out.println("Activa con: ");
				map.get(r).forEach(h -> System.out.println(h.getValue()));
			});
		}
	}
}
