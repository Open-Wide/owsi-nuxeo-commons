package fr.openwide.nuxeo.formatter.web;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.nuxeo.runtime.api.Framework;

import fr.openwide.nuxeo.formatter.FieldFormatterService;

/**
 * 
 * @author mkalam-alami
 * 
 */
@Name("fieldFormatter")
@Scope(ScopeType.SESSION)
@Install(precedence = Install.FRAMEWORK)
public class FieldFormatterWrapperBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private FieldFormatterService fieldFormatterService;

    @Unwrap
    public FieldFormatterService getFieldFormatterService() throws Exception {
        if (fieldFormatterService == null) {
            fieldFormatterService = Framework.getService(FieldFormatterService.class);
        }
        return fieldFormatterService;
    }
    
}
