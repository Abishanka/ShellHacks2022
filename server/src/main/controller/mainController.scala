package main.controller

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestBody, RequestMapping, ResponseBody, RestController}

@RestController
class mainController {

    @GetMapping(path = Array("/query"))
    def result(qry : String) : String = {
        var result = "[{test: \"test\"}, {}]"

        return result
    }
}