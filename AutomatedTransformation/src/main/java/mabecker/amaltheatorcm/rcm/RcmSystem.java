/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import java.util.ArrayList;
import org.jdom2.Element;

public class RcmSystem extends BaseElement{

	/**
	 * Elements
	 */
	private ArrayList<Node> 	nodes;
	
	public RcmSystem(String _name) {
		
		super(_name);
		nodes = new ArrayList<Node>();
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	@Override
	public Element toXml() {
		
		Element system = new Element("System");
		system.setAttribute("Name", super.getName());
		system.setAttribute("UUID", super.getId().toString());
		
		system.addContent(super.getNoteElement());
		
		for (Node node : nodes) {
			system.addContent(node.toXml());
		}
		return system;
	}
}
