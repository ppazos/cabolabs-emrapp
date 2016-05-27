package registros.valores

class DvQuantity extends DataValue {

   BigDecimal magnitude // was float but I had some issues: http://stackoverflow.com/questions/37450774/grails-2-5-3-not-binding-float-fields-correctly/37454016
   String units
}