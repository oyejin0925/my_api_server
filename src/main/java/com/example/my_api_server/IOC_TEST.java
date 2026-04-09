package com.example.my_api_server;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //컨트롤러로 등록하겠다
@RequestMapping("/test") //api 서버 주소
@RequiredArgsConstructor //DI를 자동으로 해주는 어노테이션(생성자 주입 방식)
public class IOC_TEST {
    /*
    //DI 1. 필드 주입(잘안씀)
    @Autowired
    private IOC ioc2;

    //DI 2. Setter(수정자) 주입 방식(잘안씀)
    public IOC setIoc(IOC ioc) {
        ioc2 = ioc;
        return ioc2;
    }
    @Autowired
    public IOC setIOC2(IOC ioc) {
        ioc2 = ioc;
        return ioc2;

    }

    //DI 3. 생성자 주입 방식(생성할 때 자동으로 주입받음) (주로 사용)
    //이 방법을 어노테이션으로 사용하는 방법 = RequiredArgsConstructor <- 얘로 실무에서 사용
    public void IOC(IOC ioc) {
        ioc2 = ioc;
    }
*/
    //final: 불변성 객체를 변경할 수 없음
    private final IOC ioc; //개발자가 생성x, 스프링이 객체(Bean)을 주입(DI) 해줬다 : 이 전체개념 = IOC

    //정리: private final, RequiredArgsConstructor 사용
    @GetMapping
    public void iocTest() {
        ioc.func1();
    }
}
