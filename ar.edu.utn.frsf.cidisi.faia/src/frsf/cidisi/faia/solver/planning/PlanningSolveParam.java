package frsf.cidisi.faia.solver.planning;

import frsf.cidisi.faia.agent.planning.PlanningBasedAgent;
import frsf.cidisi.faia.solver.SolveParam;

public class PlanningSolveParam extends SolveParam {

	PlanningBasedAgent planningBasedAgent;

	public PlanningSolveParam(PlanningBasedAgent planningBasedAgent) {
		this.planningBasedAgent = planningBasedAgent;
	}

	public PlanningBasedAgent getPlanningBasedAgent() {
		return planningBasedAgent;
	}

}
