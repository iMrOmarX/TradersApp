package sample;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PDFConverter {


    private static final Font font;


    static {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(
                    "ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        font = new Font(bf, 12);
    }


    public void addCell(String input, PdfPTable table, boolean setBottomBorder) {
        Phrase s = new Phrase();
        s.add(new Chunk(input, font));
        PdfPCell cell = new PdfPCell(s);
        cell.setBorder(PdfPCell.NO_BORDER);
        if (setBottomBorder) {
            cell.setBorderWidthBottom((float) 0.2);
        }
        cell.setVerticalAlignment(PdfPHeaderCell.ALIGN_CENTER);
        cell.setPaddingBottom(10);

        cell.setPaddingTop(10);
        cell.setUseDescender(true);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table.addCell(cell);
    }


    public void printBill(ArrayList<Item> boughtItems) throws IOException, DocumentException {
        String filePath = "test.pdf";
        PdfWriter writer = null;
        Document document = new Document(PageSize.LETTER);

        writer = PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));


        document.open();

        Phrase phrase = new Phrase();
        LocalDate now = LocalDate.now();


        phrase.add(new Chunk(String.valueOf(now)));

        document.add(phrase);

        phrase.clear();

        ColumnText column = new ColumnText(writer.getDirectContent());
        column.setSimpleColumn(36, 730, 569, 36);
        column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


        String[] fields = {"المجموع", "الكمية", "السعر", "اسم المنتج", "الرقم التسلسلي"};

        PdfPTable table = new PdfPTable(5);
        // الرقم التسلسلي  اسم المنتج  السعر  الكمية  المجموع


        for (String field : fields) {
            font.setSize(14);
            addCell(field, table, true);
        }


        float sum = 0;
        for (Item item : boughtItems) {
            font.setSize(12);
            addCell(((item.getQty() * item.getPrice()) + ""), table, false);
            sum += item.getQty() * item.getPrice();
            addCell(item.getQty() + "", table, false);
            addCell(item.getPrice() + "", table, false);
            addCell(item.getName() + "", table, false);
            addCell(item.getId() + "", table, false);


            addCell("", table, true);
            addCell(item.getPhoneNumber(), table, true);
            addCell("رقم الهاتف:", table, true);
            addCell(item.getTraderName() , table, true);
            addCell("اسم المورد:", table, true);

        }

        addCell((sum + ""), table, false);
        addCell("المجموع النهائي:", table, false);
        addCell("", table, false);
        addCell("", table, false);
        addCell("", table, false);

        column.addElement(table);
        column.go();

        document.close();
        openPdf(new File(filePath));

    }

    public void openPdf(File openedFile) throws IOException {
        Desktop.getDesktop().open(openedFile);
    }
}