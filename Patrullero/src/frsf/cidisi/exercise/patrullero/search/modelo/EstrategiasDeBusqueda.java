package frsf.cidisi.exercise.patrullero.search.modelo;

import frsf.cidisi.exercise.patrullero.search.CostFunction;
import frsf.cidisi.exercise.patrullero.search.Heuristic;
import frsf.cidisi.faia.solver.search.AStarSearch;
import frsf.cidisi.faia.solver.search.BreathFirstSearch;
import frsf.cidisi.faia.solver.search.DepthFirstSearch;
import frsf.cidisi.faia.solver.search.GreedySearch;
import frsf.cidisi.faia.solver.search.Strategy;
import frsf.cidisi.faia.solver.search.UniformCostSearch;

public enum EstrategiasDeBusqueda {
	AMPLITUD {
		@Override
		public Strategy getInstance() {
			return new BreathFirstSearch();
		}
	},
	PROFUNDIDAD {
		@Override
		public Strategy getInstance() {
			return new DepthFirstSearch();
		}
	},
	COSTO_UNIFORME {
		@Override
		public Strategy getInstance() {
			return new UniformCostSearch(new CostFunction());
		}
	},
	A_ASTERISCO {
		@Override
		public Strategy getInstance() {
			return new AStarSearch(new CostFunction(), new Heuristic());
		}
	},
	AVARA {
		@Override
		public Strategy getInstance() {
			return new GreedySearch(new Heuristic());
		}
	};

	public abstract Strategy getInstance();
}
