package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import ar.edu.utn.frsf.isi.ia.Guardian.datos.BaseVerbos;
import ar.edu.utn.frsf.isi.ia.Guardian.datos.Sinonimos;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Filtro;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.FiltroMayorOIgual;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.FiltroPalabrasCompuestas;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.FiltroPalabrasCompuestasTriple;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Hecho;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteMatches;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteProductionMemory;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteRule;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Unificar;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Unir;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.UnirAdapter;
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
import frsf.cidisi.faia.solver.productionsystem.ProductionMemory;
import frsf.cidisi.faia.solver.productionsystem.Rule;
import frsf.cidisi.faia.solver.productionsystem.criterias.NoDuplication;
import frsf.cidisi.faia.solver.productionsystem.criterias.Priority;
import frsf.cidisi.faia.solver.productionsystem.criterias.Random;
import frsf.cidisi.faia.solver.productionsystem.criterias.Specificity;

public class Guardian extends ProductionSystemBasedAgent {

	private List<Criteria> criterios;
	private Integer proximoIndice = 0;
	HashSet<String> setPalabrasRelevantes;

	public Guardian() throws Exception {
		// The Agent State
		String ruta = new URI(BaseVerbos.class.getResource("/db/init.pl").toString()).getPath();
		EstadoGuardian agState = new EstadoGuardian(ruta);
		this.setAgentState(agState);

		//Crear reglas
		List<Rule> reglas = crearReglas();

		//Crear memoria de trabajo
		ProductionMemory productionMemory = new ReteProductionMemory(reglas);
		this.setProductionMemory(productionMemory);

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
		try {
			baseVerbos = new BaseVerbos();
		} catch (URISyntaxException e) {
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

	private List<Rule> crearReglas() {

		List<Rule> listaReglas = new ArrayList<>();

		//DELITO CALLEJERO
		//accion delito callejero llamar 911
		Accion accion = new Accion();
		Filtro filtroDelitoCallejeroAccion = new Filtro(0, "delitoCallejero");
		accion.agregarSalida(filtroDelitoCallejeroAccion);

		ReteRule reglaAccionDelitoCallejeroLlamar911 = new ReteRule(1,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando al 911");

			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoCallejeroLlamar911);
		listaReglas.add(reglaAccionDelitoCallejeroLlamar911);


		//accion delito callejero grabar lo que sucede
		ReteRule reglaAccionDelitoCallejeroGrabar = new ReteRule(2,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return  hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Grabando audio");

			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoCallejeroGrabar);
		listaReglas.add(reglaAccionDelitoCallejeroGrabar);

		//accion delito callejero llamar familiar
		ReteRule reglaAccionDelitoCallejeroLlamar = new ReteRule(3,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return  hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando a un familiar");

			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoCallejeroLlamar);
		listaReglas.add(reglaAccionDelitoCallejeroLlamar);

		//accion delito callejero - riesgo
		Riesgo riesgo = new Riesgo();
		Filtro filtroDelitoCallejeroRiesgo = new Filtro(0, "delitoCallejero");
		riesgo.agregarSalida(filtroDelitoCallejeroRiesgo);

		Unir unionAccionRiesgo1 = new Unir(2);
		UnirAdapter unirAdapterAccion1 = new UnirAdapter(0, unionAccionRiesgo1);
		UnirAdapter unirAdapterRiesgo1 = new UnirAdapter(1, unionAccionRiesgo1);
		filtroDelitoCallejeroAccion.agregarSalida(unirAdapterAccion1);
		filtroDelitoCallejeroRiesgo.agregarSalida(unirAdapterRiesgo1);

		ReteRule reglaAccionDelitoCallejeroRiesgo = new ReteRule(4,2,3) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> h2.get(0).equals(h.get(0)))
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return rm2;
						})
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(delitoCallejero,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(delitoCallejero,"+nivelViejo+")");
				estadoGuardian.removePredicate("accion(delitoCallejero)");
			}
		};

		unionAccionRiesgo1.agregarSalida(reglaAccionDelitoCallejeroRiesgo);
		listaReglas.add(reglaAccionDelitoCallejeroRiesgo);

		//DELITO HOGAR
		//accion delito hogar llamar 911
		Filtro filtroDelitoHogarAccion = new Filtro(0, "delitoHogar");
		accion.agregarSalida(filtroDelitoHogarAccion);

		ReteRule reglaAccionDelitoHogarLlamar911 = new ReteRule(5,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando al 911");

			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarLlamar911);
		listaReglas.add(reglaAccionDelitoHogarLlamar911);

		//accion delito hogar enviar audio al 911
		ReteRule reglaAccionDelitoHogarEnviarAudio = new ReteRule(6,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return  hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Grabando audio y enviándolo al 911");

			}
		};

		filtroDelitoCallejeroAccion.agregarSalida(reglaAccionDelitoHogarEnviarAudio);
		listaReglas.add(reglaAccionDelitoHogarEnviarAudio);

		//accion delito hogar activar camara de seguridad

		ReteRule reglaAccionDelitoHogarActivarCamara = new ReteRule(7,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Activando cámara de seguridad");

			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarActivarCamara);
		listaReglas.add(reglaAccionDelitoHogarActivarCamara);

		//accion delito hogar activar alarma vecinal

		ReteRule reglaAccionDelitoHogarActivarAlarma = new ReteRule(8,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Activando alarma vecinal");

			}
		};

		filtroDelitoHogarAccion.agregarSalida(reglaAccionDelitoHogarActivarAlarma);
		listaReglas.add(reglaAccionDelitoHogarActivarAlarma);

		//accion delito hogar - riesgo
		Filtro filtroDelitoHogarRiesgo = new Filtro(0, "delitoHogar");
		riesgo.agregarSalida(filtroDelitoHogarRiesgo);

		Unir unionAccionRiesgo2 = new Unir(2);
		UnirAdapter unirAdapterAccion2 = new UnirAdapter(0, unionAccionRiesgo2);
		UnirAdapter unirAdapterRiesgo2 = new UnirAdapter(1, unionAccionRiesgo2);
		filtroDelitoHogarAccion.agregarSalida(unirAdapterAccion2);
		filtroDelitoHogarRiesgo.agregarSalida(unirAdapterRiesgo2);

		ReteRule reglaAccionDelitoHogarRiesgo = new ReteRule(9,2,3) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> h2.get(0).equals(h.get(0)))
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return rm2;
						})
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(delitoHogar,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(delitoHogar,"+nivelViejo+")");
				estadoGuardian.removePredicate("accion(delitoHogar)");
			}
		};

		unionAccionRiesgo2.agregarSalida(reglaAccionDelitoHogarRiesgo);
		listaReglas.add(reglaAccionDelitoHogarRiesgo);

		//VIOLENCIA DOMESTICA
		//accion violencia domestica grabar audio
		Filtro filtroViolenciaDomesticaAccion = new Filtro(0, "violenciaDomestica");
		accion.agregarSalida(filtroViolenciaDomesticaAccion);

		ReteRule reglaAccionViolenciaDomesticaGrabarAudio = new ReteRule(10,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Grabando audio");

			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaAccionViolenciaDomesticaGrabarAudio);
		listaReglas.add(reglaAccionViolenciaDomesticaGrabarAudio);

		//accion violencia domestica llamar 911
		ReteRule reglaAccionViolenciaDomesticaLlamar911 = new ReteRule(11,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando al 911");

			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaAccionViolenciaDomesticaLlamar911);
		listaReglas.add(reglaAccionViolenciaDomesticaLlamar911);

		//accion violencia domestica enviar audio al 911
		ReteRule reglaViolenciaDomesticaEnviarAudio = new ReteRule(12,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Enviando audio al 911");
			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaViolenciaDomesticaEnviarAudio);
		listaReglas.add(reglaViolenciaDomesticaEnviarAudio);

		//accion violencia domestica llamar familiar

		ReteRule reglaViolenciaDomesticaLlamarFamiliar = new ReteRule(13,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando a un familiar");

			}
		};

		filtroViolenciaDomesticaAccion.agregarSalida(reglaViolenciaDomesticaLlamarFamiliar);
		listaReglas.add(reglaViolenciaDomesticaLlamarFamiliar);

		//accion violencia domestica - riesgo
		Filtro filtroViolenciaDomesticaRiesgo = new Filtro(0, "violenciaDomestica");
		riesgo.agregarSalida(filtroViolenciaDomesticaRiesgo);

		Unir unionAccionRiesgo3 = new Unir(2);
		UnirAdapter unirAdapterAccion3 = new UnirAdapter(0, unionAccionRiesgo3);
		UnirAdapter unirAdapterRiesgo3 = new UnirAdapter(1, unionAccionRiesgo3);
		filtroViolenciaDomesticaAccion.agregarSalida(unirAdapterAccion3);
		filtroViolenciaDomesticaRiesgo.agregarSalida(unirAdapterRiesgo3);

		ReteRule reglaAccionViolenciaDomesticaRiesgo = new ReteRule(14,2,3) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> h2.get(0).equals(h.get(0)))
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return rm2;
						})
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(violenciaDomestica,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(violenciaDomestica,"+nivelViejo+")");
				estadoGuardian.removePredicate("accion(violenciaDomestica)");
			}
		};

		unionAccionRiesgo3.agregarSalida(reglaAccionViolenciaDomesticaRiesgo);
		listaReglas.add(reglaAccionViolenciaDomesticaRiesgo);

		//INCENDIO
		//accion incendio llamar bomberos
		Filtro filtroIncendioAccion = new Filtro(0, "incendio");
		accion.agregarSalida(filtroIncendioAccion);

		ReteRule reglaAccionIncendioLlamar = new ReteRule(15,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando bomberos");

			}
		};

		filtroIncendioAccion.agregarSalida(reglaAccionIncendioLlamar);
		listaReglas.add(reglaAccionIncendioLlamar);

		//accion incendio enviar audio a bomberos
		ReteRule reglaAccionIncendioEnviarAudio = new ReteRule(16,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Enviando audio a bomberos");

			}
		};

		filtroIncendioAccion.agregarSalida(reglaAccionIncendioEnviarAudio);
		listaReglas.add(reglaAccionIncendioEnviarAudio);

		//accion incendio - riesgo
		Filtro filtroIncendioRiesgo = new Filtro(0, "incendio");
		riesgo.agregarSalida(filtroIncendioRiesgo);

		Unir unionAccionRiesgo4 = new Unir(2);
		UnirAdapter unirAdapterAccion4 = new UnirAdapter(0, unionAccionRiesgo4);
		UnirAdapter unirAdapterRiesgo4 = new UnirAdapter(1, unionAccionRiesgo4);
		filtroIncendioAccion.agregarSalida(unirAdapterAccion4);
		filtroIncendioRiesgo.agregarSalida(unirAdapterRiesgo4);

		ReteRule reglaAccionIncendioRiesgo = new ReteRule(17,2,3) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> h2.get(0).equals(h.get(0)))
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return rm2;
						})
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(incendio,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(incendio,"+nivelViejo+")");
				estadoGuardian.removePredicate("accion(incendio)");
			}
		};

		unionAccionRiesgo4.agregarSalida(reglaAccionIncendioRiesgo);
		listaReglas.add(reglaAccionIncendioRiesgo);

		//EMERGENCIA MEDICA
		//accion emergencia medica llamar hospital
		Filtro filtroEmergenciaMedicaAccion = new Filtro(0, "emergenciaMedica");
		accion.agregarSalida(filtroEmergenciaMedicaAccion);

		ReteRule reglaAccionEmergenciaMedicaLlamar = new ReteRule(18,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando a hospital");

			}
		};

		filtroEmergenciaMedicaAccion.agregarSalida(reglaAccionEmergenciaMedicaLlamar);
		listaReglas.add(reglaAccionEmergenciaMedicaLlamar);

		//accion emergencia medica enviar audio a hospital
		ReteRule reglaAccionEmergenciaMedicaEnviar = new ReteRule(19,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Enviando audio a hospital");

			}
		};

		filtroEmergenciaMedicaAccion.agregarSalida(reglaAccionEmergenciaMedicaEnviar);
		listaReglas.add(reglaAccionEmergenciaMedicaEnviar);

		//accion emergencia medica - riesgo
		Filtro filtroEmergenciaMedicaRiesgo = new Filtro(0, "emergenciaMedica");
		riesgo.agregarSalida(filtroEmergenciaMedicaRiesgo);

		Unir unionAccionRiesgo5 = new Unir(2);
		UnirAdapter unirAdapterAccion5 = new UnirAdapter(0, unionAccionRiesgo5);
		UnirAdapter unirAdapterRiesgo5 = new UnirAdapter(1, unionAccionRiesgo5);
		filtroEmergenciaMedicaAccion.agregarSalida(unirAdapterAccion5);
		filtroEmergenciaMedicaRiesgo.agregarSalida(unirAdapterRiesgo5);

		ReteRule reglaEmergenciaMedicaRiesgo = new ReteRule(20,2,3) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> h2.get(0).equals(h.get(0)))
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return rm2;
						})
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(emergenciaMedica,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(emergenciaMedica,"+nivelViejo+")");
				estadoGuardian.removePredicate("accion(emergenciaMedica)");
			}
		};

		unionAccionRiesgo5.agregarSalida(reglaEmergenciaMedicaRiesgo);
		listaReglas.add(reglaEmergenciaMedicaRiesgo);

		//EXPLOSION
		//accion explosion llamar policia
		Filtro filtroexplosionAccion = new Filtro(0, "explosion");
		accion.agregarSalida(filtroexplosionAccion);

		ReteRule reglaAccionExplosionLlamar = new ReteRule(21,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Llamando a policía");

			}
		};

		filtroexplosionAccion.agregarSalida(reglaAccionExplosionLlamar);
		listaReglas.add(reglaAccionExplosionLlamar);

		//accion explosion enviar audio a policia
		ReteRule reglaAccionExplosionEnviarAudio = new ReteRule(22,1,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				System.out.println("Enviando audio a policía");

			}
		};

		filtroexplosionAccion.agregarSalida(reglaAccionExplosionEnviarAudio);
		listaReglas.add(reglaAccionExplosionEnviarAudio);

		//accion explosion - riesgo
		Filtro filtroExplosionRiesgo = new Filtro(0, "explosion");
		riesgo.agregarSalida(filtroExplosionRiesgo);

		Unir unionAccionRiesgo6 = new Unir(2);
		UnirAdapter unirAdapterAccion6 = new UnirAdapter(0, unionAccionRiesgo6);
		UnirAdapter unirAdapterRiesgo6 = new UnirAdapter(1, unionAccionRiesgo6);
		filtroexplosionAccion.agregarSalida(unirAdapterAccion6);
		filtroExplosionRiesgo.agregarSalida(unirAdapterRiesgo6);

		ReteRule reglaAccionExplosionRiesgo = new ReteRule(23,2,3) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> h2.get(0).equals(h.get(0)))
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return rm2;
						})
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();
				estadoGuardian.addPredicate("riesgo(explosion,0)");
				String nivelViejo = rm.getHecho(1).get(1).toString();
				estadoGuardian.removePredicate("riesgo(explosion,"+nivelViejo+")");
				estadoGuardian.removePredicate("accion(explosion)");
			}
		};

		unionAccionRiesgo6.agregarSalida(reglaAccionExplosionRiesgo);
		listaReglas.add(reglaAccionExplosionRiesgo);

		//clasificada - tiene riesgo - riesgo
		Clasificada clasificada = new Clasificada();
		TieneRiesgo tieneRiesgo = new TieneRiesgo();

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

		ReteRule reglaClasificadaTieneriesgoRiesgo = new ReteRule(24,3,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> h2.get(0).equals(h.get(0)))
						.filter(h2 -> h2.get(1).equals(h.get(1)))
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return hechos.get(2).stream()
									.filter(h3 -> h3.get(0).equals(h2.get(0)))
									.map(h3 -> {
										ReteMatches rm3 = rm2.clone();
										rm3.addHecho(h3);
										return rm3;
									}).collect(Collectors.toList());
						}).flatMap(List::stream)
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				Integer nivelViejo = new Integer(rm.getHecho(2).get(1).toString());
				Integer valor = new Integer(rm.getHecho(1).get(2).toString());
				String incidente = rm.getHecho(0).get(0).toString();
				String numero = ((Integer)(nivelViejo+valor)).toString();
				estadoGuardian.addPredicate("riesgo("+incidente+","+numero+")");
				estadoGuardian.removePredicate("riesgo("+incidente+","+nivelViejo+")");
				String palabra = rm.getHecho(0).get(1).toString();
				estadoGuardian.removePredicate("clasificada("+incidente+","+palabra+")");
			}
		};

		unificar3.agregarSalida(reglaClasificadaTieneriesgoRiesgo);
		listaReglas.add(reglaClasificadaTieneriesgoRiesgo);

		//limite riesgo - riesgo - sospecho
		LimiteRiesgo limiteRiesgo = new LimiteRiesgo();
		Sospecho sospecho = new Sospecho();

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

		Filtro filtroMayorOIgual = new FiltroMayorOIgual();
		unificar5.agregarSalida(filtroMayorOIgual);

		ReteRule reglaLimiteriesgoRiesgoSospecho = new ReteRule(25,4,5) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> h2.get(0).equals(h.get(0)))
						.filter(h2 -> {
							Integer nivel = new Integer(h2.get(1).toString());
							Integer limite = new Integer(h.get(1).toString());
							return nivel>=limite;
						})
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return hechos.get(2).stream()
									.filter(h3 -> h3.get(0).equals(h2.get(0)))
									.map(h3 -> {
										ReteMatches rm3 = rm2.clone();
										rm3.addHecho(h3);
										return rm3;
									}).collect(Collectors.toList());
						}).flatMap(List::stream)
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				ReteMatches rm = (ReteMatches) unificaciones;
				EstadoGuardian estadoGuardian = Guardian.this.getAgentState();

				String incidente = rm.getHecho(0).get(0).toString();
				estadoGuardian.addPredicate("accion("+incidente+")");
				estadoGuardian.addPredicate("noSospecho("+incidente+")");
				estadoGuardian.removePredicate("sospecho("+incidente+")");
			}
		};

		filtroMayorOIgual.agregarSalida(reglaLimiteriesgoRiesgoSospecho);
		listaReglas.add(reglaLimiteriesgoRiesgoSospecho);

		//escuchada - critica - no sospecho
		Escuchada escuchada = new Escuchada();
		Critica critica = new Critica();
		NoSospecho noSospecho = new NoSospecho();

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

		ReteRule reglaEscuchadaCriticaNosospecho = new ReteRule(26,3,3) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> h2.get(1).equals(h.get(0)))
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return hechos.get(2).stream()
									.filter(h3 -> h3.get(0).equals(h2.get(0)))
									.map(h3 -> {
										ReteMatches rm3 = rm2.clone();
										rm3.addHecho(h3);
										return rm3;
									}).collect(Collectors.toList());
						}).flatMap(List::stream)
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

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

		ReteRule reglaEscuchadaTieneriesgo = new ReteRule(27,2,2) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> h2.get(1).equals(h.get(0)))
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		unificar8.agregarSalida(reglaEscuchadaTieneriesgo);
		listaReglas.add(reglaEscuchadaTieneriesgo);

		//escuchada
		ReteRule reglaEscuchada = new ReteRule(28,1,1) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return rm;
				}).collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		escuchada.agregarSalida(reglaEscuchada);
		listaReglas.add(reglaEscuchada);

		//palabra compuesta dar plata
		Filtro filtroDar = new Filtro(0, "dar");
		escuchada.agregarSalida(filtroDar);

		Filtro filtroPlata = new Filtro(0, "plata");
		escuchada.agregarSalida(filtroPlata);

		Unir unionEscuchada1 = new Unir(2);
		UnirAdapter unirAdapterDar1 = new UnirAdapter(0, unionEscuchada1);
		UnirAdapter unirAdapterPlata = new UnirAdapter(1, unionEscuchada1);
		filtroDar.agregarSalida(unirAdapterDar1);
		filtroPlata.agregarSalida(unirAdapterPlata);

		Filtro filtroEscuchada = new FiltroPalabrasCompuestas();
		unionEscuchada1.agregarSalida(filtroEscuchada);

		ReteRule reglaDarPlata = new ReteRule(29, 3, 10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada.agregarSalida(reglaDarPlata);
		listaReglas.add(reglaDarPlata);

		//palabra compuesta dar bici
		Filtro filtroBici = new Filtro(0, "bici");
		escuchada.agregarSalida(filtroBici);

		Unir unionEscuchada2 = new Unir(2);
		UnirAdapter unirAdapterDar2 = new UnirAdapter(0, unionEscuchada2);
		UnirAdapter unirAdapterBici = new UnirAdapter(1, unionEscuchada2);
		filtroDar.agregarSalida(unirAdapterDar2);
		filtroBici.agregarSalida(unirAdapterBici);

		Filtro filtroEscuchada2 = new FiltroPalabrasCompuestas();
		unionEscuchada2.agregarSalida(filtroEscuchada2);

		ReteRule reglaDarBici = new ReteRule(30,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada2.agregarSalida(reglaDarBici);
		listaReglas.add(reglaDarBici);

		//palabra compuesta dar moto
		Filtro filtroMoto = new Filtro(0, "moto");
		escuchada.agregarSalida(filtroMoto);

		Unir unionEscuchada3 = new Unir(2);
		UnirAdapter unirAdapterDar3 = new UnirAdapter(0, unionEscuchada3);
		UnirAdapter unirAdapterMoto = new UnirAdapter(1, unionEscuchada3);
		filtroDar.agregarSalida(unirAdapterDar3);
		filtroMoto.agregarSalida(unirAdapterMoto);

		Filtro filtroEscuchada3 = new FiltroPalabrasCompuestas();
		unionEscuchada3.agregarSalida(filtroEscuchada3);

		ReteRule reglaDarMoto = new ReteRule(31,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada3.agregarSalida(reglaDarMoto);
		listaReglas.add(reglaDarMoto);

		//palabra compuesta dar celu
		Filtro filtroCelu = new Filtro(0, "celu");
		escuchada.agregarSalida(filtroCelu);

		Unir unionEscuchada4 = new Unir(2);
		UnirAdapter unirAdapterDar4 = new UnirAdapter(0, unionEscuchada4);
		UnirAdapter unirAdapterCelu = new UnirAdapter(1, unionEscuchada4);
		filtroDar.agregarSalida(unirAdapterDar4);
		filtroCelu.agregarSalida(unirAdapterCelu);

		Filtro filtroEscuchada4 = new FiltroPalabrasCompuestas();
		unionEscuchada4.agregarSalida(filtroEscuchada4);

		ReteRule reglaDarCelu = new ReteRule(32,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada4.agregarSalida(reglaDarCelu);
		listaReglas.add(reglaDarCelu);

		//palabra compuesta dar billetera
		Filtro filtroBilletera = new Filtro(0, "billetera");
		escuchada.agregarSalida(filtroBilletera);

		Unir unionEscuchada5 = new Unir(2);
		UnirAdapter unirAdapterDar5 = new UnirAdapter(0, unionEscuchada5);
		UnirAdapter unirAdapterBilletera = new UnirAdapter(1, unionEscuchada5);
		filtroDar.agregarSalida(unirAdapterDar5);
		filtroBilletera.agregarSalida(unirAdapterBilletera);

		Filtro filtroEscuchada5 = new FiltroPalabrasCompuestas();
		unionEscuchada5.agregarSalida(filtroEscuchada5);

		ReteRule reglaDarBilletera = new ReteRule(33,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada5.agregarSalida(reglaDarBilletera);
		listaReglas.add(reglaDarBilletera);

		//palabra compuesta dar cartera
		Filtro filtroCartera = new Filtro(0, "cartera");
		escuchada.agregarSalida(filtroCartera);

		Unir unionEscuchada6 = new Unir(2);
		UnirAdapter unirAdapterDar6 = new UnirAdapter(0, unionEscuchada6);
		UnirAdapter unirAdapterCartera = new UnirAdapter(1, unionEscuchada6);
		filtroDar.agregarSalida(unirAdapterDar6);
		filtroCartera.agregarSalida(unirAdapterCartera);

		Filtro filtroEscuchada6 = new FiltroPalabrasCompuestas();
		unionEscuchada6.agregarSalida(filtroEscuchada6);

		ReteRule reglaDarCartera = new ReteRule(34,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada6.agregarSalida(reglaDarCartera);
		listaReglas.add(reglaDarCartera);

		//palabra compuesta dar todo
		Filtro filtroTodo = new Filtro(0, "todo");
		escuchada.agregarSalida(filtroTodo);

		Unir unionEscuchada7 = new Unir(2);
		UnirAdapter unirAdapterDar7 = new UnirAdapter(0, unionEscuchada7);
		UnirAdapter unirAdapterTodo = new UnirAdapter(1, unionEscuchada7);
		filtroDar.agregarSalida(unirAdapterDar7);
		filtroTodo.agregarSalida(unirAdapterTodo);

		Filtro filtroEscuchada7 = new FiltroPalabrasCompuestas();
		unionEscuchada7.agregarSalida(filtroEscuchada7);

		ReteRule relgaDarTodo = new ReteRule(35,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada7.agregarSalida(relgaDarTodo);
		listaReglas.add(relgaDarTodo);

		//palabra compuesta dar mochila
		Filtro filtroMochila = new Filtro(0, "mochila");
		escuchada.agregarSalida(filtroMochila);

		Unir unionEscuchada8 = new Unir(2);
		UnirAdapter unirAdapterDar8 = new UnirAdapter(0, unionEscuchada8);
		UnirAdapter unirAdapterMochila = new UnirAdapter(1, unionEscuchada8);
		filtroDar.agregarSalida(unirAdapterDar8);
		filtroMochila.agregarSalida(unirAdapterMochila);

		Filtro filtroEscuchada8 = new FiltroPalabrasCompuestas();
		unionEscuchada8.agregarSalida(filtroEscuchada8);

		ReteRule reglaDarMochila = new ReteRule(36,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada8.agregarSalida(reglaDarMochila);
		listaReglas.add(reglaDarMochila);

		//palabra compuesta dar joyas
		Filtro filtroJoyas = new Filtro(0, "joyas");
		escuchada.agregarSalida(filtroJoyas);

		Unir unionEscuchada9 = new Unir(2);
		UnirAdapter unirAdapterDar9 = new UnirAdapter(0, unionEscuchada9);
		UnirAdapter unirAdapterJoyas = new UnirAdapter(1, unionEscuchada9);
		filtroDar.agregarSalida(unirAdapterDar9);
		filtroJoyas.agregarSalida(unirAdapterJoyas);

		Filtro filtroEscuchada9 = new FiltroPalabrasCompuestas();
		unionEscuchada9.agregarSalida(filtroEscuchada9);

		ReteRule reglaDarJoyas = new ReteRule(37,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada9.agregarSalida(reglaDarJoyas);
		listaReglas.add(reglaDarJoyas);

		//palabra compuesta vaciar caja
		Filtro filtroVaciar = new Filtro(0, "vaciar");
		escuchada.agregarSalida(filtroVaciar);

		Filtro filtroCaja = new Filtro(0, "caja");
		escuchada.agregarSalida(filtroCaja);

		Unir unionEscuchada10 = new Unir(2);
		UnirAdapter unirAdapterVaciar = new UnirAdapter(0, unionEscuchada10);
		UnirAdapter unirAdapterCaja = new UnirAdapter(1, unionEscuchada10);
		filtroVaciar.agregarSalida(unirAdapterVaciar);
		filtroCaja.agregarSalida(unirAdapterCaja);

		Filtro filtroEscuchada10 = new FiltroPalabrasCompuestas();
		unionEscuchada10.agregarSalida(filtroEscuchada10);

		ReteRule reglaVaciarCaja = new ReteRule(38,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada10.agregarSalida(reglaVaciarCaja);
		listaReglas.add(reglaVaciarCaja);

		//palabra compuesta poner todo bolsa
		Filtro filtroPoner = new Filtro(0, "poner");
		escuchada.agregarSalida(filtroPoner);

		Filtro filtroBolsa = new Filtro(0, "bolsa");
		escuchada.agregarSalida(filtroBolsa);

		Unir unionEscuchada11 = new Unir(3);
		UnirAdapter unirAdapterPoner = new UnirAdapter(0, unionEscuchada11);
		UnirAdapter unirAdapterTodo2 = new UnirAdapter(1, unionEscuchada11);
		UnirAdapter unirAdapterBolsa = new UnirAdapter(2, unionEscuchada11);
		filtroPoner.agregarSalida(unirAdapterPoner);
		filtroTodo.agregarSalida(unirAdapterTodo2);
		filtroBolsa.agregarSalida(unirAdapterBolsa);

		Filtro filtroEscuchadaTriple = new FiltroPalabrasCompuestasTriple();
		unionEscuchada11.agregarSalida(filtroEscuchadaTriple);

		ReteRule reglaPonerTodoBolsa = new ReteRule(39,4,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> {
							Integer n = new Integer(h.get(1).toString());
							Integer m = new Integer(h2.get(1).toString());
							return m==n+1;
						})
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return hechos.get(2).stream()
									.filter(h3 -> {
										Integer m = new Integer(h2.get(1).toString());
										Integer l = new Integer(h3.get(1).toString());
										return l==m+1;
									})
									.map(h3 -> {
										ReteMatches rm3 = rm2.clone();
										rm3.addHecho(h3);
										return rm3;
									}).collect(Collectors.toList());
						}).flatMap(List::stream)
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchadaTriple.agregarSalida(reglaPonerTodoBolsa);
		listaReglas.add(reglaPonerTodoBolsa);

		//palabra compuesta esto ser asalto
		Filtro filtroEsto = new Filtro(0, "esto");
		escuchada.agregarSalida(filtroEsto);

		Filtro filtroSer = new Filtro(0, "ser");
		escuchada.agregarSalida(filtroSer);

		Filtro filtroAsalto = new Filtro(0, "asalto");
		escuchada.agregarSalida(filtroAsalto);

		Unir unionEscuchada12 = new Unir(3);
		UnirAdapter unirAdapterEso = new UnirAdapter(0, unionEscuchada12);
		UnirAdapter unirAdapterSer = new UnirAdapter(1, unionEscuchada12);
		UnirAdapter unirAdapterAsalto = new UnirAdapter(2, unionEscuchada12);
		filtroEsto.agregarSalida(unirAdapterEso);
		filtroSer.agregarSalida(unirAdapterSer);
		filtroAsalto.agregarSalida(unirAdapterAsalto);

		Filtro filtroEscuchadaTriple2 = new FiltroPalabrasCompuestasTriple();
		unionEscuchada12.agregarSalida(filtroEscuchadaTriple2);

		ReteRule reglaEstoSerAsalto = new ReteRule(40,4,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> {
							Integer n = new Integer(h.get(1).toString());
							Integer m = new Integer(h2.get(1).toString());
							return m==n+1;
						})
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return hechos.get(2).stream()
									.filter(h3 -> {
										Integer m = new Integer(h2.get(1).toString());
										Integer l = new Integer(h3.get(1).toString());
										return l==m+1;
									})
									.map(h3 -> {
										ReteMatches rm3 = rm2.clone();
										rm3.addHecho(h3);
										return rm3;
									}).collect(Collectors.toList());
						}).flatMap(List::stream)
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchadaTriple2.agregarSalida(reglaEstoSerAsalto);
		listaReglas.add(reglaEstoSerAsalto);

		//palabra compuesta esto ser robo
		Filtro filtroRobo = new Filtro(0, "robo");
		escuchada.agregarSalida(filtroRobo);

		Unir unionEscuchada13 = new Unir(3);
		UnirAdapter unirAdapterEsto2 = new UnirAdapter(0, unionEscuchada13);
		UnirAdapter unirAdapterSer2 = new UnirAdapter(1, unionEscuchada13);
		UnirAdapter unirAdapterRobo = new UnirAdapter(2, unionEscuchada13);
		filtroEsto.agregarSalida(unirAdapterEsto2);
		filtroSer.agregarSalida(unirAdapterSer2);
		filtroRobo.agregarSalida(unirAdapterRobo);

		Filtro filtroEscuchadaTriple3 = new FiltroPalabrasCompuestasTriple();
		unionEscuchada13.agregarSalida(filtroEscuchadaTriple3);

		ReteRule reglaEstoSerRobo = new ReteRule(41,4,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> {
							Integer n = new Integer(h.get(1).toString());
							Integer m = new Integer(h2.get(1).toString());
							return m==n+1;
						})
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return hechos.get(2).stream()
									.filter(h3 -> {
										Integer m = new Integer(h2.get(1).toString());
										Integer l = new Integer(h3.get(1).toString());
										return l==m+1;
									})
									.map(h3 -> {
										ReteMatches rm3 = rm2.clone();
										rm3.addHecho(h3);
										return rm3;
									}).collect(Collectors.toList());
						}).flatMap(List::stream)
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchadaTriple3.agregarSalida(reglaEstoSerRobo);
		listaReglas.add(reglaEstoSerRobo);

		//palabra compuesta no golpear
		Filtro filtroNo = new Filtro(0, "no");
		escuchada.agregarSalida(filtroNo);

		Filtro filtroGolpear = new Filtro(0, "golpear");
		escuchada.agregarSalida(filtroGolpear);

		Unir unionEscuchada14 = new Unir(2);
		UnirAdapter unirAdapterNo = new UnirAdapter(0, unionEscuchada14);
		UnirAdapter unirAdapterGolpear = new UnirAdapter(1, unionEscuchada14);
		filtroNo.agregarSalida(unirAdapterNo);
		filtroGolpear.agregarSalida(unirAdapterGolpear);

		Filtro filtroEscuchada11 = new FiltroPalabrasCompuestas();
		unionEscuchada14.agregarSalida(filtroEscuchada11);

		ReteRule reglaNoGolpear = new ReteRule(42,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada11.agregarSalida(reglaNoGolpear);
		listaReglas.add(reglaNoGolpear);

		//palabra compuesta no decir nadie
		Filtro filtroDecir = new Filtro(0, "decir");
		escuchada.agregarSalida(filtroDecir);

		Filtro filtroNadie = new Filtro(0, "nadie");
		escuchada.agregarSalida(filtroNadie);

		Unir unionEscuchada15 = new Unir(3);
		UnirAdapter unirAdapterNo2 = new UnirAdapter(0, unionEscuchada15);
		UnirAdapter unirAdapterDecir = new UnirAdapter(1, unionEscuchada15);
		UnirAdapter unirAdapterNadie = new UnirAdapter(2, unionEscuchada15);
		filtroNo.agregarSalida(unirAdapterNo2);
		filtroDecir.agregarSalida(unirAdapterDecir);
		filtroNadie.agregarSalida(unirAdapterNadie);

		Filtro filtroEscuchadaTriple4 = new FiltroPalabrasCompuestasTriple();
		unionEscuchada15.agregarSalida(filtroEscuchadaTriple4);

		ReteRule reglaNoDecirNadie = new ReteRule(43,4,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> {
							Integer n = new Integer(h.get(1).toString());
							Integer m = new Integer(h2.get(1).toString());
							return m==n+1;
						})
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return hechos.get(2).stream()
									.filter(h3 -> {
										Integer m = new Integer(h2.get(1).toString());
										Integer l = new Integer(h3.get(1).toString());
										return l==m+1;
									})
									.map(h3 -> {
										ReteMatches rm3 = rm2.clone();
										rm3.addHecho(h3);
										return rm3;
									}).collect(Collectors.toList());
						}).flatMap(List::stream)
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchadaTriple4.agregarSalida(reglaNoDecirNadie);
		listaReglas.add(reglaNoDecirNadie);

		//palabra compuesta cerrar boca
		Filtro filtroCerrar = new Filtro(0, "cerrar");
		escuchada.agregarSalida(filtroCerrar);

		Filtro filtroBoca = new Filtro(0, "boca");
		escuchada.agregarSalida(filtroBoca);

		Unir unionEscuchada16 = new Unir(2);
		UnirAdapter unirAdapterCerrar = new UnirAdapter(0, unionEscuchada16);
		UnirAdapter unirAdapterBoca = new UnirAdapter(1, unionEscuchada16);
		filtroCerrar.agregarSalida(unirAdapterCerrar);
		filtroBoca.agregarSalida(unirAdapterBoca);

		Filtro filtroEscuchada12 = new FiltroPalabrasCompuestas();
		unionEscuchada16.agregarSalida(filtroEscuchada12);

		ReteRule reglaCerrarBoca = new ReteRule(44,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada12.agregarSalida(reglaCerrarBoca);
		listaReglas.add(reglaCerrarBoca);

		//palabra compuesta sacar manos encima
		Filtro filtroSacar = new Filtro(0, "sacar");
		escuchada.agregarSalida(filtroSacar);

		Filtro filtroManos = new Filtro(0, "manos");
		escuchada.agregarSalida(filtroManos);

		Filtro filtroEncima = new Filtro(0, "encima");
		escuchada.agregarSalida(filtroEncima);

		Unir unionEscuchada17 = new Unir(3);
		UnirAdapter unirAdapterSacar = new UnirAdapter(0, unionEscuchada17);
		UnirAdapter unirAdapterManos = new UnirAdapter(1, unionEscuchada17);
		UnirAdapter unirAdapterEncima = new UnirAdapter(2, unionEscuchada17);
		filtroSacar.agregarSalida(unirAdapterSacar);
		filtroManos.agregarSalida(unirAdapterManos);
		filtroEncima.agregarSalida(unirAdapterEncima);

		Filtro filtroEscuchadaTriple5 = new FiltroPalabrasCompuestasTriple();
		unionEscuchada17.agregarSalida(filtroEscuchadaTriple5);

		ReteRule reglaSacarManosEncima = new ReteRule(45,4,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
						.filter(h2 -> {
							Integer n = new Integer(h.get(1).toString());
							Integer m = new Integer(h2.get(1).toString());
							return m==n+1;
						})
						.map(h2 -> {
							ReteMatches rm2 = rm.clone();
							rm2.addHecho(h2);
							return hechos.get(2).stream()
									.filter(h3 -> {
										Integer m = new Integer(h2.get(1).toString());
										Integer l = new Integer(h3.get(1).toString());
										return l==m+1;
									})
									.map(h3 -> {
										ReteMatches rm3 = rm2.clone();
										rm3.addHecho(h3);
										return rm3;
									}).collect(Collectors.toList());
						}).flatMap(List::stream)
						.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchadaTriple5.agregarSalida(reglaSacarManosEncima);
		listaReglas.add(reglaSacarManosEncima);

		//palabra compuesta venir aca
		Filtro filtroVenir = new Filtro(0, "venir");
		escuchada.agregarSalida(filtroVenir);

		Filtro filtroAca = new Filtro(0, "aca");
		escuchada.agregarSalida(filtroAca);

		Unir unionEscuchada18 = new Unir(2);
		UnirAdapter unirAdapterVenir = new UnirAdapter(0, unionEscuchada18);
		UnirAdapter unirAdapterAca = new UnirAdapter(1, unionEscuchada18);
		filtroVenir.agregarSalida(unirAdapterVenir);
		filtroAca.agregarSalida(unirAdapterAca);

		Filtro filtroEscuchada13 = new FiltroPalabrasCompuestas();
		unionEscuchada18.agregarSalida(filtroEscuchada13);

		ReteRule reglaVenirAca = new ReteRule(46,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada13.agregarSalida(reglaVenirAca);
		listaReglas.add(reglaVenirAca);

		//palabra compuesta llamar policia
		Filtro filtroLlamar = new Filtro(0, "llamar");
		escuchada.agregarSalida(filtroLlamar);

		Filtro filtroPolicia = new Filtro(0, "policia");
		escuchada.agregarSalida(filtroPolicia);

		Unir unionEscuchada19 = new Unir(2);
		UnirAdapter unirAdapterLlamar = new UnirAdapter(0, unionEscuchada19);
		UnirAdapter unirAdapterPolicia = new UnirAdapter(1, unionEscuchada19);
		filtroLlamar.agregarSalida(unirAdapterLlamar);
		filtroPolicia.agregarSalida(unirAdapterPolicia);

		Filtro filtroEscuchada14 = new FiltroPalabrasCompuestas();
		unionEscuchada19.agregarSalida(filtroEscuchada14);

		ReteRule reglaLlamarPolicia = new ReteRule(47,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada14.agregarSalida(reglaLlamarPolicia);
		listaReglas.add(reglaLlamarPolicia);

		//palabra compuesta llamar ambulancia
		Filtro filtroAmbulancia = new Filtro(0, "ambulancia");
		escuchada.agregarSalida(filtroAmbulancia);

		Unir unionEscuchada20 = new Unir(2);
		UnirAdapter unirAdapterLlamar2 = new UnirAdapter(0, unionEscuchada20);
		UnirAdapter unirAdapterAmbulancia = new UnirAdapter(1, unionEscuchada20);
		filtroLlamar.agregarSalida(unirAdapterLlamar2);
		filtroAmbulancia.agregarSalida(unirAdapterAmbulancia);

		Filtro filtroEscuchada15 = new FiltroPalabrasCompuestas();
		unionEscuchada20.agregarSalida(filtroEscuchada15);

		ReteRule reglaLlamarAmbulancia = new ReteRule(48,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada15.agregarSalida(reglaLlamarAmbulancia);
		listaReglas.add(reglaLlamarAmbulancia);

		//palabra compuesta llamar bomberos
		Filtro filtroBomberos = new Filtro(0, "bomberos");
		escuchada.agregarSalida(filtroBomberos);

		Unir unionEscuchada21 = new Unir(2);
		UnirAdapter unirAdapterLlamar3 = new UnirAdapter(0, unionEscuchada21);
		UnirAdapter unirAdapterBomberos = new UnirAdapter(1, unionEscuchada21);
		filtroLlamar.agregarSalida(unirAdapterLlamar3);
		filtroBomberos.agregarSalida(unirAdapterBomberos);

		Filtro filtroEscuchada16 = new FiltroPalabrasCompuestas();
		unionEscuchada21.agregarSalida(filtroEscuchada16);

		ReteRule reglaLlamarBomberos = new ReteRule(49,3,10) {

			@Override
			public List<Matches> generarMatches(List<List<Hecho>> hechos) {
				return hechos.get(0).stream().map(h -> {
					ReteMatches rm = new ReteMatches();
					rm.addHecho(h);
					return hechos.get(1).stream()
							.filter(h2 -> {
								Integer n = new Integer(h.get(1).toString());
								Integer m = new Integer(h2.get(1).toString());
								return m==n+1;
							})
							.map(h2 -> {
								ReteMatches rm2 = rm.clone();
								rm2.addHecho(h2);
								return rm2;
							})
							.collect(Collectors.toList());
				}).flatMap(List::stream)
				.collect(Collectors.toList());
			}

			@Override
			public void execute(Matches unificaciones) {
				// TODO Auto-generated method stub

			}
		};

		filtroEscuchada16.agregarSalida(reglaLlamarBomberos);
		listaReglas.add(reglaLlamarBomberos);

		return listaReglas;
	}

	private HashSet<String> cargarTodasLasPalabrasRelevantes(){

		Collection<Map<String,String>> resultado = this.getAgentState().query("tieneRiesgo(Incidente, Palabra, Valor)");
		HashSet<String> setPalabrasRelevantes = new HashSet<>();
		StringTokenizer fraseTokenizer;

		for(Map<String,String> mapa: resultado){

			//El valor asociado a la clave "Palabra" es una palabra o un simbolo con formato palabra_palabra_palabra
			fraseTokenizer = new StringTokenizer(mapa.get("Palabra"), "_");

			while(fraseTokenizer.hasMoreTokens()){
				setPalabrasRelevantes.add(fraseTokenizer.nextToken());
			}
		}

		return setPalabrasRelevantes;
	}
}
