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

public class RcmSystem {

	private String 				name;
	private Notes 				notes;
	private ArrayList<Node> 	nodes;
	private UUID 				id;
	
	public RcmSystem(String _sysName) {
		name = _sysName;
		notes = new Notes(" ");
		id = UUID.randomUUID();
		nodes = new ArrayList<Node>();
	}
	
	public String getName() {
		return name;
	}
	
	public Notes getNotes() {
		return notes;
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}

	public Element toXml(boolean _referenceUse) {
		
		Element system = new Element("System");
		system.setAttribute("Name", name);
		system.setAttribute("UUID", id.toString());
		
		system.addContent(notes.toXml());
		
		for (Node node : nodes) {
			if (!_referenceUse) system.addContent(node.toXml(false));
			else system.addContent(node.toXmlReference());
		}
		return system;
	}
	
	public static void exportSystemModelFile(String _basePath, RcmSystem _system) {
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
			root.addContent(_system.toXml(true));
			
			doc.setRootElement(root);
			
			XMLOutputter outter=new XMLOutputter();
			outter.setXMLOutputProcessor(new OneAttributePerLineXmlProcessor());
			outter.setFormat(Format.getPrettyFormat());
		
			outter.output(doc, new FileWriter(new File(_basePath + "/System.rubusModel")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
