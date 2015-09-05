package projects.nerdybuzz.catchdrop;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.InputAdapter;

public class KeySequence extends InputAdapter {
	private ArrayList<Integer> sequence, keysPressed;
	public boolean sequenceFound = false;
	
	public KeySequence(ArrayList<Integer> sequence) {
		this.sequence = sequence;
		this.keysPressed = new ArrayList<Integer>();
	}
	
	public boolean sequenceFound() {
		return sequenceFound;
	}
	
	public boolean keyDown(int keycode) {
		System.out.println("keyDown from KeySequence is called.");
		keysPressed.add(keycode);
		System.out.println(keysPressed);
		/*
		if(Arrays.equals(sequence.toArray(), keysPressed.toArray())) {
			keysPressed.clear();
			System.out.println("Well, this worked...");
			sequenceFound = true;
		} else {
			sequenceFound = false;
		}
		// */
		return true;
	}
	
	public void update() {
		for(int i=0;i<sequence.toArray().length;i++){
			if(i < keysPressed.toArray().length && keysPressed.toArray()[i] != null) {
				if(keysPressed.toArray()[i] != sequence.toArray()[i]) keysPressed.clear();
			}
		}
		
		if(Arrays.equals(sequence.toArray(), keysPressed.toArray())) {
			keysPressed.clear();
			sequenceFound = true;
		}
	}
	
	public void reset() {
		keysPressed.clear();
		sequenceFound = false;
	}
}








