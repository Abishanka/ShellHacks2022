package main.controller

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestBody, RequestMapping, ResponseBody, RestController, CrossOrigin}
import abi.dex.AbiDex
import main.models.optionModel
import play.api.libs.json.Json

@RestController
@CrossOrigin(origins = Array("*"), allowedHeaders = Array("*"))
class optionController {

    @CrossOrigin(origins = Array("http://localhost:3000"), allowCredentials = "false")
    @PostMapping(path = Array("/option"))
    def result(@RequestBody op : String) : String = {  
        var qry = Json.parse(op)("qry").as[String]
        var secId = Json.parse(op)("securityId").as[String]

        println("OPTION: " + qry +" " + secId)
        // var data = Json.parse(op)("data").as[String]

        // if(data == null){
        //     return ""
        // }
        // AbiDex.init(dirty = true)
        //var result = AbiDex.queryJSONString(data,count=2)

        //return result
        return "success"
    }
}