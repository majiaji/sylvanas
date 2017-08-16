package com.fantasy.sylvanas.client.domain;

/**
 * Created by jiaji on 2017/8/16.
 */
public class FlumeData {
    private Headers headers;
    private String body;

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "FlumeData{" +
                "headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

    private static class Headers {
        String timestamp;
        String host;
        String service;
        String scene;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getScene() {
            return scene;
        }

        public void setScene(String scene) {
            this.scene = scene;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "timestamp='" + timestamp + '\'' +
                    ", host='" + host + '\'' +
                    ", service='" + service + '\'' +
                    ", scene='" + scene + '\'' +
                    '}';
        }
    }
}
