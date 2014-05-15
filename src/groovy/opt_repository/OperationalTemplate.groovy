package opt_repository

import groovy.util.Node
import groovy.util.slurpersupport.GPathResult

class OperationalTemplate {

   String templateId
   String archetypeId // root archetype id
   GPathResult opt
   
   OperationalTemplate(GPathResult opt)
   {
      // TODO: validate XML XSD
      
      this.templateId = opt.template_id.value.text()
      assert this.templateId != null, "XML received is not a valid OPT, template_id is missing"
      
      this.archetypeId = opt.definition.archetype_id.value.text()
      assert this.archetypeId != null, "Template ${templateId} doesn't have a root archetypeId"
      
      this.opt = opt
      
      // test
      def xsi = new groovy.xml.Namespace("http://www.w3.org/2001/XMLSchema-instance")
      println xsi.type
   }
   
   String test(String archetypeId, String code)
   {
      if (this.archetypeId != archetypeId)
      {
         
         //println opt.definition.'**'.grep{ it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' }
         println opt.definition.'**'.grep{ it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' && it.archetype_id.value == archetypeId }.term_definitions.find { it.@code == code }
         
         // depthFirst debe ser sobre Node no NodeList (opt.definition)
         //println opt.depthFirst().findAll { it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' }
         println opt.depthFirst().findAll { it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' && it.archetype_id.value == archetypeId }.term_definitions
         
         
         //recursiveTest( opt.definition, archetypeId )
      }
   }
   
   def recursiveTest(GPathResult nodes, String archetypeId)
   {
      nodes.each { node ->
         
         if (node instanceof String)
         {
            
         }
         else if (node instanceof GPathResult)
         {
            if (!node.'@xsi:type'.isEmpty() )
            {
               //println "xsi:type ${node.'@xsi:type'.text()}"
               if (node.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT')
               {
                  println "Found ${node.name()} ${node.archetype_id.value}"
                  if (node.archetype_id.value.text() == archetypeId)
                  {
                     println "Found archetypeId ${archetypeId}"
                  }
               }
            }
            
            
            //println "N: ${node.name()}"
            
            recursiveTest( node.children(), archetypeId )
         }
         else
         {
            println "not a String or GPathResult " + node + " is "+ node.getClass() +"\n"
         }
      }
   }
   
   String getTerm(String archetypeId, String code)
   {
      // Buscar entre todos los nodos ARCHETYPE_ROOT, el que tenga archetype_id.value = archetypeId
      // o tambien buscar en template.definition que es el ROOT de todo el template.
      // TODO
      // <children xsi:type="C_ARCHETYPE_ROOT">
      def root
      if (this.archetypeId == archetypeId)
      {
         root = opt.definition
      }
      else // busqueda en profundidad por archetype_root
      {
         println opt.definition.'**'.grep{ it.'@xsi:type' == 'C_ARCHETYPE_ROOT' }
         println opt.definition.'**'.grep{ it.'@xsi:type' == 'C_ARCHETYPE_ROOT' }.archetype_id
         
         root = opt.definition.'**'.grep{ it.'@xsi:type' == 'C_ARCHETYPE_ROOT' && it.archetype_id.value.text() == archetypeId }
      }
      
      if (!root)
      {
         println "root no encontrado para ${templateId} ${archetypeId}"
         return ''
      }
      
      // Buscar en ese nodo, dentro de sus term_definitions, el que tenga code nodeId y devolver su text
      // Dentro de term_definitions, hay dos items: text y description (pueden haber mas)
      return root.term_definitions.find { it.@code == code }.items.find{ it.@id == "text" }.text()
   }
}
