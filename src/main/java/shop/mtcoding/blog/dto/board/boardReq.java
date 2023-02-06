package shop.mtcoding.blog.dto.board;

import lombok.Getter;
import lombok.Setter;

public class boardReq {
    
    // 인증과 관련된것이 아닌것은 BoardSaveReqDto 처럼 앞에 Board를 붙여서 기능을 구분해줌
    @Setter
    @Getter
    public static class BoardSaveReqDto{
        private String title;
        private String content;

    }
}
