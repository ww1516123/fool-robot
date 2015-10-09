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
    setInterval(getAll,1500);
    var total=0;
    $(function(){
      $("#submit").click(function () {
        var url='<%=path%>/spi/talk/loop';
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
    function getAll(){
      var url='<%=path%>/spi/word/all';
      var page=0;
      var size=20;
      if(total>0){
        page=total-1;
      }
      $.ajax({
        type: "get",
        url: url,
        data: {page:page,size:size},
        //contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
          var options=[];
          total=data.total;
          $("#total").html("一共有词语:"+((total-1)*20+data.data.length)+" 个");
          $.each(data.data,function(i,item){
            options.push("<tr><td>"+item.id+"</td><td>"+item.words+"</td><td>"+item.eWords+"</td></tr>");
          });
          $("#table").html(options.join());
        },
        error: function (msg) {
          alert(msg);
        }
      });
    }

  </script>
</head>
<body>

<%--<input id="question" name="question" style="width: 200px;"> <input type="button" id="submit" value="提问">--%>
<%--<p>--%>
<%--<div id="answer"></div>--%>


<div>
  =========最后10条=========
  <table>
    <thead>
    <tr>
      <th>序号</th>
      <th>词语</th>
      <th>英语</th>
    </tr>
    </thead>
    <tbody id="table">

    </tbody>

  </table>
  <span id="total"></span>

</div>
</body>
</html>
