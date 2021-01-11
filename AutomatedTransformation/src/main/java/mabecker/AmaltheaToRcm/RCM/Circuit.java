package mabecker.AmaltheaToRcm.RCM;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.app4mc.amalthea.model.Label;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Circuit {

	private String name;
	
	private UUID id;
	private Notes notes;
	
	private String activeBehaviour;
	private Behaviour behaviour;
	private Interface iface;
	
	public Circuit(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
		activeBehaviour = "";
	}
	
	public void addInputData(PortDataIn _in) {
		iface.addInputData(_in);
	}
	
	public void addOutputData(PortDataOut _out) {
		iface.addOutputData(_out);
	}
	
	public void setBehaviour(Behaviour behaviour) {
		this.behaviour = behaviour;
	}
	
	public Behaviour getBehaviour() {
		return behaviour;
	}
	
	public void setIface(Interface iface) {
		this.iface = iface;
	}
	
	public void setNotes(String notes) {
		this.notes.setText(notes);
	}
	
	public Notes getNotes() {
		return notes;
	}
	
	public Interface getInterface() {
		return iface;
	}
	
	public String getName() {
		return name;
	}

	public Element toXml() {
		Element circuit = new Element("Circuit");
		
		circuit.setAttribute("ActiveBehaviour", activeBehaviour);
		circuit.setAttribute("Name", name);
		circuit.setAttribute("UUID", id.toString());
		
		circuit.addContent(notes.toXml());
		
		circuit.addContent(behaviour.toXlm());
		
		circuit.addContent(iface.toXml());
		
		return circuit;
	}
	
	/**
	 * Exports the components.rubusModel file.
	 * This includes all SWC that are part of the application.
	 * @param _basePath
	 * @param _circuits
	 */
	public static void exportComponentsFile(String _basePath, ArrayList<Circuit> _circuits) {
		try {	
			Document doc = new Document();
			
			Comment comment = new Comment("\n" + 
					"  Copyright Arcticus Systems AB, All rights reserved.\n" + 
					"  The format and content in the File is protected by copyright.\n" + 
					"  The File is furnished under a licence and may only be used in\n" + 
					"  accordance with the terms of this licence.\n");
			doc.addContent(comment);
			
			Element root = new Element("document");
			root.setAttribute("Model", "RCM");
			root.setAttribute("UUID", UUID.randomUUID().toString());
			root.setAttribute("Version", "2017-01-01");
			
			/* Add all Software Circuits */
			for (Circuit swc : _circuits) {
				root.addContent(swc.toXml());
			}
			
			doc.setRootElement(root);
			
			XMLOutputter outter=new XMLOutputter();
			outter.setXMLOutputProcessor(new OneAttributePerLineXmlProcessor());
			outter.setFormat(Format.getPrettyFormat());
		
			outter.output(doc, new FileWriter(new File(_basePath + "/components.rubusModel")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
