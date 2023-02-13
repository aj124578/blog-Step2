package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.ReplyService;

@Controller
public class ReplyController {
    
    @Autowired
    private HttpSession session;

    @Autowired
    private ReplyService replyService;

    /* 댓글 삭제 */
    @DeleteMapping("/reply/{id}")
    public @ResponseBody ResponseEntity<?> deleteReply(@PathVariable int id){
        // 1. 인증 체크 | 나중에는 aop or 인터셉트로 처리할거라서 안 적어도 됨
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }
        replyService.댓글삭제(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto(1, "댓글 삭제 성공", null), HttpStatus.OK);
    }

    /* 댓글 쓰기 */
    @PostMapping("/reply")
    public String save(ReplySaveReqDto replySaveReqDto){

        // 1. 인증 체크
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); 
        }

        if (replySaveReqDto.getComment() == null || replySaveReqDto.getComment().isEmpty()) {
            throw new CustomException("comment를 작성해주세요");
        }

        if (replySaveReqDto.getBoardId() == null) {
            throw new CustomException("boardId가 필요합니다.");
        }

        // 서비스 호출 (replySaveReqDto, principal.getId())
        replyService.댓글쓰기(replySaveReqDto, principal.getId());
        
        return "redirect:/board/" + replySaveReqDto.getBoardId();
    }

    
}
