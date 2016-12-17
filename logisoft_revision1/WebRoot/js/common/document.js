function viewDocument(title,fileName){
    var url = path+"/servlet/FileViewerServlet?fileName="+fileName;
    if(fileName.indexOf(".xls")>0 || fileName.indexOf(".doc")>0
	|| fileName.indexOf(".mht")>0 || fileName.indexOf(".msg")>0
	|| fileName.indexOf(".csv")>0 || fileName.indexOf(".ppt")>0){
	window.open(url, "","resizable=1,location=1,status=1,scrollbars=1, width=600,height=400");
    }else{
	if(window.parent.parent.parent.homeForm){
	    window.parent.parent.parent.showLightBox(title,url);
	}else if(window.parent.parent.homeForm){
	    window.parent.parent.showLightBox(title,url);
	}else{
	    window.parent.showLightBox(title,url);
	}
    }
}

