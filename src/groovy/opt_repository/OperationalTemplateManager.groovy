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
   private static Map<String, OperationalTemplate> _cache = [:]
   
   // templateId => timestamp de cuando fue usado por ultima vez.
   private static Map<String, Date> _timestamps = [:]
   

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
      def optxml
      def opt
      def templateId
      
      // FIXME: deberia filtrar solo archivos opt
      root.eachFile { f ->
         
         optxml = new XmlSlurper().parse( f )
         opt = new OperationalTemplate(optxml)

         this._timestamps[opt.templateId] = new Date() // actualizo timestamp
         this._cache[opt.templateId] = opt
      }
   }
   
   
   public OperationalTemplate getTemplate(String templateId)
   {
      if (!this._cache[templateId])
      {
         throw new Exception("Template ${templateId} not found")
      }
      
      this._timestamps[templateId] = new Date() // actualizo timestamp
      return this._cache[templateId]
   }
   
   public Map getCache()
   {
      return this._cache.asImmutable()
   }
   
   public List getTemplates()
   {
      return this._cache.values()
   }
}
