package mabecker.AmaltheaToRcm.RCM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class RubusDesigner {

	private RCM rcm;
	private String outputBase;
	private UUID projectId;
	
	//XXX_PROJECT_NAME_XXX
	//XXX_System_UUID_XXX
	private String workspaceNamePlaceholder = "XXX_WORKSPACE_NAME_XXX";
	private String projectIdPlaceholder = "XXX_PROJECT_UUID_XXX";
	private String projectFileNamePlaceholder = "XXX_RCM_PROJECT_XXX";
	//XXX_RUBUS_MODEL_NAME_XXX
	
	public RubusDesigner(RCM _rcm, UUID _projectId, String _outputBase) {
		rcm = _rcm;
		outputBase = _outputBase;
		projectId = _projectId;
	}
	
	public void export() {
		
		BufferedReader reader;
		
		try {
			FileWriter writer = new FileWriter(outputBase + "/" + rcm.getSystem().getName() + ".rubusDesigner");
			reader = new BufferedReader(new FileReader("src/main/resources/template.rubusDesigner"));
			String line = reader.readLine();
			while (line != null) {
				//System.out.println(line);
				
				if (line.contains(workspaceNamePlaceholder)) {
					line = line.replace(workspaceNamePlaceholder, rcm.getSystem().getName());
				} else if (line.contains(projectFileNamePlaceholder)) {
					line = line.replace(projectFileNamePlaceholder, rcm.getSystem().getName());
				} else if (line.contains(projectIdPlaceholder)) {
					line = line.replace(projectIdPlaceholder, projectId.toString());
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
	}
}
