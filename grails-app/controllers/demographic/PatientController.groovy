package demographic

//@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2')
import groovyx.net.http.*
import sesion.ClinicalSession
import org.codehaus.groovy.grails.commons.ApplicationHolder

class PatientController {

   def config = ApplicationHolder.application.config.app
   
   def index()
   {
      render "index!"
   }
   
   // TODO:
   //  1. llamar a esta por ajax desde la view
   //  2. el render se hace en el controller contra un template
   //  3. el response ajax es el html ya concinado (no hay procesamiento de la info en la vista = mas seguro xq el usuario no conoce el protocolo interno)
   //  4. hay que soportar paginacion
   //  5. hacer pruebas de que es mas rapido> pedir y procesar xml o json
   def list()
   {
      println "patient list"
      
      // Ir al listado de pacientes significa salir de la sesion clinica (un paciente seleccionado)
      session.clinicalSession = null
   }
   
   // se llama por ajax desde list view
   def patientList()
   {
      /**
       * Con timeout:
       *
       * def http = new HTTPBuilder( args[0] )
        http.request(GET,HTML) { req ->
          headers.'User-Agent' = 'GroovyHTTPBuilderTest/1.1'
          headers.'Referer' = 'http://blog.techstacks.com/'
      
          // Begin: java code to set HTTP Parameters/Properties
          // Groovy HTTPBuilder doesn't provide convenience methods
          // for many of these yet.
          req.getParams().setParameter("http.connection.timeout", new Integer(5000));
          req.getParams().setParameter("http.socket.timeout", new Integer(5000));
          // End java code to set HTTP Parameters/Properties
      
          response.success = { resp, html ->
            println "Server Response: ${resp.statusLine}"
            println "Server Type: ${resp.getFirstHeader('Server')}"
            println "Title: ${html.HEAD.TITLE.text()}"
          }
          response.failure = { resp ->
            println resp.statusLine
          }
        }
       */
      
      // TIMER
      def start = System.currentTimeMillis()
      
      
      def patientList = []
      
      println "Consulta al EHR: "+ config.ehr_ip + ':8090'
      
      //def http = new HTTPBuilder('http://localhost:8090/ehr/rest/patientList')
      // Maquina virtual en PABII
      //def http = new HTTPBuilder('http://192.168.1.101:8090/ehr/rest/patientList')
      def http = new HTTPBuilder('http://'+ config.ehr_ip +':8090/ehr/rest/patientList')
      
      // Si no hay conexion con el servidor tira excepcion
      try
      {
         // perform a GET request, expecting TEXT response data
         http.request( Method.GET, ContentType.JSON ) { req ->
           
           //uri.path = '/ajax/services/search/web'
           //uri.query = [ v:'1.0', q: 'Calvin and Hobbes' ]
           uri.query = [ format: 'json' ]
         
           headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
           
           // Begin: java code to set HTTP Parameters/Properties
           // Groovy HTTPBuilder doesn't provide convenience methods
           // for many of these yet.
           req.getParams().setParameter("http.connection.timeout", new Integer(10000));
           req.getParams().setParameter("http.socket.timeout", new Integer(10000));
           // End java code to set HTTP Parameters/Properties
         
           // response handler for a success response code:
           response.success = { resp, json ->
           
              // con XML, xml es groovy.util.slurpersupport.NodeChild
              // con TEXT, xml es InputStreamReader con .text me da el string
              
              //println json
              /*
              data = [
                 patients: [...],
                 pagination: [
                    max, offset, nextOffset, prevOffset
                 ]
              ]
              */
              
              //println xml.uid // on json los datos estan como maps
              patientList = json.patients
           }
         
           // handler for any failure status code:
           response.failure = { resp ->
              
              println "response: " + resp.getAllHeaders() + " curr thrd id: " + Thread.currentThread().getId()
              
              //println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
              throw new Exception("Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}")
           }
         }
      
      }
      catch (Exception e)
      {
         println e.message
         flash.message = 'En este momento no hay conexion con el servidor demografico, vuelva a intentarlo mas tarde'
         
         // te doy datos de mentira :D
         patientList = [
            [uid:'1235-5756', firstName:'Carlos', lastName:'Nuñez',  dob:'19260225', sex:'M', idCode:'123456', idType:'CI'],
            [uid:'2556-3434', firstName:'Petra',  lastName:'Cabeza', dob:'19990912', sex:'F', idCode:'569790', idType:'CI'],
            [uid:'3457-2443', firstName:'Ronco',  lastName:'Vaca',   dob:'19810230', sex:'M', idCode:'247288', idType:'CI'],
         ]
      }
      
      // TIMER
      def now = System.currentTimeMillis()
      println "listPatients - execution time: " + (now - start) + " ms"

      render (template:'list_table', model:[patientList: patientList])
   }
   
   
}