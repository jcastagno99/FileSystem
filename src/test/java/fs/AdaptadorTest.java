package fs;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class AdaptadorTest {

	LowLevelFileSystem mockFileSystem = mock(LowLevelFileSystem.class);
	Adaptador unAdaptador = new Adaptador(mockFileSystem);
	Buffer unBuffer = new Buffer(200);
	Buffer otroBuffer = new Buffer(3);
	int idArchivo;
	int idOtroArchivo;

	@Before
	public void init() {
		idArchivo = mockFileSystem.openFile("Path");
		idOtroArchivo = mockFileSystem.openFile("Otro Path");
		otroBuffer.bytes[0] = 0x00;
		otroBuffer.bytes[1] = 0x10;
		otroBuffer.bytes[2] = 0x00;
	}

	@Test
	public void Ejemplo1() {
		unAdaptador.leerArchivoSincronicamente(idArchivo, unBuffer, 4);
		unAdaptador.leerArchivoSincronicamente(idArchivo, unBuffer, 1);
		unAdaptador.leerArchivoSincronicamente(idArchivo, unBuffer, 5);
		unAdaptador.escribirArchivo(idOtroArchivo, unBuffer, 4);
		unAdaptador.escribirArchivo(idOtroArchivo, otroBuffer, otroBuffer.bytes.length - 1); // No sabia bien como
																							// interpretar la cantidad
																							// de bytes del bloque así
																							// que hardcodee un buffer
																							// de 3 posiciones
		unAdaptador.escribirArchivo(idOtroArchivo, unBuffer, 1);
		unAdaptador.escribirArchivo(idOtroArchivo, unBuffer, 5);

	}

	@Test
	public void Ejemplo2() {
		unAdaptador.leerArchivoSincronicamente(idArchivo, unBuffer, unBuffer.bytes.length - 1);
		unAdaptador.escribirArchivo(idOtroArchivo, unBuffer, unBuffer.bytes.length - 1);
	}

}