package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.List;

public class Unir extends Nodo {

	private List<List<Hecho>> union = new ArrayList<>();
	private Integer unionesEsperadas;
	private Integer unionesPasadas = 0;

	public Unir(Integer uniones) {
		this.unionesEsperadas = uniones;
	}

	@Override
	public void propagarHechos(List<List<Hecho>> hechos) {
		throw new UnsupportedOperationException();
	}

	public synchronized void unirEn(List<List<Hecho>> hechos, Integer lugar) {
		for(List<Hecho> lh: hechos){
			union.set(lugar++, lh);
		}

		if(++unionesPasadas == unionesEsperadas){
			super.propagarHechos(union);
		}
	}
}
