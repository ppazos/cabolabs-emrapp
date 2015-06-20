package registros.valores

import registros.Element
import registros.Structure

/**
 * Subclases se mapean en sus propias tablas:
 *   1. marcando esta clase como abstracta (sin esto 2. no funciona)
 *   2. poniendo tablePerHierarchy en false (utiliza estrategia table-per-subclass)
 * 
 * @author Pablo Pazos Gutierrez
 */
abstract class DataValue {

   //debug
   String aomType
   
   //Element parent
   
   static belongsTo = [Element]
   
   // cada subclase tiene su tabla
   static mapping = {
      tablePerHierarchy false
   }
   
   static constraints = {
      // para Item.attributes no se tiene un aomType,
      // el aomType se usa cuando el DataVAlue esta
      // en Element.value
      aomType(nullable:true)
   }
}
