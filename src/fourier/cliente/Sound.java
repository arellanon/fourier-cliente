package fourier.cliente;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Sound {
	
	boolean running = false;
	
	private static AudioFormat getFormat() {
		float sampleRate = 44100;
		int sampleSizeInBits = 8;
		int channels = 1; // mono
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	
	public byte[] microfono() {
		AudioFormat format = null;
		TargetDataLine line = null;
		format = getFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			line.open(format);
			line.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[(int) 1024];
		
		try {
			//Grabamos 10Kb
			for(int i= 0; i < 1000; i++){
				int count = 0;
				count = line.read(buffer, 0, 1024);
				if (count > 0) {
					out.write(buffer, 0, count);
				}
			}
			out.close();
			line.close();
		} catch (IOException e) {
			System.err.println("I/O problems: " + e);
			System.exit(-1);
		}
		return out.toByteArray();
	}
	

}
