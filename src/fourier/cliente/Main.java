package fourier.cliente;

import java.util.ArrayList;
import com.google.gson.Gson;
import fourier.util.Complex;
import fourier.vo.HuellaDigitalVO;

public class Main {

	public static void main(String[] args) {
		
		Sound s = new Sound();
		ApacheHttpClientPost p = new ApacheHttpClientPost();
		Fourier f = new Fourier();
		Gson gson = new Gson();

		//Recupero el audio de entrada 
		System.out.println("Grabando microfono...");
		byte[] data = s.microfono();
		//System.out.println("Tamaño: "+data.length);
		
		//Aplico la transformada de Fourier para pasar del dominio del tiempo al dominio de las frecuencias
		System.out.println("Procesando...");
		Complex[][] resultado = f.fourierTransformada(data);
		
		//Obtengo los puntos particulares para generar una lista de hash denominadas huellas digitales 
		ArrayList<HuellaDigitalVO> ListHD = f.determinarHuellaDigital(resultado);
		
		//Convierto la lista en formato Json
		String jsonString = gson.toJson(ListHD);
		
		//Envio la consulta Json al servidor
		System.out.println("Conectando con el servidor...");
		p.post(jsonString);
	}
	
}
