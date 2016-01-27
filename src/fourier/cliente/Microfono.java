package fourier.cliente;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class Microfono {
	
	String[]palabras=new String [100];
	AudioFileFormat.Type aFF_T = AudioFileFormat.Type.WAVE;
	AudioFormat aF = new AudioFormat(8000.0F, 16, 1, true, false);
	TargetDataLine tD;
	File f = new File("/home/nahuel/Documents/workspace/Microfono/Grabacion.wav");
	ByteArrayOutputStream baout = new ByteArrayOutputStream();
	
	public void grabar(){
		try {
			DataLine.Info dLI = new DataLine.Info(TargetDataLine.class,aF);
			tD = (TargetDataLine)AudioSystem.getLine(dLI);
			new CapThread().start();
			System.out.println("Grabando durante 10s...");
			Thread.sleep(10000);
			
			tD.close();
			baout.close();
			System.out.println("Fin grabacion");
			
		}catch (Exception e) {}
	}
	
	class CapThread extends Thread {
		
		public void run() {
			try {
				tD.open(aF);
				tD.start();
				AudioInputStream input = new AudioInputStream(tD);
				AudioSystem.write(input, aFF_T, f);
			}catch (Exception e){}
		}
	}
	
	public byte[] toByte() {
		String filePath = "/home/nahuel/Documents/workspace/Microfono/Grabacion.wav";
		File file = new File(filePath);
		System.out.println(file.getAbsolutePath());
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        byte[] data = null;
        try {
            final ByteArrayOutputStream baout = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int c;
            while ((c = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                baout.write(buffer, 0, c);
                System.out.println(c);
            }
            System.out.println("c: " +c);
            audioInputStream.close();
            baout.close();
            data = baout.toByteArray();
        } catch (Exception e) {
        	System.out.println("error!!!");
            e.printStackTrace();
        }
        return data;
    }
	
}	