package com.idis.core.business.statistics.posts.extensions;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public final class PdfGeneratorForPosts {
    private PostStatisticsCalculator postStatisticsCalculator;

    public PdfGeneratorForPosts(PostStatisticsCalculator postStatisticsCalculator) {
        this.postStatisticsCalculator = postStatisticsCalculator;
    }

    public byte[] generatePdfForPostStats(){
        var postStatistics = postStatisticsCalculator.calculate();

        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.COURIER, 24, Font.BOLD, BaseColor.BLUE);
            Paragraph titleParagraph = new Paragraph("Title: " + postStatistics.title(), titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            document.add(Chunk.NEWLINE);

            Font regularFont = FontFactory.getFont(FontFactory.COURIER, 16, Font.NORMAL, BaseColor.BLACK);
            Paragraph repliesCount = new Paragraph("Replies Count: " + postStatistics.repliesCount(), regularFont);
            document.add(repliesCount);
            Paragraph averageScore = new Paragraph("Average Score: " + postStatistics.averageScore(), regularFont);
            document.add(averageScore);
            Paragraph averageScoreByRating = new Paragraph("Average Score by Rating:", regularFont);
            document.add(averageScoreByRating);
            document.add(Chunk.NEWLINE);

            PdfPTable averageScoreByRatingTable = createTableFromMap(postStatistics.averageScoreByRating(),regularFont);
            document.add(averageScoreByRatingTable);

            document.close();
            writer.close();

            return outputStream.toByteArray();
        } catch (DocumentException e) {
            throw new IllegalArgumentException("Error exporting PDF",e);
        }
    }

    private static PdfPTable createTableFromMap(Map<?, ?> map, Font font) {
        var table = new PdfPTable(2);
        var cellFont = new Font(font.getFamily(), font.getSize(), font.getStyle(), font.getColor());

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            var keyCell = new PdfPCell(new Paragraph(entry.getKey().toString(),cellFont));
            var valueCell = new PdfPCell(new Paragraph(entry.getValue().toString(),cellFont));

            table.addCell(keyCell);
            table.addCell(valueCell);
        }
        return table;
    }
}
