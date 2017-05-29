package frsf.cidisi.exercise.patrullero.search.modelo;

public enum TipoIncidente {
	ALARMA_VECINAL_SUCESO_CALLEJERO("Alarma vecinal por suceso callejero"),
	ALARMA_DE_PANICO_POR_VIOLENCIA_DE_GENERO("Alarma de pánico por violencia de género"),
	ALARMA_DE_CASA_DE_FAMILIA("Alarma de casa de familia"),
	ALARMA_DE_VEHICULO("Alarma de vehículo"),
	CAMARA_EN_LA_VIA_PUBLICA("Cámara en la via pública"),
	CAMARA_EN_HOGARES("Cámara en hogares");

	private String nombre;

	private TipoIncidente(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
