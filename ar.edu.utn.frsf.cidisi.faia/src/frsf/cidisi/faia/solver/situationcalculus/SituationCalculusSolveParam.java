package frsf.cidisi.faia.solver.situationcalculus;

import frsf.cidisi.faia.agent.situationcalculus.KnowledgeBase;
import frsf.cidisi.faia.solver.SolveParam;

public class SituationCalculusSolveParam extends SolveParam {

	private KnowledgeBase knowledgeBase;

	public SituationCalculusSolveParam(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}
}
