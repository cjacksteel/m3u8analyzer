package m3u8analyzer;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

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