package edu.neumont.harmonius.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComboBox;

import edu.neumont.harmonius.note.Note;
import edu.neumont.harmonius.note.WesternScale;
import edu.neumont.harmonius.vendor.AudioFloatInputStream;
import edu.neumont.harmonius.vendor.PaintComponent;
import edu.neumont.harmonius.vendor.Yin;
import edu.neumont.harmonius.vendor.Yin.DetectedPitchHandler;

public class AnalyzerController {

	javax.sound.sampled.Mixer.Info target;
	
	public AnalyzerController(){
		
	}
	
	
	private static final long serialVersionUID = 1L;	

	//volatile because it runs on its own thread - volatile guarantees the most updated value
	volatile AudioInputProcessor aiprocessor;

	PaintComponent painter;

	JComboBox mixer_selector;

	String fileName = null;

	//can replace with other tonal scales with their notes and pitch values
	WesternScale ws;
	ArrayList<Note> notes;
	private double[] pitch_history_total = new double[6000];
	int pitchCounter = 0;
	
	public void setInputDevice(Object object){
		target = (javax.sound.sampled.Mixer.Info) object;
		
	}


	class AudioInputProcessor implements Runnable {

		private final int sampleRate;
		private final double audioBufferSize;


		public AudioInputProcessor(){
			sampleRate = 22050; //Hz
			audioBufferSize = 0.1;//Seconds
		}


		public void run() {
			javax.sound.sampled.Mixer.Info selected = (javax.sound.sampled.Mixer.Info) target;                //mixer_selector.getSelectedItem();
			if (selected == null)
				return;
			try {
				Mixer mixer = AudioSystem.getMixer(selected);
				AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,false);
				DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class,format);
				TargetDataLine line = (TargetDataLine) mixer.getLine(dataLineInfo);
				int numberOfSamples = (int) (audioBufferSize * sampleRate);
				line.open(format, numberOfSamples);
				line.start();
				AudioInputStream ais = new AudioInputStream(line);
				if(fileName != null){
					ais = AudioSystem.getAudioInputStream(new File(fileName));

				}
				AudioFloatInputStream afis = AudioFloatInputStream.getInputStream(ais);
				processAudio(afis);
				line.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			aiprocessor = null;


		}

		public void processAudio(AudioFloatInputStream afis) throws IOException, UnsupportedAudioFileException {

			Yin.processStream(afis,new DetectedPitchHandler() {

				final double pitch_history[] = new double[6000];  //600 is default value to get a pretty good visual line of pitch
				int pitch_history_pos = 0;

				@Override
				public void handleDetectedPitch(float time,float pitch) {

					String noteName = getNote(pitch);

					Graphics2D g = painter.getOfflineGraphics();
					int w = painter.getWidth();
					int h = painter.getHeight();
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(0, 0, w, h); 

					boolean noteDetected = pitch != -1;

					double detectedNote = 69D + (12D * Math.log(pitch / 440D)) / Math.log(2D);
					//noteDetected = noteDetected && Math.abs(detectedNote - Math.round(detectedNote)) > 0.3;

					if (pitch_history_pos == pitch_history.length)
						pitch_history_pos = 0;

					g.setColor(Color.BLACK);

					g.drawString((new StringBuilder("Duration: ")).append(detectedNote).toString(), 20, 20);
					g.drawString((new StringBuilder("Freq: ")).append(pitch).toString(), 20, 40);

					pitch_history_total[pitchCounter] = pitch;
					pitchCounter++;

					g.drawString((new StringBuilder("Note: ")).append(noteName).toString(), 20, 60);
					g.drawString((new StringBuilder("Time: ")).append(time).toString(), 20, 80);

					pitch_history[pitch_history_pos] = noteDetected ? detectedNote : 0.0;


					int jj = pitch_history_pos;

					for (int i = 0; i < pitch_history.length; i++) {
						double d = pitch_history[jj];
						float rr = 1;
						float gg = 1;
						float bb = 1;
						g.setColor(new Color(rr, gg, bb));
						if (d != 0.0D) {
							int x = (int) (((double) i / (double) pitch_history.length) * w);
							int y = (int) (h - ((d - 20D) / 120D) * h);
							g.drawRect(x, y, 1, 1);
						}
						if (++jj == pitch_history.length)
							jj = 0;
					}


					pitch_history_pos++;
					painter.refresh();
				}


			});

			double pitchAccum = 1.0;
			int pitchCount = 1;
			double averagePitch = 0;  
			for(int i = 0; i < pitch_history_total.length; i++){
				System.out.println(pitch_history_total[i] + "\t");
				if(pitch_history_total[i] > 0){  //filter out zeros and -1(no signal)

					//filter to get rid of overtones
					if(Math.abs(averagePitch - pitch_history_total[i]) < 300){ //tighten up

						pitchAccum += pitch_history_total[i];  
						pitchCount++;
					}
				}
				averagePitch = pitchAccum/pitchCount;
			}
			System.out.println("\n Average pitch = " + averagePitch);

		}
	}

	private String getNote(float pitch) {
		String note = "";

		for(Note n : notes){
			if(pitch <= n.getFrequency()+1.0 && pitch > n.getFrequency()-1.0){
				note = n.getNoteName() + " ON PITCH";
			}
		}
		return note;
	}

	public void startAction() {
		if (aiprocessor != null) {
			return;
		} else {
			aiprocessor = new AudioInputProcessor();
			(new Thread(aiprocessor)).start();
			return;
		}
	}

	public void stopAction() {
		if (aiprocessor == null) {
			return;
		} else {
			Yin.stop();
			return;
		}
	}	
}
