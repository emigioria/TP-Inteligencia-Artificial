package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.util.StringTokenizer;

import frsf.cidisi.faia.state.EnvironmentState;

/**
 * This class represents the real world state.
 */
public class EstadoAmbiente extends EnvironmentState {

	private StringTokenizer frasesEscuchadasTokenizer;

	public EstadoAmbiente() {
		super();
		this.initState();
	}

	public String getNextFrase() {
		if(frasesEscuchadasTokenizer.hasMoreTokens()){
			return frasesEscuchadasTokenizer.nextToken();
		}
		return null;
	}

	/**
	 * This method is used to setup the initial real world.
	 */
	@Override
	public void initState() {

	}

	public void setFrasesDichas(String frases) {
		frasesEscuchadasTokenizer = new StringTokenizer(frases, "\n\f\r.;¿?¡!");
	}

	@Override
	public String toString() {
		return "Estado ambiente.";
	}

	// The following methods are agent-specific:
}
