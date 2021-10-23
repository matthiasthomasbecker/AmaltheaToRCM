/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class PortTrigIn extends BaseElement{

	/**
	 * Properties
	 */
	private int index;
	
	public PortTrigIn(String _name) {
		super(_name);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public Element toXml() {
		
		Element portTrigIn = new Element("PortTrigIn");
		
		portTrigIn.setAttribute("Index", Integer.toString(index));
		portTrigIn.setAttribute("Name", super.getName());
		portTrigIn.setAttribute("UUID", super.getId().toString());
		
		portTrigIn.addContent(super.getNoteElement());
		
		return portTrigIn;
	}
}
