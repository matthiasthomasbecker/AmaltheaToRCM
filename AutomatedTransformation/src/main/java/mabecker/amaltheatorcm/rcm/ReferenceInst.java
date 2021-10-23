/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class ReferenceInst extends BaseElement{

	private String	path;
	private String	reference;
	
	public ReferenceInst(String _name) {
		
		super(_name);
		path = "";
		reference = "";
	}
	
	public String getPath() {
		return path;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	@Override
	public Element toXml() {
		
		Element referenceInstance = new Element("ReferenceInst");
		
		referenceInstance.setAttribute("Name", super.getName());
		referenceInstance.setAttribute("Path", path);
		referenceInstance.setAttribute("Reference", reference);
		referenceInstance.setAttribute("UUID", super.getId().toString());
		
		referenceInstance.addContent(super.getNotes());
	
		return referenceInstance;
	}
}
