package com.fstudio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.fstudio.xml.XMLParser4WC;

public class MainApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String xmlPath = "F:\\abc.xml";
		try {
			FileInputStream fis = new FileInputStream(new File(xmlPath));
			
			InputSource isSource = new InputSource(fis);
			Document document = null;
			Element root = null;
			
			DocumentBuilder docBuilder = null;
			try {
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				docBuilder = docBuilderFactory.newDocumentBuilder();
				document = docBuilder.parse(isSource);
				root = document.getDocumentElement();
				if(root != null)
				{
					XMLParser4WC parser = new XMLParser4WC(root);
				}
				//
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
