package registros

import registros.valores.DataValue
import registros.valores.DvText

class Element extends Item {

   // this.type = "ELEMENT"
   DataValue value
   DvText name // Can be DvCodedText
   
   static belongsTo = [Structure]
   
   static mapping = {
      value cascade: 'save-update'
   }
   
   static constraints = {
      value(nullable: true)
      name(nullable: true)
   }
}
