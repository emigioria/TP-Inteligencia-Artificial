/*
 * Copyright 2007-2009 Georgina Stegmayer, Milagros Gutiérrez, Jorge Roa
 * y Milton Pividori.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package frsf.cidisi.faia.solver.search;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;

/**
 * @author Jorge M. Roa
 * @version 1.0
 * @created 08-Mar-2007 13:16:04
 */
public class NTree implements Cloneable, Comparable<NTree> {

	protected int deep;
	protected double cost;
	protected SearchAction action;
	protected NTree parent;
	protected Vector<NTree> sons;
	protected SearchBasedAgentState agentState;
	protected int executionOrder;

	public NTree() {
		this.deep = 0;
		this.cost = 0D;
		this.parent = null;
		this.sons = new Vector<>();
		this.agentState = null;
		this.executionOrder = 0;
	}

	public NTree(NTree firstNode, SearchAction action, SearchBasedAgentState ast, int order) {
		this.deep = firstNode.getDeep() + 1;
		this.parent = firstNode;
		this.sons = new Vector<>();
		this.agentState = ast;
		this.action = action;
		this.executionOrder = order;
	}

	/**
	 *
	 * @param son
	 */
	public void addSon(NTree son) {
		this.getSons().addElement(son);
	}

	@Override
	public Object clone() {

		NTree node = new NTree();

		SearchBasedAgentState agSt = agentState.clone();
		/*
		 * if (parent!=null)
		 * node.setParent((NTree)parent.clone());
		 */
		node.setParent(parent);
		node.setAction(action);
		node.setAgentState(agSt);
		node.setExecutionOrder(executionOrder);

		/* node.setSons((Vector<NTree>)sons.clone()); */
		node.setSons(sons);

		return node;
	}

	public void setExecutionOrder(int executionOrder) {
		this.executionOrder = executionOrder;
	}

	public SearchAction getAction() {
		return action;
	}

	public double getCost() {
		return cost;
	}

	public int getDeep() {
		return deep;
	}

	public NTree getParent() {
		return parent;
	}

	public Vector<NTree> getSons() {
		return sons;
	}

	public List<NTree> getSonsTotal() {
		return Stream.concat(this.getSons().stream(),
				this.getSons().stream().map(hijo -> hijo.getSonsTotal()).flatMap(List::stream))
				.collect(Collectors.toList());
	}

	public SearchBasedAgentState getAgentState() {
		return agentState;
	}

	/**
	 *
	 * @param action
	 */
	public void setAction(SearchAction action) {
		this.action = action;
	}

	/**
	 *
	 * @param cost
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 *
	 * @param deep
	 */
	public void setDeep(int deep) {
		this.deep = deep;
	}

	/**
	 *
	 * @param father
	 */
	public void setParent(NTree parent) {
		this.parent = parent;
	}

	/**
	 *
	 * @param sons
	 */
	public void setSons(Vector<NTree> sons) {
		this.sons = sons;
	}

	/**
	 *
	 * @param state
	 */
	public void setAgentState(SearchBasedAgentState state) {
		this.agentState = state;
	}

	public SearchAction getActionFromBranchsTop() {
		return action;
	}

	@Override
	public String toString() {
		String eo = "Id=\"" + executionOrder + "\" ";
		String ac = "Action=\"" + action + "\" ";
		//TODO: FALTA VER COMO HACEMOS CUANDO HAY FUNCIONES DE COSTO O HEURISTICAS.-
		//		String hf = "Heu: " + getHeuristicFunction() + " ";
		//		String cf = "Cst: " + getCostFunction() + " ";

		return eo + ac;
	}

	public String toGraphviz() {
		final StringBuilder str = new StringBuilder();
		str.append("nodo" + this.executionOrder +
				"[label=\"{EO: " + this.executionOrder + "|" +
				"cost: " + this.cost + "|" +
				"A: " + this.getAction());

		if(this.getParent() != null){
			if(this.getParent().getParent() != null){
				str.append("|" + this.getParent().getAgentState().toString().replace(",", "").replace("[", "").replace("]", "").replace(" ", "\\n"));
			}
		}
		str.append("}\"]\n");
		sons.forEach(son -> {
			str.append(str + son.toGraphviz())
					.append("nodo" + this.executionOrder + " -> " +
							"nodo" + son.executionOrder + ";\n");
		});
		str.append("\n");
		return str.toString();
	}

	public int getExecutionOrder() {
		return executionOrder;
	}

	public String toXml() {
		StringBuffer str = new StringBuffer().append("<Nodo" + action + ">" + toString() + agentState.toString());

		this.getSons().forEach(son -> str.append(son.toXml()));

		str.append("</Nodo" + action + ">");
		return str.toString();
	}
	//	public String toLatex() {
	//		StringBuffer str = new StringBuffer();
	//
	//	    // Clase del documento y opciones generales
	//	    str.append("\\documentclass[a0,landscale]{a0poster}\n");
	//
	//	    // Paquetes utilizados
	//	    str.append("\\usepackage{mathptmx}\n");
	//	    str.append("\\usepackage[scaled=.90]{helvet}\n");
	//	    str.append("\\usepackage{courier}\n");
	//	    str.append("\\usepackage{qtree}\n");
	//	    str.append("\\usepackage{nodo}\n");
	//	    str.append("\\usepackage[spanish]{babel}\n");
	//	    str.append("\\usepackage[utf8]{inputenc}\n");
	//
	//	    str.append("\\title{Arbol de ejecucion - Estrategia: " +
	//	        "NO SETEADA" + "}\n");
	//	    str.append("\\author{}\n");
	//	    str.append("\\begin{document}\n");
	//	    str.append("\\maketitle\n");
	//	}

	public String toQtree() {

		StringBuffer resultado = new StringBuffer();

		//resultado.append("\\begin{figure}[!h]\n");
		resultado.append("[." + this.toLatex() + " ");
		//resultado.append("\\Tree [." + this.toLatex() + " ");

		sons.forEach(son -> resultado.append(son.toLatex() + " "));
		//		for (NTree hijo : this.sons) {
		//			resultado.append(hijo.toLatex() + " ");
		//		}

		resultado.append("]");
		//resultado.append("\\end{figure}\n");

		return resultado.toString();
	}

	private String toLatex() {
		String resultado;

		resultado = "\\nodo" + "{" + this.executionOrder + "}" + "{" + this.cost + "}";

		if(this.action != null){
			resultado += "{" + this.action.toString() + "}";
		}
		else{
			resultado += "{-}";
		}
		return resultado;
	}

	@Override
	public boolean equals(Object obj) {
		return agentState.equals(((NTree) obj).getAgentState());
	}

	@Override
	public int compareTo(NTree o) {
		if(this.cost == o.cost){
			// If both nodes have the same cost then it is necessary to check the order
			// in which those nodes have been created.-
			if(this.executionOrder == o.executionOrder){
				return 0;
			}
			else if(this.executionOrder < o.executionOrder){
				return -1;
			}
			else{
				return 1;
			}
		}
		else if(this.cost < o.cost){
			return -1;
		}
		else{
			return 1;
		}
	}
}
