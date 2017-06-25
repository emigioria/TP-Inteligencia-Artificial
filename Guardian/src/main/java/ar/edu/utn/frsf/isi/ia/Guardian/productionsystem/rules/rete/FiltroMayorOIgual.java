package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FiltroMayorOIgual extends Filtro {

	public FiltroMayorOIgual() {
		super(llh -> {
			List<Hecho> hechosPredicado1 = llh.get(0);
			List<Hecho> hechosPredicado2 = llh.get(1);

			new ArrayList<>(hechosPredicado1).parallelStream().forEach(h1 -> {
				Boolean encontrado = false;
				Iterator<Hecho> hP2It = hechosPredicado2.iterator();

				while(hP2It.hasNext() && !encontrado){
					Hecho h2 = hP2It.next();
					Integer limite = new Integer(h1.get(1).toString());
					Integer nivel = new Integer(h2.get(1).toString());
					if(nivel>=limite){
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
					Integer limite = new Integer(h1.get(1).toString());
					Integer nivel = new Integer(h2.get(1).toString());
					if(nivel>=limite){
						encontrado = true;
					}
				}

				if(!encontrado){
					hechosPredicado2.remove(h2);
				}
			});
			return llh;
		});
	}


}
