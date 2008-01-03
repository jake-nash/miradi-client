/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.main.EAM;

public class BudgetTimePeriodQuestion extends StaticChoiceQuestion
{
	public BudgetTimePeriodQuestion(String tagToUse)
	{
		super(tagToUse, EAM.text("Label|Budget Time Period"), getStaticChoices());
	}

	static ChoiceItem[] getStaticChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem(BUDGET_BY_QUARTER_CODE, EAM.text("By Quarter")),	
			new ChoiceItem(BUDGET_BY_YEAR_CODE, EAM.text("By Year")),	
		};
	}
	public static final String BUDGET_BY_QUARTER_CODE = "";
	public static final String BUDGET_BY_YEAR_CODE = "YEARLY";
}
