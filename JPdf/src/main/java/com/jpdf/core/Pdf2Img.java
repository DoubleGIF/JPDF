package com.jpdf.core;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pdf2Img {

    public static void toImgFile(File file , String formatName ){
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            // 逐页转换
            int numberOfPages = document.getNumberOfPages();
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                // 渲染页面为图像，DPI 可调整
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
                // 保存为 PNG
                ImageIO.write(bim, formatName, new File(file.getParent() + "\\" +file.getName().split("\\.")[0] + "_" +(page + 1) + "." + formatName));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void toImgFile(File file , String formatName ,int quality){
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            // 逐页转换
            int numberOfPages = document.getNumberOfPages();
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                // 渲染页面为图像，DPI 可调整
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, quality);
                // 保存为 PNG
                ImageIO.write(bim, formatName, new File(file.getParent() + "\\" +file.getName().split("\\.")[0] + "_" +(page + 1) + "." + formatName));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toImgFile(File file , String formatName , int dpi , JProgressBar progressBar){
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            // 逐页转换
            int numberOfPages = document.getNumberOfPages();
            progressBar.setValue(0);
            progressBar.setMaximum(numberOfPages);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                // 渲染页面为图像，DPI 可调整
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, dpi);
                // 保存为 PNG
                ImageIO.write(bim, formatName, new File(file.getParent() + "\\" +file.getName().split("\\.")[0] + "_" +(page + 1) + "." + formatName));
                progressBar.setValue(page + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
