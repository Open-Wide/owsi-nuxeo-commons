package fr.openwide.nuxeo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    public static final String PATH_NUXEO_CONF = "./nuxeo.conf";

    private static Log logger = LogFactory.getLog(NuxeoConfFeature.class);

    @Override
    public void initialize(FeaturesRunner runner) throws Exception {
        File nuxeoConf = new File(PATH_NUXEO_CONF);
        if (nuxeoConf.exists()) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(nuxeoConf));
                for (Entry<Object, Object> entry : properties.entrySet()) {
                    System.setProperty((String) entry.getKey(), (String) entry.getValue());
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
}