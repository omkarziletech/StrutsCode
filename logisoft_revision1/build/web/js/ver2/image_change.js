<!-- 

//***********************************
// ***** ROTATING IMAGES *****

  var how_many_layouts = 5;
  var nowQ = new Date();
  var secQ = nowQ.getSeconds();
  var imgQ = secQ % how_many_layouts;
  imgQ += 1;

  var layoutQ;


// ***** LAYOUT ONE *****
  if (imgQ==1) 
  {
    layoutQ = '<img src="../img/ver2/1.jpg">';
  }


// ***** LAYOUT TWO *****
  if (imgQ==2) 
  {
    layoutQ = '<img src="../img/ver2/2.jpg">';
  }


// ***** LAYOUT THREE *****
  if (imgQ==3) 
  {
    layoutQ = '<img src="../img/ver2/3.jpg">';
  }
  
// ***** LAYOUT FOUR *****
  if (imgQ==4) 
  {
    layoutQ = '<img src="../img/ver2/4.jpg">';
  }
  
// ***** LAYOUT FIVE *****
  if (imgQ==5) 
  {
    layoutQ = '<img src="../img/ver2/5.jpg">';
  }


// -->