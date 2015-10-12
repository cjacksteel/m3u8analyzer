package m3u8analyzer;

import java.awt.EventQueue;

public class Main {

	public static void main(String[] args) {
		//Call the UI to appear
		EventQueue.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                final UserInterface ui = new UserInterface();
	                ui.setVisible(true);
	            }
	        });
	}
}