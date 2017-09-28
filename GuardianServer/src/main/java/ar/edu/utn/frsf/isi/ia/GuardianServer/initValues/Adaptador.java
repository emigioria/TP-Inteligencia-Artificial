package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaCritica;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaLimiteRiesgo;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaTieneRiesgo;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.Fila;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.Incidente;

public class Adaptador {

	private Procesador procesador;

	public Adaptador() {
		super();
		this.procesador = new Procesador();
	}

	public void obtenerDatos(List<Fila> listaFilas, List<Incidente> listaIncidente,
			List<AxiomaLimiteRiesgo> listaLimiteRiesgo) {
		List<AxiomaTieneRiesgo> listaTieneRiesgo = new ArrayList<>();
		List<AxiomaCritica> listaCritica = new ArrayList<>();

		// proceso (obtengo) los axiomas
		procesador.leer(listaIncidente, listaTieneRiesgo, listaCritica, listaLimiteRiesgo);

		// creo una fila para cada axioma tieneRiesgo
		// por defecto, le asigno a la fila ninguna palabra es crítica
		listaTieneRiesgo.forEach(a -> listaFilas.add(new Fila(a.getIncidente(), a.getPalabra(), a.getValor(), false)));

		// para cada axioma critica seteo en true las filas correspondientes
		listaCritica.forEach(a -> obtenerFilaConIncidente(a.getIncidente(), listaFilas).setCritica(true));
	}

	private Fila obtenerFilaConIncidente(Incidente incidente, List<Fila> listaFilas) {
		Fila fila = null;

		for(Fila f : listaFilas) {
			if(f.getIncidente().equals(incidente)) {
				fila = f;
				break;
			}
		}

		return fila;
	}

	public void guardarDatos(List<Fila> listaFilas, List<Incidente> listaIncidente,
			List<AxiomaLimiteRiesgo> listaLimiteRiesgo) {
		List<AxiomaTieneRiesgo> listaTieneRiesgo = new ArrayList<>();
		List<AxiomaCritica> listaCritica = new ArrayList<>();

		// para cada fila creo un axioma tieneRiesgo
		// si la palabra de la fila es crítica, creo el axioma critica asociado
		listaFilas.forEach(f -> {
			listaTieneRiesgo.add(new AxiomaTieneRiesgo(f.getIncidente(), f.getPalabraStr(), f.getValorStr()));
			if(f.getCriticaBool()) {
				listaCritica.add(new AxiomaCritica(f.getIncidente(), f.getPalabraStr()));
			}
		});

		procesador.guardar(listaIncidente, listaTieneRiesgo, listaCritica, listaLimiteRiesgo);
	}
}
