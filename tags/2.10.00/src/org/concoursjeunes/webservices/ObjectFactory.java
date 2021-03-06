
package org.concoursjeunes.webservices;

import javax.xml.bind.annotation.XmlRegistry;

import org.concoursjeunes.plugins.PluginDescription;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.concoursjeunes.webservices package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.concoursjeunes.webservices
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PluginDescriptionArray }
     * 
     */
    public PluginDescriptionArray createPluginDescriptionArray() {
        return new PluginDescriptionArray();
    }

    /**
     * Create an instance of {@link PluginDescription }
     * 
     */
    public PluginDescription createPluginDescription() {
        return new PluginDescription();
    }

}
