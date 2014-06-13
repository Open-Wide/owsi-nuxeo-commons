package fr.openwide.nuxeo.test;


import org.nuxeo.ecm.webengine.test.WebEngineFeature;
import org.nuxeo.runtime.test.runner.Jetty;

@Jetty(port = WebEngineJettyFeature.PORT)
public class WebEngineJettyFeature extends WebEngineFeature {
	
    public static final int PORT = 8082;

    public static final String NUXEO_SITES_URL = "http://localhost:" + PORT;

}
