package mabecker.AmaltheaToRcm.RCM;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Node {

	private String 			name;
	private Notes 			notes;
	private UUID 			id;
	private ArrayList<Core>	cores;
	
	public Node(String _name) {

		id = UUID.randomUUID();
		name = _name;
		
		cores = new ArrayList<Core>();
		notes = new Notes("");
	}
	
	public void addCore(Core _core) {
		cores.add(_core);
	}
	
	public ArrayList<Core> getCores() {
		return cores;
	}

	public String getName() {
		return name;
	}

	public Element toXml(boolean _referenceUse) {
		
		Element node = new Element("Node"); 
		
		node.setAttribute("Name", name);
		node.setAttribute("UUID", id.toString());
		
		node.addContent(notes.toXml());
		
		for (Core core : cores) {
			node.addContent(core.toXml(_referenceUse));
		}
		return node;
	}
	
	public UUID getId() {
		return id;
	}
	
	public Element toXmlReference() {
		

		Element declare = new Element("Declare");
		
		declare.setAttribute("Name", name);
		declare.setAttribute("Path", "./" + name + ".rubusModel");
		declare.setAttribute("Reference", name);
		declare.setAttribute("UUID", id.toString());
		
		return declare;
	}

	public static void exportNodeModelFile(String _basePath, Node _node) {
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
			
			/* Add all nodes as reference */
			root.addContent(_node.toXml(true));
			
			doc.setRootElement(root);
			
			XMLOutputter outter=new XMLOutputter();
			outter.setXMLOutputProcessor(new OneAttributePerLineXmlProcessor());
			outter.setFormat(Format.getPrettyFormat());
		
			outter.output(doc, new FileWriter(new File(_basePath + "/" + _node.getName() + ".rubusModel")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
