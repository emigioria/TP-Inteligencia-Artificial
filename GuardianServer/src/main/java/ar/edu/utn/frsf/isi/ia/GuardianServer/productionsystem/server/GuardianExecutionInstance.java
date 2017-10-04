package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.AmbienteCiudad;
import ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.EstadoAmbienteServer;
import ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.GuardianServer;
import frsf.cidisi.faia.simulator.ProductionSystemBasedAgentSimulator;

public class GuardianExecutionInstance {

	private String id;
	private SendMessageListener sml;
	private LinkedBlockingDeque<String> frasesEscuchadas;
	private Thread thread;

	private Boolean iniciado = false;
	private Semaphore semEstado = new Semaphore(1);
	private ScheduledThreadPoolExecutor scheduledPool;
	private Runnable apagar = () -> this.finalizar();

	private Runnable crearGuardIAn = () -> {
		GuardianServer agenteGuardian;
		try{
			agenteGuardian = new GuardianServer(id) {
				@Override
				public void enviarAccion(String message) {
					sml.enviarMensaje(new Mensaje(id, message));
				}
			};
		} catch(Exception e1){
			e1.printStackTrace();
			return;
		}

		EstadoAmbienteServer estadoAmbienteServer = new EstadoAmbienteServer(frasesEscuchadas);
		AmbienteCiudad ambienteCiudad = new AmbienteCiudad(estadoAmbienteServer);
		ProductionSystemBasedAgentSimulator simulator = new ProductionSystemBasedAgentSimulator(ambienteCiudad, agenteGuardian);

		try{
			File archivoSalida = new File("SalidaSimulacion" + id + ".txt");
			if(archivoSalida.exists()){
				archivoSalida.delete();
			}
			archivoSalida.createNewFile();
			System.setOut(new PrintStream(new FileOutputStream(archivoSalida)));
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("No se guardará la ejecución");
		}

		simulator.start();
	};

	public GuardianExecutionInstance(String id, SendMessageListener sml) {
		this.id = id;
		this.sml = sml;
		this.scheduledPool = new ScheduledThreadPoolExecutor(1);
	}

	public void iniciar() {
		try{
			semEstado.acquire();
		} catch(InterruptedException e){
			e.printStackTrace();
			return;
		}
		if(!iniciado){
			iniciarInseguro();
		}

		scheduledPool.getQueue().clear();
		scheduledPool.schedule(apagar, 30, TimeUnit.MINUTES);

		semEstado.release();
	}

	private void iniciarInseguro() {
		frasesEscuchadas = new LinkedBlockingDeque<>();
		iniciado = true;
		thread = new Thread(crearGuardIAn);
		thread.start();
	}

	public void finalizar() {
		try{
			semEstado.acquire();
		} catch(InterruptedException e){
			e.printStackTrace();
			return;
		}
		thread.interrupt();
		iniciado = false;
		semEstado.release();
	}

	public void escuchar(String mensaje) throws InterruptedException {
		semEstado.acquire();
		if(!iniciado){
			iniciarInseguro();
		}
		frasesEscuchadas.put(mensaje);

		scheduledPool.getQueue().clear();
		scheduledPool.schedule(apagar, 30, TimeUnit.MINUTES);

		semEstado.release();
	}

	public String getId() {
		return id;
	}
}
