package m3u8analyzer;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class UserInterface extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField textField;
	private JTable table;
	private JPanel panel;
	private JScrollPane scrollPane;

	public UserInterface() {
		setTitle("Cory's m3u8analyzer");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
        panel = new JPanel();
        
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(412, 632, 200, 40);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		panel.setLayout(null);
		getContentPane().add(panel);
		
		panel.add(btnExit);
		
		JLabel lblEnterUrlOf = new JLabel("Enter URL of Master HLS File:");
		lblEnterUrlOf.setBounds(12, 13, 175, 16);
		panel.add(lblEnterUrlOf);
		
		textField = new JTextField();
		textField.setBounds(185, 10, 809, 22);
		panel.add(textField);
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
						
						ReadFile lr = new ReadFile(table);
						lr.readLog(p.getLogfile());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Malformed URL.\nURL must be in the format http://www.example.com/hls-master-file.m3u8",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
			}
		});
		btnProcessFile.setBounds(185, 42, 133, 25);
		panel.add(btnProcessFile);
		
		String columns[] = {"Error No.", "Severity", "File Name", "Line No.", "Error Details"};
		DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);
		//Define column widths
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(65);
		table.getColumnModel().getColumn(2).setPreferredWidth(300);
		table.getColumnModel().getColumn(3).setPreferredWidth(60);
		table.getColumnModel().getColumn(4).setPreferredWidth(496);
		//Enable clicking on the headers to sort columns		
		table.setAutoCreateRowSorter(true);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 89, 984, 532);
		panel.add(scrollPane, BorderLayout.CENTER);

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
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmValidTags = new JMenuItem("Valid Tags");
		mntmValidTags.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//EditDialog edit = new EditDialog();
				EditInterface edit = new EditInterface();
				//edit.pack();
				//edit.setLocationRelativeTo(panel);
				edit.setVisible(true);
			}
		});
		mnEdit.add(mntmValidTags);
	}
	
	public void insertRow(Object[] obj){
		DefaultTableModel y =(DefaultTableModel)table.getModel(); 
        y.addRow(obj); 
	}
}		