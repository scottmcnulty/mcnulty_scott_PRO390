package edu.neumont.ui.harmonius;

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

import edu.neumont.note.harmonius.Note;
import edu.neumont.note.harmonius.WesternScale;
import edu.neumont.vendor.harmonius.AudioFloatInputStream;
import edu.neumont.vendor.harmonius.PaintComponent;
import edu.neumont.vendor.harmonius.Yin;
import edu.neumont.vendor.harmonius.Yin.DetectedPitchHandler;



public class Startup extends JFrame {	
	
	
	
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

				final double pitch_history[] = new double[600];
				int pitch_history_pos = 0;

				@Override
				public void handleDetectedPitch(float time,float pitch) {
					
					String noteName = getNote(pitch);
					
					Graphics2D g = painter.getOfflineGraphics();
					int w = painter.getWidth();
					int h = painter.getHeight();
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, w, h); 
					boolean noteDetected = pitch != -1;
					double detectedNote = 69D + (12D * Math.log(pitch / 440D)) / Math.log(2D);
					//noteDetected = noteDetected && Math.abs(detectedNote - Math.round(detectedNote)) > 0.3;

					if (pitch_history_pos == pitch_history.length)
						pitch_history_pos = 0;
					
					g.setColor(Color.WHITE);
					g.drawString((new StringBuilder("Duration: ")).append(detectedNote).toString(), 20, 20);
					g.drawString((new StringBuilder("Freq: ")).append(pitch).toString(), 20, 40);
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

				private String getNote(float pitch) {
					String note = "";
					
					for(Note n : notes){
						if(pitch <= n.getFrequency()+1.0 && pitch > n.getFrequency()-1.0){
							note = n.getNoteName() + " ON PITCH";
						}
					}
					return note;
				}
			});
		}
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
	
	WesternScale ws;
	ArrayList<Note> notes;
	
	
	public Startup(String fileName) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		ws = new WesternScale();
		notes = ws.getNotes();
		aiprocessor = null;
		this.fileName = fileName;

		painter = new PaintComponent();
		setSize(1000, 800);
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
		
		this.setTitle("Harmonius"); 
		this.setSize(1024,700); 
		
		JTabbedPane jtp = new JTabbedPane();

		getContentPane().add(jtp);
		JPanel jp1 = new JPanel();
		JPanel jp2 = new JPanel();
		JPanel jp3 = new JPanel();
		JPanel jp4 = new JPanel();
		JPanel jp5 = new JPanel();
		
		JLabel label1 = new JLabel();
		label1.setText("Main Menu");
		jp1.add(label1);
		
		JLabel label2 = new JLabel();
		label2.setText("Pitch Warm Up Training");
		jp2.add(label2);
		
		JLabel label3 = new JLabel();
		label3.setText("Perfect Pitch Training");
		jp3.add(label3);
		
		JLabel label4 = new JLabel();
		label4.setText("Visual FeedBack Note Training");
		jp4.add(label4);
		
		JLabel label5 = new JLabel();
		label5.setText("Session Training");
		jp5.add(label5);
		
		jtp.addTab("Main Menu", main);
		jtp.addTab("Pitch Warm Up", jp2);
		jtp.addTab("Perfect Pitch", jp3);
		jtp.addTab("Visual Feedback", jp4);
		jtp.addTab("Session Training", jp5);
		
		setVisible(true); 
		
		for (int i = 0; i < mixers.length; i++) {
			javax.sound.sampled.Mixer.Info mixerinfo = mixers[i];
			if (AudioSystem.getMixer(mixerinfo).getTargetLineInfo().length != 0)
				capMixers.add(mixerinfo);
		}

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