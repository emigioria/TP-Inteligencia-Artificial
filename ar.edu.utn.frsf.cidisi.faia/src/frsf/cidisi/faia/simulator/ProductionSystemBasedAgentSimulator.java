/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.simulator;

import java.util.Vector;

import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.GoalBasedAgent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemAction;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemBasedAgent;
import frsf.cidisi.faia.environment.Environment;
import frsf.cidisi.faia.simulator.events.EventType;
import frsf.cidisi.faia.simulator.events.SimulatorEventNotifier;

/**
 * Clase que implementa el simulador de un agente basado en conocimiento.
 */
public class ProductionSystemBasedAgentSimulator extends frsf.cidisi.faia.simulator.Simulator {

	/**
	 * Constructor.
	 *
	 * @param environment
	 * @param agents
	 */
	public ProductionSystemBasedAgentSimulator(Environment environment, Vector<Agent> agents) {
		super(environment, agents);
	}

	/**
	 * Constructor.
	 *
	 * @param environment
	 * @param agent
	 */
	public ProductionSystemBasedAgentSimulator(Environment environment, Agent agent) {
		Vector<Agent> v = new Vector<>();
		v.add(agent);
		this.environment = environment;
		this.agents = v;
	}

	@Override
	public void start() {

		System.out.println("----------------------------------------------------");
		System.out.println("------------- " + this.getSimulatorName() + " ------------");
		System.out.println("----------------------------------------------------");
		System.out.println();

		Perception perception;
		Action action;
		ProductionSystemBasedAgent agent;

		agent = (ProductionSystemBasedAgent) this.getAgents().firstElement();

		do{

			System.out.println("------------------------------------");

			System.out.println("Sending perception to agent...");
			perception = this.getPercept();
			agent.see(perception);
			System.out.println("Perception: " + perception);

			System.out.println("Agent State: " + agent.getAgentState());
			System.out.println("Environment: " + environment);

			System.out.println("Asking the agent that start the learning process...");
			action = agent.selectAction();

			System.out.println();

			if(action != null){
				this.ruleReturned(agent, action);
			}

		} while(action != null && !this.finishForRule(action) && !this.finishForAgentState(agent));

		// Check what happened.
		if(action != null){
			System.out.println("The agent don't have any more to learn.");
		}
		else if(this.finishForRule(action)){
			System.out.println("The agent has executed the finish rule.");
		}
		else{
			System.out.println("The agent has finished learning!");
		}

		// Leave a blank line
		System.out.println();

		// This call can be moved to the Simulator class
		this.close();

		// Launch simulationFinished event
		SimulatorEventNotifier.runEventHandlers(EventType.SimulationFinished, null);

	}

	public boolean finishForAgentState(ProductionSystemBasedAgent agent) {
		return agent.finish();
	};

	public boolean finishForRule(Action action) {
		ProductionSystemAction a = (ProductionSystemAction) action;
		return a.finish();
	};

	public String getSimulatorName() {
		return "Knowledge Based Simulator";
	}

	public void showSolution() {
		GoalBasedAgent agent = (GoalBasedAgent) this.getAgents().firstElement();
		agent.getSolver().showSolution();
	}

	public void ruleReturned(Agent agent, Action action) {
		this.updateState(action);
		this.showSolution();
	}

	/**
	 * Here we update the state of the agent and the real state of the
	 * simulator.
	 *
	 * @param action
	 */
	protected void updateState(Action action) {
		this.getEnvironment().updateState(((GoalBasedAgent) agents.elementAt(0)).getAgentState(), action);
	}

	@Override
	public void close() {
		this.environment.close();
	}

}
