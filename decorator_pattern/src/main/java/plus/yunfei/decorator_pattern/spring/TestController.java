package plus.yunfei.decorator_pattern.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author houyunfei
 */
@RestController
public class TestController {

    @PostMapping("/test")
    public Map<String, Object> test(@TimestampRequestBody Map<String, Object> body) {
        return body;
    }
}

@SpringBootApplication
class Test {
    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }
}

