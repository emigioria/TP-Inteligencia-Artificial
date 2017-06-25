package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import ar.edu.utn.frsf.isi.ia.Guardian.datos.BaseVerbos;
import ar.edu.utn.frsf.isi.ia.Guardian.datos.Sinonimos;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Predicado;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteMatches;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteProductionMemory;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteRule;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Unir;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.UnirAdapter;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro.FiltroIgualdad;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro.FiltroMayorOIgual;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro.FiltroPalabrasCompuestas;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro.FiltroPalabrasCompuestasTriple;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro.Unificar;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Accion;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Clasificada;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Critica;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Escuchada;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.LimiteRiesgo;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.NoSospecho;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Riesgo;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.Sospecho;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.predicados.TieneRiesgo;
import ar.edu.utn.frsf.isi.ia.Guardian.util.Singularizador;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemAction;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemBasedAgent;
import frsf.cidisi.faia.solver.productionsystem.Criteria;
import frsf.cidisi.faia.solver.productionsystem.Matches;
import frsf.cidisi.faia.solver.productionsystem.Rule;
import frsf.cidisi.faia.solver.productionsystem.criterias.NoDuplication;
import frsf.cidisi.faia.solver.productionsystem.criterias.Priority;
import frsf.cidisi.faia.solver.productionsystem.criterias.Random;
import frsf.cidisi.faia.solver.productionsystem.criterias.Specificity;

public class Guardian extends ProductionSystemBasedAgent {

	private List<Criteria> criterios;
	private Integer proximoIndice = 0;
	private Set<String> setPalabrasRelevantes;

	private List<Rule> listaReglas;
	private List<Predicado> listaPredicados;

	public Guardian() throws Exception {
		// The Agent State
		String ruta = new URI(BaseVerbos.class.getResource("/db/init.pl").toString()).getPath();
		EstadoGuardian agState = new EstadoGuardian(ruta);
		this.setAgentState(agState);

		//Crear reglas
		crearPredicadosYReglas();
		listaPredicados.parallelStream().forEach(p -> p.setRWM(agState));

		//Crear memoria de trabajo
		ReteProductionMemory productionMemory = new ReteProductionMemory(listaReglas);
		this.setProductionMemory(productionMemory);
		listaPredicados.parallelStream().forEach(p -> productionMemory.agregarSalida(p));
		productionMemory.inicializar();

		//Crear criterios para resolver conflictos
		criterios = new ArrayList<>();
		criterios.add(new NoDuplication(this));
		criterios.add(new Priority());
		criterios.add(new Specificity());
		criterios.add(new Random());

		//Cargar todas las palabras relevantes
		setPalabrasRelevantes = cargarTodasLasPalabrasRelevantes();
	}

	/**
	 * This method is executed by the simulator to give the agent a perception.
	 * Then it updates its state.
	 *
	 * @param p
	 */
	@Override
	public void see(Perception p) {
		GuardianPerception gPerception = (GuardianPerception) p;
		ArrayList<String> palabras = new ArrayList<>();

		StringTokenizer palabrasTokenizer = new StringTokenizer(gPerception.getPercepcion().toLowerCase(), " ,()\"\'");

		//Obtenemos las palabras individuales percibidas
		while(palabrasTokenizer.hasMoreTokens()){
			palabras.add(palabrasTokenizer.nextToken());
		}

		if(palabras.isEmpty()){
			return;
		}

		BaseVerbos baseVerbos;
		try{
			baseVerbos = new BaseVerbos();
		} catch(URISyntaxException e){
			//TODO manejar excepcion
			return;
		}
		Singularizador singularizador = new Singularizador();
		ArrayList<String> palabrasProcesadas = new ArrayList<>();
		String palabraEnInfinitivo;

		//Se las pone en infinitivo y se las singulariza
		for(String palabra: palabras){
			palabraEnInfinitivo = baseVerbos.infinitivo(palabra);

			if(palabraEnInfinitivo != null){
				palabrasProcesadas.add(palabraEnInfinitivo);
			}
			else{
				//se hace en el else porque si se encontró su infinitivo no necesita singularizarse
				palabrasProcesadas.add(singularizador.singularizar(palabra));
			}
		}

		ArrayList<ArrayList<String>> listaDelistasDeSinonimos = new ArrayList<>();
		ArrayList<String> sinonimos;
		Sinonimos baseSinonimos = new Sinonimos();

		//Se busca los sinonimos de cada palabra
		for(String palabra: palabrasProcesadas){
			sinonimos = baseSinonimos.sinonimosDe(palabra);
			sinonimos.add(palabra);
			listaDelistasDeSinonimos.add(sinonimos);
		}

		//De cada lista de sinonimos nos quedamos con las palabras clave
		for(ArrayList<String> listaDeSinonimos: listaDelistasDeSinonimos){
			listaDeSinonimos.retainAll(setPalabrasRelevantes);
		}

		//Agregamos las palabras escuchadas a la memoria de trabajo
		for(ArrayList<String> listaDeSinonimos: listaDelistasDeSinonimos){

			for(String palabra: listaDeSinonimos){
				this.getAgentState().addPredicate("escuchada(" + palabra + "," + proximoIndice + ")");
			}

			proximoIndice++;
		}

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
	public ProductionSystemAction learn() {

		return null;
	}

	@Override
	public boolean finish() {

		return false;
	}

	private void crearPredicadosYReglas() {

		this.listaReglas = new ArrayList<>();
		this.listaPredicados = new ArrayList<>();

		//DELITO CALLEJERO
		//accion delito callejero llamar 911
		Accion accion = new Accion();
		listaPredicados.add(accion);

		FiltroIgualdad filtroDelitoCallejeroAccion = new FiltroIgualdad(0, "delitoCallejero");
		accion.agregarSalida(filtroDelitoCallejeroAccion);

		ReteRule reglaAccionDelitoCallejeroLlamar911 = new ReteRule(1, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando al 911");

			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoCallejeroLlamar911);
		listaReglas.add(reglaAccionDelitoCallejeroLlamar911);

		//accion delito callejero grabar lo que sucede
		ReteRule reglaAccionDelitoCallejeroGrabar = new ReteRule(2, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Grabando audio");
			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoCallejeroGrabar);
		listaReglas.add(reglaAccionDelitoCallejeroGrabar);

		//accion delito callejero llamar familiar
		ReteRule reglaAccionDelitoCallejeroLlamar = new ReteRule(3, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando a un familiar");
			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoCallejeroLlamar);
		listaReglas.add(reglaAccionDelitoCallejeroLlamar);

		//accion delito callejero - riesgo
		Riesgo riesgo = new Riesgo();
		listaPredicados.add(riesgo);

		FiltroIgualdad filtroDelitoCallejeroRiesgo = new FiltroIgualdad(0, "delitoCallejero");
		riesgo.agregarSalida(filtroDelitoCallejeroRiesgo);

		Unir unionAccionRiesgo1 = new Unir(2);
		UnirAdapter unirAdapterAccion1 = new UnirAdapter(0, unionAccionRiesgo1);
		UnirAdapter unirAdapterRiesgo1 = new UnirAdapter(1, unionAccionRiesgo1);
		filtroDelitoCallejeroAccion.agregarSalida(unirAdapterAccion1);
		filtroDelitoCallejeroRiesgo.agregarSalida(unirAdapterRiesgo1);

		ReteRule reglaAccionDelitoCallejeroRiesgo = new ReteRule(4, 2, 3) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(delitoCallejero,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(delitoCallejero," + nivelViejo + ")");
				estadoGuardian.removePredicate("accion(delitoCallejero)");
			}
		};

		unionAccionRiesgo1.agregarSalida(reglaAccionDelitoCallejeroRiesgo);
		listaReglas.add(reglaAccionDelitoCallejeroRiesgo);

		//DELITO HOGAR
		//accion delito hogar llamar 911
		FiltroIgualdad filtroDelitoHogarAccion = new FiltroIgualdad(0, "delitoHogar");
		accion.agregarSalida(filtroDelitoHogarAccion);

		ReteRule reglaAccionDelitoHogarLlamar911 = new ReteRule(5, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando al 911");

			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarLlamar911);
		listaReglas.add(reglaAccionDelitoHogarLlamar911);

		//accion delito hogar enviar audio al 911
		ReteRule reglaAccionDelitoHogarEnviarAudio = new ReteRule(6, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Grabando audio y enviándolo al 911");

			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoHogarEnviarAudio);
		listaReglas.add(reglaAccionDelitoHogarEnviarAudio);

		//accion delito hogar activar camara de seguridad

		ReteRule reglaAccionDelitoHogarActivarCamara = new ReteRule(7, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Activando cámara de seguridad");

			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarActivarCamara);
		listaReglas.add(reglaAccionDelitoHogarActivarCamara);

		//accion delito hogar activar alarma vecinal

		ReteRule reglaAccionDelitoHogarActivarAlarma = new ReteRule(8, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Activando alarma vecinal");

			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarActivarAlarma);
		listaReglas.add(reglaAccionDelitoHogarActivarAlarma);

		//accion delito hogar - riesgo
		FiltroIgualdad filtroDelitoHogarRiesgo = new FiltroIgualdad(0, "delitoHogar");
		riesgo.agregarSalida(filtroDelitoHogarRiesgo);

		Unir unionAccionRiesgo2 = new Unir(2);
		UnirAdapter unirAdapterAccion2 = new UnirAdapter(0, unionAccionRiesgo2);
		UnirAdapter unirAdapterRiesgo2 = new UnirAdapter(1, unionAccionRiesgo2);
		filtroDelitoHogarAccion.agregarSalida(unirAdapterAccion2);
		filtroDelitoHogarRiesgo.agregarSalida(unirAdapterRiesgo2);

		ReteRule reglaAccionDelitoHogarRiesgo = new ReteRule(9, 2, 3) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(delitoHogar,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(delitoHogar," + nivelViejo + ")");
				estadoGuardian.removePredicate("accion(delitoHogar)");
			}
		};

		unionAccionRiesgo2.agregarSalida(reglaAccionDelitoHogarRiesgo);
		listaReglas.add(reglaAccionDelitoHogarRiesgo);

		//VIOLENCIA DOMESTICA
		//accion violencia domestica grabar audio
		FiltroIgualdad filtroViolenciaDomesticaAccion = new FiltroIgualdad(0, "violenciaDomestica");
		accion.agregarSalida(filtroViolenciaDomesticaAccion);

		ReteRule reglaAccionViolenciaDomesticaGrabarAudio = new ReteRule(10, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Grabando audio");

			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaAccionViolenciaDomesticaGrabarAudio);
		listaReglas.add(reglaAccionViolenciaDomesticaGrabarAudio);

		//accion violencia domestica llamar 911
		ReteRule reglaAccionViolenciaDomesticaLlamar911 = new ReteRule(11, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando al 911");

			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaAccionViolenciaDomesticaLlamar911);
		listaReglas.add(reglaAccionViolenciaDomesticaLlamar911);

		//accion violencia domestica enviar audio al 911
		ReteRule reglaViolenciaDomesticaEnviarAudio = new ReteRule(12, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Enviando audio al 911");
			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaViolenciaDomesticaEnviarAudio);
		listaReglas.add(reglaViolenciaDomesticaEnviarAudio);

		//accion violencia domestica llamar familiar

		ReteRule reglaViolenciaDomesticaLlamarFamiliar = new ReteRule(13, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando a un familiar");

			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaViolenciaDomesticaLlamarFamiliar);
		listaReglas.add(reglaViolenciaDomesticaLlamarFamiliar);

		//accion violencia domestica - riesgo
		FiltroIgualdad filtroViolenciaDomesticaRiesgo = new FiltroIgualdad(0, "violenciaDomestica");
		riesgo.agregarSalida(filtroViolenciaDomesticaRiesgo);

		Unir unionAccionRiesgo3 = new Unir(2);
		UnirAdapter unirAdapterAccion3 = new UnirAdapter(0, unionAccionRiesgo3);
		UnirAdapter unirAdapterRiesgo3 = new UnirAdapter(1, unionAccionRiesgo3);
		filtroViolenciaDomesticaAccion.agregarSalida(unirAdapterAccion3);
		filtroViolenciaDomesticaRiesgo.agregarSalida(unirAdapterRiesgo3);

		ReteRule reglaAccionViolenciaDomesticaRiesgo = new ReteRule(14, 2, 3) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(violenciaDomestica,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(violenciaDomestica," + nivelViejo + ")");
				estadoGuardian.removePredicate("accion(violenciaDomestica)");
			}
		};

		unionAccionRiesgo3.agregarSalida(reglaAccionViolenciaDomesticaRiesgo);
		listaReglas.add(reglaAccionViolenciaDomesticaRiesgo);

		//INCENDIO
		//accion incendio llamar bomberos
		FiltroIgualdad filtroIncendioAccion = new FiltroIgualdad(0, "incendio");
		accion.agregarSalida(filtroIncendioAccion);

		ReteRule reglaAccionIncendioLlamar = new ReteRule(15, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando bomberos");

			}
		};

		filtroIncendioAccion.agregarSalida(reglaAccionIncendioLlamar);
		listaReglas.add(reglaAccionIncendioLlamar);

		//accion incendio enviar audio a bomberos
		ReteRule reglaAccionIncendioEnviarAudio = new ReteRule(16, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Enviando audio a bomberos");

			}
		};

		filtroIncendioAccion.agregarSalida(reglaAccionIncendioEnviarAudio);
		listaReglas.add(reglaAccionIncendioEnviarAudio);

		//accion incendio - riesgo
		FiltroIgualdad filtroIncendioRiesgo = new FiltroIgualdad(0, "incendio");
		riesgo.agregarSalida(filtroIncendioRiesgo);

		Unir unionAccionRiesgo4 = new Unir(2);
		UnirAdapter unirAdapterAccion4 = new UnirAdapter(0, unionAccionRiesgo4);
		UnirAdapter unirAdapterRiesgo4 = new UnirAdapter(1, unionAccionRiesgo4);
		filtroIncendioAccion.agregarSalida(unirAdapterAccion4);
		filtroIncendioRiesgo.agregarSalida(unirAdapterRiesgo4);

		ReteRule reglaAccionIncendioRiesgo = new ReteRule(17, 2, 3) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(incendio,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(incendio," + nivelViejo + ")");
				estadoGuardian.removePredicate("accion(incendio)");
			}
		};

		unionAccionRiesgo4.agregarSalida(reglaAccionIncendioRiesgo);
		listaReglas.add(reglaAccionIncendioRiesgo);

		//EMERGENCIA MEDICA
		//accion emergencia medica llamar hospital
		FiltroIgualdad filtroEmergenciaMedicaAccion = new FiltroIgualdad(0, "emergenciaMedica");
		accion.agregarSalida(filtroEmergenciaMedicaAccion);

		ReteRule reglaAccionEmergenciaMedicaLlamar = new ReteRule(18, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando a hospital");

			}
		};

		filtroEmergenciaMedicaAccion.agregarSalida(reglaAccionEmergenciaMedicaLlamar);
		listaReglas.add(reglaAccionEmergenciaMedicaLlamar);

		//accion emergencia medica enviar audio a hospital
		ReteRule reglaAccionEmergenciaMedicaEnviar = new ReteRule(19, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Enviando audio a hospital");

			}
		};

		filtroEmergenciaMedicaAccion.agregarSalida(reglaAccionEmergenciaMedicaEnviar);
		listaReglas.add(reglaAccionEmergenciaMedicaEnviar);

		//accion emergencia medica - riesgo
		FiltroIgualdad filtroEmergenciaMedicaRiesgo = new FiltroIgualdad(0, "emergenciaMedica");
		riesgo.agregarSalida(filtroEmergenciaMedicaRiesgo);

		Unir unionAccionRiesgo5 = new Unir(2);
		UnirAdapter unirAdapterAccion5 = new UnirAdapter(0, unionAccionRiesgo5);
		UnirAdapter unirAdapterRiesgo5 = new UnirAdapter(1, unionAccionRiesgo5);
		filtroEmergenciaMedicaAccion.agregarSalida(unirAdapterAccion5);
		filtroEmergenciaMedicaRiesgo.agregarSalida(unirAdapterRiesgo5);

		ReteRule reglaEmergenciaMedicaRiesgo = new ReteRule(20, 2, 3) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(emergenciaMedica,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(emergenciaMedica," + nivelViejo + ")");
				estadoGuardian.removePredicate("accion(emergenciaMedica)");
			}
		};

		unionAccionRiesgo5.agregarSalida(reglaEmergenciaMedicaRiesgo);
		listaReglas.add(reglaEmergenciaMedicaRiesgo);

		//EXPLOSION
		//accion explosion llamar policia
		FiltroIgualdad filtroexplosionAccion = new FiltroIgualdad(0, "explosion");
		accion.agregarSalida(filtroexplosionAccion);

		ReteRule reglaAccionExplosionLlamar = new ReteRule(21, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando a policía");

			}
		};

		filtroexplosionAccion.agregarSalida(reglaAccionExplosionLlamar);
		listaReglas.add(reglaAccionExplosionLlamar);

		//accion explosion enviar audio a policia
		ReteRule reglaAccionExplosionEnviarAudio = new ReteRule(22, 1, 5) {

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Enviando audio a policía");

			}
		};

		filtroexplosionAccion.agregarSalida(reglaAccionExplosionEnviarAudio);
		listaReglas.add(reglaAccionExplosionEnviarAudio);

		//accion explosion - riesgo
		FiltroIgualdad filtroExplosionRiesgo = new FiltroIgualdad(0, "explosion");
		riesgo.agregarSalida(filtroExplosionRiesgo);

		Unir unionAccionRiesgo6 = new Unir(2);
		UnirAdapter unirAdapterAccion6 = new UnirAdapter(0, unionAccionRiesgo6);
		UnirAdapter unirAdapterRiesgo6 = new UnirAdapter(1, unionAccionRiesgo6);
		filtroexplosionAccion.agregarSalida(unirAdapterAccion6);
		filtroExplosionRiesgo.agregarSalida(unirAdapterRiesgo6);

		ReteRule reglaAccionExplosionRiesgo = new ReteRule(23, 2, 3) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(explosion,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(explosion," + nivelViejo + ")");
				estadoGuardian.removePredicate("accion(explosion)");
			}
		};

		unionAccionRiesgo6.agregarSalida(reglaAccionExplosionRiesgo);
		listaReglas.add(reglaAccionExplosionRiesgo);

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

		ReteRule reglaClasificadaTieneriesgoRiesgo = new ReteRule(24, 3, 5) {

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

		ReteRule reglaLimiteriesgoRiesgoSospecho = new ReteRule(25, 4, 5) {

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

		ReteRule reglaEscuchadaCriticaNosospecho = new ReteRule(26, 3, 3) {

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

		ReteRule reglaEscuchadaTieneriesgo = new ReteRule(27, 2, 2) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String incidente = rm.getHecho(1).get(0).toString();
				String palabra = rm.getHecho(0).get(0).toString();
				estadoGuardian.addPredicate("clasificada(" + incidente + "," + palabra + ")");
				String n = rm.getHecho(0).get(1).toString();
				estadoGuardian.removePredicate("escuchada(" + palabra + "," + n + ")");
			}
		};

		unificar8.agregarSalida(reglaEscuchadaTieneriesgo);
		listaReglas.add(reglaEscuchadaTieneriesgo);

		//escuchada
		ReteRule reglaEscuchada = new ReteRule(28, 1, 1) {

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

		//palabra compuesta dar plata
		FiltroIgualdad filtroDar = new FiltroIgualdad(0, "dar");
		escuchada.agregarSalida(filtroDar);

		FiltroIgualdad filtroPlata = new FiltroIgualdad(0, "plata");
		escuchada.agregarSalida(filtroPlata);

		Unir unionEscuchada1 = new Unir(2);
		UnirAdapter unirAdapterDar1 = new UnirAdapter(0, unionEscuchada1);
		UnirAdapter unirAdapterPlata = new UnirAdapter(1, unionEscuchada1);
		filtroDar.agregarSalida(unirAdapterDar1);
		filtroPlata.agregarSalida(unirAdapterPlata);

		FiltroPalabrasCompuestas filtroEscuchada = new FiltroPalabrasCompuestas();
		unionEscuchada1.agregarSalida(filtroEscuchada);

		ReteRule reglaDarPlata = new ReteRule(29, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada.agregarSalida(reglaDarPlata);
		listaReglas.add(reglaDarPlata);

		//palabra compuesta dar bici
		FiltroIgualdad filtroBici = new FiltroIgualdad(0, "bici");
		escuchada.agregarSalida(filtroBici);

		Unir unionEscuchada2 = new Unir(2);
		UnirAdapter unirAdapterDar2 = new UnirAdapter(0, unionEscuchada2);
		UnirAdapter unirAdapterBici = new UnirAdapter(1, unionEscuchada2);
		filtroDar.agregarSalida(unirAdapterDar2);
		filtroBici.agregarSalida(unirAdapterBici);

		FiltroPalabrasCompuestas filtroEscuchada2 = new FiltroPalabrasCompuestas();
		unionEscuchada2.agregarSalida(filtroEscuchada2);

		ReteRule reglaDarBici = new ReteRule(30, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada2.agregarSalida(reglaDarBici);
		listaReglas.add(reglaDarBici);

		//palabra compuesta dar moto
		FiltroIgualdad filtroMoto = new FiltroIgualdad(0, "moto");
		escuchada.agregarSalida(filtroMoto);

		Unir unionEscuchada3 = new Unir(2);
		UnirAdapter unirAdapterDar3 = new UnirAdapter(0, unionEscuchada3);
		UnirAdapter unirAdapterMoto = new UnirAdapter(1, unionEscuchada3);
		filtroDar.agregarSalida(unirAdapterDar3);
		filtroMoto.agregarSalida(unirAdapterMoto);

		FiltroPalabrasCompuestas filtroEscuchada3 = new FiltroPalabrasCompuestas();
		unionEscuchada3.agregarSalida(filtroEscuchada3);

		ReteRule reglaDarMoto = new ReteRule(31, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada3.agregarSalida(reglaDarMoto);
		listaReglas.add(reglaDarMoto);

		//palabra compuesta dar celu
		FiltroIgualdad filtroCelu = new FiltroIgualdad(0, "celu");
		escuchada.agregarSalida(filtroCelu);

		Unir unionEscuchada4 = new Unir(2);
		UnirAdapter unirAdapterDar4 = new UnirAdapter(0, unionEscuchada4);
		UnirAdapter unirAdapterCelu = new UnirAdapter(1, unionEscuchada4);
		filtroDar.agregarSalida(unirAdapterDar4);
		filtroCelu.agregarSalida(unirAdapterCelu);

		FiltroPalabrasCompuestas filtroEscuchada4 = new FiltroPalabrasCompuestas();
		unionEscuchada4.agregarSalida(filtroEscuchada4);

		ReteRule reglaDarCelu = new ReteRule(32, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada4.agregarSalida(reglaDarCelu);
		listaReglas.add(reglaDarCelu);

		//palabra compuesta dar billetera
		FiltroIgualdad filtroBilletera = new FiltroIgualdad(0, "billetera");
		escuchada.agregarSalida(filtroBilletera);

		Unir unionEscuchada5 = new Unir(2);
		UnirAdapter unirAdapterDar5 = new UnirAdapter(0, unionEscuchada5);
		UnirAdapter unirAdapterBilletera = new UnirAdapter(1, unionEscuchada5);
		filtroDar.agregarSalida(unirAdapterDar5);
		filtroBilletera.agregarSalida(unirAdapterBilletera);

		FiltroPalabrasCompuestas filtroEscuchada5 = new FiltroPalabrasCompuestas();
		unionEscuchada5.agregarSalida(filtroEscuchada5);

		ReteRule reglaDarBilletera = new ReteRule(33, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada5.agregarSalida(reglaDarBilletera);
		listaReglas.add(reglaDarBilletera);

		//palabra compuesta dar cartera
		FiltroIgualdad filtroCartera = new FiltroIgualdad(0, "cartera");
		escuchada.agregarSalida(filtroCartera);

		Unir unionEscuchada6 = new Unir(2);
		UnirAdapter unirAdapterDar6 = new UnirAdapter(0, unionEscuchada6);
		UnirAdapter unirAdapterCartera = new UnirAdapter(1, unionEscuchada6);
		filtroDar.agregarSalida(unirAdapterDar6);
		filtroCartera.agregarSalida(unirAdapterCartera);

		FiltroPalabrasCompuestas filtroEscuchada6 = new FiltroPalabrasCompuestas();
		unionEscuchada6.agregarSalida(filtroEscuchada6);

		ReteRule reglaDarCartera = new ReteRule(34, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada6.agregarSalida(reglaDarCartera);
		listaReglas.add(reglaDarCartera);

		//palabra compuesta dar todo
		FiltroIgualdad filtroTodo = new FiltroIgualdad(0, "todo");
		escuchada.agregarSalida(filtroTodo);

		Unir unionEscuchada7 = new Unir(2);
		UnirAdapter unirAdapterDar7 = new UnirAdapter(0, unionEscuchada7);
		UnirAdapter unirAdapterTodo = new UnirAdapter(1, unionEscuchada7);
		filtroDar.agregarSalida(unirAdapterDar7);
		filtroTodo.agregarSalida(unirAdapterTodo);

		FiltroPalabrasCompuestas filtroEscuchada7 = new FiltroPalabrasCompuestas();
		unionEscuchada7.agregarSalida(filtroEscuchada7);

		ReteRule relgaDarTodo = new ReteRule(35, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada7.agregarSalida(relgaDarTodo);
		listaReglas.add(relgaDarTodo);

		//palabra compuesta dar mochila
		FiltroIgualdad filtroMochila = new FiltroIgualdad(0, "mochila");
		escuchada.agregarSalida(filtroMochila);

		Unir unionEscuchada8 = new Unir(2);
		UnirAdapter unirAdapterDar8 = new UnirAdapter(0, unionEscuchada8);
		UnirAdapter unirAdapterMochila = new UnirAdapter(1, unionEscuchada8);
		filtroDar.agregarSalida(unirAdapterDar8);
		filtroMochila.agregarSalida(unirAdapterMochila);

		FiltroPalabrasCompuestas filtroEscuchada8 = new FiltroPalabrasCompuestas();
		unionEscuchada8.agregarSalida(filtroEscuchada8);

		ReteRule reglaDarMochila = new ReteRule(36, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada8.agregarSalida(reglaDarMochila);
		listaReglas.add(reglaDarMochila);

		//palabra compuesta dar joyas
		FiltroIgualdad filtroJoyas = new FiltroIgualdad(0, "joyas");
		escuchada.agregarSalida(filtroJoyas);

		Unir unionEscuchada9 = new Unir(2);
		UnirAdapter unirAdapterDar9 = new UnirAdapter(0, unionEscuchada9);
		UnirAdapter unirAdapterJoyas = new UnirAdapter(1, unionEscuchada9);
		filtroDar.agregarSalida(unirAdapterDar9);
		filtroJoyas.agregarSalida(unirAdapterJoyas);

		FiltroPalabrasCompuestas filtroEscuchada9 = new FiltroPalabrasCompuestas();
		unionEscuchada9.agregarSalida(filtroEscuchada9);

		ReteRule reglaDarJoyas = new ReteRule(37, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada9.agregarSalida(reglaDarJoyas);
		listaReglas.add(reglaDarJoyas);

		//palabra compuesta vaciar caja
		FiltroIgualdad filtroVaciar = new FiltroIgualdad(0, "vaciar");
		escuchada.agregarSalida(filtroVaciar);

		FiltroIgualdad filtroCaja = new FiltroIgualdad(0, "caja");
		escuchada.agregarSalida(filtroCaja);

		Unir unionEscuchada10 = new Unir(2);
		UnirAdapter unirAdapterVaciar = new UnirAdapter(0, unionEscuchada10);
		UnirAdapter unirAdapterCaja = new UnirAdapter(1, unionEscuchada10);
		filtroVaciar.agregarSalida(unirAdapterVaciar);
		filtroCaja.agregarSalida(unirAdapterCaja);

		FiltroPalabrasCompuestas filtroEscuchada10 = new FiltroPalabrasCompuestas();
		unionEscuchada10.agregarSalida(filtroEscuchada10);

		ReteRule reglaVaciarCaja = new ReteRule(38, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada10.agregarSalida(reglaVaciarCaja);
		listaReglas.add(reglaVaciarCaja);

		//palabra compuesta poner todo bolsa
		FiltroIgualdad filtroPoner = new FiltroIgualdad(0, "poner");
		escuchada.agregarSalida(filtroPoner);

		FiltroIgualdad filtroBolsa = new FiltroIgualdad(0, "bolsa");
		escuchada.agregarSalida(filtroBolsa);

		Unir unionEscuchada11 = new Unir(3);
		UnirAdapter unirAdapterPoner = new UnirAdapter(0, unionEscuchada11);
		UnirAdapter unirAdapterTodo2 = new UnirAdapter(1, unionEscuchada11);
		UnirAdapter unirAdapterBolsa = new UnirAdapter(2, unionEscuchada11);
		filtroPoner.agregarSalida(unirAdapterPoner);
		filtroTodo.agregarSalida(unirAdapterTodo2);
		filtroBolsa.agregarSalida(unirAdapterBolsa);

		FiltroPalabrasCompuestasTriple filtroEscuchadaTriple = new FiltroPalabrasCompuestasTriple();
		unionEscuchada11.agregarSalida(filtroEscuchadaTriple);

		ReteRule reglaPonerTodoBolsa = new ReteRule(39, 4, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				String palabra3 = rm.getHecho(2).get(0).toString();
				String l = rm.getHecho(2).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "_" + palabra3 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra3 + "," + l + ")");
			}
		};

		filtroEscuchadaTriple.agregarSalida(reglaPonerTodoBolsa);
		listaReglas.add(reglaPonerTodoBolsa);

		//palabra compuesta esto ser asalto
		FiltroIgualdad filtroEsto = new FiltroIgualdad(0, "esto");
		escuchada.agregarSalida(filtroEsto);

		FiltroIgualdad filtroSer = new FiltroIgualdad(0, "ser");
		escuchada.agregarSalida(filtroSer);

		FiltroIgualdad filtroAsalto = new FiltroIgualdad(0, "asalto");
		escuchada.agregarSalida(filtroAsalto);

		Unir unionEscuchada12 = new Unir(3);
		UnirAdapter unirAdapterEso = new UnirAdapter(0, unionEscuchada12);
		UnirAdapter unirAdapterSer = new UnirAdapter(1, unionEscuchada12);
		UnirAdapter unirAdapterAsalto = new UnirAdapter(2, unionEscuchada12);
		filtroEsto.agregarSalida(unirAdapterEso);
		filtroSer.agregarSalida(unirAdapterSer);
		filtroAsalto.agregarSalida(unirAdapterAsalto);

		FiltroPalabrasCompuestasTriple filtroEscuchadaTriple2 = new FiltroPalabrasCompuestasTriple();
		unionEscuchada12.agregarSalida(filtroEscuchadaTriple2);

		ReteRule reglaEstoSerAsalto = new ReteRule(40, 4, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				String palabra3 = rm.getHecho(2).get(0).toString();
				String l = rm.getHecho(2).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "_" + palabra3 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra3 + "," + l + ")");
			}
		};

		filtroEscuchadaTriple2.agregarSalida(reglaEstoSerAsalto);
		listaReglas.add(reglaEstoSerAsalto);

		//palabra compuesta esto ser robo
		FiltroIgualdad filtroRobo = new FiltroIgualdad(0, "robo");
		escuchada.agregarSalida(filtroRobo);

		Unir unionEscuchada13 = new Unir(3);
		UnirAdapter unirAdapterEsto2 = new UnirAdapter(0, unionEscuchada13);
		UnirAdapter unirAdapterSer2 = new UnirAdapter(1, unionEscuchada13);
		UnirAdapter unirAdapterRobo = new UnirAdapter(2, unionEscuchada13);
		filtroEsto.agregarSalida(unirAdapterEsto2);
		filtroSer.agregarSalida(unirAdapterSer2);
		filtroRobo.agregarSalida(unirAdapterRobo);

		FiltroPalabrasCompuestasTriple filtroEscuchadaTriple3 = new FiltroPalabrasCompuestasTriple();
		unionEscuchada13.agregarSalida(filtroEscuchadaTriple3);

		ReteRule reglaEstoSerRobo = new ReteRule(41, 4, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				String palabra3 = rm.getHecho(2).get(0).toString();
				String l = rm.getHecho(2).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "_" + palabra3 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra3 + "," + l + ")");
			}
		};

		filtroEscuchadaTriple3.agregarSalida(reglaEstoSerRobo);
		listaReglas.add(reglaEstoSerRobo);

		//palabra compuesta no golpear
		FiltroIgualdad filtroNo = new FiltroIgualdad(0, "no");
		escuchada.agregarSalida(filtroNo);

		FiltroIgualdad filtroGolpear = new FiltroIgualdad(0, "golpear");
		escuchada.agregarSalida(filtroGolpear);

		Unir unionEscuchada14 = new Unir(2);
		UnirAdapter unirAdapterNo = new UnirAdapter(0, unionEscuchada14);
		UnirAdapter unirAdapterGolpear = new UnirAdapter(1, unionEscuchada14);
		filtroNo.agregarSalida(unirAdapterNo);
		filtroGolpear.agregarSalida(unirAdapterGolpear);

		FiltroPalabrasCompuestas filtroEscuchada11 = new FiltroPalabrasCompuestas();
		unionEscuchada14.agregarSalida(filtroEscuchada11);

		ReteRule reglaNoGolpear = new ReteRule(42, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada11.agregarSalida(reglaNoGolpear);
		listaReglas.add(reglaNoGolpear);

		//palabra compuesta no decir nadie
		FiltroIgualdad filtroDecir = new FiltroIgualdad(0, "decir");
		escuchada.agregarSalida(filtroDecir);

		FiltroIgualdad filtroNadie = new FiltroIgualdad(0, "nadie");
		escuchada.agregarSalida(filtroNadie);

		Unir unionEscuchada15 = new Unir(3);
		UnirAdapter unirAdapterNo2 = new UnirAdapter(0, unionEscuchada15);
		UnirAdapter unirAdapterDecir = new UnirAdapter(1, unionEscuchada15);
		UnirAdapter unirAdapterNadie = new UnirAdapter(2, unionEscuchada15);
		filtroNo.agregarSalida(unirAdapterNo2);
		filtroDecir.agregarSalida(unirAdapterDecir);
		filtroNadie.agregarSalida(unirAdapterNadie);

		FiltroPalabrasCompuestasTriple filtroEscuchadaTriple4 = new FiltroPalabrasCompuestasTriple();
		unionEscuchada15.agregarSalida(filtroEscuchadaTriple4);

		ReteRule reglaNoDecirNadie = new ReteRule(43, 4, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				String palabra3 = rm.getHecho(2).get(0).toString();
				String l = rm.getHecho(2).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "_" + palabra3 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra3 + "," + l + ")");
			}
		};

		filtroEscuchadaTriple4.agregarSalida(reglaNoDecirNadie);
		listaReglas.add(reglaNoDecirNadie);

		//palabra compuesta cerrar boca
		FiltroIgualdad filtroCerrar = new FiltroIgualdad(0, "cerrar");
		escuchada.agregarSalida(filtroCerrar);

		FiltroIgualdad filtroBoca = new FiltroIgualdad(0, "boca");
		escuchada.agregarSalida(filtroBoca);

		Unir unionEscuchada16 = new Unir(2);
		UnirAdapter unirAdapterCerrar = new UnirAdapter(0, unionEscuchada16);
		UnirAdapter unirAdapterBoca = new UnirAdapter(1, unionEscuchada16);
		filtroCerrar.agregarSalida(unirAdapterCerrar);
		filtroBoca.agregarSalida(unirAdapterBoca);

		FiltroPalabrasCompuestas filtroEscuchada12 = new FiltroPalabrasCompuestas();
		unionEscuchada16.agregarSalida(filtroEscuchada12);

		ReteRule reglaCerrarBoca = new ReteRule(44, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada12.agregarSalida(reglaCerrarBoca);
		listaReglas.add(reglaCerrarBoca);

		//palabra compuesta sacar manos encima
		FiltroIgualdad filtroSacar = new FiltroIgualdad(0, "sacar");
		escuchada.agregarSalida(filtroSacar);

		FiltroIgualdad filtroManos = new FiltroIgualdad(0, "manos");
		escuchada.agregarSalida(filtroManos);

		FiltroIgualdad filtroEncima = new FiltroIgualdad(0, "encima");
		escuchada.agregarSalida(filtroEncima);

		Unir unionEscuchada17 = new Unir(3);
		UnirAdapter unirAdapterSacar = new UnirAdapter(0, unionEscuchada17);
		UnirAdapter unirAdapterManos = new UnirAdapter(1, unionEscuchada17);
		UnirAdapter unirAdapterEncima = new UnirAdapter(2, unionEscuchada17);
		filtroSacar.agregarSalida(unirAdapterSacar);
		filtroManos.agregarSalida(unirAdapterManos);
		filtroEncima.agregarSalida(unirAdapterEncima);

		FiltroPalabrasCompuestasTriple filtroEscuchadaTriple5 = new FiltroPalabrasCompuestasTriple();
		unionEscuchada17.agregarSalida(filtroEscuchadaTriple5);

		ReteRule reglaSacarManosEncima = new ReteRule(45, 4, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				String palabra3 = rm.getHecho(2).get(0).toString();
				String l = rm.getHecho(2).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "_" + palabra3 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra3 + "," + l + ")");
			}
		};

		filtroEscuchadaTriple5.agregarSalida(reglaSacarManosEncima);
		listaReglas.add(reglaSacarManosEncima);

		//palabra compuesta venir aca
		FiltroIgualdad filtroVenir = new FiltroIgualdad(0, "venir");
		escuchada.agregarSalida(filtroVenir);

		FiltroIgualdad filtroAca = new FiltroIgualdad(0, "aca");
		escuchada.agregarSalida(filtroAca);

		Unir unionEscuchada18 = new Unir(2);
		UnirAdapter unirAdapterVenir = new UnirAdapter(0, unionEscuchada18);
		UnirAdapter unirAdapterAca = new UnirAdapter(1, unionEscuchada18);
		filtroVenir.agregarSalida(unirAdapterVenir);
		filtroAca.agregarSalida(unirAdapterAca);

		FiltroPalabrasCompuestas filtroEscuchada13 = new FiltroPalabrasCompuestas();
		unionEscuchada18.agregarSalida(filtroEscuchada13);

		ReteRule reglaVenirAca = new ReteRule(46, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada13.agregarSalida(reglaVenirAca);
		listaReglas.add(reglaVenirAca);

		//palabra compuesta llamar policia
		FiltroIgualdad filtroLlamar = new FiltroIgualdad(0, "llamar");
		escuchada.agregarSalida(filtroLlamar);

		FiltroIgualdad filtroPolicia = new FiltroIgualdad(0, "policia");
		escuchada.agregarSalida(filtroPolicia);

		Unir unionEscuchada19 = new Unir(2);
		UnirAdapter unirAdapterLlamar = new UnirAdapter(0, unionEscuchada19);
		UnirAdapter unirAdapterPolicia = new UnirAdapter(1, unionEscuchada19);
		filtroLlamar.agregarSalida(unirAdapterLlamar);
		filtroPolicia.agregarSalida(unirAdapterPolicia);

		FiltroPalabrasCompuestas filtroEscuchada14 = new FiltroPalabrasCompuestas();
		unionEscuchada19.agregarSalida(filtroEscuchada14);

		ReteRule reglaLlamarPolicia = new ReteRule(47, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada14.agregarSalida(reglaLlamarPolicia);
		listaReglas.add(reglaLlamarPolicia);

		//palabra compuesta llamar ambulancia
		FiltroIgualdad filtroAmbulancia = new FiltroIgualdad(0, "ambulancia");
		escuchada.agregarSalida(filtroAmbulancia);

		Unir unionEscuchada20 = new Unir(2);
		UnirAdapter unirAdapterLlamar2 = new UnirAdapter(0, unionEscuchada20);
		UnirAdapter unirAdapterAmbulancia = new UnirAdapter(1, unionEscuchada20);
		filtroLlamar.agregarSalida(unirAdapterLlamar2);
		filtroAmbulancia.agregarSalida(unirAdapterAmbulancia);

		FiltroPalabrasCompuestas filtroEscuchada15 = new FiltroPalabrasCompuestas();
		unionEscuchada20.agregarSalida(filtroEscuchada15);

		ReteRule reglaLlamarAmbulancia = new ReteRule(48, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada15.agregarSalida(reglaLlamarAmbulancia);
		listaReglas.add(reglaLlamarAmbulancia);

		//palabra compuesta llamar bomberos
		FiltroIgualdad filtroBomberos = new FiltroIgualdad(0, "bomberos");
		escuchada.agregarSalida(filtroBomberos);

		Unir unionEscuchada21 = new Unir(2);
		UnirAdapter unirAdapterLlamar3 = new UnirAdapter(0, unionEscuchada21);
		UnirAdapter unirAdapterBomberos = new UnirAdapter(1, unionEscuchada21);
		filtroLlamar.agregarSalida(unirAdapterLlamar3);
		filtroBomberos.agregarSalida(unirAdapterBomberos);

		FiltroPalabrasCompuestas filtroEscuchada16 = new FiltroPalabrasCompuestas();
		unionEscuchada21.agregarSalida(filtroEscuchada16);

		ReteRule reglaLlamarBomberos = new ReteRule(49, 3, 10) {

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String palabra1 = rm.getHecho(0).get(0).toString();
				String n = rm.getHecho(0).get(1).toString();
				String palabra2 = rm.getHecho(1).get(0).toString();
				String m = rm.getHecho(1).get(1).toString();
				estadoGuardian.addPredicate("escuchada(" + palabra1 + "_" + palabra2 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra1 + "," + n + ")");
				estadoGuardian.removePredicate("escuchada(" + palabra2 + "," + m + ")");
			}
		};

		filtroEscuchada16.agregarSalida(reglaLlamarBomberos);
		listaReglas.add(reglaLlamarBomberos);
	}

	private HashSet<String> cargarTodasLasPalabrasRelevantes() {

		Collection<Map<String, String>> resultado = this.getAgentState().query("tieneRiesgo(Incidente, Palabra, Valor)");
		HashSet<String> setPalabrasRelevantes = new HashSet<>();
		StringTokenizer fraseTokenizer;

		for(Map<String, String> mapa: resultado){

			//El valor asociado a la clave "Palabra" es una palabra o un simbolo con formato palabra_palabra_palabra
			fraseTokenizer = new StringTokenizer(mapa.get("Palabra"), "_");

			while(fraseTokenizer.hasMoreTokens()){
				setPalabrasRelevantes.add(fraseTokenizer.nextToken());
			}
		}

		return setPalabrasRelevantes;
	}
}
