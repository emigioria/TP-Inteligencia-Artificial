/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.agent.productionsystem;

import java.util.List;
import java.util.function.Function;

import frsf.cidisi.faia.solver.productionsystem.Matches;

/**
 * Clase que modela elas reglas del sistema de produccion.
 */
public abstract class Rule {

	private Function<List<Matches>, Boolean> condition;
	private Function<Matches, Void> then;
	private Integer id;
	private Integer specificity;
	private Integer priority;
	private Integer novelty;

	public Rule(Integer id) {
		this.id = id;
		specificity = 0;
		priority = 0;
		novelty = 0;
	}

	public Integer getId() {
		return new Integer(id);
	}

	public void setId(int identificador) {
		id = identificador;
	}

	public Integer getSpecificity() {
		return specificity;
	}

	public void setSpecificity(Integer s) {
		specificity = s;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer p) {
		priority = p;
	}

	public Integer getNovelty() {
		return novelty;
	}

	public void setNovelty(Integer n) {
		novelty = n;
	}

	public void setCondition(Function<List<Matches>, Boolean> o) {
		condition = o;
	}

	public Function<List<Matches>, Boolean> getCondition() {
		return condition;
	}

	public void setThen(Function<Matches, Void> t) {
		then = t;
	}

	public Function<Matches, Void> getThen() {
		return then;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Rule other = (Rule) obj;
		if(id.equals(other.id)){
			return true;
		}
		return false;
	}

	public Boolean isActive(List<Matches> matchesList) {
		return condition.apply(matchesList);
	}

	public void execute(Matches unificaciones) {
		then.apply(unificaciones);
	}

}
