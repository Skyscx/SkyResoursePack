package me.skyscx.skyresoursepack;

import java.io.IOException;
import java.net.URL;

public class Functions {
    public boolean isUrlValid(String urlString) {
        try {
            URL url = new URL(urlString);
            url.openConnection().connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
