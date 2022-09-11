package main.controller

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestBody, RequestMapping, ResponseBody, RestController, CrossOrigin}

@RestController
@CrossOrigin(origins = Array("*"), allowedHeaders = Array("*"))
class mainController {

    @CrossOrigin(origins = Array("http://localhost:3000"), allowCredentials = "false")
    @PostMapping(path = Array("/query"))
    def result(qry : String) : String = {
        var result = "[{test: \"test\"}, {}]"

        return result
    }
}