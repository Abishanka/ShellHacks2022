package main.controller

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestBody, RequestMapping, ResponseBody, RestController, CrossOrigin}
import abi.dex.AbiDex
import play.api.libs.json.Json

@RestController
@CrossOrigin(origins = Array("*"), allowedHeaders = Array("*"))
class mainController {

    @CrossOrigin(origins = Array("http://localhost:3000"), allowCredentials = "false")
    @PostMapping(path = Array("/query"))
    def result(@RequestBody qry : String) : String = {     
        println(qry)   
        var data = Json.parse(qry)("param").as[String]
        if(data == null){
            return ""
        }
        var result = AbiDex.queryJSONString(data,count=15)

        return result
    }
}