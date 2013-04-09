package fr.openwide.nuxeo.ordering;

import java.util.Comparator;

import org.nuxeo.ecm.platform.types.Type;

public class EcmTypeComparator implements Comparator<Type> {
    
    private final EcmTypeSortMethod sortMethod;

    public EcmTypeComparator(EcmTypeSortMethod sortMethod) {
        this.sortMethod = sortMethod;
    }
    
    public int compare(Type t1, Type t2) {
        if (sortMethod == EcmTypeSortMethod.ALPHABETICAL) {
            return t1.getLabel().compareTo(t2.getLabel());
        }
        else {
            // Default
            return t1.getLabel().compareTo(t2.getLabel());
        }
    }
    
}