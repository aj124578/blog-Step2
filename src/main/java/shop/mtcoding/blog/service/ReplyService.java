package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.controller.ReplyController.ReplySaveReqDto;
import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.ReplyRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;

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


}
