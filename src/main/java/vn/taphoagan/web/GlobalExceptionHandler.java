package vn.taphoagan.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ModelAndView business(BusinessException ex) {
        ModelAndView mv = new ModelAndView("error/business");
        mv.addObject("message", ex.getMessage());
        mv.addObject("code", ex.getCode());
        mv.setStatus(HttpStatus.BAD_REQUEST);
        return mv;
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFound(NotFoundException ex) {
        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("message", ex.getMessage());
        mv.setStatus(HttpStatus.NOT_FOUND);
        return mv;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView denied(AccessDeniedException ex) {
        ModelAndView mv = new ModelAndView("error/403");
        mv.addObject("message", "Bạn không có quyền truy cập.");
        mv.setStatus(HttpStatus.FORBIDDEN);
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView generic(Exception ex) {
        log.error("Unhandled error", ex);
        ModelAndView mv = new ModelAndView("error/500");
        mv.addObject("message", "Có lỗi xảy ra. Vui lòng thử lại.");
        mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mv;
    }
}
