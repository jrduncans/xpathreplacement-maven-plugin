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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.NamespaceContext;

/**
 * NamespaceContext implemented by a map backend.
 * 
 * @author stephen.duncan (Stephen C. Duncan Jr.
 *         &lt;stephen.duncan@gmail.com&gt;)
 * @since 1.0
 */
public class MapNamespaceContext implements NamespaceContext
{
	/** Map of prefixes to namespaces. */
	private final Map<String, String> prefixToNamespace;

	/**
	 * @param prefixToNamespace
	 */
	public MapNamespaceContext(final Map<String, String> prefixToNamespace)
	{
		this.prefixToNamespace = prefixToNamespace;
	}

	/**
	 * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
	 */
	public String getNamespaceURI(final String prefix)
	{
		return this.prefixToNamespace.get(prefix);
	}

	/**
	 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
	 */
	public String getPrefix(final String namespaceURI)
	{
		for(Entry<String, String> entry : this.prefixToNamespace.entrySet())
		{
			if(entry.getValue().equals(namespaceURI))
			{
				return entry.getKey();
			}
		}

		return null;
	}

	/**
	 * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
	 */
	public Iterator<String> getPrefixes(final String namespaceURI)
	{
		final Collection<String> prefixes = new LinkedList<String>();

		for(Entry<String, String> entry : this.prefixToNamespace.entrySet())
		{
			if(entry.getValue().equals(namespaceURI))
			{
				prefixes.add(entry.getKey());
			}
		}

		return prefixes.iterator();
	}
}