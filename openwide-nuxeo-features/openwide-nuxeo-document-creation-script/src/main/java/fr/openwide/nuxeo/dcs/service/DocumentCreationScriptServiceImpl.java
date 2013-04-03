package fr.openwide.nuxeo.dcs.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.openwide.nuxeo.dcs.DocumentCreationScript;
import fr.openwide.nuxeo.dcs.DocumentCreationScriptImpl;

/**
 * 
 * @author mkalam-alami
 *
 */
public class DocumentCreationScriptServiceImpl extends DefaultComponent implements DocumentCreationScriptService {
    
    public static final String EXTENSION_POINT_SCRIPTS = "scripts";

    private static Logger logger = Logger.getLogger(DocumentCreationScriptServiceImpl.class);

    private static Map<String, DocumentCreationScript> scripts = new HashMap<String, DocumentCreationScript>();

    private static Map<String, DocumentCreationScript> disabledScripts = new HashMap<String, DocumentCreationScript>();

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor)
            throws Exception {
        DocumentCreationScriptDescriptor descriptor = (DocumentCreationScriptDescriptor) contribution;
        if (descriptor.name != null) {
            // Fetch, create or reset
            DocumentCreationScript script = null;
            if (descriptor.append) {
                script = scripts.get(descriptor.name);
            }
            if (script == null) {
                script = new DocumentCreationScriptImpl(descriptor.name);
            }
            
            // Append script elements
            if (descriptor.creationDescriptors != null) {
                for (DocumentCreationDescriptor creationDescriptor : descriptor.creationDescriptors) {
                    script.appendDocumentCreation(creationDescriptor);
                }
            }
            
            // Registration / (en|dis)ablement
            registerScript(script);
            setScriptEnabled(script.getName(), descriptor.enabled);
        }
        else {
            logger.warn("Could not register document creation script from " + contributor.getName() + " : name is null");
        }
    }

    public void registerScript(DocumentCreationScript script) {
        scripts.put(script.getName(), script);
    }

    public void setScriptEnabled(String name, boolean enabled) {
        if (enabled) {
            DocumentCreationScript documentCreationScript = disabledScripts.remove(name);
            if (documentCreationScript != null) {
                scripts.put(name, documentCreationScript);
            }
        }
        else {
            DocumentCreationScript documentCreationScript = scripts.remove(name);
            if (documentCreationScript != null) {
                disabledScripts.put(name, documentCreationScript);
            }
        }
    }

    @Override
    public void runScript(CoreSession session, String name, boolean overwrite) throws ClientException {
        DocumentCreationScript script = scripts.get(name);
        if (script != null) {
            script.run(session, overwrite);
        }
    }

    @Override
    public void runScript(CoreSession session, String name, DocumentModel context, boolean overwrite)
            throws ClientException {
        DocumentCreationScript script = scripts.get(name);
        if (script != null) {
            script.run(session, context, overwrite);
        }
    }

}
