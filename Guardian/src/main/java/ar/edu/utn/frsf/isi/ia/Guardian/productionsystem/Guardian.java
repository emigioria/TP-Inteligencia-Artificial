/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Predicado;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteMatcher;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteMatches;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteProductionMemory;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteRule;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Unir;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.UnirAdapter;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro.FiltroIgualdad;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro.FiltroMayorOIgual;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro.FiltroPalabrasCompuestas;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro.Unificar;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Accion;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Clasificada;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Critica;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Escuchada;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Frase;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.LimiteRiesgo;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.NoSospecho;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Riesgo;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Sospecho;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.TieneRiesgo;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemBasedAgent;
import frsf.cidisi.faia.solver.productionsystem.Criteria;
import frsf.cidisi.faia.solver.productionsystem.InferenceEngine;
import frsf.cidisi.faia.solver.productionsystem.Matcher;
import frsf.cidisi.faia.solver.productionsystem.Matches;
import frsf.cidisi.faia.solver.productionsystem.ProductionSystemSolveParam;
import frsf.cidisi.faia.solver.productionsystem.Rule;
import frsf.cidisi.faia.solver.productionsystem.criterias.NoDuplication;
import frsf.cidisi.faia.solver.productionsystem.criterias.Novelty;
import frsf.cidisi.faia.solver.productionsystem.criterias.Priority;
import frsf.cidisi.faia.solver.productionsystem.criterias.Random;
import frsf.cidisi.faia.solver.productionsystem.criterias.Specificity;

public class Guardian extends ProductionSystemBasedAgent {

	private List<Criteria> criterios;
	private List<Rule> listaReglas;
	private List<Predicado> listaPredicados;

	public Guardian() throws Exception {
		// The Agent State
		String ruta = new URI(Guardian.class.getResource("/db/init.pl").toString()).getPath();
		EstadoGuardian agState = new EstadoGuardian(ruta);
		this.setAgentState(agState);

		inicializarGuardian();
	}

	public Guardian(EstadoGuardian estadoGuardian) {
		// The Agent State
		this.setAgentState(estadoGuardian);

		inicializarGuardian();
	}

	private void inicializarGuardian() {
		//Crear reglas
		crearPredicadosYReglas();
		listaPredicados.parallelStream().forEach(p -> p.setRWM(this.getAgentState()));

		//Crear memoria de trabajo
		ReteProductionMemory productionMemory = new ReteProductionMemory(listaReglas);
		this.setProductionMemory(productionMemory);
		listaPredicados.parallelStream().forEach(p -> productionMemory.agregarSalida(p));
		productionMemory.inicializar();

		//Crear criterios para resolver conflictos
		criterios = new ArrayList<>();
		criterios.add(new NoDuplication(this));
		criterios.add(new Novelty());
		criterios.add(new Priority());
		criterios.add(new Specificity());
		criterios.add(new Random());
	}

	/**
	 * This method is executed by the simulator to give the agent a perception.
	 * Then it updates its state.
	 *
	 * @param p
	 */
	@Override
	public void see(Perception p) {
		this.getAgentState().updateState(p);

		//Borramos las reglas usadas previamente.
		this.getUsedRules().clear();
	}

	@Override
	public EstadoGuardian getAgentState() {
		return (EstadoGuardian) super.getAgentState();
	}

	/**
	 * This method is executed by the simulator to ask the agent for an action.
	 */
	@Override
	public Action learn() {
		Matcher matcher = new ReteMatcher();

		// Set the InferenceEngine.
		this.setSolver(new InferenceEngine(criterios, matcher));

		// Ask the solver for the best action
		Action selectedAction = null;
		try{
			selectedAction = this.getSolver().solve(new ProductionSystemSolveParam(this.getProductionMemory(), this.getAgentState()));
		} catch(Exception ex){
			Logger.getLogger(Guardian.class.getName()).log(Level.SEVERE, null, ex);
		}

		// Return the selected action
		return selectedAction;
	}

	@Override
	public boolean finish() {
		return false;
	}

	protected void crearPredicadosYReglas() {

		this.listaReglas = new ArrayList<>();
		this.listaPredicados = new ArrayList<>();

		//DELITO CALLEJERO
		//accion delito callejero llamar 911
		Accion accion = new Accion();
		listaPredicados.add(accion);

		FiltroIgualdad filtroDelitoCallejeroAccion = new FiltroIgualdad(0, "delitoCallejero");
		accion.agregarSalida(filtroDelitoCallejeroAccion);

		ReteRule reglaAccionDelitoCallejeroLlamar911 = new ReteRule(1, 1, 20) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Grabando audio");
			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoCallejeroLlamar911);
		listaReglas.add(reglaAccionDelitoCallejeroLlamar911);

		//accion delito callejero grabar lo que sucede
		ReteRule reglaAccionDelitoCallejeroGrabar = new ReteRule(2, 1, 19) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Llamando a la policia");
			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoCallejeroGrabar);
		listaReglas.add(reglaAccionDelitoCallejeroGrabar);

		//accion delito callejero llamar policia
		ReteRule reglaAccionDelitoCallejeroLlamar = new ReteRule(3, 1, 18) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Enviando audio y lugar a la policia");
				mandarPatruIA();
			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoCallejeroLlamar);
		listaReglas.add(reglaAccionDelitoCallejeroLlamar);

		//DELITO HOGAR
		//accion delito hogar llamar 911
		FiltroIgualdad filtroDelitoHogarAccion = new FiltroIgualdad(0, "delitoHogar");
		accion.agregarSalida(filtroDelitoHogarAccion);

		ReteRule reglaAccionDelitoHogarLlamar911 = new ReteRule(4, 1, 20) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Llamando al 911");
			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarLlamar911);
		listaReglas.add(reglaAccionDelitoHogarLlamar911);

		//accion delito hogar enviar audio al 911
		ReteRule reglaAccionDelitoHogarEnviarAudio = new ReteRule(5, 1, 19) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Enviando audio y lugar al 911");
				mandarPatruIA();
			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarEnviarAudio);
		listaReglas.add(reglaAccionDelitoHogarEnviarAudio);

		//accion delito hogar activar camara de seguridad

		ReteRule reglaAccionDelitoHogarActivarCamara = new ReteRule(6, 1, 18) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Activando cámara de seguridad");
			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarActivarCamara);
		listaReglas.add(reglaAccionDelitoHogarActivarCamara);

		//accion delito hogar activar alarma vecinal

		ReteRule reglaAccionDelitoHogarActivarAlarma = new ReteRule(7, 1, 17) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Activando alarma vecinal");
			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarActivarAlarma);
		listaReglas.add(reglaAccionDelitoHogarActivarAlarma);

		//VIOLENCIA DOMESTICA
		//accion violencia domestica grabar audio
		FiltroIgualdad filtroViolenciaDomesticaAccion = new FiltroIgualdad(0, "violenciaDomestica");
		accion.agregarSalida(filtroViolenciaDomesticaAccion);

		ReteRule reglaAccionViolenciaDomesticaGrabarAudio = new ReteRule(8, 1, 20) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Grabando audio");
			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaAccionViolenciaDomesticaGrabarAudio);
		listaReglas.add(reglaAccionViolenciaDomesticaGrabarAudio);

		//accion violencia domestica llamar 911
		ReteRule reglaAccionViolenciaDomesticaLlamar911 = new ReteRule(9, 1, 19) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Llamando al 911");
			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaAccionViolenciaDomesticaLlamar911);
		listaReglas.add(reglaAccionViolenciaDomesticaLlamar911);

		//accion violencia domestica enviar audio al 911
		ReteRule reglaViolenciaDomesticaEnviarAudio = new ReteRule(10, 1, 18) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Enviando audio y lugar al 911");
				mandarPatruIA();
			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaViolenciaDomesticaEnviarAudio);
		listaReglas.add(reglaViolenciaDomesticaEnviarAudio);

		//accion violencia domestica llamar familiar

		ReteRule reglaViolenciaDomesticaLlamarFamiliar = new ReteRule(11, 1, 17) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Llamando a un familiar");
			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaViolenciaDomesticaLlamarFamiliar);
		listaReglas.add(reglaViolenciaDomesticaLlamarFamiliar);

		//INCENDIO
		//accion incendio llamar bomberos
		FiltroIgualdad filtroIncendioAccion = new FiltroIgualdad(0, "incendio");
		accion.agregarSalida(filtroIncendioAccion);

		ReteRule reglaAccionIncendioLlamar = new ReteRule(12, 1, 20) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Llamando bomberos");
			}
		};

		filtroIncendioAccion.agregarSalida(reglaAccionIncendioLlamar);
		listaReglas.add(reglaAccionIncendioLlamar);

		//accion incendio enviar audio a bomberos
		ReteRule reglaAccionIncendioEnviarAudio = new ReteRule(13, 1, 19) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Enviando audio y lugar a bomberos");
				mandarPatruIA();
			}
		};

		filtroIncendioAccion.agregarSalida(reglaAccionIncendioEnviarAudio);
		listaReglas.add(reglaAccionIncendioEnviarAudio);

		//EMERGENCIA MEDICA
		//accion emergencia medica llamar hospital
		FiltroIgualdad filtroEmergenciaMedicaAccion = new FiltroIgualdad(0, "emergenciaMedica");
		accion.agregarSalida(filtroEmergenciaMedicaAccion);

		ReteRule reglaAccionEmergenciaMedicaLlamar = new ReteRule(14, 1, 20) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Llamando a hospital");
			}
		};

		filtroEmergenciaMedicaAccion.agregarSalida(reglaAccionEmergenciaMedicaLlamar);
		listaReglas.add(reglaAccionEmergenciaMedicaLlamar);

		//accion emergencia medica enviar audio a hospital
		ReteRule reglaAccionEmergenciaMedicaEnviar = new ReteRule(15, 1, 19) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Enviando audio y lugar a hospital");
				mandarPatruIA();
			}
		};

		filtroEmergenciaMedicaAccion.agregarSalida(reglaAccionEmergenciaMedicaEnviar);
		listaReglas.add(reglaAccionEmergenciaMedicaEnviar);

		//EXPLOSION
		//accion explosion llamar policia
		FiltroIgualdad filtroexplosionAccion = new FiltroIgualdad(0, "explosion");
		accion.agregarSalida(filtroexplosionAccion);

		ReteRule reglaAccionExplosionLlamar = new ReteRule(16, 1, 20) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Llamando a policía");
			}
		};

		filtroexplosionAccion.agregarSalida(reglaAccionExplosionLlamar);
		listaReglas.add(reglaAccionExplosionLlamar);

		//accion explosion enviar audio a policia
		ReteRule reglaAccionExplosionEnviarAudio = new ReteRule(17, 1, 19) {

			@Override
			public void execute(Matches unificaciones) {
				mostrarAccion("Enviando audio y lugar a policía");
				mandarPatruIA();
			}
		};

		filtroexplosionAccion.agregarSalida(reglaAccionExplosionEnviarAudio);
		listaReglas.add(reglaAccionExplosionEnviarAudio);

		//reiniciar riesgos
		Riesgo riesgo = new Riesgo();
		listaPredicados.add(riesgo);

		Unir unionAccionRiesgo = new Unir(2);
		UnirAdapter unirAdapterAccion = new UnirAdapter(0, unionAccionRiesgo);
		UnirAdapter unirAdapterRiesgo = new UnirAdapter(1, unionAccionRiesgo);
		accion.agregarSalida(unirAdapterAccion);
		riesgo.agregarSalida(unirAdapterRiesgo);

		Unificar unificarAccionRiesgo = new Unificar(0, 0, 1, 0);
		unionAccionRiesgo.agregarSalida(unificarAccionRiesgo);

		ReteRule reglaReiniciarRiesgos = new ReteRule(18, 2, 11) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				String tipoIncidente = rm.getHecho(0).get(0).toString();
				estadoGuardian.addPredicate("riesgo(" + tipoIncidente + ",0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(" + tipoIncidente + "," + nivelViejo + ")");
				estadoGuardian.removePredicate("accion(" + tipoIncidente + ")");
			}
		};

		unificarAccionRiesgo.agregarSalida(reglaReiniciarRiesgos);
		listaReglas.add(reglaReiniciarRiesgos);

		//clasificada - tiene riesgo - riesgo
		Clasificada clasificada = new Clasificada();
		listaPredicados.add(clasificada);

		TieneRiesgo tieneRiesgo = new TieneRiesgo();
		listaPredicados.add(tieneRiesgo);

		Unir unionClasificadaTieneriesgoRiesgo = new Unir(3);
		UnirAdapter unirAdapterClasificada = new UnirAdapter(0, unionClasificadaTieneriesgoRiesgo);
		UnirAdapter unirAdapterTieneRiesgo = new UnirAdapter(1, unionClasificadaTieneriesgoRiesgo);
		UnirAdapter unirAdapterRiesgo7 = new UnirAdapter(2, unionClasificadaTieneriesgoRiesgo);
		clasificada.agregarSalida(unirAdapterClasificada);
		tieneRiesgo.agregarSalida(unirAdapterTieneRiesgo);
		riesgo.agregarSalida(unirAdapterRiesgo7);

		Unificar unificar1 = new Unificar(0, 0, 1, 0); //incidente entre clasificada y tiene riesgo
		unionClasificadaTieneriesgoRiesgo.agregarSalida(unificar1);

		Unificar unificar2 = new Unificar(1, 0, 2, 0); //incidente entre tiene riesgo y riesgo
		unificar1.agregarSalida(unificar2);

		Unificar unificar3 = new Unificar(0, 1, 1, 1); //palabra entre clasificada y tiene riesgo
		unificar2.agregarSalida(unificar3);

		ReteRule reglaClasificadaTieneriesgoRiesgo = new ReteRule(19, 3, 5) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				Integer nivelViejo = new Integer(rm.getHecho(2).get(1).toString());
				Integer valor = new Integer(rm.getHecho(1).get(2).toString());
				String incidente = rm.getHecho(0).get(0).toString();
				String numero = ((Integer) (nivelViejo + valor)).toString();
				estadoGuardian.addPredicate("riesgo(" + incidente + "," + numero + ")");
				estadoGuardian.removePredicate("riesgo(" + incidente + "," + nivelViejo + ")");
				String palabra = rm.getHecho(0).get(1).toString();
				estadoGuardian.removePredicate("clasificada(" + incidente + "," + palabra + ")");
			}
		};

		unificar3.agregarSalida(reglaClasificadaTieneriesgoRiesgo);
		listaReglas.add(reglaClasificadaTieneriesgoRiesgo);

		//limite riesgo - riesgo - sospecho
		LimiteRiesgo limiteRiesgo = new LimiteRiesgo();
		listaPredicados.add(limiteRiesgo);

		Sospecho sospecho = new Sospecho();
		listaPredicados.add(sospecho);

		Unir unionLimiteriesgoRiesgoSospecho = new Unir(3);
		UnirAdapter unirAdapterLimiteRiesgo = new UnirAdapter(0, unionLimiteriesgoRiesgoSospecho);
		UnirAdapter unirAdapterRiesgo8 = new UnirAdapter(1, unionLimiteriesgoRiesgoSospecho);
		UnirAdapter unirAdapterSospecho = new UnirAdapter(2, unionLimiteriesgoRiesgoSospecho);
		limiteRiesgo.agregarSalida(unirAdapterLimiteRiesgo);
		riesgo.agregarSalida(unirAdapterRiesgo8);
		sospecho.agregarSalida(unirAdapterSospecho);

		Unificar unificar4 = new Unificar(0, 0, 1, 0); //incidente entre limite riesgo y riesgo
		unionLimiteriesgoRiesgoSospecho.agregarSalida(unificar4);

		Unificar unificar5 = new Unificar(1, 0, 2, 0); //incidente entre riesgo y sospecho
		unificar4.agregarSalida(unificar5);

		FiltroMayorOIgual filtroMayorOIgual = new FiltroMayorOIgual();
		unificar5.agregarSalida(filtroMayorOIgual);

		ReteRule reglaLimiteriesgoRiesgoSospecho = new ReteRule(20, 4, 5) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String incidente = rm.getHecho(0).get(0).toString();
				estadoGuardian.addPredicate("accion(" + incidente + ")");
				estadoGuardian.addPredicate("noSospecho(" + incidente + ")");
				estadoGuardian.removePredicate("sospecho(" + incidente + ")");
			}
		};

		filtroMayorOIgual.agregarSalida(reglaLimiteriesgoRiesgoSospecho);
		listaReglas.add(reglaLimiteriesgoRiesgoSospecho);

		//escuchada - critica - no sospecho
		Escuchada escuchada = new Escuchada();
		listaPredicados.add(escuchada);

		Critica critica = new Critica();
		listaPredicados.add(critica);

		NoSospecho noSospecho = new NoSospecho();
		listaPredicados.add(noSospecho);

		Unir unionEscuchadaCriticaNosospecho = new Unir(3);
		UnirAdapter unirAdapterEscuchada01 = new UnirAdapter(0, unionEscuchadaCriticaNosospecho);
		UnirAdapter unirAdapterCritica = new UnirAdapter(1, unionEscuchadaCriticaNosospecho);
		UnirAdapter unirAdapterNoSospecho = new UnirAdapter(2, unionEscuchadaCriticaNosospecho);
		escuchada.agregarSalida(unirAdapterEscuchada01);
		critica.agregarSalida(unirAdapterCritica);
		noSospecho.agregarSalida(unirAdapterNoSospecho);

		Unificar unificar6 = new Unificar(0, 0, 1, 1); //palabra entre escuchada y critica
		unionEscuchadaCriticaNosospecho.agregarSalida(unificar6);

		Unificar unificar7 = new Unificar(1, 0, 2, 0); //incidente entre critica y no sospecho
		unificar6.agregarSalida(unificar7);

		ReteRule reglaEscuchadaCriticaNosospecho = new ReteRule(21, 3, 3) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String incidente = rm.getHecho(1).get(0).toString();
				estadoGuardian.addPredicate("sospecho(" + incidente + ")");
				estadoGuardian.removePredicate("noSospecho(" + incidente + ")");
			}
		};

		unificar7.agregarSalida(reglaEscuchadaCriticaNosospecho);
		listaReglas.add(reglaEscuchadaCriticaNosospecho);

		//Escuchada - tiene riesgo
		Unir unionEscuchadaTieneriesgo = new Unir(2);
		UnirAdapter unirAdapterEscuchada02 = new UnirAdapter(0, unionEscuchadaTieneriesgo);
		UnirAdapter unirAdapterTieneRiesgo2 = new UnirAdapter(1, unionEscuchadaTieneriesgo);
		escuchada.agregarSalida(unirAdapterEscuchada02);
		tieneRiesgo.agregarSalida(unirAdapterTieneRiesgo2);

		Unificar unificar8 = new Unificar(0, 0, 1, 1); //palabra entre escuchada y tiene riesgo
		unionEscuchadaTieneriesgo.agregarSalida(unificar8);

		ReteRule reglaEscuchadaTieneriesgo = new ReteRule(22, 2, 2) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String incidente = rm.getHecho(1).get(0).toString();
				String palabra = rm.getHecho(0).get(0).toString();
				estadoGuardian.addPredicate("clasificada(" + incidente + "," + palabra + ")");
			}
		};

		unificar8.agregarSalida(reglaEscuchadaTieneriesgo);
		listaReglas.add(reglaEscuchadaTieneriesgo);

		//escuchada
		ReteRule reglaEscuchada = new ReteRule(23, 1, 1) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				estadoGuardian.removePredicate("escuchada(" + palabra + "," + n + ")");
			}
		};

		escuchada.agregarSalida(reglaEscuchada);
		listaReglas.add(reglaEscuchada);

		//armar frases
		Frase frase = new Frase();
		listaPredicados.add(frase);

		Unir unionArmarFrase = new Unir(3);
		UnirAdapter unirAdapterFrase = new UnirAdapter(0, unionArmarFrase);
		UnirAdapter unirAdapterEscuchada1 = new UnirAdapter(1, unionArmarFrase);
		UnirAdapter unirAdapterEscuchada2 = new UnirAdapter(2, unionArmarFrase);
		frase.agregarSalida(unirAdapterFrase);
		escuchada.agregarSalida(unirAdapterEscuchada1);
		escuchada.agregarSalida(unirAdapterEscuchada2);

		Unificar unificar9 = new Unificar(0, 0, 1, 0); //palabra entre frase y escuchada1
		unionArmarFrase.agregarSalida(unificar9);

		Unificar unificar10 = new Unificar(0, 1, 2, 0); //palabra entre frase y escuchada2
		unificar9.agregarSalida(unificar10);

		FiltroPalabrasCompuestas filtroEscuchada = new FiltroPalabrasCompuestas();
		unificar10.agregarSalida(filtroEscuchada);

		ReteRule reglaArmarFrase = new ReteRule(24, 2, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(1).get(0).toString();
				String n = rm.getHecho(1).get(1).toString();
				String palabra2 = rm.getHecho(2).get(0).toString();
				String m = rm.getHecho(2).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + m + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada.agregarSalida(reglaArmarFrase);
		listaReglas.add(reglaArmarFrase);
	}

	protected void mostrarAccion(String accion) {
		System.out.println(accion);
	}

	@Override
	public String toString() {
		return "GuardIAn";
	}

	protected void mandarPatruIA() {
		System.out.println("Mandando móvil con el sistema PatruIA");
	}

}
