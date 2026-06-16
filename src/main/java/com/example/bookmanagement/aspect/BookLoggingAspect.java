package com.example.bookmanagement.aspect;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BookLoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookLoggingAspect.class);

    @Pointcut("""
            execution(* com.example.bookmanagement.service.BookService.create(..)) ||
            execution(* com.example.bookmanagement.service.BookService.update(..)) ||
            execution(* com.example.bookmanagement.service.BookService.patch(..))
            """)
    public void createOrUpdateBookAction() {
    }

    @Before("createOrUpdateBookAction()")
    public void logBeforeCreateOrUpdate(JoinPoint joinPoint) {
        LOGGER.info("Đang xử lý phương thức: {}", joinPoint.getSignature().getName());
        LOGGER.info("Dữ liệu đầu vào: {}", Arrays.toString(joinPoint.getArgs()));
    }
}
