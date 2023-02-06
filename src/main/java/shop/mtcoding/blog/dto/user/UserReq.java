package shop.mtcoding.blog.dto.user;

import lombok.Getter;
import lombok.Setter;

public class UserReq {
    
    // static 으로 내부 클래스 만들면 외부에서 띄울 수 있고 한곳에서 관리하기 때문에 편함 (선생님 방식)
    @Setter
    @Getter
    public static class JoinReqDto{
        private String username;
        private String password;
        private String email;
    }

    @Setter
    @Getter
    public static class LoginReqDto {
        private String username;
        private String password;
    }
}
