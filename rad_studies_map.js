// Cuando selecciono la categoria se filtra a los tipos de estudio (codigo)
// Y la localizacion anatomica puede tener solo las localizaciones de los
// estudios dentro de la categoria seleccionada.

var rad_studies = [
  {value:'TC de la cabeza',      code:'24725-4', method:'CT'},
  {value:'TC tobillo izquierdo', code:'36426-5', method:'CT'},
  {value:'TC tobillo derecho',   code:'36428-1', method:'CT'},
  {value:'TC de torax',          code:'29252-4', method:'CT'},
  {value:'TC rodilla izquierda', code:'36225-1', method:'CT'},
  {value:'TC rodilla derecha',   code:'36227-7', method:'CT'},
  {value:'TC de cuello',         code:'36235-0', method:'CT'},
  {value:'TC de femur',          code:'36338-2', method:'CR'},
  {value:'RM de pecho',             code:'36283-0', method:'MRI'},
  {value:'RM cerebral',             code:'24590-2', method:'MRI'},
  {value:'RM abdominal',            code:'24556-3', method:'MRI'},
  {value:'RM de cuello',            code:'24839-3', method:'MRI'},
  {value:'RM de rodilla izquierda', code:'26200-6', method:'MRI'},
  {value:'RM de rodilla derecha',   code:'26201-4', method:'MRI'},
  {value:'ECO de senos',            code:'24601-7', method:'US'},
  {value:'ECO de pecho',            code:'24630-6', method:'US'},
  {value:'ECO de muñeca izquierda', code:'26280-8', method:'US'},
  {value:'ECO de muñeca derecha',   code:'26282-4', method:'US'},
  {value:'ECO de cuello',           code:'37918-0', method:'US'},
  {value:'ECO de ovarios',          code:'69390-3', method:'US'},
  {value:'RX de muñeca izquierda',  code:'26170-1', method:'XR'},
  {value:'RX de muñeca derecha',    code:'26171-9', method:'XR'},
  {value:'RX de columna dorsal',    code:'24940-9', method:'XR'},
  {value:'RX columna vertebral',    code:'24984-7', method:'XR'}
];