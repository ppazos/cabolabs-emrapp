package emr

//@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2')

import groovyx.net.http.*
//import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.XML
import java.text.SimpleDateFormat
import sesion.ClinicalSession
import xml.XmlSerializer

import grails.util.Holders

class CommitJob {
   
   def config = Holders.config
   
   static triggers = {
      simple repeatInterval: 30000l // execute job once in 100 seconds
   }

   def execute() {
      
      // execute job
      println "JOB! "+ new Date().toString()
    
      def csess = ClinicalSession.findAllByOpenAndCommitted(false, false)
      
      def serializer
      def serializedDocs // List<String>
      def versions = "" // <versions><version>..</version>...</versions>
      
      // http://groovy.codehaus.org/modules/http-builder/doc/rest.html
      def ehr = new RESTClient(config.server.protocol + config.server.ip +':'+ config.server.port + config.server.path)
      def token
      
      def res // response de requests
      def ehrUid // para lookup
      
      // path para pedir el ehrUid del patientUid
      // rest/ehrForSubject ?subjectUid&format
         
      // path para el commit
      // ehr/commit ?ehrUid
      
      Random random = new Random()
      csess.each { cses ->
         
         token = cses.authToken
         
         println "Commit de sesion: "+ cses.id + " para patUid: "+ cses.patientUid
         
         // Serializa a XML los documentos que estan en la sesion clinica
         serializer = new XmlSerializer(cses)
         serializedDocs = serializer.serializeSessionDocs()
         
         // Arma parametros para el request HTTP
         serializedDocs.eachWithIndex { serDoc, i ->
         
            // logging
            def filename = 'committed' + File.separator +'composition_'+ random.nextInt(10000) +'_'+ i +'.xml'
            def compoFile = new File(filename)
            compoFile << serDoc
            
            println "save $filename"
            // /logging
         
            // Remove namespace declarations for version, will be added to versions
            serDoc = serDoc.replace(' xmlns="http://schemas.openehr.org/v1"', '')
            serDoc = serDoc.replace(' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"', '')
            
            versions += serDoc
         }
         
         // lookup de ehrUid
         try
         {
            res = ehr.get(
                path:'rest/ehrForSubject',
                query:[subjectUid:cses.patientUid, format:'json'],
                headers:['Authorization': 'Bearer '+ token]
            )
            
            // FIXME: el paciente puede existir y no tener EHR, verificar si devuelve el EHR u otro error, ej. paciente no existe...
            ehrUid = res.data.uid
         }
         catch (Exception e)
         {
            // si es una except por que dio error el request la except tiene response,
            // pero si es una except por otro error, no va a tener response
            //
            //  No such property: response for class: org.apache.http.conn.HttpHostConnectException
            // TODO: preguntar porque en los ejemplos del sitio web usan exception.response pero la except no tiene ese atributo
            /*
            if (e?.response.status == 404)
            {
               // TODO> ver que hacer con los errores
            }
            */
            
            println "except 1: "+ e.message
            return // no debe serguir si falla el lookup
         }

         println "ehrUid: $ehrUid"
         //println "Commit!"
       
         //println "versions: "+ params
         
         // post de commit de versions
         try
         {
            //params['ehrUid'] = ehrUid
            //params['auditSystemId'] = 'CABOLABS_EHR' // TODO: should be configurable
            //params['auditCommitter'] = cses.composer.name
            // TODO: auditCommitterId
            
            def commit_versions ='<versions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://schemas.openehr.org/v1">'+ versions +'</versions>'
            
            def versions_file = new File('committed' + File.separator +'versions_'+ random.nextInt(10000) +'_.xml')
            versions_file << commit_versions
            
            // Sin URLENC da error null pointer exception sin mas datos... no se porque es. PREGUNTAR!
            res = ehr.post(
               path:'rest/commit',
               requestContentType: XML,
               query:  [
                  ehrUid: ehrUid,
                  auditSystemId: 'CABOLABS_EHR',
                  auditCommitter: cses.composer
               ],
               headers:['Authorization': 'Bearer '+ token],
               body: commit_versions
            )
               
            /*
             * result {
                  type {
                     code('AR')                         // application reject
                     codeSystem('HL7::TABLES::TABLE_8') // http://amisha.pragmaticdata.com/~gunther/oldhtml/tables.html
                  }
                  message('El parametro versions es obligatorio')
                  code('ISIS_EHR_SERVER::COMMIT::ERRORS::401') // sys::service::concept::code
               }
             */
            println "res: " + res.responseData.message
            //println "res2: " + res.responseData.getClass() // nodeChild
            //println "res3: " + res.responseData.name() // result
            
            if (res.responseData.type.code.text() != "AA")
            {
               throw new Exception("Server rejected the commit")
            }
         }
         catch (Exception e)
         {
            // FIXME: log a disco
            println "except 2:" + e.message
            
            e.printStackTrace(System.out)
            
            println "3"
            
            /*
               if (e?.response.status == 404)
               {
                  // TODO> ver que hacer con los errores
               }
            */
            
            return // si falla el commit no debe marcar como commiteado
         }
            
         // TODO:
         // REQUEST AL SERVER...
         // Y SI ESTA TODO BIEN:
         
         cses.committed = true
         if (!cses.save(flush:true))
         {
            println cses.errors
         }
      }
      
      /*
      // URL Configurable
      def http = new HTTPBuilder('http://localhost:8080/')
      
      // FIXME: seleccionar los docs no enviados (TODO: hacer log)
      // TODO: los docs creados en la misma sesion deberian comitearse juntos en la misma contribution para el mismo EHR.
      def docs = Document.list()
      def ser = new xml.XmlSerializer()
      def versionText
      
      docs.each { doc ->
      
         versionText = ser.toXml( doc, true )
      
         // auth omitted...
         http.request( POST, XML ) {
            
           //uri.path = 'update.xml'
           uri.path = 'ehr/ehr/commit'
           uri.query = [ ehrId:'asdasdfads' ] // FIXME: deberia tener la referencai al EHR para el que se crean los docs
           
           body =  [ verions : versionText ]
           
           requestContentType = ContentType.URLENC // ???
          
           response.success = { resp ->
             println "Tweet response status: ${resp.statusLine}"
             assert resp.statusLine.statusCode == 200
           }
         }
      }
      */
   }
}
