package frsf.cidisi.faia.solver.productionsystem;

import java.util.HashMap;

public class Matches implements Cloneable {

	private HashMap<Object, Object> matches;

	public Matches() {
		this.matches = new HashMap<>();
	}

	private Matches(Matches m) {
		this.matches = new HashMap<>(m.matches);
	}

	public Object matchValue(Object key) {
		return matches.get(key);
	}

	public void addMatch(Object key, Object value) {
		matches.put(key, value);
	}

	@Override
	public Matches clone() {
		return new Matches(this);
	}

	@Override
	public int hashCode() {
		return matches.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return matches.equals(obj);
	}
}
