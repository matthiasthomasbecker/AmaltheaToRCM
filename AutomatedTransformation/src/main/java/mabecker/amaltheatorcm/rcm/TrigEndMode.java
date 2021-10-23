/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class TrigEndMode extends BaseElement{

	/**
	 * Properties
	 */
	private String entry;
	
	/**
	 * Elements
	 */
	private PortTrigIn trigIn;
	
	public TrigEndMode(String _name) {

		super(_name);
		entry = "";
		
		trigIn = new PortTrigIn("TI1");
		trigIn.setIndex(0);
	}
	
	@Override
	public Element toXml() {
		Element trigStartMode = new Element("TrigEndMode");
		
		trigStartMode.setAttribute("Entry", entry);
		trigStartMode.setAttribute("Name", super.getName());
		trigStartMode.setAttribute("UUID", super.getId().toString());
		
		trigStartMode.addContent(super.getNoteElement());
		
		trigStartMode.addContent(trigIn.toXml());
		return trigStartMode;
	}

	public String getTrigRef() {
		return super.getName() + "\\" + trigIn.getName();
	}
}
