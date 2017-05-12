package frsf.cidisi.faia.solver.search;

import frsf.cidisi.faia.agent.search.Problem;
import frsf.cidisi.faia.solver.SolveParam;

public class SearchSolveParam extends SolveParam {

	Problem problem;

	public SearchSolveParam(Problem problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return problem;
	}

}
