package com.idis.core.business.statistics.category.extensions;

import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.List;
import java.util.UUID;

public class PdfGeneratorForCategory {
    private CategoryStatisticsCalculator categoryStatisticsCalculator;

    public PdfGeneratorForCategory(CategoryStatisticsCalculator categoryStatisticsCalculator) {
        this.categoryStatisticsCalculator = categoryStatisticsCalculator;
    }

    public byte[] generatePdfForCategoryStats(){
        var categoryStatistics = categoryStatisticsCalculator.calculate();

        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.COURIER, 24, Font.BOLD, BaseColor.BLUE);
            Paragraph titleParagraph = new Paragraph("Title: " + categoryStatistics.categoryName(), titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            document.add(Chunk.NEWLINE);

            Font regularFont = FontFactory.getFont(FontFactory.COURIER, 16, Font.NORMAL, BaseColor.BLACK);
            Paragraph postsCount = new Paragraph("Posts Count: " + categoryStatistics.postCount(), regularFont);
            document.add(postsCount);
            Paragraph averageScore = new Paragraph("Average Score: " + categoryStatistics.averageScore(), regularFont);
            document.add(averageScore);

            Paragraph postsByRating = new Paragraph("Posts by Rating:", regularFont);
            document.add(postsByRating);
            document.add(Chunk.NEWLINE);

            PdfPTable postsByRatingTable = createTableFromMapInMap(categoryStatistics.postsByRatings(),regularFont);
            document.add(postsByRatingTable);

            Paragraph averageScorePerRating = new Paragraph("Average Score per Rating:", regularFont);
            document.add(averageScorePerRating);
            document.add(Chunk.NEWLINE);

            PdfPTable averageScorePerRatingTable = createTableFromMap(categoryStatistics.averageScorePerRating(),regularFont);
            document.add(averageScorePerRatingTable);

            Paragraph postsByAverageScore = new Paragraph("Posts by Average Score:", regularFont);
            document.add(postsByAverageScore);
            document.add(Chunk.NEWLINE);

            PdfPTable postsByAverageScoreTable = createTableFromMap(categoryStatistics.postsByAverageScore(),regularFont);
            document.add(postsByAverageScoreTable);

            Paragraph postsAndDays = new Paragraph("Posts and Days:", regularFont);
            document.add(postsAndDays);
            document.add(Chunk.NEWLINE);

            PdfPTable postsAndDaysTable = createTableFromMap(categoryStatistics.postsAndDays(),regularFont);
            document.add(postsAndDaysTable);

            Paragraph postsCountByDay = new Paragraph("Posts Count by Days:", regularFont);
            document.add(postsCountByDay);
            document.add(Chunk.NEWLINE);

            PdfPTable postsCountByDayTable = createTableFromMap(categoryStatistics.postCountByDay(),regularFont);
            document.add(postsCountByDayTable);

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
    private static PdfPTable createTableFromMapInMap(Map<String, Map<UUID, Integer>> map, Font font) {
        PdfPTable table = new PdfPTable(2);
        Font cellFont = new Font(font.getFamily(), font.getSize(), font.getStyle(), font.getColor());

        for (Map.Entry<String, Map<UUID, Integer>> entry : map.entrySet()) {
            PdfPCell keyCell = new PdfPCell(new Paragraph(entry.getKey(), cellFont));

            PdfPTable nestedTable = new PdfPTable(2);
            for (Map.Entry<UUID, Integer> nestedEntry : entry.getValue().entrySet()) {
                UUID uuid = nestedEntry.getKey();
                String name = new String();
                var posts = QueryProvider.getAll(Post.class);
                for (Post post:posts){
                    if(post.getId().equals(uuid)){
                        name = post.getTitle();
                        break;
                    }
                }

                PdfPCell nestedKeyCell = new PdfPCell(new Paragraph(name, cellFont));
                PdfPCell nestedValueCell = new PdfPCell(new Paragraph(nestedEntry.getValue().toString(), cellFont));

                nestedTable.addCell(nestedKeyCell);
                nestedTable.addCell(nestedValueCell);
            }

            PdfPCell valueCell = new PdfPCell(nestedTable);
            table.addCell(keyCell);
            table.addCell(valueCell);
        }

        return table;
    }

}
