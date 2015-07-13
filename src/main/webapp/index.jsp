<%--
  Created by IntelliJ IDEA.
  User: ranchaowen
  Date: 15/7/12
  Time: 下午9:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path=request.getContextPath();%>
<html>
<head>
  <script type="text/javascript" src="<%=path%>/common/jquery-1.11.2.min.js"></script>
  <script type="text/javascript">
    $(function(){

      $("#submit").click(function () {
        var url='<%=path%>/spi/talk/question';

        $.ajax({
          type: "post",
          url: url,
          data: {question:$("#question").val()},
          //contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function (data) {
            if(data.state){
              $("#answer").html(data.msg);
              $("#question").val("");
            }else{
              alert(data.msg);
            }
          },
          error: function (msg) {
            alert(msg);
          }
        });
      });
    });

  </script>
</head>
<body>

<input id="question" name="question" style="width: 200px;"> <input type="button" id="submit" value="提问">
<p>
<div id="answer"></div>
</body>
</html>
