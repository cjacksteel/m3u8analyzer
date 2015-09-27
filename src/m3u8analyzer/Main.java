package m3u8analyzer;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) throws MalformedURLException, IOException {
		//Call the UI to appear
		EventQueue.invokeLater(new Runnable() {
		        
	            @Override
	            public void run() {
	                UserInterface ui = new UserInterface();
	                ui.setVisible(true);
	            }
	        });
		
		//Read in the conf file and determine what the URL is
		//ConfReader configFile = new ConfReader();
		//URL website = new URL(configFile.readConf("conf/address.conf"));
	}
}