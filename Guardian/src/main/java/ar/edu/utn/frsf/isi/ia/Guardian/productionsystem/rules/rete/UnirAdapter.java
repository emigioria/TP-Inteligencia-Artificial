package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;

public class UnirAdapter extends Nodo {

	private Unir union;
	private Integer lugar;

	public UnirAdapter(Integer lugar, Unir union) {
		this.lugar = lugar;
		this.union = union;
	}

	@Override
	public void propagarHechos(List<List<Hecho>> hechos) {
		union.unirEn(hechos, lugar);
	}

}
