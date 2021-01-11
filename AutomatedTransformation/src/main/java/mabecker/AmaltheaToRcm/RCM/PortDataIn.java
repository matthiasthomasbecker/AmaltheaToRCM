package mabecker.AmaltheaToRcm.RCM;

import java.util.UUID;

import org.eclipse.app4mc.amalthea.model.Label;
import org.jdom2.Element;

public class PortDataIn {

	private String 			dataPassingMethod;
	private String			dimension;
	private int				index;
	private String			name;
	private	UUID			id;
	private	String			valueInitial;
	private Notes			notes;
	private ReferenceInst 	referenceInstance;
	private Label 			label;
	
	public PortDataIn(String _name, Label _label) {
		name = _name;
		dimension = "";
		index = 0;
		id = UUID.randomUUID();
		valueInitial = "";
		notes = new Notes("");
		referenceInstance = null;
		label = _label;
	}
	
	public Label getLabel() {
		return label;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDataPassingMethod(String dataPassingMethod) {
		this.dataPassingMethod = dataPassingMethod;
	}
	
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setNotes(Notes notes) {
		this.notes = notes;
	}
	
	public void setReferenceInstance(ReferenceInst referenceInstance) {
		this.referenceInstance = referenceInstance;
	}
	
	public void setValueInitial(String valueInitial) {
		this.valueInitial = valueInitial;
	}
	
	public String getDataPassingMethod() {
		return dataPassingMethod;
	}
	
	public String getDimension() {
		return dimension;
	}
	
	public UUID getId() {
		return id;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Notes getNotes() {
		return notes;
	}
	
	public ReferenceInst getReferenceInstance() {
		return referenceInstance;
	}
	
	public String getValueInitial() {
		return valueInitial;
	}
	
	public Element toXml() {
		
		Element portDataIn = new Element("PortDataIn");
		
		if (dataPassingMethod != null) portDataIn.setAttribute("DataPassingMethod", dataPassingMethod);
		if (dimension != null) portDataIn.setAttribute("Dimension", dimension);
		portDataIn.setAttribute("Index", Integer.toString(index));
		portDataIn.setAttribute("Name", name);
		portDataIn.setAttribute("UUID", id.toString());
		if (valueInitial != null) portDataIn.setAttribute("ValueInitial", valueInitial);
		
		portDataIn.addContent(notes.toXml());
		if (referenceInstance != null) portDataIn.addContent(referenceInstance.toXml());
		
		return portDataIn;
	}
}
