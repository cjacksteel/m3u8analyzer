package m3u8analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ReadFile {
	DefaultTableModel model = new DefaultTableModel();
	public static Logger logger;
	private Object[] data = new Object[1000];
	
	public ReadFile(JTable table){
		model = (DefaultTableModel)table.getModel();
	}

	public void readLog(File f) throws FileNotFoundException, IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
		    String line;
		    int errorCount = 10000;
		    while ((line = br.readLine()) != null) {
		    	errorCount++;
		    	String[] tokens = line.split("`");
		    	int tokenCount = tokens.length;
		    	data[0] = errorCount;
		    	for (int i=1; i <= tokenCount; i++){
		    		data[i] = tokens[i-1].toString();
		    	}		    	
		    	model.addRow(data);
		    }
		}
	}
	
	public void readConf(File f) throws FileNotFoundException, IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	data[0] = line;
		    	model.addRow(data);
		    }
		}
	}

}