package edu.neumont.harmonius.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import edu.neumont.harmonius.controller.AnalyzerController;
import edu.neumont.harmonius.vendor.PaintComponent;


public class ApplicationView extends JFrame {
	
	PaintComponent painter = new  PaintComponent();

	public ApplicationView(final AnalyzerController analyzer){
		this.setTitle("Harmonius"); 
		setSize(1024,768); 
		setLocationRelativeTo(null);
		//setTitle("Pitch detector" + fileName == null ? "" : " " + fileName);
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
		
		JTextArea jp2InstructionsJTextArea  = new javax.swing.JTextArea();
		JTextArea jp2ResultsJTextArea = new javax.swing.JTextArea();
        JLabel jp2InstructionsLabel = new javax.swing.JLabel();
        JLabel jp2ResultsLabel = new javax.swing.JLabel();

        jp2InstructionsJTextArea.setColumns(20);
        jp2InstructionsJTextArea.setRows(5);
        jp2InstructionsJTextArea.setText("This is for Pitch Warm-Up exercises.");
        jp2.add(jp2InstructionsJTextArea);

        jp2ResultsJTextArea.setColumns(20);
        jp2ResultsJTextArea.setRows(5);
        jp2ResultsJTextArea.setText("This is for results");
        jp2.add(jp2ResultsJTextArea);

        jp2InstructionsLabel.setText("INSTRUCTIONS");
        jp2.add(jp2InstructionsLabel);
        jp2ResultsLabel.setText("RESULTS");
        jp2.add(jp2ResultsLabel);
		
		
		
		
		
		//create GUI for jp3 (JPanel Three)
		JPanel jp3 = new JPanel();
		
		JLabel label3 = new JLabel();
		label3.setText("Interval Training");
		jp3.add(label3);
		
		JTextArea jp3InstructionsJTextArea  = new javax.swing.JTextArea();
		JTextArea jp3ResultsJTextArea = new javax.swing.JTextArea();
        JLabel jp3InstructionsLabel = new javax.swing.JLabel();
        JLabel jp3ResultsLabel = new javax.swing.JLabel();

        jp3InstructionsJTextArea.setColumns(20);
        jp3InstructionsJTextArea.setRows(5);
        jp3InstructionsJTextArea.setText("This is for Interval Training exercises.");
        jp3.add(jp3InstructionsJTextArea);

        jp3ResultsJTextArea.setColumns(20);
        jp3ResultsJTextArea.setRows(5);
        jp3ResultsJTextArea.setText("This is for results");
        jp3.add(jp3ResultsJTextArea);

        jp3InstructionsLabel.setText("INSTRUCTIONS");
        jp3.add(jp3InstructionsLabel);
        jp3ResultsLabel.setText("RESULTS");
        jp3.add(jp3ResultsLabel);
		
		
		
		
		//create GUI for jp4 (JPanel Four)
		JPanel jp4 = new JPanel();
		
		JLabel titleLabel4 = new JLabel();
		titleLabel4.setText("Note Identification Training");
		jp4.add(titleLabel4);
		
		JTextArea jp4InstructionsJTextArea  = new javax.swing.JTextArea();
		JTextArea jp4ResultsJTextArea = new javax.swing.JTextArea();
        JLabel jp4InstructionsLabel = new javax.swing.JLabel();
        JLabel jp4ResultsLabel = new javax.swing.JLabel();

        jp4InstructionsJTextArea.setColumns(20);
        jp4InstructionsJTextArea.setRows(5);
        jp4InstructionsJTextArea.setText("This is for Note ID training exercises.");
        jp4.add(jp4InstructionsJTextArea);

        jp4ResultsJTextArea.setColumns(20);
        jp4ResultsJTextArea.setRows(5);
        jp4ResultsJTextArea.setText("This is for results");
        jp4.add(jp4ResultsJTextArea);

        jp4InstructionsLabel.setText("INSTRUCTIONS");
        jp4.add(jp4InstructionsLabel);
        jp4ResultsLabel.setText("RESULTS");
        jp4.add(jp4ResultsLabel);
		 
		
		//create GUI for jp5 (JPanel Five)
		JPanel jp5 = new JPanel();
		
		JLabel label5 = new JLabel();
		label5.setText("Perfect Pitch Training");
		jp5.add(label5);
		
		JTextArea jp5InstructionsJTextArea  = new javax.swing.JTextArea();
		JTextArea jp5ResultsJTextArea = new javax.swing.JTextArea();
        JLabel jp5InstructionsLabel = new javax.swing.JLabel();
        JLabel jp5ResultsLabel = new javax.swing.JLabel();

        jp5InstructionsJTextArea.setColumns(20);
        jp5InstructionsJTextArea.setRows(5);
        jp5InstructionsJTextArea.setText("This is for Perfect Pitch exercises.");
        jp5.add(jp5InstructionsJTextArea);

        jp5ResultsJTextArea.setColumns(20);
        jp5ResultsJTextArea.setRows(5);
        jp5ResultsJTextArea.setText("This is for results");
        jp5.add(jp5ResultsJTextArea);

        jp5InstructionsLabel.setText("INSTRUCTIONS");
        jp5.add(jp5InstructionsLabel);
        jp5ResultsLabel.setText("RESULTS");
        jp5.add(jp5ResultsLabel);
		
			
	
		
		
		
		//create GUI for jp6 (JPanel Six)
		JPanel jp6 = new JPanel();
		
		JLabel label6 = new JLabel();
		label6.setText("Session Training");
		jp6.add(label6);
		
		JTextArea jp6InstructionsJTextArea  = new javax.swing.JTextArea();
		JTextArea jp6ResultsJTextArea = new javax.swing.JTextArea();
        JLabel jp6InstructionsLabel = new javax.swing.JLabel();
        JLabel jp6ResultsLabel = new javax.swing.JLabel();
		
		jp6InstructionsJTextArea.setColumns(20);
        jp6InstructionsJTextArea.setRows(5);
        jp6InstructionsJTextArea.setText("This is for Session Training exercises.");
        jp6.add(jp6InstructionsJTextArea);

        jp6ResultsJTextArea.setColumns(20);
        jp6ResultsJTextArea.setRows(5);
        jp6ResultsJTextArea.setText("This is for results");
        jp6.add(jp6ResultsJTextArea);

        jp6InstructionsLabel.setText("INSTRUCTIONS");
        jp6.add(jp6InstructionsLabel);
        jp6ResultsLabel.setText("RESULTS");
        jp6.add(jp6ResultsLabel);
		
		
		
		//adds panels jp1 - jp5 to the appropriate tabbed windows
		jtp.addTab("Welcome Page", jp1);
		jtp.addTab("Warm Up", main);
		jtp.addTab("Interval Training", jp3);
		jtp.addTab("Note ID", jp4);
		jtp.addTab("Perfect Pitch", jp5);
		jtp.addTab("Session Training", jp6);
		
		setVisible(true); 	
	
	
	
	for (int i = 0; i < mixers.length; i++) {
		javax.sound.sampled.Mixer.Info mixerinfo = mixers[i];
		if (AudioSystem.getMixer(mixerinfo).getTargetLineInfo().length != 0)
			capMixers.add(mixerinfo);
	}

	
	// GUI components   - buttons, combobox, etc.
	JComboBox mixer_selector = new JComboBox(capMixers.toArray());
	buttonpanel.add(mixer_selector);
	JButton startbutton = new JButton("Start");
	startbutton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			//analyzer.setInputDevice(mixer_selector.getSelectedItem());
			analyzer.startAction();
		}
	});
	
	buttonpanel.add(startbutton);
	JButton stopbutton = new JButton("stop");
	stopbutton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			analyzer.stopAction();
		}
	});
	
	buttonpanel.add(stopbutton);
	
	main.add("Center", painter);
	}
}


