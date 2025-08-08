package io.github.warleysr.autopixreforged.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.github.warleysr.autopixreforged.AutoPixReforged;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QRCodeGenerator {
    
    private static final String QR_CODE_DIRECTORY = "config/autopix-reforged/qrcodes";
    private static final int QR_CODE_SIZE = 300;
    
    /**
     * Generates a QR code for the given PIX code and saves it to a file
     * 
     * @param pixCode The PIX code to encode
     * @param orderId The order ID for file naming
     * @return The path to the generated QR code file, or null if generation failed
     */
    public static String generateQRCode(String pixCode, int orderId) {
        try {
            // Create directory if it doesn't exist
            Path qrDirectory = Paths.get(QR_CODE_DIRECTORY);
            if (!Files.exists(qrDirectory)) {
                Files.createDirectories(qrDirectory);
            }
            
            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(pixCode, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);
            
            // Save to file
            String fileName = String.format("pix_order_%d_%d.png", orderId, System.currentTimeMillis());
            Path qrCodePath = qrDirectory.resolve(fileName);
            
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrCodePath);
            
            AutoPixReforged.getLogger().info("QR Code gerado para o pedido {}: {}", orderId, qrCodePath.toString());
            
            return qrCodePath.toString();
            
        } catch (WriterException | IOException e) {
            AutoPixReforged.getLogger().error("Erro ao gerar QR Code para o pedido {}: ", orderId, e);
            return null;
        }
    }
    
    /**
     * Generates a QR code and returns it as a base64 encoded string
     * 
     * @param pixCode The PIX code to encode
     * @return Base64 encoded PNG image, or null if generation failed
     */
    public static String generateQRCodeBase64(String pixCode) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(pixCode, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);
            
            // Convert to base64
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            byte[] imageBytes = outputStream.toByteArray();
            return java.util.Base64.getEncoder().encodeToString(imageBytes);
            
        } catch (WriterException | IOException e) {
            AutoPixReforged.getLogger().error("Erro ao gerar QR Code base64: ", e);
            return null;
        }
    }
    
    /**
     * Cleans up old QR code files (older than specified hours)
     * 
     * @param hoursOld Files older than this many hours will be deleted
     */
    public static void cleanupOldQRCodes(int hoursOld) {
        try {
            Path qrDirectory = Paths.get(QR_CODE_DIRECTORY);
            if (!Files.exists(qrDirectory)) {
                return;
            }
            
            long cutoffTime = System.currentTimeMillis() - (hoursOld * 60 * 60 * 1000L);
            
            Files.list(qrDirectory)
                    .filter(path -> path.toString().endsWith(".png"))
                    .filter(path -> {
                        try {
                            return Files.getLastModifiedTime(path).toMillis() < cutoffTime;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            AutoPixReforged.getLogger().debug("QR Code antigo removido: {}", path.toString());
                        } catch (IOException e) {
                            AutoPixReforged.getLogger().warn("Erro ao remover QR Code antigo {}: ", path.toString(), e);
                        }
                    });
                    
        } catch (IOException e) {
            AutoPixReforged.getLogger().error("Erro ao limpar QR Codes antigos: ", e);
        }
    }
}