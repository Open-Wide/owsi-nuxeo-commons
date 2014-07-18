package fr.openwide.nuxeo.utils.document;

import org.nuxeo.ecm.core.query.sql.NXQL;

/**
 *
 * @author jguillemotte
 */
public class NXQLQueryHelper {

    public static final String NXQL_SELECT_FROM = "SELECT * FROM ";
    public static final String NXQL_WHERE = " WHERE ";
    public static final String NXQL_AND = " AND ";
    public static final String NXQL_OR = " OR ";
    public static final String NXQL_IS_NULL = " IS NULL";
    public static final String NXQL_QUOTE = "'";

    public static final String NXQL_IS_NOT_DELETED = NXQL.ECM_LIFECYCLESTATE + " != 'deleted'";
    public static final String NXQL_IS_NOT_VERSIONED = NXQL.ECM_ISVERSION + " = 0";
    public static final String NXQL_IS_VERSIONED = NXQL.ECM_ISVERSION + " = 1";
    public static final String NXQL_IS_NO_PROXY = NXQL.ECM_ISPROXY + " = 0";
    public static final String NXQL_IS_PROXY = NXQL.ECM_ISPROXY + " = 1";
    
    /** WARNING harder to use for cross-Phase/subproject, because their Path is relative,
     * so the project can only be known by the subprojectId (which contains the live subproject Path) */
    public static final String NXQL_PATH_STARTSWITH = NXQL.ECM_PATH + " STARTSWITH '";
    
    public static final String NXQL_NO_DELETED_DOCUMENTS_CRITERIA = " AND " + NXQL_IS_NOT_DELETED;

    public static final String NQXL_NOT_VERSIONED_CRITERIA = " AND " + NXQL_IS_NOT_VERSIONED;
    public static final String NQXL_NON_PROXIES_CRITERIA = " AND " + NXQL_IS_NO_PROXY;
    public static final String NQXL_PROXIES_CRITERIA = " AND " + NXQL_IS_PROXY;

        
}
