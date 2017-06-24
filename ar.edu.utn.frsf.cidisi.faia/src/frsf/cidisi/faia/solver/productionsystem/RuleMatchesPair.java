package frsf.cidisi.faia.solver.productionsystem;

import javafx.util.Pair;

@SuppressWarnings("serial")
public class RuleMatchesPair extends Pair<Rule, Matches> {

	private Integer novelty;

	public RuleMatchesPair(Rule key, Matches value, Integer novelty) {
		super(key, value);
		this.novelty = novelty;
	}

	public RuleMatchesPair(Rule key, Matches value) {
		this(key, value, 0);
	}

	public Integer getNovelty() {
		return novelty;
	}

	public void setNovelty(Integer novelty) {
		this.novelty = novelty;
	}
}
