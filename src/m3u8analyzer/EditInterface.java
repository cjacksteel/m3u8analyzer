package m3u8analyzer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;



public class EditInterface extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JButton btnAddTag;

	public EditInterface() {
		setTitle("Edit Valid Tags");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
		
        panel = new JPanel();
		panel.setLayout(null);
		getContentPane().add(panel);
		
		String columns[] = {"Tag Name"};
		DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);
		//Define column widths
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		//Enable clicking on the headers to sort columns		
		table.setAutoCreateRowSorter(true);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(12, 12, 358, 190);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//force the table to stop editing so the final row added is saved
				TableCellEditor editor = table.getCellEditor();
				if (editor != null) {
				  editor.stopCellEditing();
				}
			try	(Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream("conf/validtags.conf"), "utf-8"))){
		        for (int i=0; i < table.getRowCount(); i++){
				String s = table.getValueAt(i, 0).toString();
				if ((s != "" || s != null) && i < (table.getRowCount() - 1)){
					writer.write(s + "\n");
				}
				else if ((s != "" || s != null)){
					writer.write(s);
				}
			}
			}
			catch (Exception e1){	
			}

			dispose();
			}
		});
		btnSave.setBounds(165, 215, 97, 25);
		panel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(273, 215, 97, 25);
		panel.add(btnCancel);
		
		btnAddTag = new JButton("Add Tag");
		btnAddTag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] obj = new Object[1];
				obj = null;
				DefaultTableModel y =(DefaultTableModel)table.getModel(); 
		        y.addRow(obj); 
			}
		});
		btnAddTag.setBounds(12, 215, 97, 25);
		panel.add(btnAddTag);
		
		//Add the file rows to the table
		Object[] data = new Object[1000];
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("conf/validtags.conf"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line;
		try {
			while ((line = br.readLine()) != null) {
			    	data[0] = line;
			    	this.insertRow(data);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void insertRow(Object[] obj){
		DefaultTableModel y =(DefaultTableModel)table.getModel(); 
        y.addRow(obj); 
	}
}		