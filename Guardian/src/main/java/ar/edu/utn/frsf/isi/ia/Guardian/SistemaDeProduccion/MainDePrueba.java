package ar.edu.utn.frsf.isi.ia.Guardian.SistemaDeProduccion;

import java.util.ArrayList;
import java.util.Map;

import org.jpl7.*;

public class MainDePrueba {

	public static void main(String[] args) {

		conectar();
		Query q1 = new Query("italianFood(A,B)");
		Query q2 = new Query("italianCity(A,B)");
		ArrayList<Query> queries = new ArrayList<>();
		queries.add(q1);
		queries.add(q2);

		ArrayList<Map<String, Term>[]> soluciones = new ArrayList<>();
		//queries.stream().parallel().forEach(x -> System.out.println(((x.hasSolution())?"bien":"mal")));
		queries.stream().parallel().forEach(x -> soluciones.add(x.allSolutions()));

		System.out.println(soluciones.size());
		Map<String, Term>[] solucion = soluciones.get(0);

		for (int i = 0; i < solucion.length; i++) {
			Map<String, Term> map = solucion[i];
			System.out.println(map.get("B").toString());
		}

		Map<String, Term>[] solucion2 = soluciones.get(1);

		for (int i = 0; i < solucion2.length; i++) {
			Map<String, Term> map2 = solucion2[i];
			System.out.println(map2.get("A").toString());
		}


	}

	private static void conectar() {
		try{
			JPL.init();
		} catch(UnsatisfiedLinkError ex){
			ex.printStackTrace();
		}

		Query prologQuery;
		prologQuery = new Query("consult('italian.pl')");

		try {
			System.out.println((prologQuery.hasSolution())?"conectado a base de conocimiento":"no conectado");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
