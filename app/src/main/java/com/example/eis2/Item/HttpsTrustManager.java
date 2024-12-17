package com.example.eis2.Item;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsTrustManager  {
    public static void allowAllSSL() {
        TrustManager[] victimizedManager = new TrustManager[]{

                new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {

                        X509Certificate[] myTrustedAnchors = new X509Certificate[0];

                        return myTrustedAnchors;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        System.out.println("Auth = " + authType);
                        if(chain == null || chain.length == 0)throw new IllegalArgumentException("Certificate is null or empty");
                        if(authType == null || authType.length() == 0) throw new IllegalArgumentException("Authtype is null or empty");
                        if(!authType.equalsIgnoreCase("ECDHE_RSA") &&
                                !authType.equalsIgnoreCase("ECDHE_ECDSA") &&
                                !authType.equalsIgnoreCase("RSA") &&
                                !authType.equalsIgnoreCase("GENERIC") &&
                                !authType.equalsIgnoreCase("ECDSA")) throw new CertificateException("Certificate is not trust");
                        try {
                            chain[0].checkValidity();
                        } catch (Exception e) {
                            throw new CertificateException("Certificate is not valid or trusted");
                        }
                    }
                }
        };
        try {
            final SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, victimizedManager, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    if (hostname.equals("apisec.tvip.co.id") || (hostname.equals("hrd.tvip.co.id")) || (hostname.equals("assessment.tvip.co.id"))) {
                        return true;
                    }
                    return false;
                }
            });

        } catch (Exception exc) {

        }
    }
}
