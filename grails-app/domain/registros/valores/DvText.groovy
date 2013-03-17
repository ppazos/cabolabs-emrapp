package registros.valores

import registros.Element

class DvText extends DataValue {

   String value
   
   static belongsTo = [Element]
}