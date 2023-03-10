<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <%@ include file="../layout/header.jsp" %>

        <div class="container my-3">
            <c:if test="${boardDto.userId == principal.id}" >
            </c:if>
                <div class="mb-3">
                    <a href="/board/${boardDto.id}/updateForm" class="btn btn-warning">수정</a>
                    <button onclick="deleteById(${dto.id})" class="btn btn-danger">삭제</button>
                </div>
            
            <script> // java 스크립트안에서 el 표현식 쓰지말기, 쓰면 function을 따로 파일로 못뺌
                function deleteById(id){
                    $.ajax({
                        type:"delete",
                        url:"/board/"+id,
                        dataType:"json"
                    }).done((res) => { // 20x 일때
                        alert(res.msg);
                        location.href="/";
                    }).fail((err) => { // 40x, 50x 일때
                        alert(err.responseJSON.msg);
                    });
                }

            </script>

            <div class="mb-2">
                글 번호 : <span id="id"><i>${boardDto.id} </i></span> 작성자 : <span class="me-3"><i>${dto.username} </i></span>

                <i id="heart" class="fa-regular fa-heart my-xl my-cursor" value="no"></i>
            </div>

            <div>
                <h3>${boardDto.title}</h3>
            </div>
            <hr />
            <div>
                <div>${boardDto.content}</div>
            </div>
            <hr />

            <div class="card">
                <form action="/reply" method="post">
                    <input type="hidden" name="boardId" value="${boardDto.id}">
                    <div class="card-body">
                        <textarea name="comment" id="reply-comment" class="form-control" rows="1"></textarea>
                    </div>
                    <div class="card-footer">
                        <button type="submit" id="btn-reply-save" class="btn btn-primary">등록</button>
                    </div>
                </form>
            </div>
            <br />
            <div class="card">
                <div class="card-header">댓글 리스트</div>
                <ul id="reply-box" class="list-group">
                    <c:forEach items="${replyDtos}" var="reply">
                    <li id="reply-${reply.id}" class="list-group-item d-flex justify-content-between">
                        <div>${reply.comment}</div>
                        <div class="d-flex">
                            <div class="font-italic">작성자 : ${reply.username} </div>
                            <button onClick="deleteByReplyId(${reply.id})" class="badge bg-secondary">삭제</button>
                        </div>
                    </li>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <script>
            function deleteByReplyId(id) {
                // $("#reply-"+id).remove();
                // location.reload();
                
            }
        </script>

        <%@ include file="../layout/footer.jsp" %>