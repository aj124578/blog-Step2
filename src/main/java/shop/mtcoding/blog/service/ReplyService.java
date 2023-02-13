package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.ReplyRepository;

@Slf4j
@Service
public class ReplyService {
    
    @Autowired
    private ReplyRepository replyRepository;

    @Transactional // insert가 동시에 안되고 메서드 자체는 동시에 가능함. 하지만 동시접근 불가능으로 생각하면
    public void 댓글쓰기(ReplySaveReqDto replySaveReqDto, int principalId){

        int result = replyRepository.insert(
                replySaveReqDto.getComment(), replySaveReqDto.getBoardId(), principalId);
        if (result != 1) {// 이거를 controller가 받아서 처리 할 필요가 없으므로 void로 여기서 처리하고 끝냄
            throw new CustomException("회원가입실패");
        }
    }

    
    @Transactional
    public void 댓글삭제(int id, int principalId) {
        Reply reply = replyRepository.findById(id); // reply : db에서 받은 데이터를 저장하는곳 즉, 모델이 됨
        if (reply == null) {
            throw new CustomApiException("댓글이 존재하지 않습니다."); // bad request니까 default로 설정되어 있어 안넣어도 됨
        }
        if (reply.getUserId() != principalId) {
            throw new CustomApiException("댓글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    
    // 1. 인증 ok, 2. 댓글 존재유무확인, 3. 권한 ok -> aop를 쓰면 이런 부가적인 코드를 자동화 시킬 수 있음

    // 제어가 안될땐 내가 try catch로 묶음

    try {
        replyRepository.deleteById(id);    
    } catch (Exception e) {
        log.error("서버에러 : " + e.getMessage());
        // 버퍼달고, 파일에 쓰기 | 어노테이션 쓰면 로그를 찍을때마다 파일에 써줌

        System.out.println("서버에러 : " + e.getMessage());
        throw new CustomApiException("댓글 삭제 실패 - 서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    }


}
