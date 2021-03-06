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

package org.miradi.project;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.martus.util.UnicodeReader;
import org.miradi.exceptions.NotMiradiProjectFileException;
import org.miradi.exceptions.ProjectFileTooNewException;
import org.miradi.exceptions.ProjectFileTooOldException;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.migrations.VersionRange;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.threatrating.RatingValueSet;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.StringUtilities;

abstract public class AbstractProjectLoader 
{
	protected AbstractProjectLoader(final UnicodeReader readerToUse) throws Exception
	{
		reader = readerToUse;
		
		bundleNameToBundleMap = new HashMap<String, ThreatRatingBundle>();
	}
	
	protected void load() throws Exception
	{
		prepareToLoad();
		
		boolean foundEnd = false;
		
		String fileHeaderLine = reader.readLine();
		validateHeaderLine(fileHeaderLine);

		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;

            if(line.equals(StringUtilities.EMPTY_LINE))
                continue;

			if (line.startsWith(MiradiProjectFileUtilities.STOP_MARKER))
			{
				foundEnd = true;
				long lastModified = processStopLine(line);
				setLastModifiedTime(lastModified);
				continue;
			}
			else if(foundEnd)
			{
				throw new IOException("Project file is corrupted (data after end marker)");
			}

			processLine(line);
		}
		
		if(!foundEnd)
			throw new IOException("Project file is corrupted (no end marker found)");
	}

	protected long getLastModified() throws Exception
	{
		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;

			if (line.startsWith(MiradiProjectFileUtilities.STOP_MARKER))
			{
				long lastModified = processStopLine(line);
				return lastModified;
			}
		}

		return 0;
	}

	protected void validateHeaderLine(String fileHeaderLine) throws Exception
	{
		if(fileHeaderLine == null || !fileHeaderLine.startsWith(AbstractMiradiProjectSaver.getBasicFileHeader()))
			throw new NotMiradiProjectFileException();
		
		VersionRange versionRange = loadVersionRange(fileHeaderLine);
		int lowVersion = versionRange.getLowVersion();
		if(lowVersion > Project.VERSION_HIGH)
			throw new ProjectFileTooNewException(lowVersion, Project.VERSION_HIGH);
		
		int highVersion = versionRange.getHighVersion();
		if(highVersion < Project.VERSION_LOW)
			throw new ProjectFileTooOldException(highVersion, Project.VERSION_LOW);
	}
	
	protected VersionRange loadVersionRange(String fileHeaderLine) throws Exception
	{
		final String WHITESPACE_REGEXP = "\\s+";
		String[] parts = fileHeaderLine.split(WHITESPACE_REGEXP);
		/*String baseFileHeader = parts[0];*/
		int lowVersion = Integer.parseInt(parts[1]);
		int highVersion = Integer.parseInt(parts[2]);
		
		return new VersionRange(lowVersion, highVersion);
	}

	private void processLine(String line) throws Exception
	{
		if (line.startsWith(MiradiProjectFileUtilities.UPDATE_PROJECT_VERSION_CODE))
			loadProjectVersionLine(line);

		else if (line.startsWith(MiradiProjectFileUtilities.UPDATE_PROJECT_INFO_CODE))
			loadProjectInfoLine(line);
		
		else if (line.startsWith(MiradiProjectFileUtilities.UPDATE_LAST_MODIFIED_TIME_CODE))
			loadLastModified(line);
		
		else if (line.startsWith(MiradiProjectFileUtilities.CREATE_OBJECT_CODE))
			loadCreateObjectLine(line);
		
		else if (line.startsWith(MiradiProjectFileUtilities.UPDATE_OBJECT_CODE))
			loadUpdateObjectline(line);
		
		else if (line.startsWith(MiradiProjectFileUtilities.CREATE_SIMPLE_THREAT_RATING_BUNDLE_CODE))
			loadCreateSimpleThreatRatingLine(line);
		
		else if (line.startsWith(MiradiProjectFileUtilities.UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE))
			loadUpdateSimpleThreatRatingLine(line);
		
		else if (line.startsWith(MiradiProjectFileUtilities.UPDATE_QUARANTINE_CODE))
			loadQuarantine(line);
		
		else if(line.startsWith(MiradiProjectFileUtilities.UPDATE_EXCEPTIONS_CODE))
			loadExceptions(line);
		
		else
			throw new IOException("Unexpected action: " + line);
	}
	
	private long processStopLine(String stopLine) throws Exception
	{
		final String WHITESPACE_REGEXP = "\\s+";
		String[] parts = stopLine.split(WHITESPACE_REGEXP);
		if(parts.length < 2)
			return 0;
		
		/*String stopMarker = parts[0];*/
		String forComputers = parts[1];
		return Long.parseLong(forComputers);
	}

	private void loadExceptions(String line) throws Exception
	{
		String[] tagValue = parseTagValueLine(line);
		String tag = tagValue[0];
		String value = tagValue[1];
		if(!tag.equals(MiradiProjectFileUtilities.EXCEPTIONS_DATA_TAG))
			throw new Exception("Unknown Exceptions field: " + tag);

		updateExceptionLog(value);
	}

	private void loadQuarantine(String line) throws Exception
	{
		String[] tagValue = parseTagValueLine(line);
		String tag = tagValue[0];
		String value = tagValue[1];
		if(!tag.equals(MiradiProjectFileUtilities.QUARANTINE_DATA_TAG))
			throw new Exception("Unknown Quarantine field: " + tag);

		updateQuarantineFile(value);
	}

	private String[] parseTagValueLine(String line) throws Exception
	{
		final String tag = StringUtilities.substringBetween(line, MiradiProjectFileUtilities.TAB, MiradiProjectFileUtilities.EQUALS);
		final String value = StringUtilities.substringAfter(line, MiradiProjectFileUtilities.EQUALS);;

		return new String[] {tag, value};
	}

	private void loadProjectVersionLine(String line)
	{
	}
	
	private void loadProjectInfoLine(final String line)
	{
		String[] splitLine = line.split(MiradiProjectFileUtilities.TAB);
		String[] tagValue = splitLine[1].split(MiradiProjectFileUtilities.EQUALS);
		String tag = tagValue[0];
		String value = tagValue[1];
		if (tag.equals(ProjectInfo.TAG_PROJECT_METADATA_ID))
			updateProjectMetadataId(value);
		if (tag.equals(ProjectInfo.TAG_HIGHEST_OBJECT_ID))
			updateHighestId(value);
	}

	private void loadLastModified(String line)
	{
	}
	
	private void loadCreateSimpleThreatRatingLine(String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String threatIdTargetIdString = tokenizer.nextToken();
		String[] threatIdTargetIdParts = threatIdTargetIdString.split("-");
		FactorId threatId = new FactorId(Integer.parseInt(threatIdTargetIdParts[0]));
		FactorId targetId = new FactorId(Integer.parseInt(threatIdTargetIdParts[1]));
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, BaseId.INVALID);
		bundleNameToBundleMap.put(threatIdTargetIdString, bundle);
		saveSimpleThreatRatingBundle(bundle);
	}

	private void loadUpdateSimpleThreatRatingLine(String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String threatIdTargetIdString = tokenizer.nextToken();
		ThreatRatingBundle bundleToUpdate = bundleNameToBundleMap.get(threatIdTargetIdString);
		String tag = tokenizer.nextToken(EQUALS_DELIMITER_TAB_PREFIXED);
		String value = tokenizer.nextToken(EQUALS_DELIMITER_NEWLINE_POSTFIXED);
		if (tag.equals(ThreatRatingBundle.TAG_VALUES))
		{
			RatingValueSet ratings = new RatingValueSet();
			ratings.fillFrom(value);
			bundleToUpdate.setRating(ratings);
		}
		if (tag.equals(ThreatRatingBundle.TAG_DEFAULT_VALUE_ID))
		{
			bundleToUpdate.setDefaultValueId(new BaseId(Integer.parseInt(value)));
		}
	}

	private void loadCreateObjectLine(String line) throws Exception
	{
		ORef ref = extractRefFromLine(line);
		createObject(ref);
	}

	public static ORef extractRefFromLine(String line)
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String refString = tokenizer.nextToken();
		
		return extractRef(refString);
	}

	private void loadUpdateObjectline(String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String refString = tokenizer.nextToken();
		ORef ref = extractRef(refString);
		String tag = tokenizer.nextToken(EQUALS_DELIMITER_TAB_PREFIXED);
		final boolean hasData = tokenizer.hasMoreTokens();
		if (hasData)
		{
			String value = StringUtilities.substringAfter(line, EQUALS_DELIMITER);
			updateObjectWithData(ref, tag, value);
		}
	}

	public static ORef extractRef(String refString)
	{
		String[] refParts = refString.split(":");
		int objectType = Integer.parseInt(refParts[0]);
		BaseId objectId = new BaseId(Integer.parseInt(refParts[1]));
		
		return new ORef(objectType, objectId);
	}
	
	protected UnicodeReader getStringReader()
	{
		return reader;
	}

	abstract protected void setLastModifiedTime(long lastModified);
	
	abstract protected void saveSimpleThreatRatingBundle(ThreatRatingBundle bundle) throws Exception;
	
	abstract protected void updateHighestId(String value);

	abstract protected void updateProjectMetadataId(String value);
	
	abstract protected void updateExceptionLog(String value) throws Exception;

	abstract protected void updateQuarantineFile(String value) throws Exception;

	abstract protected void prepareToLoad() throws Exception;

	abstract protected void createObject(ORef ref) throws Exception;
	
	abstract protected void updateObjectWithData(ORef ref, String tag, String value)	throws Exception;
	
	private HashMap<String, ThreatRatingBundle> bundleNameToBundleMap;
	private UnicodeReader reader;
	
	public static final String EQUALS_DELIMITER_TAB_PREFIXED = " \t=";
	private static final String EQUALS_DELIMITER_NEWLINE_POSTFIXED = "=\n";
	public static final String EQUALS_DELIMITER = "=";
}
