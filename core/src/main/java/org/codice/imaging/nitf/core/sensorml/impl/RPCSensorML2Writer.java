package org.codice.imaging.nitf.core.sensorml.impl;
/*
 * @author Gobe Hobona - Open Geospatial Consortium (OGC)
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 */

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.header.impl.NitfParser;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RPCSensorML2Writer {

	/*
	 *  This method takes in a NITF file, and if that file contains an RPC00B TRE, the method exports the RPC00B fields out to an OGC SensorML 2 document.
	 * 
	 */
	public void write(FileInputStream is, FileOutputStream os)
			throws NitfFormatException, ParserConfigurationException, IOException, TransformerException {
		
		SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.HEADERS_ONLY);
		NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(is));
		NitfParser.parse(reader, parseStrategy);

		NitfHeader nitfFileHeader = parseStrategy.getNitfHeader();

		ImageSegment image = parseStrategy.getDataSource().getImageSegments().get(0);
		TreCollection tres = image.getTREsRawStructure();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element root = doc.createElement("sml:SimpleProcess");
		root.setAttribute("xmlns:sml", "http://www.opengis.net/sensorml/2.0");
		root.setAttribute("xmlns:gml", "http://www.opengis.net/gml/3.2");

		root.setAttribute("gml:id", "model.1");
		root.setAttribute("xmlns:swe", "http://www.opengis.net/swe/2.0");

		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
		root.setAttribute("xsi:schemaLocation",
				"http://www.opengis.net/sensorml/2.0 http://schemas.opengis.net/sensorML/2.0/sensorML.xsd");
		root.setAttribute("definition", "http://www.opengis.net/def/sensor-model/NTB/2.1/NITF/RPC00B");

		doc.appendChild(root);

		Element gmlIdentifier = doc.createElement("gml:identifier");
		gmlIdentifier.setAttribute("codeSpace", "uid");
		gmlIdentifier.setTextContent("urn:ogc:sensor-model:ntb-nitf:2.1:RPC00B");
		root.appendChild(gmlIdentifier);

		Element identification = doc.createElement("sml:identification");

		Element identifierList = doc.createElement("sml:IdentifierList");

		Element smlIdentifier = doc.createElement("sml:identifier");

		Element term = doc.createElement("sml:Term");
		Element smlLabel1 = doc.createElement("sml:label");
		smlLabel1.setTextContent("TRE");
		Element smlValue1 = doc.createElement("sml:value");
		smlValue1.setTextContent("RPC00B");
		term.appendChild(smlLabel1);
		term.appendChild(smlValue1);

		smlIdentifier.appendChild(term);
		identifierList.appendChild(smlIdentifier);
		identification.appendChild(identifierList);

		root.appendChild(identification);

		Element classification = doc.createElement("sml:classification");
		Element classifierList = doc.createElement("sml:ClassifierList");
		Element classifier = doc.createElement("sml:classifier");
		Element classifierTerm = doc.createElement("sml:Term");
		classifierTerm.setAttribute("definition", "http://sweetontology.net/reprMathFunction/Polynomial");
		Element classifierLabel = doc.createElement("sml:label");
		classifierLabel.setTextContent("Math Function Type");
		Element classifierValue = doc.createElement("sml:value");
		classifierValue.setTextContent("Rational Polynomial Coefficients");

		classifierTerm.appendChild(classifierLabel);
		classifierTerm.appendChild(classifierValue);
		classifier.appendChild(classifierTerm);
		classifierList.appendChild(classifier);
		classification.appendChild(classifierList);

		root.appendChild(classification);

		Element characteristics = doc.createElement("sml:characteristics");
		characteristics.setAttribute("name", "ignored");
		root.appendChild(characteristics);

		Element latitudeInput = doc.createElement("sml:input");
		latitudeInput.setAttribute("name", "latitude");
		Element latitudeUom = doc.createElement("swe:uom");
		latitudeUom.setAttribute("code", "deg");
		Element latitudeQuantity = doc.createElement("swe:Quantity");
		latitudeQuantity.setAttribute("definition", "http://www.opengis.net/def/axis/EPSG/9.9/108");
		latitudeQuantity.appendChild(latitudeUom);
		latitudeInput.appendChild(latitudeQuantity);

		Element longitudeInput = doc.createElement("sml:input");
		longitudeInput.setAttribute("name", "longitude");
		Element longitudeUom = doc.createElement("swe:uom");
		longitudeUom.setAttribute("code", "deg");
		Element longitudeQuantity = doc.createElement("swe:Quantity");
		longitudeQuantity.setAttribute("definition", "http://www.opengis.net/def/axis/EPSG/9.9/109");
		longitudeQuantity.appendChild(longitudeUom);
		longitudeInput.appendChild(longitudeQuantity);

		Element altitudeInput = doc.createElement("sml:input");
		altitudeInput.setAttribute("name", "altitude");
		Element altitudeUom = doc.createElement("swe:uom");
		altitudeUom.setAttribute("code", "deg");
		Element altitudeQuantity = doc.createElement("swe:Quantity");
		altitudeQuantity.setAttribute("definition", "http://www.opengis.net/def/sensor-model-param/xdomes/altitude");
		altitudeQuantity.appendChild(altitudeUom);
		altitudeInput.appendChild(altitudeQuantity);

		Element inputs = doc.createElement("sml:inputs");
		Element inputList = doc.createElement("sml:InputList");
		inputList.appendChild(latitudeInput);
		inputList.appendChild(longitudeInput);
		inputList.appendChild(altitudeInput);
		inputs.appendChild(inputList);
		root.appendChild(inputs);

		Element rowOutput = doc.createElement("sml:output");
		rowOutput.setAttribute("name", "r");
		Element rowUom = doc.createElement("swe:uom");
		rowUom.setAttribute("code", "px");
		Element rowQuantity = doc.createElement("swe:Quantity");
		rowQuantity.setAttribute("definition", "http://www.opengis.net/def/ogc/Row");
		rowQuantity.appendChild(rowUom);
		rowOutput.appendChild(rowQuantity);

		Element columnOutput = doc.createElement("sml:output");
		columnOutput.setAttribute("name", "c");
		Element columnUom = doc.createElement("swe:uom");
		columnUom.setAttribute("code", "px");
		Element columnQuantity = doc.createElement("swe:Quantity");
		columnQuantity.setAttribute("definition", "http://www.opengis.net/def/ogc/Column");
		columnQuantity.appendChild(columnUom);
		columnOutput.appendChild(columnQuantity);

		Element outputs = doc.createElement("sml:outputs");
		Element outputList = doc.createElement("sml:outputList");
		outputList.appendChild(rowOutput);
		outputList.appendChild(columnOutput);
		outputs.appendChild(outputList);
		root.appendChild(outputs);

		Element parameterList = doc.createElement("sml:parameterList");
		Element parameters = doc.createElement("sml:parameters");

		List<Tre> treList = tres.getTREsWithName("RPC00B");
		if(treList.size()==0) throw new IOException("No RPC00B found. This method only supports export of parameters from RPC00B TREs");
		if(treList.size()>1) throw new IOException("More than one RPC00B found. This method only supports export of parameters from a single RPC00B TRE");
		
		for (int j = 0; j < treList.size(); j++) {
			Tre lastTre = treList.get(j);

			List<TreEntry> listTreEntries = lastTre.getEntries();
			for (int i = 0; i < listTreEntries.size(); i++) {

				TreEntry treEntry = listTreEntries.get(i);
				
				if(treEntry.getName().equals("SUCCESS")) continue;  // SUCCESS is not a typical RPC00B field

				if (treEntry.getName().endsWith("_COEFF")) {    // This buffers the Key-arrayValues

					StringBuffer sb = new StringBuffer();

					if (treEntry.hasGroups()) {

						int groupCounter = 0;
						for (TreGroup group : treEntry.getGroups()) {
							groupCounter++;
							if (group.getEntries().size() > 1) {
								for (int entryCounter = 0; entryCounter < group.getEntries().size(); ++entryCounter) {
									TreEntry entryInGroup = group.getEntries().get(entryCounter);
									sb.append(entryInGroup.getFieldValue() + " ");

								}
							} else {
								TreEntry entryInGroup = group.getEntries().get(0);
								sb.append(entryInGroup.getFieldValue() + " ");
							}
						}
					}

					parameterList
							.appendChild(createArrayParameter(doc, listTreEntries.get(i).getName(), sb.toString()));
					
				} else {   // All other values are simple Key-Value fields
					parameterList.appendChild(createSimpleParameter(doc, listTreEntries.get(i).getName(),
							listTreEntries.get(i).getFieldValue()));
				}

			}
			lastTre.dump();
		}
		is.close();

		parameters.appendChild(parameterList);
		root.appendChild(parameters);

		Element method = doc.createElement("sml:method");
		Element processMethod = doc.createElement("sml:processMethod");
		Element description = doc.createElement("sml:description");
		description.setTextContent(
				"The RPC00B Tagged Record Extension contains rational function polynomial coefficients and normalization parameters that define the physical relationship between image coordinates and ground coordinates.");
		processMethod.appendChild(description);
		method.appendChild(processMethod);
		root.appendChild(method);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transf = transformerFactory.newTransformer();

		transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transf.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource source = new DOMSource(doc);

		
		StreamResult outfile = new StreamResult(os);
		transf.transform(source, outfile);
		//StreamResult console = new StreamResult(System.out);
		//transf.transform(source, console);

	}

	private Element createSimpleParameter(Document doc, String fieldName, String value) {
		String uom = null;
		if (fieldName.contentEquals("ERR_BIAS") ||
				fieldName.contentEquals("ERR_RAND") ||
				fieldName.contentEquals("HEIGHT_OFF") ||
				fieldName.contentEquals("HEIGHT_SCALE"))
		{
			uom = "m";
		}
		else if(fieldName.contentEquals("LINE_OFF") ||
				fieldName.contentEquals("SAMP_OFF") ||
				fieldName.contentEquals("LINE_SCALE") ||
				fieldName.contentEquals("SAMP_SCALE")) {
			uom = "px";
		}
		else if(fieldName.contentEquals("LAT_OFF") ||
				fieldName.contentEquals("LONG_OFF") ||
				fieldName.contentEquals("LAT_SCALE") ||
				fieldName.contentEquals("LONG_SCALE")) {
			uom = "deg";
		}		

		Element element = doc.createElement("sml:parameter");
		element.setAttribute("name", fieldName);
		Element dataRecord = doc.createElement("swe:DataRecord");
		dataRecord.setAttribute("definition",
				"http://www.opengis.net/def/sensor-model-param/NTB/2.1/NITF/RPC00B/" + fieldName);
		Element label = doc.createElement("swe:label");
		label.setTextContent(fieldName);
		Element fieldElement = doc.createElement("swe:field");
		fieldElement.setAttribute("name", fieldName);

		Element quantity = doc.createElement("swe:Quantity");
		quantity.setAttribute("definition",
				"http://www.opengis.net/def/sensor-model-param/NTB/2.1/NITF/RPC00B/" + fieldName);
		Element label2 = doc.createElement("swe:label");
		label2.setTextContent(fieldName);
		quantity.appendChild(label2);
		Element uomElement = doc.createElement("swe:uom");
		uomElement.setAttribute("code", uom);
		quantity.appendChild(uomElement);
		Element valueElement = doc.createElement("swe:value");
		valueElement.setTextContent(value);
		quantity.appendChild(valueElement);
		fieldElement.appendChild(quantity);

		dataRecord.appendChild(label);
		dataRecord.appendChild(fieldElement);
		element.appendChild(dataRecord);

		return element;
	}

	private Element createArrayParameter(Document doc, String fieldName, String values) {

		Element element = doc.createElement("sml:parameter");
		element.setAttribute("name", fieldName);
		Element dataArray = doc.createElement("swe:DataArray");
		dataArray.setAttribute("definition",
				"http://www.opengis.net/def/sensor-model-param/NTB/2.1/NITF/RPC00B/" + fieldName);
		Element elementCount = doc.createElement("swe:elementCount");

		Element count = doc.createElement("swe:Count");
		Element valueElement = doc.createElement("swe:value");

		valueElement.setTextContent((values != null) ? ("" + values.trim().split(" ").length) : "0");

		count.appendChild(valueElement);
		elementCount.appendChild(count);

		Element elementType = doc.createElement("swe:elementType");
		elementType.setAttribute("name", fieldName);

		Element encoding = doc.createElement("swe:encoding");
		Element textEncoding = doc.createElement("swe:TextEncoding");
		textEncoding.setAttribute("tokenSeparator", " ");
		textEncoding.setAttribute("blockSeparator", ",");
		textEncoding.setAttribute("decimalSeparator", ".");
		encoding.appendChild(textEncoding);

		Element valuesElement = doc.createElement("swe:values");
		valuesElement.setTextContent(values);

		dataArray.appendChild(elementCount);
		dataArray.appendChild(elementType);
		dataArray.appendChild(encoding);
		dataArray.appendChild(valuesElement);
		element.appendChild(dataArray);

		return element;
	}

}
