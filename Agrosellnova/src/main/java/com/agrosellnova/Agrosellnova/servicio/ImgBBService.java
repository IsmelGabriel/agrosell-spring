package com.agrosellnova.Agrosellnova.servicio;


import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImgBBService {

    @Value("${imgbb.api.key}")
    private String imgbbApiKey;

    public String uploadImage(MultipartFile file) throws Exception {

        // Convertir imagen a Base64
        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("key", imgbbApiKey)
                .add("image", base64Image)
                .build();

        Request request = new Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Error subiendo imagen a ImgBB: " + response);
        }

        String json = response.body().string();

        // Extraer URL de la respuesta JSON
        String url = json.split("\"url\":\"")[1].split("\"")[0];

        return url;
    }
}