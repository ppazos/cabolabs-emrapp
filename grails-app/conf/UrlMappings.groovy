class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		//"/"(view:"/index")
      "/"( // por defecto va a login
         controller: 'user',
         action: 'login'
      )
		"500"(view:'/error')
	}
}
