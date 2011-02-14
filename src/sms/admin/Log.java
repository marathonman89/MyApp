package sms.admin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

import sms.lookup.Lookup;
import sms.lookup.LookupQuery;

@AuthorizeInstantiation("Administration")
public class Log extends Layout{
	private String logContent;
	
	public Log() throws IOException{
		Lookup lookup = Lookup.AllLookupStatus();
		
		String fileName = lookup.getLog_file_used();
		logContent = getLog(fileName);
		
		Label warningLabel = new Label("warning", "Warning: System log is disabled, Go to System Config to enable it.");
		Label currentfileLabel = new Label("currentfile", fileName);
		TextArea logTextarea = new TextArea("log", new PropertyModel(this, "logContent"));
		
		if(lookup.isLog_enabled()) warningLabel.setVisible(false);
		add(warningLabel);
		add(currentfileLabel);
		add(logTextarea);
	}
	public String getLog(String fileName) throws IOException{
		String content = "";
		FileReader input;
		BufferedReader reader;
		String line;
		try {
			 input = new FileReader("E:\\SmsSystem\\SMS\\logs\\"+fileName);
			 reader = new BufferedReader(input);
			 line = reader.readLine();
			 
			 while (line != null){
				 System.out.println(line);
				 content+=line+"\n";
				 line = reader.readLine();
			 }

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return content;
	}
	public static void loggedAction(Boolean logEnabled, String fileName, String action){
		
		if(logEnabled != null && logEnabled){
			try {
				FileWriter writer = new FileWriter("E:\\SmsSystem\\SMS\\logs\\"+fileName, true);
				String DATE_FORMAT_NOW = "MM-dd-yyyy, HH:mm:ss";
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
				InetAddress IP = InetAddress.getLocalHost();

				writer.append("["+sdf.format(cal.getTime())+"] " + "[IP: " + IP.getHostAddress() + "] " +action + "\n");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
