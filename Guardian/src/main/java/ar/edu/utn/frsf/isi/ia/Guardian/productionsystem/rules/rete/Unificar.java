package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Unificar extends Nodo {

	private Integer indicePredicado1;
	private Integer indiceHecho1;
	private Integer indicePredicado2;
	private Integer indiceHecho2;

	public Unificar(Integer indicePredicado1, Integer indiceHecho1, Integer indicePredicado2, Integer indiceHecho2) {
		this.indicePredicado1 = indicePredicado1;
		this.indiceHecho1 = indiceHecho1;
		this.indicePredicado2 = indicePredicado2;
		this.indiceHecho2 = indiceHecho2;
	}

	@Override
	public void propagarHechos(List<List<Hecho>> hechos) {
		List<Hecho> hechosPredicado1 = hechos.get(indicePredicado1);
		List<Hecho> hechosPredicado2 = hechos.get(indicePredicado2);

		new ArrayList<>(hechosPredicado1).parallelStream().forEach(h1 -> {
			Boolean encontrado = false;
			Iterator<Hecho> hP2It = hechosPredicado2.iterator();

			while(hP2It.hasNext() && !encontrado){
				Hecho h2 = hP2It.next();
				if(h1.get(indiceHecho1).equals(h2.get(indiceHecho2))){
					encontrado = true;
				}
			}

			if(!encontrado){
				hechosPredicado1.remove(h1);
			}
		});
		new ArrayList<>(hechosPredicado2).parallelStream().forEach(h2 -> {
			Boolean encontrado = false;
			Iterator<Hecho> hP1It = hechosPredicado1.iterator();

			while(hP1It.hasNext() && !encontrado){
				Hecho h1 = hP1It.next();
				if(h2.get(indiceHecho2).equals(h1.get(indiceHecho1))){
					encontrado = true;
				}
			}

			if(!encontrado){
				hechosPredicado2.remove(h2);
			}
		});

		super.propagarHechos(hechos);
	}
}
