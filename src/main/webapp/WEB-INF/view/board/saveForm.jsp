<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <%@ include file="../layout/header.jsp" %>

        <div class="container my-3">
            <form class="mb-1">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Enter title"  id="title">
                </div>

                <div class="form-group">
                    <textarea class="form-control summernote" rows="5" id="content"></textarea>
                </div>
                <button onclick = "save()" type="button" class="btn btn-primary">글쓰기완료</button>
            </form>
        </div>

        <script> 
                function save(){
                    let data = {
                        "title":$("#title").val(),
                        "content":$("#content").val()
                    }
                        console.log(data)
                    $.ajax({
                        type:"post",
                        url:"/board",
                        data: JSON.stringify(data),
                        contentType:"application/json; charset=UTF-8",
                        dataType:"json" // default : 응답의 mime 타입으로 유추함
                    }).done((res) => { 
                        
                        alert(res.msg);
                        location.href="/";
                    }).fail((err) => { 
                        console.log(err);
                        alert(err.responseJSON.msg);
                    });
                }
        </script>

        <script>
            $('.summernote').summernote({
                tabsize: 2,
                height: 400
            });
        </script>

        <%@ include file="../layout/footer.jsp" %>