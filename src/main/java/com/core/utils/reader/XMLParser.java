package main.java.com.core.utils.reader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class XMLParser {
    public static void main(String[] args) {
        try {
            // Load the XML file using Jsoup
            File input = new File("path/to/your/xml/file.xml");
            Document document = Jsoup.parse(input, "UTF-8");
            
            // Access elements in the XML document
            Elements elements = document.select("your_xml_element_selector");
            
            // Iterate through the selected elements
            for (Element element : elements) {
                // Access and process individual elements
                String text = element.text();
                System.out.println("Element Text: " + text);
                
                // You can also access attributes if needed
                // String attributeValue = element.attr("attribute_name");
                // System.out.println("Attribute Value: " + attributeValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
