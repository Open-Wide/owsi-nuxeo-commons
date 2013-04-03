package fr.openwide.nuxeo.properties;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import com.google.inject.Inject;

import fr.openwide.nuxeo.properties.PropertiesService;
import fr.openwide.nuxeo.test.AbstractNuxeoTest;

/**
 * 
 * @author mkalam-alami
 * 
 */
@Deploy("fr.openwide.nuxeo.commons.genericprops")
@LocalDeploy("fr.openwide.nuxeo.commons.genericprops:OSGI-INF/properties-test.xml")
public class PropertiesTest extends AbstractNuxeoTest {
    
    @Inject
    PropertiesService properties;

    @Inject
    CoreSession documentManager;
    
    @Test
    public void test() throws Exception {
        Assert.assertEquals("String property must be properly set", "world", properties.getValue("hello"));
        
        Assert.assertEquals("Unspecified property must be properly set as string", "yo", properties.getValue("default"));
        
        Assert.assertEquals("Number property must be properly set", new Long(40), properties.getNumberValue("mynumber"));
        
        List<String> listValue = properties.getListValue("mylist");
        Assert.assertNotNull("List property must be properly set (1/3)", listValue);
        Assert.assertEquals("List property must be properly set (2/3)", 3, listValue.size());
        Assert.assertEquals("List property must be properly set (3/3)", "C", listValue.get(2));
        
        Map<String, String> mapValue = properties.getMapValue("mymap");
        Assert.assertNotNull("Map property must be properly set (1/2)", mapValue);
        Assert.assertEquals("Map property must be properly set (2/2)", "hoho", mapValue.get("haha"));
        
    }

}