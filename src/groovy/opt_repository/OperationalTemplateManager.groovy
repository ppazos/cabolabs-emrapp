package opt_repository

import org.apache.log4j.Logger
import groovy.util.slurpersupport.GPathResult

/**
 * Placeholder for a complete OTP manager with parser etc. (will integrate https://github.com/ppazos/openEHR-OPT).
 * For now this is just for testing the integration of OPTs and to remove ArchetypeManager.
 * @author pab
 *
 */
class OperationalTemplateManager {

   // Parsed OPTs in memory, templateId -> OPT
   private static Map<String, GPathResult> cache = [:]
   
   // templateId => timestamp de cuando fue usado por ultima vez.
   private static Map<String, Date> timestamps = [:]
   

   private Logger log = Logger.getLogger(getClass())
   
   
   // Singleton
   private static OperationalTemplateManager instance = null
   
   private OperationalTemplateManager() {}
   
   public static OperationalTemplateManager getInstance()
   {
      if (!instance) instance = new OperationalTemplateManager()
      return instance
   }
   // /Singleton
   
   
   public void loadAll(String from)
   {
      def root = new File( from )
      def opt
      def tempalteId
      
      // FIXME: deberia filtrar solo archivos opt
      root.eachFile { f ->
         
         opt = new XmlParser().parse( f )
         templateId = opt.template_id.value.text()
         
         if (!templateId) throw new Exception(f.name + " is not a valid OPT, template_id is missing")
         
         this.timestamps[templateId] = new Date() // actualizo timestamp
         this.cache[templateId] = opt
      }
   }
   
   
   public GPathResult getTemplate(String templateId)
   {
      if (!this.cache[templateId])
      {
         throw new Exception("Template ${templateId} not found")
      }
      
      this.timestamps[templateId] = new Date() // actualizo timestamp
      return this.cache[templateId]
   }
   
   public Map getCache()
   {
      return this.cache.asImmutable()
   }
   
   public List getTemplates()
   {
      return this.cache.values()
   }
}
