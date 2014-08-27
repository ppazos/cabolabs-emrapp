package opt_repository

import groovy.util.Node
import groovy.util.slurpersupport.GPathResult

class OperationalTemplate1 {

   String templateId
   String archetypeId // root archetype id
   GPathResult opt
   Map nodes = [:] // TemplatePath -> GPathResult (node) para pedir restricciones

   OperationalTemplate1(GPathResult opt)
   {
      // TODO: validate XML XSD
      
      this.templateId = opt.template_id.value.text()
      assert this.templateId != null, "XML received is not a valid OPT, template_id is missing"
      
      this.archetypeId = opt.definition.archetype_id.value.text()
      assert this.archetypeId != null, "Template ${templateId} doesn't have a root archetypeId"
      
      this.opt = opt
      
      parseObjectNode(this.opt.definition, '/')
      
      
      // DEBUG
      //println groovy.xml.XmlUtil.serialize( opt )
      
      /*
      // test: to see the inyected paths
      println this.nodes.size() // son 66
      this.nodes.each { k, v -> // Solo muestra algunos, imprimiendo key muestra 23, imprimiendo value muestra 2 !!!!
         //println groovy.xml.XmlUtil.serialize( entry.value )
         println "X"+ k +": "+ v
      }
      
      println ""
      println ""
      //println this.nodes // meustra todas las paths y valores que no muestra en el each!!!
      
      def outputBuilder = new groovy.xml.StreamingMarkupBuilder()
      String result = outputBuilder.bind{ mkp.yield this.opt }
      println( result )
      */
   }
   
   GPathResult getNode(String path)
   {
      return this.nodes[path]
   }
   
   
   // Parser ---------------------------------------------------------------------------------
   private parseObjectNode(GPathResult node, String parentPath)
   {
      //println "parseObjectNode ${parentPath}"
      
      // Path calculation
      def currentPath = parentPath
      if (currentPath != '/')
      {
         // comienza de nuevo con las paths relativas al root de este arquetipo
         if (!node.archetype_id.value.isEmpty())
         {
            currentPath += '[archetype_id='+ node.archetype_id.value +']' // slot in the path instead of node_id
         }
         // para tag vacia empty da false pero text es vacio ej. <node_id/>
         else if (!node.node_id.isEmpty() && node.node_id.text() != '')
         {
            currentPath += '['+ node.node_id.text() + ']'
         }
      }
      /*
      def obn = new ObjectNode(
         rmTypeName: node.rm_type_name.text(),
         nodeId: node.node_id.text(),
         type: node.'@xsi:type'.text(),
         archetypeId: node.archetype_id.value.text(), // This is optional, just resolved slots have archId
         path: path
         // TODO: default_values
      )
      */
      // TODO: parse occurrences
      
      
      this.nodes[currentPath] = node
      
      //println " - add node ${path}"
      
      
      // Agrega la path al XML para que el nodo tenga su path
      node.appendNode {
         //delegate.path(path)
         path(currentPath)
      }
      
      
      node.attributes.each { xatn ->
      
         //obn.attributes << parseAttributeNode(xatn, path)
         parseAttributeNode(xatn, currentPath)
      }
   }
   
   private parseAttributeNode(GPathResult node, String parentPath)
   {
      //println "parseAttributeNode ${parentPath}"
      
      // Path calculation
      def path = parentPath
      if (path == '/') path += node.rm_attribute_name.text() // Avoids to repeat '/'
      else path += '/'+ node.rm_attribute_name.text()
      
      /*
      def atn = new AttributeNode(
         rmAttributeName: node.rm_attribute_name.text(),
         type: node.'@xsi:type'.text()
         // TODO: cardinality
         // TODO: existence
      )
      */
      
      node.children.each { xobn ->
      
         //atn.children << parseObjectNode(xobn, path)
         parseObjectNode(xobn, path)
      }
   }
   // /Parser ---------------------------------------------------------------------------------
   
   
   /**
   String test(String archetypeId, String code)
   {
      println "test ${archetypeId} ${code}"
      if (this.archetypeId != archetypeId)
      {
         //println opt.definition.'**'.grep{ it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' }
         //println opt.definition.'**'.grep{ it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' && it.archetype_id.value == archetypeId }.term_definitions.find { it.@code == code }
         
         // depthFirst debe ser sobre Node no NodeList (opt.definition)
         //println opt.depthFirst().findAll { it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' }
         //println opt.depthFirst().findAll { it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' && it.archetype_id.value == archetypeId }.term_definitions
         
         def archetypeRootNode = opt.depthFirst().find { it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' && it.archetype_id.value == archetypeId }
         
         println archetypeRootNode.term_definitions.getClass() // ArrayList
         println archetypeRootNode.term_definitions.size() // 1

         def term = archetypeRootNode.term_definitions.find { it.@code.text() == code }
         
         term.items.find { it.@id.text() == "text" }.text()

         
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
   */
   
   String getTerm(String archetypeId, String code)
   {
      return this.getFromOntology(archetypeId, code, "text")
   }
   
   String getDescription(String archetypeId, String code)
   {
      return this.getFromOntology(archetypeId, code, "description")
   }
   
   GPathResult getTermBindings(String archetypeId)
   {
      def root
      if (this.archetypeId == archetypeId)
      {
         root = opt.definition
      }
      else // busqueda en profundidad por archetype_root
      {
         root = opt.depthFirst().find { it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' && it.archetype_id.value == archetypeId }
      }
      
      if (!root)
      {
         println "root no encontrado para ${templateId} ${archetypeId}"
         return ''
      }
      
      // Buscar en ese nodo, dentro de sus term_definitions, el que tenga code nodeId y devolver su text
      // Dentro de term_definitions, hay dos items: text y description (pueden haber mas)
      
      /**
       *  <term_bindings terminology="SNOMED-CT">
          <items code="at0000">
            <value>
              <terminology_id>
                <value>SNOMED-CT(2003)</value>
              </terminology_id>
              <code_string>163020007</code_string>
            </value>
          </items>
          <items code="at0004">
            <value>
              <terminology_id>
                <value>SNOMED-CT(2003)</value>
              </terminology_id>
              <code_string>163030003</code_string>
            </value>
          </items>
       */
      return root.term_bindings
   }
   
   private String getFromOntology(String archetypeId, String code, String part)
   {
      assert part == "text" || part == "description", "part should be text or description and is ${part}"
      
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
         root = opt.depthFirst().find { it.'@xsi:type' && it.'@xsi:type'.text() == 'C_ARCHETYPE_ROOT' && it.archetype_id.value == archetypeId }
      }
      
      if (!root)
      {
         println "root no encontrado para ${templateId} ${archetypeId}"
         return ''
      }
      
      // Buscar en ese nodo, dentro de sus term_definitions, el que tenga code nodeId y devolver su text
      // Dentro de term_definitions, hay dos items: text y description (pueden haber mas)
      
      def term = root.term_definitions.find { it.@code.text() == code }
      
      return term.items.find { it.@id.text() == part }.text()
   }
}
