package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.example.eis2.menu.txt_alpha;

public class pdfview extends AppCompatActivity {
    private PdfRenderer pdfRenderer;
    private ParcelFileDescriptor fileDescriptor;
    private String pdfUrl = "http://assessment.tvip.co.id/usr/cv/downloadcv/"; // Update with the correct URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        HttpsTrustManager.allowAllSSL(); // Ensure this is safe to use in your app

        LinearLayout pdfContainer = findViewById(R.id.pdfContainer);

        try {
            // Download the file and open the renderer
            openRenderer();

            // Render all pages
            for (int i = 0; i < pdfRenderer.getPageCount(); i++) {
                renderPage(i, pdfContainer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openRenderer() throws IOException {
        // Download the PDF file
        File file = downloadPdf(pdfUrl);

        if (file == null || !file.exists()) {
            throw new IOException("Failed to download PDF file.");
        }

        // Open the file with PdfRenderer
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(fileDescriptor);
    }

    private File downloadPdf(String urlString) {
        File file = new File(getCacheDir(), "downloaded.pdf"); // Temporary file

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null; // Handle HTTP errors
            }

            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void renderPage(int pageIndex, LinearLayout container) {
        if (pdfRenderer == null || pageIndex < 0 || pageIndex >= pdfRenderer.getPageCount()) {
            return;
        }

        // Open the page
        PdfRenderer.Page page = pdfRenderer.openPage(pageIndex);

        // Create a bitmap for the page
        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);

        // Render the page into the bitmap
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Create an ImageView and set the bitmap
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add the ImageView to the container
        container.addView(imageView);

        // Close the page
        page.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the PdfRenderer
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
        if (fileDescriptor != null) {
            try {
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}