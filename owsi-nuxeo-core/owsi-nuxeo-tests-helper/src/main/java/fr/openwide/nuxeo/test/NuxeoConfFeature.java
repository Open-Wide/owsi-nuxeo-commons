package fr.openwide.nuxeo.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.SimpleFeature;

/**
 * Loads a nuxeo.conf files as system properties if present
 * 
 * @author mkalamalami
 *
 */
public class NuxeoConfFeature extends SimpleFeature {

    private static Log logger = LogFactory.getLog(NuxeoConfFeature.class);

    @Override
    public void initialize(FeaturesRunner runner) throws Exception {
        InputStream is = getClass().getResourceAsStream("/nuxeo.conf");
        if (is != null) {
            try {
                Properties properties = new Properties();
                properties.load(is);
                for (Entry<Object, Object> entry : properties.entrySet()) {
                    System.setProperty((String) entry.getKey(), (String) entry.getValue());
                }
            } catch (IOException e) {
                logger.error(e);
            } finally {
                is.close();
            }
        }
    }
}