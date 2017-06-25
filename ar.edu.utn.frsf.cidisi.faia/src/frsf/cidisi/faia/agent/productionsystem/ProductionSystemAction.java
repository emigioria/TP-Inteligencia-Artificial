/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.agent.productionsystem;

import frsf.cidisi.faia.solver.productionsystem.RuleMatchesPair;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

/**
 * Clase que implementa las acciones que el sistema de produccion puede
 * devolverle al ambiente.
 */
public class ProductionSystemAction extends frsf.cidisi.faia.agent.Action {

	private RuleMatchesPair ruleData;

	public ProductionSystemAction(RuleMatchesPair rd) {
		ruleData = rd;
	}

	@Override
	public String toString() {
		return "Ejecutar";
	}

	public RuleMatchesPair getPeerRuleData() {
		return ruleData;
	}

	@Override
	public EnvironmentState execute(AgentState ast, EnvironmentState est) {
		ruleData.getKey().execute(ruleData.getValue());
		return est;
	}

	public boolean finish() {
		return ruleData.getKey().finish(ruleData.getValue());
	}

	public boolean finishLearning() {
		return ruleData.getKey().finishLearning(ruleData.getValue());
	}
}
