package registros.valores

import registros.Element

class DvCodedText extends DvText {

   // campos de CodePhrase
   String codeString
   String terminologyIdName
   String terminologyIdVersion
   
   static belongsTo = [Element]
   
   static constraints = {
      terminologyIdVersion (nullable: true)
      terminologyIdName (nullable: false)
      codeString (nullable: false)
      value (nullable: false)
   }
}
