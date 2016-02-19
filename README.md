# PDFProcessor
PDF processor based on itext java lib
 
解决生成含中文文档显示中文的办法        http://www.cnblogs.com/jston/archive/2013/02/01/2888472.html <b>
  step 1: 添加 itext-asian.jar作为lib   https://sourceforge.net/projects/itext/files/extrajars/  <b>
  step 2: 下载中文字体 http://www.fontpalace.com/font-details/SimSun/ 并放到project下/resourses目录下  <b>
  step 3:  在类中加入如下方法 <b>
      public static Font setChineseFont() {
          BaseFont bf = null;
          Font fontChinese = null;
          try {
              bf = BaseFont.createFont("resources/SimSun.ttf",
                      BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
              fontChinese = new Font(bf, 12, Font.NORMAL);
          } catch (DocumentException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
          return fontChinese;
      }
  step 4: 生成文档时 <b>
    Paragraph pdfContent = new Paragraph(stringBuffer.toString(), setChineseFont());
    document.add(pdfContent);
