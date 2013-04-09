package fr.openwide.nuxeo.ordering.web;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.nuxeo.runtime.api.Framework;

import fr.openwide.nuxeo.ordering.service.EcmTypesOrderingService;

/**
 * 
 * @author mkalam-alami
 *
 */
@Name("ecmTypesOrdering")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.FRAMEWORK)
public class EcmTypesOrderingServiceWrapperBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private EcmTypesOrderingService ecmTypesOrderingService;

    @Unwrap
    public EcmTypesOrderingService getEcmTypesOrderingService() throws Exception {
        if (ecmTypesOrderingService == null) {
            ecmTypesOrderingService = Framework.getService(EcmTypesOrderingService.class);
        }
        return ecmTypesOrderingService;
    }

}
