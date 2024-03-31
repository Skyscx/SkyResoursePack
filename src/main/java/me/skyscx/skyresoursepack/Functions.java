package me.skyscx.skyresoursepack;

import org.bukkit.command.CommandSender;

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
    public boolean isNumeric(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
}
