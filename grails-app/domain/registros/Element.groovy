package registros

import registros.valores.DataValue

class Element extends Item {

   // this.type = "ELEMENT"
   DataValue value
   
   static belongsTo = [Structure]
   
   static mapping = {
      value cascade: 'save-update'
   }
   
   static constraints = {
      value(nullable: true)
   }
}