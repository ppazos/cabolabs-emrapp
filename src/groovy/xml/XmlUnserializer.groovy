package xml
import groovy.util.slurpersupport.GPathResult
import registros.Document
import registros.Structure
import registros.Element

class XmlUnserializer {

   def doc
   
   public XmlUnserializer()
   {
      doc = new Document(bindData:[:])
   }
   
   public Document toDocument(String xml)
   {
      // xml can be Version or Composition
      
      def parsedDoc = new XmlSlurper().parseText(xml)
      
      if (parsedDoc.name() == 'version') processVersion(parsedDoc)
      else processComposition(parsedDoc)
      
      return doc
   }
   
   private processVersion(parsedVersion)
   {
      // Saving this allows to commit a new version of an existing document on the servers
      doc.versionUid = parsedVersion.uid.value.text()
      
      processComposition(parsedVersion.data)
   }
   
   private processComposition(parsedComposition)
   {
      doc.compositionArchetypeId = parsedComposition.archetype_details.archetype_id.value.text()
      doc.templateId = parsedComposition.archetype_details.template_id.value.text()
      //doc.start = new Date(parsedComposition.context.start_time.value.text()) // FIXME: test parser
      
      parsedComposition.content.each { contentItem ->
         processContent(contentItem, getNodePath('',  contentItem))
      }
   }
   
   private processContent(parsedContent, path)
   {
      doc.content = []
      processContentRecursive(doc.content, parsedContent, path)
   }
   
   
   private processContentRecursive(container, parsedContent, parentPath)
   {
      def item
      def newpath
      def archetype_node_id // archetype or node_id
      def processDvMethod
      parsedContent.each { contentNode ->
         
         contentNode.children().each { child ->
           
            println "path: "+ getNodePath(parentPath, child)
            
            if (child.'@xsi:type'.text() == 'ELEMENT')
            {
               println "Para de procesar porque es ELEMENT"
               
               processDvMethod = 'process_'+ child.value.'@xsi:type'.text()

               this."$processDvMethod"(child.value, getNodePath(parentPath, child))
            }
            else
            {
               processContentRecursive([], child, getNodePath(parentPath, child))
            }
         }
         /*
         if (contentNode.'xsi:type' == 'ELEMENT')
         {
            item = new Element(
               type: ,
               attr: ,
               archetypeId: ,
               path: ,
               nodeId:,
               aomType: ,
               attributes: [],
               value: processDataValue(contentNode.value)
            )
         }
         else
         {
            item = new Structure(
               items: [],
               type: ,
               attr: ,
               archetypeId: ,
               path: ,
               nodeId:, 
               aomType: ,
               attributes: []
            )
         }
         */
      }
   }
   
   /**
    * From EHRServer IndexDataJob
    */
   private String getNodePath(String parentPath, GPathResult node)
   {
      def path
      /*
      if (parentPath == '')
      {
         path = '/'
      }
      else
      */ 
      if (!node.'@archetype_node_id'.isEmpty()) // Si tiene archetype_node_id
      {
         // Para que los hijos de la raiz no empiecen con //
         if (parentPath == '/') parentPath = ''
         
         // Si es un nodo atNNNN
         if (node.'@archetype_node_id'.text().startsWith('at'))
         {
            path = parentPath + '/' + node.name() + '[' + node.'@archetype_node_id'.text() + ']'
         }
         else // Si es un archetypeId
         {
            path = parentPath + '/' + node.name() + '[archetype_id='+ node.'@archetype_node_id'.text() +']'

            //archetypeId = node.'@archetype_node_id'.text()
         }
      }
      else // No tiene archetype_node_id
      {
         // Para que los hijos de la raiz no empiecen con //
         if (parentPath == '/') parentPath = ''
         
         path = parentPath + '/' + node.name()
      }
      
      return path
   }
   
   private process_DV_DATE_TIME(parsedDv, path)
   {
      /*
       * <value xsi:type="DV_DATE_TIME">
       *   <value>20140911T134040,0971-0300</value>
       * </value>
       */
      
   }
   
   private process_DV_QUANTITY(parsedDv, path)
   {
      println "path quantity: "+ getNodePath(path, parsedDv)
      println "path magnitude"+ getNodePath(path, parsedDv) + '/magnitude'
      println "path units"+ getNodePath(path, parsedDv) + '/units'
      /*
       * <value xsi:type="DV_QUANTITY">
       *   <magnitude>24.0</magnitude>
       *   <units>°C</units>
       * </value>
       */
      doc.bindData[getNodePath(path, parsedDv) + '/magnitude'] = parsedDv.magnitude.text()
      doc.bindData[getNodePath(path, parsedDv) + '/units'] = parsedDv.units.text()
   }
   
   private process_DV_CODED_TEXT(parsedDv, path)
   {
      /*
       * <change_type>
            <value>creation</value>
            <defining_code>
              <terminology_id>
                <value>openehr</value>
              </terminology_id>
              <code_string>249</code_string>
            </defining_code>
          </change_type>
       */
      
   }
   
   private process_DV_TEXT(parsedDv, path)
   {
      /*
       * <value xsi:type="DV_TEXT">
       *   <value>...</value>
       * </value>
       */
      
   }
   
   private process_DV_BOOLEAN(parsedDv, path)
   {
      /*
       * <value xsi:type="DV_BOOLEAN">
       *   <value>24.0</value>
       * </value>
       */
      
   }
   
   private process_DV_COUNT(parsedDv, path)
   {
      /*
       * <value xsi:type="DV_QUANTITY">
       *   <magnitude>24.0</magnitude>
       *   <units>°C</units>
       * </value>
       */
      
   }
   
   private process_DV_PROPORTION(parsedDv, path)
   {
      /*
       * <value xsi:type="DV_QUANTITY">
       *   <magnitude>24.0</magnitude>
       *   <units>°C</units>
       * </value>
       */
      
   }
}
