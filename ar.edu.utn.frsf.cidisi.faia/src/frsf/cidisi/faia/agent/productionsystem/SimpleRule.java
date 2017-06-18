/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.agent.productionsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import frsf.cidisi.faia.solver.productionsystem.Matches;
import frsf.cidisi.faia.solver.productionsystem.Rule;

/**
 * Clase que modela elas reglas del sistema de produccion.
 */
public class SimpleRule implements Rule {

	private Function<List<Matches>, Boolean> condition;
	private Function<Matches, Void> then;
	private Integer id;
	private Integer specificity;
	private Integer priority;
	private Integer novelty;

	public SimpleRule(Integer id) {
		this.id = id;
		specificity = 0;
		priority = 0;
		novelty = 0;
	}

	@Override
	public Integer getId() {
		return new Integer(id);
	}

	public void setId(int identificador) {
		id = identificador;
	}

	@Override
	public Integer getSpecificity() {
		return specificity;
	}

	public void setSpecificity(Integer s) {
		specificity = s;
	}

	@Override
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer p) {
		priority = p;
	}

	@Override
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
		SimpleRule other = (SimpleRule) obj;
		if(id.equals(other.id)){
			return true;
		}
		return false;
	}

	@Override
	public List<Matches> match() {
		List<Matches> matchesList = new ArrayList<>();
		condition.apply(matchesList);
		return matchesList;
	}

	@Override
	public Boolean isActive(List<Matches> matchesList) {
		return condition.apply(matchesList);
	}

	@Override
	public void execute(Matches unificaciones) {
		then.apply(unificaciones);
	}

}
