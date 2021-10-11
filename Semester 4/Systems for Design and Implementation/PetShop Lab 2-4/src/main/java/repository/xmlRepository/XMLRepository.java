package repository.xmlRepository;

import domain.BaseEntity;
import domain.exceptions.PetShopException;
import domain.validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import repository.PersistentRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class XMLRepository<ID, E extends BaseEntity<ID>> extends PersistentRepository<ID, E, Element> {
    protected final String documentRootName;
    protected final String filePath;
    protected Document rootDocument;

    public XMLRepository(Validator<E> validator, String filePath, String documentRootName) {
        super(validator);
        this.filePath = filePath;
        this.documentRootName = documentRootName;
        loadData();
    }

    /**
     *  Loads the data from the XML file
     */
    @Override
    protected void loadData() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            rootDocument = documentBuilder.parse(filePath);
            NodeList nodeList = rootDocument.getDocumentElement().getChildNodes();
            List<E> entities = new ArrayList<>();
            IntStream
                    .range(0, nodeList.getLength())
                    .filter((i) -> nodeList.item(i) instanceof Element)
                    .forEach((i) -> entities.add(extractEntity((Element) nodeList.item(i))));
            loadEntities(entities);
        } catch (ParserConfigurationException e) {
            throw new PetShopException("Parser Configuration Exception: " + e.getMessage());
        } catch (SAXException e) {
            throw new PetShopException("SAXException: " + e.getMessage());
        } catch (IOException ioException) {
            throw new PetShopException("IOException: " + ioException.getMessage());
        }
    }

    /**
     *  Saves data to the XML File
     */
    @Override
    protected void saveData() {
        try {
            this.rootDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            this.rootDocument.appendChild(this.rootDocument.createElement(this.documentRootName));
        } catch (ParserConfigurationException e) {
            throw new PetShopException("Parser Configuration Exception: " + e.getMessage());
        }
        findAll().forEach(entity -> {
            Element element = convertEntity(entity);
            rootDocument.getDocumentElement().appendChild(element);
        });
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(
                    new DOMSource(rootDocument),
                    new StreamResult(new File(filePath))
            );
        } catch (TransformerConfigurationException e) {
            throw new PetShopException("Transformer Configuration Exception: " + e.getMessage());
        } catch (TransformerException e) {
            throw new PetShopException("Transformer Exception: " + e.getMessage());
        }
    }

    /**
     *  Adds text content to element
     * @param parent - parent element
     * @param tagName - name of tag
     * @param textContent - content to be added
     */
    protected void addChildWithTextContent(Element parent, String tagName, String textContent) {
        Element childElement = rootDocument.createElement(tagName);
        childElement.setTextContent(textContent);
        parent.appendChild(childElement);
    }
}
