/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.DimensionData;
import org.conservationmeasures.eam.objectdata.ORefData;
import org.conservationmeasures.eam.objectdata.PointData;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class DiagramFactor extends BaseObject
{
	public DiagramFactor(ObjectManager objectManager, DiagramFactorId diagramFactorIdToUse, CreateDiagramFactorParameter extraInfo) throws Exception
	{
		super(objectManager, diagramFactorIdToUse);
		
		clear();
		setData(TAG_WRAPPED_REF, extraInfo.getFactorRef().toString());
		size.setDimension(getDefaultSize());
	}
	
	public DiagramFactor(DiagramFactorId diagramFactorIdToUse, CreateDiagramFactorParameter extraInfo)
	{
		super(diagramFactorIdToUse);
		
		clear();
		underlyingObjectRef.set(extraInfo.getFactorRef());
		size.setDimension(getDefaultSize());
	}
	
	public DiagramFactor(ObjectManager objectManager, int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new DiagramFactorId(idToUse), json);
		
		ORef wrappedRef = ORef.createFromString(json.getString(TAG_WRAPPED_REF));
		underlyingObjectRef.set(wrappedRef);
	}
	
	public DiagramFactor(int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(new DiagramFactorId(idToUse), json);
		
		ORef wrappedRef = ORef.createFromString(json.getString(TAG_WRAPPED_REF));
		underlyingObjectRef.set(wrappedRef);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject jsonObject = super.toJson();
		jsonObject.put(TAG_WRAPPED_REF, underlyingObjectRef.toString());
		
		return jsonObject;
	}
	
	public static Dimension getDefaultSize(int type)
	{
		if (type == ObjectType.TEXT_BOX)
			return new Dimension(180, 30);
		
		return getDefaultSize();
	}
	
	public static Dimension getDefaultSize()
	{
		return new Dimension(120, 60);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.DIAGRAM_FACTOR;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.CAUSE: 
				return true;
			case ObjectType.STRATEGY: 
				return true;
			case ObjectType.TARGET: 
				return true;
			case ObjectType.INTERMEDIATE_RESULT:
				return true;
			case ObjectType.THREAT_REDUCTION_RESULT:
				return true;
			case ObjectType.TEXT_BOX:
				return true;
				
			default:
				return false;
		}
	}
	
	public Set<String> getReferencedObjectTags()
	{
		Set set = super.getReferencedObjectTags();
		set.add(TAG_WRAPPED_REF);
		return set;
	}
		
	public DiagramFactorId getDiagramFactorId()
	{
		return (DiagramFactorId)getId(); 
	}
	
	public int getWrappedType()
	{
		return getWrappedORef().getObjectType();
	}
	
	public FactorId getWrappedId()
	{
		return new FactorId(getWrappedORef().getObjectId().asInt());
	}
	
	public ORef getWrappedORef()
	{
		return underlyingObjectRef.getRawRef();
	}

	public Dimension getSize()
	{
		return size.getDimension();
	}
	
	public Point getLocation()
	{
		return location.getPoint();
	}
	
	public String getFontSize()
	{
		return fontSize.get();
	}
	
	public String getFontColor()
	{
		return fontColor.get();
	}
	
	public void setLocation(Point pointToUse)
	{
		location.setPoint(pointToUse);
	}
	
	public void setSize(Dimension dimensionToUse)
	{
		size.setDimension(dimensionToUse);
	}
	
	public Command[] loadDataFromJson(EnhancedJsonObject json) throws Exception
	{
		Vector commands = new Vector();
		String dimensionAsString = json.getString(DiagramFactor.TAG_SIZE);
		CommandSetObjectData setSizeCommand = new CommandSetObjectData(getRef(), DiagramFactor.TAG_SIZE, dimensionAsString);
		commands.add(setSizeCommand);
		
		String locationAsString = json.getString(DiagramFactor.TAG_LOCATION);
		CommandSetObjectData setLocationCommand = new CommandSetObjectData(getRef(), DiagramFactor.TAG_LOCATION, locationAsString);
		commands.add(setLocationCommand);
		
		String fontColorAsString = json.optString(TAG_FONT_COLOR);
		CommandSetObjectData setFontColorCommand = new CommandSetObjectData(getRef(), DiagramFactor.TAG_FONT_COLOR, fontColorAsString);
		commands.add(setFontColorCommand);

		String fontSizeAsString = json.optString(TAG_FONT_SIZE);
		CommandSetObjectData setFontSizeCommand = new CommandSetObjectData(getRef(), DiagramFactor.TAG_FONT_SIZE, fontSizeAsString);
		commands.add(setFontSizeCommand);
		
		return (Command[]) commands.toArray(new Command[0]);
	}
	
	public Command[] createCommandsToMirror(DiagramFactorId newlyCreatedId)
	{
		Vector commands = new Vector();
		String sizeAsString = EnhancedJsonObject.convertFromDimension(getSize());
		CommandSetObjectData setSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_SIZE, sizeAsString);
		commands.add(setSizeCommand);
		
		String locationAsString = EnhancedJsonObject.convertFromPoint(getLocation());
		CommandSetObjectData setLocationCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_LOCATION, locationAsString);
		commands.add(setLocationCommand);
		
		CommandSetObjectData setFontSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_FONT_SIZE, fontSize.get());
		commands.add(setFontSizeCommand);
		
		CommandSetObjectData setFontColorCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_FONT_COLOR, fontColor.get());
		commands.add(setFontColorCommand);
		
		return (Command[]) commands.toArray(new Command[0]);
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateDiagramFactorParameter(getWrappedORef());
	}
	
	void clear()
	{
		super.clear();

		size = new DimensionData();
		location = new PointData();
		underlyingObjectRef = new ORefData();
		fontSize = new ChoiceData();
		fontColor = new ChoiceData();
		
		addField(TAG_SIZE, size);
		addField(TAG_LOCATION, location);
		addField(TAG_WRAPPED_REF, underlyingObjectRef);
		addField(TAG_FONT_SIZE, fontSize);
		addField(TAG_FONT_COLOR, fontColor);
	}
	
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_SIZE = "Size";
	public static final String TAG_WRAPPED_REF = "WrappedFactorRef";
	public static final String TAG_FONT_SIZE = "FontSize";
	public static final String TAG_FONT_COLOR = "FontColor";
	
	static final String OBJECT_NAME = "DiagramFactor";
	
	private DimensionData size;
	private PointData location;
	private ORefData underlyingObjectRef;
	private ChoiceData fontSize;
	private ChoiceData fontColor;
}
