/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.generic;

import java.io.IOException;
import java.util.Vector;


class ObjectSchemaElement extends SchemaElement
{
	public ObjectSchemaElement(String objectTypeNameToUse)
	{
		objectTypeName = objectTypeNameToUse;
		attributes = new Vector<AttributeSchemaElement>();
		fields = new Vector<FieldSchemaElement>();
	}
	
	public void createIdAttribute(String attributeNameToUse)
	{
		AttributeSchemaElement attribute = new IdAttributeSchemaElement();
		attributes.add(attribute);
	}
	
	public void createTextField(String fieldNameToUse)
	{
		FieldSchemaElement field = new TextFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	public void createOptionalTextField(String fieldNameToUse)
	{
		FieldSchemaElement field = new OptionalTextFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}

	public void createOptionalNumericField(String fieldNameToUse)
	{
		FieldSchemaElement field = new OptionalNumericFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}

	public void createOptionalDateField(String fieldNameToUse)
	{
		FieldSchemaElement field = new OptionalDateFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createDateUnitEffortsField(String fieldNameToUse)
	{
		FieldSchemaElement field = new DateUnitEffortsFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createDateUnitExpenseField(String fieldNameToUse)
	{
		FieldSchemaElement field = new DateUnitExpenseFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}

	protected void createThreatTargetThreatRatingField()
	{
		FieldSchemaElement field = new ThreatTargetThreatRatingFieldSchemaElement(getObjectTypeName());
		fields.add(field);
	}
	
	public void createThreatReductionResultsThreatIdField(String fieldNameToUse)
	{
		FieldSchemaElement field = new ThreatReductionResultsThreatIdSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	public void createFundingSourceIdField(String fieldNameToUse)
	{
		FieldSchemaElement field = new FundingSourceIdSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	public void createAccountingCodeIdField(String fieldNameToUse)
	{
		FieldSchemaElement field = new AccountingCodeIdSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}

	protected void createWrappedByDiagramFactorIdField(String fieldNameToUse)
	{
		FieldSchemaElement field = new WrappedByDiagramFactorIdFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createWrappedByDiagramLinkIdField(String fieldNameToUse)
	{
		FieldSchemaElement field = new WrappedByDiagramLinkIdFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createLinkableFactorIdField(String fieldNameToUse)
	{
		FieldSchemaElement field = new LinkableFactorIdFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createIdField(String fieldNameToUse, String idNameToUse)
	{
		FieldSchemaElement field = new IdFieldSchemaElement(getObjectTypeName(), fieldNameToUse, idNameToUse);
		fields.add(field);
	}
	
	protected void createGeospatialLocationField(String fieldNameToUse)
	{
		FieldSchemaElement field = new LocationFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createDiagramPointField(String fieldNameToUse)
	{
		FieldSchemaElement field = new DiagramPointFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createDiagramPointListField(String fieldNameToUse)
	{
		FieldSchemaElement field = new DiagramPointListFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createDiagramSizeField(String fieldNameToUse)
	{
		FieldSchemaElement field = new DiagramSizeFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createIdListField(String fieldNameToUse, String storedObjectTypeName)
	{
		FieldSchemaElement field = new IdListFieldSchemaElement(getObjectTypeName(), fieldNameToUse, storedObjectTypeName);
		fields.add(field);
	}

	protected void createCodeListField(String fieldNameToUse)
	{
		FieldSchemaElement field = new CodeListFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createCodeField(String fieldNameToUse, String vocabularyNameToUse)
	{
		FieldSchemaElement field = new CodeFieldSchemaElement(getObjectTypeName(), fieldNameToUse, vocabularyNameToUse);
		fields.add(field);
	}
	
	protected void createBooleanField(String fieldNameToUse)
	{
		FieldSchemaElement field = new BooleanFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createFiscalYearStartField(String fieldNameToUse)
	{
		FieldSchemaElement field = new FiscalYearStartFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}

	public String getObjectTypeName()
	{
		return objectTypeName;
	}

	@Override
	public void output(SchemaWriter writer) throws IOException
	{
		writer.defineAlias(getDotElement(getObjectTypeName()), "element miradi:" + getObjectTypeName());
		writer.startBlock();
		for(int i = 0; i < attributes.size(); ++i)
		{
			AttributeSchemaElement attributeElement = attributes.get(i);
			attributeElement.output(writer);
			if(i < attributes.size() - 1)
				writer.println(" &");
		}
		
		if(attributes.size() > 0 && fields.size() > 0)
			writer.printlnIndented("&");
		
		for(int i = 0; i < fields.size(); ++i)
		{
			FieldSchemaElement fieldElement = fields.get(i);
			fieldElement.output(writer);
			if(i < fields.size() - 1)
				writer.print(" &");
			
			writer.println();
		}
		writer.endBlock();
	}
	
	public boolean isContainer()
	{
		return false;
	}
	
	private String objectTypeName;
	private Vector<AttributeSchemaElement> attributes;
	private Vector<FieldSchemaElement> fields;
}