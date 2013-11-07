package edu.neumont.harmonius.view;
import javax.swing.*;

import java.awt.Font;
import java.awt.event.*;

public class GUITest extends JFrame {    //tabbedPane style looks promising if I can dress it up


	public GUITest() {

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
		
		
		
		jtp.addTab("Main Menu", jp1);
		jtp.addTab("Pitch Warm Up", jp2);
		jtp.addTab("Perfect Pitch", jp3);
		jtp.addTab("Visual Feedback", jp4);
		jtp.addTab("Session", jp5);


		setVisible(true); 
	}



	//example usage
	public static void main (String []args){
		GUITest tab = new GUITest();
	}
}

