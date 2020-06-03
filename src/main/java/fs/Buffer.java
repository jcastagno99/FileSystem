package fs;

public class Buffer {
	byte[] bytes;
	int ultimaPosicion;
	
	public Buffer(int tamanio) {
		this.bytes = new byte[tamanio];
		this.ultimaPosicion = 0;
	}

	void actualizarPosicion(int cantidad) {
		this.ultimaPosicion += cantidad;
	}

}
