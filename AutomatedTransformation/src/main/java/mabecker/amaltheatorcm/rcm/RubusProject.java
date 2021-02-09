package mabecker.amaltheatorcm.rcm;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class RubusProject {

	private String projectNamePlaceholder = "XXX_PROJECT_NAME_XXX";
	private String projectIdPlaceholder = "XXX_PROJECT_ID_XXX";
	private String modelNamePlaceholder = "XXX_RUBUS_MODEL_NAME_XXX";

	private String systemIdPlaceholder = "XXX_SYSTEM_ID_XXX";
	private String nodeIdPlaceholder = "XXX_NODE_ID_XXX";
	private String partitionIdPlaceholder = "XXX_PARTITION_ID_XXX";
	
	private RCM rcm;
	private String outputPath;
	
	public RubusProject(RCM _rcm, String _outputPath) {
		rcm = _rcm;
		outputPath = _outputPath;
	}
	
	public UUID export() {
		UUID id = UUID.randomUUID();
		
		BufferedReader reader;
		
		if (rcm.getSystem().getNodes().size() != 1) {
			
			System.err.println("RCM Project can only include 1 Node.");
			return null;
		} else {
			if (rcm.getSystem().getNodes().get(0).getCores().size() != 1) {
				System.err.println("RCM Project can only include 1 Core.");
				return null;
			}
		}
		
		try {
			FileWriter writer = new FileWriter(outputPath + "/" + rcm.getSystem().getName() + ".rubusProject");
			
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("template.rubusProject");
			
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = reader.readLine();
			while (line != null) {
				//System.out.println(line);
				
				if (line.contains(projectNamePlaceholder)) {
					line = line.replace(projectNamePlaceholder, rcm.getSystem().getName() + "_Project");
				} else if (line.contains(projectIdPlaceholder)) {
					line = line.replace(projectIdPlaceholder, id.toString());
				} else if (line.contains(modelNamePlaceholder)) {
					line = line.replace(modelNamePlaceholder, rcm.getSystem().getName());
				} else if (line.contains(systemIdPlaceholder)) {
					line = line.replace(systemIdPlaceholder, rcm.getSystem().getId().toString());
				} else if (line.contains(nodeIdPlaceholder)) {
					line = line.replace(nodeIdPlaceholder, rcm.getSystem().getNodes().get(0).getId().toString());
				} else if (line.contains(partitionIdPlaceholder)) {
					line = line.replace(partitionIdPlaceholder, rcm.getSystem().getNodes().get(0).getCores().get(0).getPartition().getId().toString());
				} 
				
				writer.write(line + "\r\n");
				
				// read next line
				line = reader.readLine();				
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return id;
	}
}
