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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Handles XPath replacement.
 * 
 * @author stephen.duncan (Stephen C. Duncan Jr.
 *         &lt;stephen.duncan@gmail.com&gt;)
 * @since 1.0
 */
public class XPathReplacement
{
	/**
	 * Disables object creation.
	 */
	private XPathReplacement()
	{
		// Hide constructor
	}

	/**
	 * Reads the input into a XML Document.
	 * 
	 * @param reader
	 * @param namespaceAware
	 * @return document represented by the input.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private static Document readXml(final Reader reader, final boolean namespaceAware) throws SAXException, IOException, ParserConfigurationException
	{
		final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();

		if(namespaceAware)
		{
			domFactory.setNamespaceAware(true);
		}

		final DocumentBuilder builder = domFactory.newDocumentBuilder();
		return builder.parse(new InputSource(reader));
	}

	/**
	 * Replaces all matches of the xpathExpression with the given value, using
	 * the given reader and writer for input and output.
	 * 
	 * @param xpathExpression
	 *        the expression to match.
	 * @param prefixToNamespace
	 *        the map of namespace prefixes in the xpathExpression to the
	 *        namespace uri's.
	 * @param value
	 *        the value to replace matches with.
	 * @param input
	 *        the reader for the input source.
	 * @param output
	 *        the writer for the output destination.
	 * @throws ParserConfigurationException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws XPathExpressionException
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static void replace(final String xpathExpression, final Map<String, String> prefixToNamespace, final String value, final Reader input, final Writer output) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException
	{
		final Document document = readXml(input, true);

		final XPathFactory factory = XPathFactory.newInstance();
		final XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(new MapNamespaceContext(prefixToNamespace));
		final XPathExpression expression = xpath.compile(xpathExpression);

		replace(expression, value, document);
		writeXml(document, output);
	}

	/**
	 * Replaces all matches of the xpathExpression with the given value, using
	 * the given reader and writer for input and output.
	 * 
	 * @param xpathExpression
	 *        the expression to match.
	 * @param value
	 *        the value to replace matches with.
	 * @param input
	 *        the reader for the input source.
	 * @param output
	 *        the writer for the output destination.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static void replace(final String xpathExpression, final String value, final Reader input, final Writer output) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException
	{
		final Document document = readXml(input, false);

		final XPathFactory factory = XPathFactory.newInstance();
		final XPath xpath = factory.newXPath();
		final XPathExpression expression = xpath.compile(xpathExpression);

		replace(expression, value, document);
		writeXml(document, output);
	}

	/**
	 * Replaces matches of the XPathExpression with the value in the document.
	 * 
	 * @param expression
	 * @param value
	 * @param document
	 * @throws XPathExpressionException
	 */
	private static void replace(final XPathExpression expression, final String value, final Document document) throws XPathExpressionException
	{
		final NodeList nodes = (NodeList)expression.evaluate(document, XPathConstants.NODESET);
		for(int i = 0; i < nodes.getLength(); i++)
		{
			nodes.item(i).setNodeValue(value);
		}
	}

	/**
	 * Writes the XML Document to the Writer.
	 * 
	 * @param document
	 * @param writer
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @throws IOException
	 */
	private static void writeXml(final Document document, final Writer writer) throws TransformerFactoryConfigurationError, TransformerException, IOException
	{
		final Source source = new DOMSource(document);
		final Result result = new StreamResult(writer);
		final Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(source, result);
		writer.flush();
		writer.close();
	}
}