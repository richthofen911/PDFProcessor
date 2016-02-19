package org.webkits.pdfprocessor;

import java.io.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

/**
 * Created by Richthofen80 on 2/18/2016.
 */
public class PdfMerger {

    private String outputPathPrefix;
    private String middleTxt1Path;
    private String middleTxt2Path;
    private String middleTxtMergedPath;
    private String outputPdfPath;

    public PdfMerger(String outputPathPrefix){
        this.outputPathPrefix = outputPathPrefix;
        this.middleTxt1Path = outputPathPrefix + "tmp1.txt";
        this.middleTxt2Path = outputPathPrefix + "tmp2.txt";
        this.middleTxtMergedPath = outputPathPrefix + "tmpMerged.txt";
        this.outputPdfPath = outputPathPrefix + "merged.pdf";
    }

    public void parsePdfToTxt(String sourcePdfPath, String middleTxtPath) throws IOException {
        PdfReader pdfReader = new PdfReader(sourcePdfPath);
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
        PrintWriter out = new PrintWriter(new FileOutputStream(middleTxtPath));
        TextExtractionStrategy strategy;
        for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
            strategy = pdfReaderContentParser.processContent(i, new SimpleTextExtractionStrategy());
            out.println(strategy.getResultantText());
        }
        out.flush();
        out.close();
        pdfReader.close();
    }

    public void mergeTxtFiles(){
        try{
            File resultTxt = new File(middleTxtMergedPath);
            FileOutputStream fos = new FileOutputStream(resultTxt);

            FileInputStream fis1 = new FileInputStream(new File(middleTxt1Path));
            FileInputStream fis2 = new FileInputStream(new File(middleTxt2Path));

            byte[] b = new byte[1];
            while(fis1.read(b) != -1){
                fos.write(b);
            }
            while(fis2.read(b) != -1){
                fos.write(b);
            }
            fos.flush();
            System.out.println("merge txt files: success");
        }catch (Exception e){
            System.out.println("exception: " + e);
        }

    }

    public void convertTxtToPdf() throws DocumentException, IOException{
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPdfPath));
        document.open();
        Paragraph pdfContent = new Paragraph();
        FileInputStream fis = new FileInputStream(new File(middleTxtMergedPath));
        byte[] bytes = new byte[1024];
        while (fis.read(bytes) != -1){
            pdfContent.add(new String(bytes, "UTF-8"));
            document.add(pdfContent);
        }
        document.close();
    }

    public void mergePdf(String pdfPath1, String pdfPath2){
        try{
            parsePdfToTxt(pdfPath1, middleTxt1Path);
            parsePdfToTxt(pdfPath2, middleTxt2Path);
            mergeTxtFiles();
            convertTxtToPdf();
        }catch (IOException e){
            e.printStackTrace();
        }catch (DocumentException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {

        PdfMerger pdfMerger = new PdfMerger("resources/");
        //pdfMerger.mergePdf(args[0], args[1]);
        pdfMerger.mergePdf("/home/admin/Downloads/ielts_u.pdf", "/home/admin/Downloads/ielts_q.pdf");

    }
}
