
package fr.openwide.nuxeo.avatar;

import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.SimpleFeature;

/**
 * @author mkalam-alami
 *
 */
@Deploy({
    "org.nuxeo.ecm.core.api",
    "org.nuxeo.ecm.platform.content.template",
    "org.nuxeo.ecm.platform.userworkspace.api",
    "org.nuxeo.ecm.platform.dublincore",
    "org.nuxeo.ecm.platform.userworkspace.types",
    "org.nuxeo.ecm.platform.usermanager",
    "org.nuxeo.ecm.platform.usermanager.api",
    "org.nuxeo.ecm.directory.api",
    "org.nuxeo.ecm.directory",
    "org.nuxeo.ecm.directory.sql",
    "org.nuxeo.ecm.platform.userworkspace.core",
    "org.nuxeo.ecm.platform.userworkspace.api",
    "org.nuxeo.ecm.platform.userworkspace.core"
})
public class UserWorkspaceCoreFeature extends SimpleFeature {

}
