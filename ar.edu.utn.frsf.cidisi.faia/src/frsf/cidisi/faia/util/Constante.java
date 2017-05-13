package frsf.cidisi.faia.util;

public enum Constante {
	TREE("tree"),
	BRANCH("branch"),
	LEAF("leaf"),
	NODE("node"),
	EDGE("edge"),
	ID("Id"),
	ACTION("Action"),
	COST("Cost"),
	AGENT_STATE("Estado_Agente"),
	SOURCE("source"),
	TARGET("target"),

	INT("Int"),
	INTEGER("Integer"),
	LONG("Long"),
	FLOAT("Float"),
	REAL("Real"),
	STRING("String"),
	DATE("Date"),
	CATEGORY("Category"),

	// prefuse-specific allowed types
	BOOLEAN("Boolean"),
	DOUBLE("Double");

	private final String text;

	private Constante(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
