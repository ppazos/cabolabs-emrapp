package ehr

import groovyx.net.http.*
import grails.util.Holders

class EhrService {

   // TODO: operation here and job to free the cache
   def cache = [:]

   def config = Holders.config
   def server_url = config.server.protocol + config.server.ip +':'+ config.server.port + config.server.path
   
   def login(String username, String password, String orgnumber)
   {
      // service login
      // set token on session
      def ehr = new RESTClient(server_url)
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
         println "except login:" + e.message
         e.printStackTrace(System.out)
      }
   }
   
   /**
    * helps to get the uid of the current org to create ehrs for that org.
    * @param username
    * @return
    */
   def profile(String token, String username)
   {
      def ehr = new RESTClient(server_url)
      ehr.headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
      ehr.headers.'Authorization' = 'Bearer '+ token
      try
      {
         // Sin URLENC da error null pointer exception sin mas datos... no se porque es. PREGUNTAR!
         def res = ehr.get(
            path: 'rest/profile/'+username,
            query: [format: 'json']
         )
         
         //println "profile res:"+ res.responseData.toString()
         //println res.responseData.getClass()
         
         // username, email, organizations
         return res.responseData
      }
      catch (Exception e)
      {
         // FIXME: log a disco
         println "except profile:" + e.message
         e.printStackTrace(System.out)
      }
   }
   
   def createEhr(String token, String subjectUid)
   {
      // service login
      // set token on session
      def ehr = new RESTClient(server_url)
      ehr.headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
      ehr.headers.'Authorization' = 'Bearer '+ token
      try
      {
         // Sin URLENC da error null pointer exception sin mas datos... no se porque es. PREGUNTAR!
         def res = ehr.post(
            path:'rest/ehrs',
            requestContentType: ContentType.URLENC,
            body: [subjectUid: subjectUid]
         )
         
         // ehr
         return res.responseData
      }
      catch (Exception e)
      {
         // FIXME: log a disco
         println "post ehrs:" + e.message
         e.printStackTrace(System.out)
      }
   }
   
   def getCompositions(String token, String ehrUid)
   {
      def res = []
      try
      {
         def ehr = new RESTClient(server_url)
         
         // custom handlers to access the raw response from the reader
         ehr.handler.failure = { resp, reader ->
            println "failure handler"
            [response:resp, reader:reader]
         }
         ehr.handler.success = { resp, reader ->
            println "success handler"
            [response:resp, reader:reader]
         }
         
         def data = ehr.get( path: 'rest/compositions',
                        query: [ehrUid: ehrUid, format: 'json'],
                        headers: ['Authorization': 'Bearer '+ token] )
         
         res = data.reader.result
         
         // JSON
         //println res.reader.toString()
         /*
          * {
               "result": [{
                  "id": 1,
                  "archetypeId": "openEHR-EHR-COMPOSITION.signos.v1",
                  "category": "event",
                  "dataIndexed": true,
                  "ehrUid": "11111111-1111-1111-1111-111111111111",
                  "lastVersion": true,
                  "organizationUid": "d6b7bc9b-4909-4b80-ad8b-4f2c8ffa985c",
                  "startTime": "2016-05-24 01:00:13",
                  "subjectId": "11111111-1111-1111-1111-111111111111",
                  "templateId": "Signos",
                  "uid": "e20e3cc9-5def-47e6-a0d2-96cbf6675698"
               }],
               "pagination": {
                  "max": 30,
                  "offset": 0,
                  "nextOffset": 30,
                  "prevOffset": 0
               }
            }
          */
         
         // XML
         //println groovy.xml.XmlUtil.serialize(res.reader)
         /* for reader class NodeChild (format = xml)
          * <?xml version="1.0" encoding="UTF-8"?>
          * <map>
              <entry key="result">
                <compositionIndex id="1">
                  <archetypeId>openEHR-EHR-COMPOSITION.signos.v1</archetypeId>
                  <category>event</category>
                  <dataIndexed>true</dataIndexed>
                  <ehrUid>11111111-1111-1111-1111-111111111111</ehrUid>
                  <lastVersion>true</lastVersion>
                  <organizationUid>d6b7bc9b-4909-4b80-ad8b-4f2c8ffa985c</organizationUid>
                  <startTime>2016-05-24 01:00:13</startTime>
                  <subjectId>11111111-1111-1111-1111-111111111111</subjectId>
                  <templateId>Signos</templateId>
                  <uid>e20e3cc9-5def-47e6-a0d2-96cbf6675698</uid>
                </compositionIndex>
              </entry>
              <entry key="pagination">
                <entry key="max">30</entry>
                <entry key="offset">0</entry>
                <entry key="nextOffset">30</entry>
                <entry key="prevOffset">0</entry>
              </entry>
            </map>
          */
         
         //println res.response.data.name() // map
      }
      catch (Exception e)
      {
         // TODO: log a disco
         render "Ocurrio un error al obtener los registros del paciente "+ e.message
         return res
      }
      
      return res
   }
   
   

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
