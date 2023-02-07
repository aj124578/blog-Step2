package shop.mtcoding.blog.handler.ex;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException{

    private HttpStatus status;

    public CustomApiException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }

    public CustomApiException(String msg) {
       this(msg, HttpStatus.BAD_REQUEST);
       // this로 본인의 생성자를 호출해서 넣어줌
       
    }
}
