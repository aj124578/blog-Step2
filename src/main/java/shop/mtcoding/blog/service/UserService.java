package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Transactional // insert가 동시에 안되고 메서드 자체는 동시에 가능함. 하지만 동시접근 불가능으로 생각하면
    public int 회원가입(JoinReqDto joinReqDto){
        User sameUser = userRepository.findByUsername(joinReqDto.getUsername());
        if (sameUser != null) {
            throw new CustomException("동일한 username이 존재합니다");
        }
        int result = userRepository.insert(joinReqDto.getUsername(), joinReqDto.getPassword(), joinReqDto.getEmail());
        return result;
    }

    // 한번 봤던 데이터를 계속 보기위하여 readOnly를 걸어줌
    @Transactional(readOnly = true) // 연산되고 있을때 데이터가 변경되면 꼬일 수가 있기 때문에 트랜잭션해서 readOnly를 걸면 변경이 안되서 한번 봤던 데이터를 계속 보게 해줌 그래서 일반적으로는 select에서도 readonly를 걸어줌
    public User 로그인(LoginReqDto loginReqDto) {
        User principal = userRepository.findByUsernameAndPassword(
            loginReqDto.getUsername(), loginReqDto.getPassword()
        );

        if (principal == null) {
            throw new CustomException("유저네임 혹은 패스워드가 잘못 입력되었습니다");
        }
        
        return principal;
    }
}
