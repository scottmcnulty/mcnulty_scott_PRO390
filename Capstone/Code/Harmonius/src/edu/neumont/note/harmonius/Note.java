package edu.neumont.note.harmonius;

public class Note {

	private String noteName;
	private float frequency;
	private float noteDuration;
	
	
	public String getNoteName() {
		return noteName;
	}
	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}
	public double getFrequency() {
		return frequency;
	}
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
	
	public Note(String name, float freq){
		this.frequency = freq;
		this.noteName = name;
	}
	public float getNoteDuration() {
		return noteDuration;
	}
	public void setNoteDuration(float noteDuration) {
		this.noteDuration = noteDuration;
	}
}
