/*
* Copyright (c) 2013, Open Wide. All rights reserved.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA 02110-1301 USA
*/

package fr.openwide.nuxeo.schemas;

/**
 * @author mkalam-alami
 *
 */
public interface SchemaXVocabulary {

    static final String SCHEMA_XVOCABULARY = "xvocabulary";

    static final String COLUMN_LABEL = "label";
    static final String XPATH_LABEL = SCHEMA_XVOCABULARY + ":" + COLUMN_LABEL;

    static final String COLUMN_PARENT = "parent";
    static final String XPATH_PARENT = SCHEMA_XVOCABULARY + ":" + COLUMN_PARENT;
    
}
