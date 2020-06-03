package fs;

import java.util.function.Consumer;

public class Adaptador {

	LowLevelFileSystem interfazFea;
	
	public Adaptador(LowLevelFileSystem interfazFea) {
		this.interfazFea = interfazFea;
	}

	int abrirArchivo(String path) {
		int id = interfazFea.openFile(path);
		if (id < 0) {
			throw new FalloAperturaArchivoException("Ocurrió un error al abrir el archivo"); // asumí que si el id es
																								// negativo hubo un
																								// error.
		}
		return id;
	}

	void cerrarArchivo(int id) {
		interfazFea.closeFile(id);
	}

	int leerArchivoSincronicamente(int id, Buffer unBuffer, int cantidadBytesALeer) {
		// Asumo que el control sobre si el archivo esta o no abierto es algo que hace
		// LowLevelFileSystem
		if (cantidadBytesALeer < 0) {
			throw new CantidadBytesInvalidosException("La cantidad de bytes a leer no puede ser negativa");
		}

		int bufferEnd = unBuffer.ultimaPosicion + cantidadBytesALeer - 1;

		if (bufferEnd > unBuffer.bytes.length - 1) {
			throw new LimiteBufferException("No hay espacio disponible en el buffer");
		}

		int totalLeido = interfazFea.syncReadFile(id, unBuffer.bytes, unBuffer.ultimaPosicion, bufferEnd);
		unBuffer.actualizarPosicion(totalLeido);

		/*
		 * if(totalLeido<0) { throw new
		 * FalloLecturaException("Error en la lectura del archivo"); // control
		 * descartado : Smell, no cumple failfast. }
		 */

		return totalLeido;
	}

	void escribirArchivo(int id, Buffer unBuffer, int cantidadBytesAEscribir) {

		if (cantidadBytesAEscribir < 0) {
			throw new CantidadBytesInvalidosException("La cantidad de bytes a escribir no puede ser negativa");
		}

		int bufferEnd = unBuffer.ultimaPosicion + cantidadBytesAEscribir - 1;
		
		if (bufferEnd > unBuffer.bytes.length - 1) {
			throw new LimiteBufferException("No hay espacio disponible en el buffer");
		}
		
		interfazFea.syncWriteFile(id, unBuffer.bytes, unBuffer.ultimaPosicion, bufferEnd);
	}

	void leerArchivoAsincronicamente(int id, Buffer unBuffer, int cantidadBytesALeer, Consumer<Integer> callback) {
		int bufferEnd = unBuffer.ultimaPosicion + cantidadBytesALeer - 1;
		interfazFea.asyncReadFile(id, unBuffer.bytes, unBuffer.ultimaPosicion, bufferEnd, callback); // tengo entendido
																										// que callback
																										// actualiza el
																										// buffer solo
	}

}
