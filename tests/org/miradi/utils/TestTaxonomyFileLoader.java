/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.utils;

import java.io.BufferedReader;
import java.io.StringReader;

import org.miradi.main.MiradiTestCase;
import org.miradi.objecthelpers.TaxonomyFileLoader;
import org.miradi.objecthelpers.TwoLevelEntry;

public class TestTaxonomyFileLoader extends MiradiTestCase
{

	public TestTaxonomyFileLoader(String name)
	{
		super(name);
	}

	public void testNoDataReturnsDefautSelection() throws Exception
	{
		TwoLevelEntry[] twoLevelItem = loadDelimitedData(new StringReader(""));
		assertEquals(1, twoLevelItem.length);
	}

	public void testOne() throws Exception
	{
		TwoLevelEntry[] twoLevelItem = loadDelimitedData(new StringReader("# \t \"header\" \t \"xxxx\" \n H10.10 \t \"my level 1 descriptor\"  \t  \"my level 2 descriptor\" \t Descriptor 1 \t descriptor 2 \t someExample "));
		assertEquals(3, twoLevelItem.length);
		assertEquals("Not Specified", twoLevelItem[0].getEntryLabel());
		assertEquals("H10", twoLevelItem[1].getEntryCode());		
		assertEquals(true, twoLevelItem[1].getEntryLabel().endsWith("my level 1 descriptor"));
		assertEquals("H10.10", twoLevelItem[2].getEntryCode());
		assertEquals(true, twoLevelItem[2].getEntryLabel().endsWith("my level 2 descriptor"));
	}
	

	private TwoLevelEntry[] loadDelimitedData(StringReader stringReader) throws Exception
	{
		BufferedReader reader = new BufferedReader( stringReader );
		TwoLevelEntry[] twoLevelItem = new TaxonomyFileLoader("").load(reader);
		return twoLevelItem;
	}


}
