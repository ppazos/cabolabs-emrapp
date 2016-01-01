package ehr

import groovyx.net.http.*
import grails.util.Holders

class EhrService {

   // TODO: operation here and job to free the cache
   def cache = [:]

   def config = Holders.config
   
   
   def login(String username, String password, String orgnumber)
   {
      // service login
      // set token on session
      def ehr = new RESTClient(config.server.protocol + config.server.ip +':'+ config.server.port + config.server.path)
      try
      {
         // Sin URLENC da error null pointer exception sin mas datos... no se porque es. PREGUNTAR!
         def res = ehr.post(
            path:'rest/login',
            requestContentType: ContentType.URLENC,
            body: [username: username, password: password, organization: orgnumber]
         )
         
         // token
         return res.responseData.token
      }
      catch (Exception e)
      {
         // FIXME: log a disco
         println "except 2:" + e.message
         e.printStackTrace(System.out)
      }
   }
   
   
   /**
    * Get a list of patients from the server.
    * @return
    */
   def getPatients(String token, int max = 20)
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
      
      log.info( "Consulta al EHR: "+ config.server.protocol + config.server.ip +':'+ config.server.port + config.server.path +'rest/patientList')
      
      def http = new HTTPBuilder(config.server.protocol + config.server.ip +':'+ config.server.port + config.server.path +'rest/patientList')
      
      // Si no hay conexion con el servidor tira excepcion
      try
      {
         // perform a GET request, expecting TEXT response data
         http.request( Method.GET, ContentType.JSON ) { req ->
           
           //uri.path = '/ajax/services/search/web'
           //uri.query = [ v:'1.0', q: 'Calvin and Hobbes' ]
           uri.query = [ format: 'json', max: max ] // TODO: max/offset are not implemented yet
         
           headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
           headers.'Authorization' = 'Bearer '+ token
           
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
              
              // Caches the result
              patientList.each { patient ->
                 cache[patient.uid] = patient
              }
           }
         
           // handler for any failure status code:
           response.failure = { resp ->
              
              println "response: " + resp.getAllHeaders() + " curr thrd id: " + Thread.currentThread().getId()
              
              //println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
              throw new Exception("Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}")
           }
         }
      }
      catch (org.apache.http.conn.HttpHostConnectException e) // no hay conectividad
      {
         log.error ( e.message )
         //g.message(code:'patient.patientList.error.noServer') )
      }
      catch (groovyx.net.http.HttpResponseException e) // hay conectividad pero da un error del lado del servidor
      {
         // TODO: log a disco
         log.error ( e.message )
         // g.message(code:'patient.patientList.error.serverError')
         
         /*
         // te doy datos de mentira :D
         patientList = [
            [uid:'1235-5756', firstName:'Carlos', lastName:'Nuñez',  dob:'19260225', sex:'M', idCode:'123456', idType:'CI'],
            [uid:'2556-3434', firstName:'Petra',  lastName:'Cabeza', dob:'19990912', sex:'F', idCode:'569790', idType:'CI'],
            [uid:'3457-2443', firstName:'Ronco',  lastName:'Vaca',   dob:'19810230', sex:'M', idCode:'247288', idType:'CI'],
         ]
         */
      }
      
      return patientList
   }
   
   /**
    * Get a patient by uid from the server or local cache.
    * @param uid
    * @return
    */
   def getPatient(String uid, String token)
   {
      if (cache[uid])
      {
         log.info("Cache hit for patient $uid")
         return cache[uid]
      }
      
      log.info("Cache miss for patient $uid, querying the server")
      
      def http = new HTTPBuilder(config.server.protocol + config.server.ip +':'+ config.server.port + config.server.path +'rest/getPatient')
      
      // Si no hay conexion con el servidor tira excepcion
      try
      {
         // perform a GET request, expecting TEXT response data
         http.request( Method.GET, ContentType.JSON ) { req ->
            
            //uri.path = '/ajax/services/search/web'
            //uri.query = [ v:'1.0', q: 'Calvin and Hobbes' ]
            uri.query = [ format: 'json', uid: uid ]
          
            headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
            headers.'Authorization' = 'Bearer '+ token
            
            // Begin: java code to set HTTP Parameters/Properties
            // Groovy HTTPBuilder doesn't provide convenience methods
            // for many of these yet.
            req.getParams().setParameter("http.connection.timeout", new Integer(10000));
            req.getParams().setParameter("http.socket.timeout", new Integer(10000));
            // End java code to set HTTP Parameters/Properties
          
            // response handler for a success response code:
            response.success = { resp, json ->
            
               //println json
               /* es un map...
               [uid:3fe33dee-0a9a-43cd-a2b7-ce88b25734ba,
               firstName:Pablo,
               lastName:Pazos,
               dob:19811024,
               sex:M,
               idCode:4116238-0,
               idType:CI]
               */
               cache[uid] = json
            }
          
            // handler for any failure status code:
            response.failure = { resp ->
               
               println "response: " + resp.getAllHeaders() + " curr thrd id: " + Thread.currentThread().getId()
               
               //println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
               throw new Exception("Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}")
            }
         }
      }
      catch (org.apache.http.conn.HttpHostConnectException e) // no hay conectividad
      {
         println e.message
         flash.message = g.message(code:'registros.list.error.noServer')
      }
      catch (groovyx.net.http.HttpResponseException e) // hay conectividad pero da un error del lado del servidor
      {
         // TODO: log a disco
         println e.message
         flash.message = g.message(code:'registros.list.error.serverError')
      }
      
      return cache[uid]
      
   } // getPatient
   
   
   def String getEhrIdByPatientId(String patientUid, String token)
   {
      def res
      def ehrUid
      
      // Pide datos al EHR Server
      def ehr = new RESTClient(config.server.protocol + config.server.ip +':'+ config.server.port + config.server.path)
      
      
      // Lookup de ehrId por subjectId
      // FIXME: esto se puede evitar si viene el dato con el paciente
      try
      {
         // Si ocurre un error (status >399), tira una exception porque el defaultFailureHandler asi lo hace.
         // Para obtener la respuesta del XML que devuelve el servidor, se accede al campo "response" en la exception.
         res = ehr.get( path: 'rest/ehrForSubject',
                        query: [subjectUid:patientUid, format:'json'],
                        headers: ['Authorization': 'Bearer '+ token] )
         
         // FIXME: el paciente puede existir y no tener EHR, verificar si devuelve el EHR u otro error, ej. paciente no existe...
         // WONTFIX: siempre tira una excepcion en cada caso de error porque el servidor tira error 500 not found en esos casos.
         ehrUid = res.data.uid
      }
      catch (org.apache.http.conn.HttpHostConnectException e) // no hay conectividad
      {
         log.error( e.message )
         return
      }
      catch (groovyx.net.http.HttpResponseException e)
      {
         // puedo acceder al response usando la excepci�n!
         // 500 class groovyx.net.http.HttpResponseDecorator
         println e.response.status.toString() +" "+ e.response.class.toString()
         
         // errorEHR no encontrado para el paciente $subjectId, se debe crear un EHR para el paciente
         println e.response.data
         
         // WARNING: es el XML parseado, no el texto en bruto!
         // class groovy.util.slurpersupport.NodeChild
         println e.response.data.getClass()
         
         // Procesando el XML
         println e.response.data.code.text() // error
         println e.response.data.message.text() // el texto
         
         // text/xml
         println e.response.contentType
         
         // TODO: log a disco
         // no debe serguir si falla el lookup
         //render "Ocurrio un error al obtener el ehr del paciente "+ e.message
         log.error( e.response.data.message.text() )
         return
      }
      
      return ehrUid
      
   } // getEhrIdByPatientId
}
