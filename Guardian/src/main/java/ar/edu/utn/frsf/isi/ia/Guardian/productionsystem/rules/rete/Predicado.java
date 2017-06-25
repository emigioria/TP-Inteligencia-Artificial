/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public abstract class Predicado extends NodoRete implements ReteWorkingMemoryChangeListener {

	private ReteWorkingMemory rwm;
	private String nombre;
	private String nombreConsulta;

	public Predicado(String nombre, Integer cantidadParamentros) {
		nombre = Character.toLowerCase(nombre.charAt(0)) + nombre.substring(1, nombre.length());
		this.nombre = nombre;
		this.nombreConsulta = nombre + getStringParametros(cantidadParamentros);
	}

	public void setRWM(ReteWorkingMemory rwm) {
		this.rwm = rwm;
		rwm.suscribe(this);
	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		List<Matches> nuevosHechos = rwm.query(nombreConsulta).stream().map(map -> {
			List<Object> valores = new ArrayList<>();
			for(Entry<String, String> hecho: map.entrySet()){
				valores.set(this.number(hecho.getKey()), hecho.getValue());
			}
			return new Hecho(valores);
		}).map(h -> {
			ReteMatches rm = new ReteMatches();
			rm.addHecho(h);
			return rm;
		}).collect(Collectors.toList());
		hechos.addAll(nuevosHechos);

		super.propagarHechos(hechos);
	}

	private String getStringParametros(Integer cantidadParamentros) {
		StringBuilder retorno = new StringBuilder("(");
		for(int i = 1; i < cantidadParamentros; i++){
			retorno.append(alpha(i)).append(",");
		}
		if(cantidadParamentros > 0){
			retorno.append(alpha(cantidadParamentros));
		}
		return retorno.append(")").toString();
	}

	private static char[] letras;
	static{
		letras = new char['Z' - 'A' + 1];
		for(char i = 'A'; i <= 'Z'; i++){
			letras[i - 'A'] = i;
		}
	}

	private StringBuilder alpha(int i) {
		char r = letras[--i % letras.length];
		int n = i / letras.length;
		return n == 0 ? new StringBuilder().append(r) : alpha(n).append(r);
	}

	private Integer number(String s) {
		return numberRec(s.toCharArray(), s.length() - 1);
	}

	private Integer numberRec(char[] s, int i) {
		if(i < 0){
			return 0;
		}
		char letra = s[i];
		int n = letra - 'A' + 1;
		return numberRec(s, i - 1) * letras.length + n;
	}

	@Override
	public void cambio(String query) {
		if(query.startsWith(nombre + "(")){
			this.propagarHechos(new ArrayList<>());
		}
	}
}
