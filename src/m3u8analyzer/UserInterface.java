package m3u8analyzer;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.miginfocom.swing.MigLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class UserInterface extends JFrame {
	public JTextField textField;
	
	public UserInterface() {
		setTitle("Cory's m3u8analyzer");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(412, 632, 200, 40);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		getContentPane().setLayout(null);
		getContentPane().add(btnExit);
		
		JLabel lblEnterUrlOf = new JLabel("Enter URL of Master HLS File:");
		lblEnterUrlOf.setBounds(12, 13, 175, 16);
		getContentPane().add(lblEnterUrlOf);
		
		textField = new JTextField();
		textField.setBounds(185, 10, 809, 22);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnProcessFile = new JButton("Process File");
		btnProcessFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					try {
						URL website = new URL(textField.getText());
						textField.setEnabled(false);
						btnProcessFile.setEnabled(false);
						//Make the log file name dynamic based on the passed URL
						System.setProperty("logfilename", website.toString().replaceAll("[-+.^:,/]","_"));
						
						//Get the files from the specified URL
						FileFetcher ff = new FileFetcher(website);
						ff.downloadMasterFile();
						File masterFile = new File(ff.getMasterFileLocation());
						ff.downloadChildFiles(masterFile);
						
						//Create a parser that will parse each file and apply analysis
						FileParser p = new FileParser();
						p.parse(p.getFileArray(ff.getRootDirectory()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Malformed URL.\nURL must be in the format http://www.example.com/hls-master-file.m3u8",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
			}
		});
		btnProcessFile.setBounds(185, 42, 133, 25);
		getContentPane().add(btnProcessFile);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
	}
}
