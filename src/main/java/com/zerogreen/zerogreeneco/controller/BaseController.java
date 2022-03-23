package com.zerogreen.zerogreeneco.controller;

import com.zerogreen.zerogreeneco.entity.userentity.VegetarianGrade;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// 컨트롤러 전체에서 발생하는 예외를 한 곳에서 관리하고 처리하는 어노테이션
// ModelAttribute를 사용할 경우, 컨트롤러 전체에서 사용할 수 있다.
@ControllerAdvice
public class BaseController {
    @ModelAttribute("allPartVegan")
    public VegetarianGrade[] vegetarianGrades() {
        VegetarianGrade[] vegans = VegetarianGrade.values();
        return vegans;
    }

}
