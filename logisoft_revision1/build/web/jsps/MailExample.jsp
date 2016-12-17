
<jsp:directive.page import="org.apache.commons.mail.HtmlEmail"/>
<jsp:directive.page import="java.net.URL"/>
<jsp:directive.page import="java.net.MalformedURLException"/>
<jsp:directive.page import="org.apache.commons.mail.EmailException"/>
<%

	try{
		  HtmlEmail email = new HtmlEmail();
		  email.setHostName("172.21.4.80");
		  email.setTLS(true);
		  email.addTo("devi_challenge2007@yahoo.co.in", "Gayatri Dei");
		  email.setFrom("gayatrivision2006@gmail.com", "soni");
		  email.setSubject("Test email with inline image");
		  URL url = new URL("http://www.logiwareinc.com/images/stories/tag.jpg");
		  String cid = email.embed(url, "Apache logo");
		  String htmlmessage = "<html>" +"The logiware logo - <img src=\"cid:"+cid+"\">" +
		  		"<body><p>This is an example of an html message being sent from a java program</body>"+
		  				"</html>";
		  email.setHtmlMsg(htmlmessage);
		  email.setTextMsg("Your email client does not support HTML messages");
		  email.send();
	}
	catch(MalformedURLException malformedURLException){
		System.out.println("malformedURLException"+malformedURLException);
	}catch(EmailException emailException){
		System.out.println("emailException"+emailException);
	}catch(Exception exception){
		System.out.println("exception"+exception);
	}
	

%>

