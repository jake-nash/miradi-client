/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objects;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.utils.EnhancedJsonObject;

public class DiagramFactor extends BaseObject
{
	public DiagramFactor(ObjectManager objectManager, DiagramFactorId diagramFactorIdToUse) throws Exception
	{
		super(objectManager, diagramFactorIdToUse, new DiagramFactorSchema());
		
		setDimensionData(TAG_SIZE, getDefaultSize());
	}
	
	public DiagramFactor(ObjectManager objectManager, int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new DiagramFactorId(idToUse), json, new DiagramFactorSchema());
		
		ORef wrappedRef = ORef.createFromString(json.getString(TAG_WRAPPED_REF));
		setRefData(TAG_WRAPPED_REF, wrappedRef);
	}
	
	@Override
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject jsonObject = super.toJson();
		jsonObject.put(TAG_WRAPPED_REF, getWrappedORef().toString());
		
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
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			ConceptualModelDiagramSchema.getObjectType(), 
			ResultsChainDiagramSchema.getObjectType()
			};
	}
	
	@Override
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getGroupBoxChildrenRefs());
		
		return deepObjectRefsToCopy;
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
		return getRefData(TAG_WRAPPED_REF);
	}
	
	public Factor getWrappedFactor()
	{
		return Factor.findFactor(getObjectManager(), getWrappedORef());
	}

	public Dimension getSize()
	{
		return getDimensionData(TAG_SIZE);
	}
	
	public Point getLocation()
	{
		return getPointData(TAG_LOCATION);
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle(getLocation().x, getLocation().y, getSize().width, getSize().height);
	}
	
	public String getFontSize()
	{
		return getStringData(TAG_FONT_SIZE);
	}
	
	public String getFontColor()
	{
		return getStringData(TAG_FOREGROUND_COLOR);
	}
	
	public String getFontStyle()
	{
		return getStringData(TAG_FONT_STYLE);
	}
	
	public String getBackgroundColor()
	{
		return getStringData(TAG_BACKGROUND_COLOR);
	}
	
	public void setLocation(Point pointToUse)
	{
		setPointData(TAG_LOCATION, pointToUse);
	}
	
	public void setSize(Dimension dimensionToUse)
	{
		setDimensionData(TAG_SIZE, dimensionToUse);
	}
	
	public ORefList getGroupBoxChildrenRefs()
	{
		return getRefListData(TAG_GROUP_BOX_CHILDREN_REFS);
	}
	
	public ORefSet getGroupBoxChildrenSet()
	{
		return new ORefSet(getGroupBoxChildrenRefs());
	}
	
	public ORef getOwningGroupBoxRef()
	{
		ORefList diagramFactorReferers = findObjectsThatReferToUs();
		ORef groupBoxDiagramFactorRef = diagramFactorReferers.getRefForType(DiagramFactorSchema.getObjectType());

		return groupBoxDiagramFactorRef;
	}
	
	public ORefList getSelfAndChildren()
	{
		ORefList selfAndChildren = new ORefList(getRef());
		if (isGroupBoxFactor())
			selfAndChildren.addAll(getGroupBoxChildrenRefs());
		
		return selfAndChildren;
	}
	
	public ORefList getSelfOrChildren()
	{
		if (isGroupBoxFactor())
			return getGroupBoxChildrenRefs();
		
		return new ORefList(getRef());
	}
	
	@Override
	public String getFullName()
	{
		Factor wrappedFactor = Factor.findFactor(getObjectManager(), getWrappedORef());
		if(wrappedFactor == null)
			return EAM.text("(Unknown)");
		return wrappedFactor.getFullName();
	}
	
	public boolean isGroupBoxFactor()
	{
		if (getWrappedType() == GroupBoxSchema.getObjectType())
			return true;
		
		return false;
	}
	
	public ORefList findDiagramsThatReferToUs()
	{
		ORefList result = new ORefList();
		result.addAll(findObjectsThatReferToUs(ConceptualModelDiagramSchema.getObjectType()));
		result.addAll(findObjectsThatReferToUs(ResultsChainDiagramSchema.getObjectType()));
		return result;
	}

	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_GROUP_BOX_CHILDREN_REFS))
			return GroupBoxSchema.getObjectType();
		
		return super.getAnnotationType(tag);
	}
	
	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_GROUP_BOX_CHILDREN_REFS))
			return true;
		
		return super.isRefList(tag);
	}
	
	public Command[] createCommandsToMirror(DiagramFactorId newlyCreatedId)
	{
		Vector<CommandSetObjectData> commands = new Vector<CommandSetObjectData>();
		String sizeAsString = EnhancedJsonObject.convertFromDimension(getSize());
		CommandSetObjectData setSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_SIZE, sizeAsString);
		commands.add(setSizeCommand);
		
		String locationAsString = EnhancedJsonObject.convertFromPoint(getLocation());
		CommandSetObjectData setLocationCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_LOCATION, locationAsString);
		commands.add(setLocationCommand);
		
		CommandSetObjectData setFontSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_FONT_SIZE, getStringData(TAG_FONT_SIZE));
		commands.add(setFontSizeCommand);
		
		CommandSetObjectData setFontColorCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_FOREGROUND_COLOR, getStringData(TAG_FOREGROUND_COLOR));
		commands.add(setFontColorCommand);
		
		CommandSetObjectData setFontStyleCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_FONT_STYLE, getStringData(TAG_FONT_STYLE));
		commands.add(setFontStyleCommand);
		
		CommandSetObjectData setBackgroundColorCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_BACKGROUND_COLOR, getStringData(TAG_BACKGROUND_COLOR));
		commands.add(setBackgroundColorCommand);
		
		return commands.toArray(new Command[0]);
	}
	
	public boolean isCoveredByGroupBox()
	{
		ORefList groupBoxFactors = findObjectsThatReferToUs(DiagramFactorSchema.getObjectType());
		return (groupBoxFactors.size() > 0);
	}
	
	public static void ensureType(ORef diagramFactorRef)
	{
		if (!is(diagramFactorRef))
			throw new RuntimeException(diagramFactorRef + " is not of type DiagramFactor");
	}
	
	public boolean isDefaultZOrder()
	{
		String zOrderCode = getStringData(TAG_TEXT_BOX_Z_ORDER_CODE);
		return zOrderCode.equals(TextBoxZOrderQuestion.DEFAULT_Z_ORDER);
	}
		
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == DiagramFactorSchema.getObjectType();
	}
	
	public static DiagramFactor find(ObjectManager objectManager, ORef diagramFactorRef)
	{
		return (DiagramFactor) objectManager.findObject(diagramFactorRef);
	}
	
	public static DiagramFactor find(Project project, ORef diagramFactorRef)
	{
		return find(project.getObjectManager(), diagramFactorRef);
	}
	
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_SIZE = "Size";
	public static final String TAG_WRAPPED_REF = "WrappedFactorRef";
	public static final String TAG_FONT_SIZE = "FontSize";
	public static final String TAG_FOREGROUND_COLOR = "FontColor";
	public static final String TAG_FONT_STYLE = "FontStyle";
	public static final String TAG_GROUP_BOX_CHILDREN_REFS = "GroupBoxChildrenRefs";
	public static final String TAG_BACKGROUND_COLOR = "BackgroundColor";
	public static final String TAG_TEXT_BOX_Z_ORDER_CODE = "TextBoxZOrderCode";
	
	public static final Dimension DEFAULT_STRESS_SIZE = new Dimension(60, 30);
	public static final Dimension DEFAULT_ACTIVITY_SIZE = new Dimension(60, 30);
}
