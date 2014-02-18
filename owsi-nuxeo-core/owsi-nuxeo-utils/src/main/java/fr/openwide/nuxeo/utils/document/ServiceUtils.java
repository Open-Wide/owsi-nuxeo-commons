package fr.openwide.nuxeo.utils.document;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.runtime.api.Framework;

public class ServiceUtils {

   /**
    * Removes the need a a try/catch around ramework.getService(clazz)
    * @param clazz
    * @return
    * @throws ClientException
    */
   public static <T> T getService(Class<T> clazz) throws ClientException {
      try {
         return Framework.getService(clazz);
      } catch (Exception e) {
         throw new ClientException("Can't get service " + clazz, e);
      }     
   }
   
}
