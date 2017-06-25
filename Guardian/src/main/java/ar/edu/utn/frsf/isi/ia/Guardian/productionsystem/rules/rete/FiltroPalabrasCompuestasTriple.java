package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FiltroPalabrasCompuestasTriple extends Filtro {

	public FiltroPalabrasCompuestasTriple() {
		super(llh -> {
			List<Hecho> hechosPredicado1 = llh.get(0);
			List<Hecho> hechosPredicado2 = llh.get(1);
			List<Hecho> hechosPredicado3 = llh.get(2);

			new ArrayList<>(hechosPredicado1).parallelStream().forEach(h1 -> {
				Boolean encontrado = false;
				Iterator<Hecho> hP2It = hechosPredicado2.iterator();

				while(hP2It.hasNext() && !encontrado){
					Hecho h2 = hP2It.next();
					Integer n = new Integer(h1.get(1).toString());
					Integer m = new Integer(h2.get(1).toString());
					if(m==n+1){
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
					Integer n = new Integer(h1.get(1).toString());
					Integer m = new Integer(h2.get(1).toString());
					if(m==n+1){
						encontrado = true;
					}
				}

				if(!encontrado){
					hechosPredicado2.remove(h2);
				}
			});

			new ArrayList<>(hechosPredicado2).parallelStream().forEach(h2 -> {
				Boolean encontrado = false;
				Iterator<Hecho> hP3It = hechosPredicado3.iterator();

				while(hP3It.hasNext() && !encontrado){
					Hecho h3 = hP3It.next();
					Integer m = new Integer(h2.get(1).toString());
					Integer l = new Integer(h3.get(1).toString());
					if(l==m+1){
						encontrado = true;
					}
				}

				if(!encontrado){
					hechosPredicado2.remove(h2);
				}
			});
			new ArrayList<>(hechosPredicado3).parallelStream().forEach(h3 -> {
				Boolean encontrado = false;
				Iterator<Hecho> hP2It = hechosPredicado2.iterator();

				while(hP2It.hasNext() && !encontrado){
					Hecho h2 = hP2It.next();
					Integer m = new Integer(h2.get(1).toString());
					Integer l = new Integer(h3.get(1).toString());
					if(l==m+1){
						encontrado = true;
					}
				}

				if(!encontrado){
					hechosPredicado3.remove(h3);
				}
			});
			return llh;
		});
	}


}
