package m3u8analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LogReader {
	DefaultTableModel model = new DefaultTableModel();
	public static Logger logger;
	private Object[] data = new Object[1000];
	
	public LogReader(JTable table){
		model = (DefaultTableModel)table.getModel();
	}

	public void readLog(File f) throws FileNotFoundException, IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] tokens = line.split("`");
		    	int tokenCount = tokens.length;
		    	for (int i=0; i < tokenCount; i++){
		    		data[i] = tokens[i].toString();
		    	}		    	
		    	model.addRow(data);
		    }
		}
	}

}