<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@attribute name="id"%>
<script>
    function set${id}(v){
        if(v){
            var comment = $('#comment').val();
            $("#${id}").val(comment)
        }
    }
    function get${id}(){
        return $('#${id}').val();    
    }

    function ${id}Text(){
        return 'Edit Text \n\<br/> \n\<textarea rows="8" cols="107" id="comment" name="comment"  style="cursor:default">'+get${id}()+"</textarea>";    
    }

</script>
<img onclick="Prompt(${id}Text(),set${id},{width:'600px',buttons: {Ok:true,Cancel:false}})" title="Edit" src="${path}/images/common/edit.png" style="cursor: pointer"/>
