package edu.neumont.harmonius.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.neumont.harmonius.note.Note;
import edu.neumont.harmonius.note.WesternScale;
import edu.neumont.harmonius.vendor.AudioFloatInputStream;
import edu.neumont.harmonius.vendor.PaintComponent;
import edu.neumont.harmonius.vendor.Yin;
import edu.neumont.harmonius.vendor.Yin.DetectedPitchHandler;



public class Startup extends JFrame {	
	
	
	private double[] pitch_history_total = new double[6000];
	int pitchCounter = 0;
	
	class AudioInputProcessor implements Runnable {

		private final int sampleRate;
		private final double audioBufferSize;
		
		
		public AudioInputProcessor(){
			sampleRate = 22050; //Hz
			audioBufferSize = 0.1;//Seconds
		}

		
		//
		public void run() {
			javax.sound.sampled.Mixer.Info selected = (javax.sound.sampled.Mixer.Info) mixer_selector.getSelectedItem();
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
	
	
	
	
	private static final long serialVersionUID = 1L;	
	
	//volatile because it runs on its own thread - volatile guarantees the most updated value
	volatile AudioInputProcessor aiprocessor;
	
	PaintComponent painter;
	
	JComboBox mixer_selector;
	
	String fileName = null;
	
	//can replace with other tonal scales with their notes and pitch values
	WesternScale ws;
	ArrayList<Note> notes;
	
	
	public Startup(String fileName) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		
		ws = new WesternScale();
		notes = ws.getNotes();
		aiprocessor = null;
		this.fileName = fileName;
		painter = new PaintComponent();
		
		setTitle("Harmonius"); 
		setSize(1024,768); 
		setLocationRelativeTo(null);
		setTitle("Pitch detector" + fileName == null ? "" : " " + fileName);
		setDefaultCloseOperation(3);
		
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		
		JPanel buttonpanel = new JPanel();
		buttonpanel.setLayout(new FlowLayout(0));
		main.add("North", buttonpanel);
		ArrayList<javax.sound.sampled.Mixer.Info> capMixers = new ArrayList<javax.sound.sampled.Mixer.Info>();
		javax.sound.sampled.Mixer.Info mixers[] = AudioSystem.getMixerInfo();
		
		
		JTabbedPane jtp = new JTabbedPane();
		getContentPane().add(jtp);
		
		
		//create GUI for jp1 (JPanel One)
		JPanel jp1 = new JPanel();
		//jp1.setLayout(new BorderLayout());
		//JPanel jp1ButtonPanel = new JPanel();
		//jp1ButtonPanel.setLayout(new FlowLayout(0));
		//jp1.add("North", buttonpanel);
		JLabel label1 = new JLabel();
		label1.setText("Welcome Page");
		jp1.add(label1);
		
		
		//create GUI for jp2 (JPanel Two)
		JPanel jp2 = new JPanel();
		
		JLabel label2 = new JLabel();
		label2.setText("Pitch Warm Up Training");
		jp2.add(label2);
		
		
		//create GUI for jp3 (JPanel Three)
		JPanel jp3 = new JPanel();
		
		JLabel label3 = new JLabel();
		label3.setText("Perfect Pitch Training");
		jp3.add(label3);
		
		
		//create GUI for jp4 (JPanel Four)
		JPanel jp4 = new JPanel();
		
		JLabel label4 = new JLabel();
		label4.setText("Note Identification Training");
		jp4.add(label4);
		
		//create GUI for jp5 (JPanel Five)
		JPanel jp5 = new JPanel();
		
		JLabel label5 = new JLabel();
		label5.setText("Visual Feedback");
		jp5.add(label5);
		
		
		//create GUI for jp6 (JPanel Six)
		JPanel jp6 = new JPanel();
		
		JLabel label6 = new JLabel();
		label6.setText("Session Training");
		jp6.add(label6);
		
		
		
		//adds panels jp1 - jp5 to the appropriate tabbed windows
		jtp.addTab("Welcome Page", jp1);
		jtp.addTab("Warm Up", main);
		jtp.addTab("Perfect Pitch", jp3);
		jtp.addTab("Note ID", jp4);
		jtp.addTab("Visual Feedback", jp5);
		jtp.addTab("Session Training", jp6);
		
		setVisible(true); 
		
		
		
		for (int i = 0; i < mixers.length; i++) {
			javax.sound.sampled.Mixer.Info mixerinfo = mixers[i];
			if (AudioSystem.getMixer(mixerinfo).getTargetLineInfo().length != 0)
				capMixers.add(mixerinfo);
		}
	
		
		// GUI components   - buttons, combobox, etc.
		mixer_selector = new JComboBox(capMixers.toArray());
		buttonpanel.add(mixer_selector);
		JButton startbutton = new JButton("Start");
		startbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startAction();
			}
		});
		
		buttonpanel.add(startbutton);
		JButton stopbutton = new JButton("stop");
		stopbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopAction();
			}
		});
		
		buttonpanel.add(stopbutton);
		
		main.add("Center", painter);
	}

	
	
	public static void main(String args[]) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if(args.length==0 | args.length == 2){
			String fileName = args.length==0 ? null : args[1];
			Startup frame = new Startup(fileName);
			frame.setVisible(true);
		} else {
			Yin.main(args);
		}
	}

}