/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import java.util.UUID;

import org.jdom2.CDATA;
import org.jdom2.Element;

/**
 * This is the base class that contains elements shared by all RCM elements.
 * @author mabecker
 *
 */
public abstract class BaseElement {

	private String name;	/* name of this element */
	private UUID id;		/* unique ID */
	private String notes;	/* comments */
	
	public BaseElement(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = ".";
	}

	public String getName() {
		return name;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Element getNoteElement() {
		Element note = new Element("Notes");
		note.addContent(new CDATA(notes));
		return note;
	}
	
	public abstract Element toXml();
}
