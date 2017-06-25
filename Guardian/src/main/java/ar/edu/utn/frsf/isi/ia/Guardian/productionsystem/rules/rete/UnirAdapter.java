package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public class UnirAdapter extends NodoRete {

	private Unir union;
	private Integer lugar;

	public UnirAdapter(Integer lugar, Unir union) {
		this.lugar = lugar;
		this.union = union;
	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		union.unirEn(hechos, lugar);
	}

}
