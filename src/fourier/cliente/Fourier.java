package fourier.cliente;

import java.util.ArrayList;

import fourier.util.Complex;
import fourier.util.FFT;
import fourier.vo.HuellaDigitalVO;

public class Fourier {

	double highscores[][];
	long points[][];
	
	public final int UPPER_LIMIT = 300;
	public final int LOWER_LIMIT = 40;

	public final int[] RANGE = new int[] { 40, 80, 120, 180, UPPER_LIMIT + 1 };

	// Find out in which range
	public int getIndex(int freq) {
		int i = 0;
		while (RANGE[i] < freq)
			i++;
		return i;
	}
	 
	private static final int FUZ_FACTOR = 2;

	private long hash(long p1, long p2, long p3, long p4) {
		return (p4 - (p4 % FUZ_FACTOR)) * 100000000 + (p3 - (p3 % FUZ_FACTOR))
				* 100000 + (p2 - (p2 % FUZ_FACTOR)) * 100
				+ (p1 - (p1 % FUZ_FACTOR));
	}
	
	public Complex[][] fourierTransformada(byte[] audio ) {
		
		final int tamañoTotal = audio.length;
		int tamañoVentana = 4096;
		int cantVentanas = tamañoTotal / tamañoVentana;

		Complex[][] resultado = new Complex[cantVentanas][];

		// Para todas las ventanas
		for (int j = 0; j < cantVentanas; j++) {
			Complex[] ventana = new Complex[tamañoVentana];
			for (int i = 0; i < tamañoVentana; i++) {
				// completamos el numero complejo con parte imaginaria en 0
				ventana[i] = new Complex(audio[(j * tamañoVentana) + i], 0);
			}
			// Analisis de Fourier sobre la ventana
			resultado[j] = FFT.fft(ventana);
		}
		//devuelve matriz [tiempo][frecuencia]
		return resultado;
	}
			
	ArrayList<HuellaDigitalVO> determinarHuellaDigital(Complex[][] resultado) {
		
		ArrayList<HuellaDigitalVO> listHuellaDigital = new ArrayList<HuellaDigitalVO>();
		
		highscores = new double[resultado.length][5];
		for (int i = 0; i < resultado.length; i++) {
			for (int j = 0; j < 5; j++) {
				highscores[i][j] = 0;
			}
		}
		
		points = new long[resultado.length][5];
		for (int i = 0; i < resultado.length; i++) {
			for (int j = 0; j < 5; j++) {
				points[i][j] = 0;
			}
		}

		for (int t = 0; t < resultado.length; t++) {
			//por cada tiempo me quedo con la intensidad mas alta para cada rango de frequencia.
			
			for (int freq = LOWER_LIMIT; freq < UPPER_LIMIT - 1; freq++) {
				// obtener magnitud
				double mag = Math.log(resultado[t][freq].abs() + 1);

				// indentificar rango de frecuencia
				int index = getIndex(freq);

				// almacenar las mejores marcas
				if (mag > highscores[t][index]) {
					highscores[t][index] = mag;
					points[t][index] = freq;
				}
			}

			long h = hash(points[t][0], points[t][1], points[t][2], points[t][3]);

			HuellaDigitalVO huellaDigital = new HuellaDigitalVO();
			huellaDigital.setHash(h);
			huellaDigital.setIdCancion(0);
			huellaDigital.setTiempo(t);
			listHuellaDigital.add(huellaDigital);
		}
		
		return listHuellaDigital;
	}
}
