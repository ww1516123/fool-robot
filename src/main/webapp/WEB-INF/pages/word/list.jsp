<%--
  Created by IntelliJ IDEA.
  User: ranchaowen
  Date: 15/7/12
  Time: 下午3:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path=request.getContextPath();%>
<html>
<head>
  <title></title>
  <script type="text/javascript" src="<%=path%>/common/jquery-1.11.2.min.js"></script>
  <script type="text/javascript">
    $(function(){
      var url='<%=path%>/spi/word/all';
      $.ajax({
        type: "get",
        url: url,
        data: {page:0,size:10},
        //contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
          var options=[];
          $.each(data,function(i,item){
            options.push("<tr><td>"+item.id+"</td><td>"+item.words+"</td><td>"+item.eWords+"</td></tr>");
          });
          $("#table").html(options.join());
        },
        error: function (msg) {
          alert(msg);
        }
      });
      $("#submit").click(function () {
        var url='<%=path%>/spi/word/add';
        var data= $("#form").serialize();
        $.ajax({
          type: "post",
          url: url,
          data: data,
          //contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function (data) {
            if(data.state){
              alert(data.msg);
              $("#submit").reset();
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

<div>
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

</div>


<form id="form"  action="<%=path%>/spi/word/add">

  <p>
    <label>词语:</label>
    <input type="text" name="words" >
  </p>

  <p>
    <label>英文:</label>
    <input type="text" name="eWords">
  </p>
  <p>
    <input type="reset" value="重置">
    <input type="button"  id="submit" value="保存">
  </p>
</form>

</body>
</html>
