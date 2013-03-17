package registros.valores

import registros.Element
import registros.Structure

class DvDateTime extends DataValue {

   Date value
   
   static belongsTo = [Element]
}