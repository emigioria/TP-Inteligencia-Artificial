package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.ia.Guardian.datos.BaseVerbos;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteProductionMemory;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemAction;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemBasedAgent;
import frsf.cidisi.faia.solver.productionsystem.Criteria;
import frsf.cidisi.faia.solver.productionsystem.ProductionMemory;
import frsf.cidisi.faia.solver.productionsystem.Rule;
import frsf.cidisi.faia.solver.productionsystem.criterias.NoDuplication;
import frsf.cidisi.faia.solver.productionsystem.criterias.Priority;
import frsf.cidisi.faia.solver.productionsystem.criterias.Random;
import frsf.cidisi.faia.solver.productionsystem.criterias.Specificity;

public class Guardian extends ProductionSystemBasedAgent {

	private List<Criteria> criterios;

	public Guardian() throws Exception {
		// The Agent State
		String ruta = new URI(BaseVerbos.class.getResource("/db/init.pl").toString()).getPath();
		EstadoGuardian agState = new EstadoGuardian(ruta);
		this.setAgentState(agState);

		//Crear reglas
		//TODO hacer
		List<Rule> reglas = null;

		//Crear memoria de trabajo
		ProductionMemory productionMemory = new ReteProductionMemory(reglas);
		this.setProductionMemory(productionMemory);

		//Crear criterios para resolver conflictos
		criterios = new ArrayList<>();
		criterios.add(new NoDuplication(this));
		criterios.add(new Priority());
		criterios.add(new Specificity());
		criterios.add(new Random());
	}

	/**
	 * This method is executed by the simulator to give the agent a perception.
	 * Then it updates its state.
	 *
	 * @param p
	 */
	@Override
	public void see(Perception p) {

	}

	@Override
	public EstadoGuardian getAgentState() {
		return (EstadoGuardian) super.getAgentState();
	}

	/**
	 * This method is executed by the simulator to ask the agent for an action.
	 */
	@Override
	public ProductionSystemAction learn() {

		return null;
	}

	@Override
	public boolean finish() {

		return false;
	}
}
