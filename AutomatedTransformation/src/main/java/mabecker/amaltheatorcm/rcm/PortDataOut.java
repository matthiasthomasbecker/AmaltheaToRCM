package mabecker.amaltheatorcm.rcm;

import org.eclipse.app4mc.amalthea.model.Label;
import org.jdom2.Element;

public class PortDataOut extends BaseElement{

	private String 			dataPassingMethod;
	private String			dimension;
	private int				index;
	private	String			valueInitial;
	private ReferenceInst 	referenceInstance;
	private Label 			label;

	public PortDataOut(String _name, Label _label) {
		
		super(_name);
		
		dimension = "";
		index = 0;
		valueInitial = "";
		referenceInstance = null;
		label = _label;
	}
	
	public Label getLabel() {
		return label;
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

	public int getIndex() {
		return index;
	}

	public ReferenceInst getReferenceInstance() {
		return referenceInstance;
	}

	public String getValueInitial() {
		return valueInitial;
	}
	
	public Element toXml() {
		
		Element portDataOut = new Element("PortDataOut");
		
		if (dataPassingMethod != null) portDataOut.setAttribute("DataPassingMethod", dataPassingMethod);
		if (dimension != null) portDataOut.setAttribute("Dimension", dimension);
		portDataOut.setAttribute("Index", Integer.toString(index));
		portDataOut.setAttribute("Name", super.getName());
		portDataOut.setAttribute("UUID", super.getId().toString());
		if (valueInitial != null) portDataOut.setAttribute("ValueInitial", valueInitial);
		
		portDataOut.addContent(super.getNoteElement());
		if (referenceInstance != null) portDataOut.addContent(referenceInstance.toXml());
		
		return portDataOut;
	}
}
