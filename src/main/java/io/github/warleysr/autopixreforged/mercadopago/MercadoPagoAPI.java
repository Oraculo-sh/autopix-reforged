package io.github.warleysr.autopixreforged.mercadopago;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.warleysr.autopixreforged.AutoPixReforged;
import io.github.warleysr.autopixreforged.config.AutoPixConfig;
import io.github.warleysr.autopixreforged.domain.Order;
import io.github.warleysr.autopixreforged.domain.PixData;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MercadoPagoAPI {
    private static final String BASE_URL = "https://api.mercadopago.com";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private static OkHttpClient client;
    private static Gson gson;
    
    static {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
    }
    
    public static PixData createPixPayment(Order order) throws IOException {
        AutoPixConfig config = AutoPixReforged.getConfig();
        
        JsonObject paymentData = new JsonObject();
        paymentData.addProperty("transaction_amount", order.getPrice());
        paymentData.addProperty("description", "Compra no servidor - Produto: " + order.getProductId());
        paymentData.addProperty("payment_method_id", "pix");
        
        JsonObject payer = new JsonObject();
        payer.addProperty("email", "player@minecraft.com");
        paymentData.add("payer", payer);
        
        JsonObject pointOfInteraction = new JsonObject();
        JsonObject transactionData = new JsonObject();
        transactionData.addProperty("qr_code_base64", true);
        transactionData.addProperty("qr_code", true);
        pointOfInteraction.add("transaction_data", transactionData);
        paymentData.add("point_of_interaction", pointOfInteraction);
        
        String json = gson.toJson(paymentData);
        
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/v1/payments")
                .header("Authorization", "Bearer " + config.getMercadoPagoToken())
                .header("Content-Type", "application/json")
                .post(body)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na API do MercadoPago: " + response.code() + " - " + response.message());
            }
            
            String responseBody = response.body().string();
            JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
            
            if (config.isPixDebugEnabled()) {
                AutoPixReforged.getLogger().info("Resposta da API MercadoPago: {}", responseBody);
            }
            
            String paymentId = responseJson.get("id").getAsString();
            String status = responseJson.get("status").getAsString();
            
            JsonObject poi = responseJson.getAsJsonObject("point_of_interaction");
            JsonObject transData = poi.getAsJsonObject("transaction_data");
            String qrCode = transData.get("qr_code").getAsString();
            String qrCodeBase64 = transData.get("qr_code_base64").getAsString();
            
            PixData pixData = new PixData(paymentId, order.getId(), status, qrCode);
            pixData.setQrCodeBase64(qrCodeBase64);
            
            return pixData;
        }
    }
    
    public static String checkPaymentStatus(String paymentId) throws IOException {
        AutoPixConfig config = AutoPixReforged.getConfig();
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/v1/payments/" + paymentId)
                .header("Authorization", "Bearer " + config.getMercadoPagoToken())
                .get()
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao consultar status do pagamento: " + response.code());
            }
            
            String responseBody = response.body().string();
            JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
            
            return responseJson.get("status").getAsString();
        }
    }
    
    public static boolean cancelPayment(String paymentId) throws IOException {
        AutoPixConfig config = AutoPixReforged.getConfig();
        
        JsonObject cancelData = new JsonObject();
        cancelData.addProperty("status", "cancelled");
        
        String json = gson.toJson(cancelData);
        RequestBody body = RequestBody.create(json, JSON);
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/v1/payments/" + paymentId)
                .header("Authorization", "Bearer " + config.getMercadoPagoToken())
                .header("Content-Type", "application/json")
                .put(body)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }
    
    public static boolean validatePixTransaction(String pixCode) throws IOException {
        AutoPixConfig config = AutoPixReforged.getConfig();
        
        // Search for PIX transactions using the E2E code
        String url = BASE_URL + "/v1/payments/search?sort=date_created&criteria=desc&external_reference=" + pixCode;
        
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + config.getMercadoPagoToken())
                .get()
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return false;
            }
            
            String responseBody = response.body().string();
            JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
            
            // Check if any transaction matches the PIX code and is approved
            if (responseJson.has("results") && responseJson.getAsJsonArray("results").size() > 0) {
                JsonObject firstResult = responseJson.getAsJsonArray("results").get(0).getAsJsonObject();
                String status = firstResult.get("status").getAsString();
                return "approved".equals(status);
            }
            
            return false;
        }
    }
}