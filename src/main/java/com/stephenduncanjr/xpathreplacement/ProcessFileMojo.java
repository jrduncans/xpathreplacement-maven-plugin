/*
 * Copyright 2006 Stephen Duncan Jr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.stephenduncanjr.xpathreplacement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Replaces matches of an XPath expression in a file.
 * 
 * @author stephen.duncan (Stephen C. Duncan Jr.
 *         &lt;stephen.duncan@gmail.com&gt;)
 * @since 1.0
 * 
 * @goal process-file
 * @phase process-resources
 */
public class ProcessFileMojo extends AbstractMojo
{
	/**
	 * File to make replacements to.
	 * 
	 * @parameter expression="${xpathReplacement.input}"
	 * @required
	 */
	private File inputFile;

	/**
	 * Mapping of prefixes to namespaces in the XPath expression.
	 * 
	 * @parameter
	 */
	private Map<String, String> namespaceMappings;

	/**
	 * Directory to place output in.
	 * 
	 * @parameter expression="${xpathReplacement.outputDirectory}"
	 *            default-value="${project.build.outputDirectory}"
	 */
	private File outputDirectory;

	/**
	 * Value to replace matches with.
	 * 
	 * @parameter expression="${xpathReplacement.value}
	 * @required
	 */
	private String value;

	/**
	 * XPath expression to replace match.
	 * 
	 * @parameter expression="${xpathReplacement.xpath}
	 * @required
	 */
	private String xpath;

	/**
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		final File outputFile = new File(this.outputDirectory.getAbsolutePath() + File.separator + this.inputFile.getName());

		try
		{
			if(this.namespaceMappings == null)
			{
				XPathReplacement.replace(this.xpath, this.value, new FileReader(this.inputFile), new FileWriter(outputFile));
			}
			else
			{
				XPathReplacement.replace(this.xpath, this.namespaceMappings, this.value, new FileReader(this.inputFile), new FileWriter(outputFile));
			}
		}
		catch(final Exception e)
		{
			throw new MojoExecutionException("Error performing replacement. Reason: " + e.getMessage(), e);
		}
	}
}