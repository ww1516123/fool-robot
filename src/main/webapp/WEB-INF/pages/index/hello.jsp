<%--
  Created by IntelliJ IDEA.
  User: Maple
  Date: 2015/4/5
  Time: 17:50
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
            var url='<%=path%>/spi/talk/allWordType';
            $.getJSON(url,function(data){
                var options=[];
                $.each(data,function(i,item){
                    options.push("<option value='"+item.id+"'>"+item.name+"</option>");
                });
                $("#parent").html(options.join());
            });
            $("#submit").click(function () {
                var url='<%=path%>/spi/talk/addWordType';
               var data= $("#form").serialize();
                $.getJSON(url,function(data){
                    if(data&&data.state){
                        alert(data.msg);
                        $("#form").reset();
                    }else{
                        alert(data.msg);
                    }
                });
            });
        });

    </script>
</head>
<body>
<h1>welecome to maple robot home </h1>

<form id="form"  action="<%=path%>/spi/talk/addWordType">
    <p>
        <label>上级类型<%=path%>:</label>
        <select id="parent" name="parent">

        </select>
    </p>

    <p>
        <label>词语类型:</label>
        <input type="text" name="name" >
    </p>

    <p>
        <label>描述:</label>
        <input type="text" name="description">
    </p>
    <p>
        <input type="reset" value="重置">
        <input type="button"  id="submit" value="保存">
    </p>
</form>
</body>
</html>
