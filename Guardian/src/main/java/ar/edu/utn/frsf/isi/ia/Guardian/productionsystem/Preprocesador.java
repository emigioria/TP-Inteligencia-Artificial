package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import ar.edu.utn.frsf.isi.ia.Guardian.datos.BaseVerbos;
import ar.edu.utn.frsf.isi.ia.Guardian.datos.Sinonimos;
import ar.edu.utn.frsf.isi.ia.Guardian.util.NormalizadorDeTexto;

public class Preprocesador {
	private NormalizadorDeTexto normalizadorDeTexto;
	private BaseVerbos baseVerbos;
	private Sinonimos baseSinonimos;
	private Set<String> setPalabrasRelevantes;

	public Preprocesador(Set<String> palabrasRelevantes) throws Exception {
		baseVerbos = new BaseVerbos();
		normalizadorDeTexto = new NormalizadorDeTexto();
		setPalabrasRelevantes = palabrasRelevantes;
		baseSinonimos = new Sinonimos();
	}

	public List<List<String>> procesar(GuardianPerception gPerception) {
		List<String> palabras = new ArrayList<>();

		//TODO: reemplazar el StringTokenizer por un objeto de la clase Sentence del Simple CoreNLP
		//Sentence frase = new Sentence();

		StringTokenizer palabrasTokenizer = new StringTokenizer(
				normalizadorDeTexto.reemplazarCaracteresRaros(gPerception.getPercepcion().toLowerCase()),
				" ,()\"\'");

		//Obtenemos las palabras individuales percibidas
		while(palabrasTokenizer.hasMoreTokens()){
			palabras.add(palabrasTokenizer.nextToken());
		}

		if(palabras.isEmpty()){
			return new ArrayList<>();
		}

		baseVerbos.conectar();

		List<String> palabrasProcesadas = palabras.stream()
				.map(palabra -> {
					if(palabra.equals("dame")){
						return "dar";
					}
					return baseVerbos.infinitivo(palabra);
				})
				.collect(Collectors.toList());

		baseVerbos.desconectar();

		List<String> palabrasProcesadas2 = palabras.stream()
				.map(palabra -> normalizadorDeTexto.singularizar(palabra))
				.collect(Collectors.toList());

		List<List<String>> listaDeListasDeSinonimos = palabrasProcesadas
				.parallelStream()
				.map(palabra -> sinonimosClavesDe(palabra))
				.collect(Collectors.toList());

		List<List<String>> listaDeListasDeSinonimos2 = palabrasProcesadas2
				.parallelStream()
				.map(palabra -> sinonimosClavesDe(palabra))
				.collect(Collectors.toList());

		for(int i = 0; i < listaDeListasDeSinonimos.size(); i++){
			listaDeListasDeSinonimos.get(i).addAll(listaDeListasDeSinonimos2.get(i));
		}

		return listaDeListasDeSinonimos;
	}

	private List<String> sinonimosClavesDe(String palabra) {
		if(palabra == null){
			return new ArrayList<>();
		}

		//Saco los sinónimos
		List<String> sinonimos = baseSinonimos.sinonimosDe(palabra);
		sinonimos.add(palabra);

		//De cada lista de sinonimos nos quedamos con las palabras clave
		sinonimos.retainAll(setPalabrasRelevantes);

		return sinonimos.parallelStream().distinct().collect(Collectors.toList());
	}
}
