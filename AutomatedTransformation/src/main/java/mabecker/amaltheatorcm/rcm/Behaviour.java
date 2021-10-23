/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

/**
 * Behaviour RCM element
 * @author mabecker
 *
 */
public class Behaviour extends BaseElement{

	private String entry;
	private Runtime runtime;
	
	public Behaviour(String _name) {
		
		super(_name);
		
		entry = _name;
	}
	
	public void setRuntime(Runtime runtime) {
		this.runtime = runtime;
	}
	
	public Runtime getRuntime() {
		return runtime;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}
	
	public String getEntry() {
		return entry;
	}
	
	@Override
	public Element toXml() {
		
		Element behaviour = new Element("Behaviour");
		
		behaviour.setAttribute("Entry", entry);
		behaviour.setAttribute("Name", super.getName());
		behaviour.setAttribute("UUID", super.getId().toString());
		
		behaviour.addContent(super.getNoteElement());
		
		behaviour.addContent(runtime.toXml());
		
		return behaviour;
	}
}
