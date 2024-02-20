package LarionovOleksandrBackEndCapstone.D.DBlog.exceptions;


import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.errorsPayloads.ErrorsDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.errorsPayloads.ErrorsPayloadWhitList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsPayloadWhitList handleBadRequest(BadRequestException ex) {
        List<String> errorsMessages = new ArrayList<>();
        if(ex.getErrorList() != null)
            errorsMessages = ex.getErrorList().stream().map(errore -> errore.getDefaultMessage()).toList();
        return new ErrorsPayloadWhitList(ex.getMessage(), newDateAndHour(), errorsMessages);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorsDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsDTO(ex.getMessage(),newDateAndHour());
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsDTO accessDenied(AccessDeniedException ex){
        return new ErrorsDTO("Access Denied", newDateAndHour());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFound(NotFoundException ex) {
        return new ErrorsDTO(ex.getMessage(),newDateAndHour());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleGenericError(Exception ex) {
        ex.printStackTrace();
        return new ErrorsDTO("Work in progress!", newDateAndHour());
    }
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String faviconError(Exception ex){
        return ex.getMessage();
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<String> handlerLikeNotFoundException(LikeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    public static String newDateAndHour(){
        String pattern = "E, dd MMM yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }

}
