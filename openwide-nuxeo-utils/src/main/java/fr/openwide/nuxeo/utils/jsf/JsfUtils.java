package fr.openwide.nuxeo.utils.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import fr.openwide.nuxeo.schemas.SchemaXVocabulary;

/**
 * Converts document models to select items entries, to feed JSF select menus.
 * 
 * @author mkalam-alami
 *
 */
public class JsfUtils {

    public static List<SelectItem> modelsToSelectItems(DocumentModelList models) throws ClientException {
        return modelsToSelectItems(models, new SimpleSelectItemLabelBuilder(), false);
    }
            
    public static List<SelectItem> modelsToSelectItems(DocumentModelList models,
            SelectItemLabelBuilder labelBuilder) throws ClientException {
        return modelsToSelectItems(models, labelBuilder, false);
    }
    
    public static List<SelectItem> modelsToSelectItems(DocumentModelList models,
            SelectItemLabelBuilder labelBuilder, boolean usePathAsValue) throws ClientException {
        
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (DocumentModel model : models) {
            if (SchemaXVocabulary.SCHEMA_XVOCABULARY.equals(model.getType())) {
                selectItems.add(new SelectItem(model.getPropertyValue(SchemaXVocabulary.XPATH_PARENT)
                        + "/" + model.getId(), labelBuilder.getLabel(model)));
            }
            else if (usePathAsValue) {
                selectItems.add(new SelectItem(model.getPathAsString(), labelBuilder.getLabel(model)));
            }
            else {
                selectItems.add(new SelectItem(model.getId(), labelBuilder.getLabel(model)));
            }
        }
        return selectItems;
    }

}
