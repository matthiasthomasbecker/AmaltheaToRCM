/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class CrossRef extends BaseElement{

	/**
	 * Properties
	 */
	private String reference;
	
	public CrossRef(String _name) {
		
		super(_name);
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReference() {
		return reference;
	}
	
	@Override
	public Element toXml() {
		
		Element crossref = new Element("CrossRef");
		
		crossref.setAttribute("Name", super.getName());
		crossref.setAttribute("Reference", reference);
		crossref.setAttribute("UUID", super.getId().toString());
		
		crossref.addContent(super.getNoteElement());
		
		return crossref;
	}
}
