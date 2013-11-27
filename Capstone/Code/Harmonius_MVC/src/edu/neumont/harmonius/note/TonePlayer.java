package edu.neumont.harmonius.note;

import edu.neumont.harmonius.vendor.StdAudio;

public class TonePlayer {

	  // create a tone of the given frequency for the given duration
	  // uses a double[] to hold values
	
	    public static double[] tone(double hz, double duration) { 
	        int N = (int) (StdAudio.SAMPLE_RATE * duration);
	        double[] a = new double[N+1];
	        for (int i = 0; i <= N; i++) {
	            a[i] = Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
	        }
	        return a; 
	    } 


	    public static void main(String[] args) {

	        // frequency
	        double hz = Double.parseDouble("440.00");

	        // number of seconds to play the note
	        double duration = Double.parseDouble("3.0");

	        // create the array
	        double[] a = tone(hz, duration);

	        // play it using standard audio
	        StdAudio.play(a);
	    }
	}
