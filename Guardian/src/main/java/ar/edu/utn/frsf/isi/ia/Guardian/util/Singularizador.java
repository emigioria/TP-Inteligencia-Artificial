/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.util;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.TreeMap;

public class Singularizador {

	private String cortar(String s, String suffix) {
		return (s.substring(0, s.length() - suffix.length()));
	}

	public static final Map<String, String> irregular = new FinalMap<>(
			"oes", "o", "espráis", "espray",
			"noes", "no",
			"yoes", "yos",
			"volúmenes", "volumen",
			"cracs", "crac",
			"albalaes", "albalá",
			"faralaes", "faralá",
			"clubes", "club",
			"países", "país",
			"jerséis", "jersey",
			"especímenes", "espécimen",
			"caracteres", "carácter",
			"menús", "menú",
			"regímenes", "régimen",
			"currículos", "curriculum",
			"ultimatos", "ultimátum",
			"memorandos", "memorándum",
			"referendos", "referéndum",
			"canciones", "canción",
			"sándwiches", "sándwich");

	/**
	 * Contains word forms that can either be plural or singular
	 */
	public static final Set<String> singularYPlural = new FinalSet<>(
			"dux",
			"paraguas",
			"tijeras",
			"compost",
			"test",
			"valses",
			"escolaridad",
			"análisis",
			"caries",
			"trust",
			"dosis",
			"éxtasis",
			"hipótesis",
			"metamorfosis",
			"síntesis",
			"tesis",
			"alias",
			"crisis",
			"rascacielos",
			"parabrisas",
			"sacacorchos",
			"pararrayos",
			"portaequipajes",
			"guardarropas",
			"marcapasos",
			"gafas",
			"vacaciones",
			"víveres",
			"lunes",
			"afrikáans",
			"fórceps",
			"triceps",
			"cuadriceps",
			"martes",
			"miércoles",
			"jueves",
			"viernes",
			"cumpleaños",
			"virus",
			"atlas",
			"sms",
			"déficit");

	/**
	 * contains special words
	 */
	public static final Set<String> sinPlural = new FinalSet<>(
			"nada",
			"nadie",
			"pereza",
			"adolescencia",
			"generosidad",
			"pánico",
			"decrepitud",
			"eternidad",
			"caos",
			"yo",
			"tu",
			"tú",
			"el",
			"él",
			"ella",
			"nosotros",
			"nosotras",
			"vosotros",
			"vosotras",
			"ellos",
			"ellas",
			"viescas");

	public String singularizar(String pString) {
		String lowerText = pString.toLowerCase();

		//Procesar irregulares
		String irreg = irregular.get(lowerText);
		if(irreg != null){
			return (lowerText = irreg);
		}

		//Procesar palabras que pueden ser singulares y plurales a la vez
		if(singularYPlural.contains(lowerText)){
			return lowerText;
		}
		//Procesar palabras que no tienen plural
		if(sinPlural.contains(lowerText)){
			return lowerText;
		}

		//Reglas
		if(lowerText.endsWith("bs")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//crac -- cracs
		if(lowerText.endsWith("cs")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//verdad -- verdades
		if(lowerText.endsWith("des")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//carriles
		if(lowerText.endsWith("les")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//meses
		if(lowerText.endsWith("mes")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//relojes
		if(lowerText.endsWith("jes")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//raids -- raid
		if(lowerText.endsWith("ds")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//charteres
		if(lowerText.endsWith("res")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//camiones
		if(lowerText.endsWith("nes")){
			return lowerText = cortar(lowerText, "es");
		}

		//casa -- casas
		if(lowerText.endsWith("as")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//puf -- pufs
		if(lowerText.endsWith("fs")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//zigzag -- zigzags
		if(lowerText.endsWith("gs")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//relojes -- reloj
		if(lowerText.endsWith("jes")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//chandals
		if(lowerText.endsWith("ls")){
			return (lowerText = cortar(lowerText, "s"));
		}
		if(lowerText.endsWith("ks")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//relojes -- reloj
		if(lowerText.endsWith("les")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//items item
		if(lowerText.endsWith("ms")){
			return (lowerText = cortar(lowerText, "s"));
		}
		if(lowerText.endsWith("nes")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//chip -- chips
		if(lowerText.endsWith("ps")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//chip -- chips
		if(lowerText.endsWith("os")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//bunker -- bunkers
		if(lowerText.endsWith("rs")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//complot- complots
		if(lowerText.endsWith("ts")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//claxons eslalons...
		if(lowerText.endsWith("ns")){
			return (lowerText = cortar(lowerText, "s"));
		}
		if(lowerText.endsWith("vs")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//champús -- champu
		if(lowerText.endsWith("ús")){
			return (lowerText = cortar(lowerText, "s"));
		}

		if(lowerText.endsWith("xes")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//ley -- leyes mas irregulares
		if(lowerText.endsWith("yes")){
			return (lowerText = cortar(lowerText, "es"));
		}
		// alhelí -- alhelís
		if(lowerText.endsWith("ís")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//jabali -- jabalíes
		if(lowerText.endsWith("íes")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//rondó  -- rondoes
		if(lowerText.endsWith("oes")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//xes
		if(lowerText.endsWith("xes")){
			return (lowerText = cortar(lowerText, "es"));
		}
		//rondó  -- rondós
		if(lowerText.endsWith("ós")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//bajá  -- bajás
		if(lowerText.endsWith("ás")){
			return (lowerText = cortar(lowerText, "s"));
		}
		//luz -- luces
		if(lowerText.endsWith("ces")){
			return (lowerText = cortar(lowerText, "ces").concat("z"));
		}
		//jerseis
		if(lowerText.endsWith("éis") || lowerText.endsWith("áis") || lowerText.endsWith("óis") || lowerText.endsWith("úis")){
			return lowerText = cortar(lowerText, "xxx").concat("y");
		}
		//Regla generica para palabras terminadas en es
		if(lowerText.endsWith("es")){
			return (lowerText = cortar(lowerText, "s"));
		}

		//Si ninguna regla se activa, devuelvo la entrada en minúscula
		return lowerText;
	}
}

/**
 * This class is part of the Java Tools (see http://mpii.de/yago-naga/javatools). It is licensed
 * under the Creative Commons Attribution License (see http://creativecommons.org/licenses/by/3.0)
 * by the YAGO-NAGA team (see http://mpii.de/yago-naga).
 *
 *
 *
 *
 *
 * This class provides a very simple container implementation with zero overhead. A FinalSet bases
 * on a sorted, unmodifiable array. The constructor can either be called with a sorted unmodifiable
 * array (default constructor) or with an array that can be cloned and sorted beforehand if desired.
 * Example:
 *
 * <PRE>
 * FinalSet<String> f=new FinalSet("a","b","c");
 * // equivalently:
 * //   FinalSet<String> f=new FinalSet(new String[]{"a","b","c"});
 * //   FinalSet<String> f=new FinalSet(SHALLNOTBECLONED,ISSORTED,"a","b","c");
 * System.out.println(f.get(1));
 * --> b
 * </PRE>
 *
 * @param <T>
 */
class FinalSet<T extends Comparable<T>> extends AbstractList<T> implements Set<T> {

	/**
	 * Holds the data, must be sorted
	 */
	public T[] data;

	/**
	 * Constructs a FinalSet from an array, clones and sorts the array if indicated.
	 */
	@SuppressWarnings("unchecked")
	public FinalSet(boolean clone, T... a) {
		if(clone){
			Comparable<T>[] b = new Comparable[a.length];
			System.arraycopy(a, 0, b, 0, a.length);
			a = (T[]) b;
		}
		Arrays.sort(a);
		data = a;
	}

	/**
	 * Constructs a FinalSet from an array that does not need to be cloned
	 */
	@SuppressWarnings("unchecked")
	public FinalSet(T... a) {
		this(false, a);
	}

	/**
	 * Tells whether x is in the container
	 */
	public boolean contains(T x) {
		return (Arrays.binarySearch(data, x) >= 0);
	}

	/**
	 * Returns the position in the array or -1
	 */
	public int indexOf(T x) {
		int r = Arrays.binarySearch(data, x);
		return (r >= 0 ? r : -1);
	}

	/**
	 * Returns the element at position i
	 */
	@Override
	public T get(int i) {
		return (data[i]);
	}

	/**
	 * Returns the number of elements in this FinalSet
	 */
	@Override
	public int size() {
		return (data.length);
	}

	@Override
	public Spliterator<T> spliterator() {
		return Set.super.spliterator();
	}

}

/**
 * This class is part of the Java Tools (see http://mpii.de/yago-naga/javatools).
 * It is licensed under the Creative Commons Attribution License
 * (see http://creativecommons.org/licenses/by/3.0) by
 * the YAGO-NAGA team (see http://mpii.de/yago-naga).
 *
 *
 *
 * Provides a nicer constructor for a TreeMap.
 * Example:
 *
 * <PRE>
   FinalMap<String,Integer> f=new FinalMap(
     "a",1,
     "b",2,
     "c",3);
   System.out.println(f.get("b"));
   --> 2
 * </PRE>
 */
class FinalMap<T1 extends Comparable<T1>, T2> extends TreeMap<T1, T2> {

	private static final long serialVersionUID = 1L;

	/** Constructs a FinalMap from an array that contains key/value sequences */
	@SuppressWarnings("unchecked")
	public FinalMap(Object... a) {
		super();
		for(int i = 0; i < a.length - 1; i += 2){
			if(containsKey(a[i])){
				throw new RuntimeException("Duplicate key in FinalMap: " + a[i]);
			}
			put((T1) a[i], (T2) a[i + 1]);
		}
	}

}