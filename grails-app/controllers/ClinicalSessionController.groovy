import sesion.ClinicalSession

class ClinicalSessionController {

   def list()
   {
      def list = ClinicalSession.list()
      
      // Ir al listado de sesiones clinicas significa salir de la sesion clinica actual (un paciente seleccionado)
      session.clinicalSession = null
      
      [list: list, total:ClinicalSession.count()]
   }
}