
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String string = "&2[BreweryX]&f ";
        String string1 = "&#084CFB&lT&#185CF4&le&#286CED&ls&#387CE6&lt &#589CD8&lM&#68ADD0&le&#78BDC9&ls&#88CDC2&ls&#98DDBB&la&#A8EDB4&lg&#B8FDAD&le";
        System.out.println(convert(string1, true, '&', true));
    }

    public static String convert(String legacy, boolean concise, char chara, boolean rgb) {
        String miniMessage;

        miniMessage = legacy.replaceAll(chara + "0", "<black>")
                .replaceAll(chara + "1", "<dark_blue>")
                .replaceAll(chara + "2", "<dark_green>")
                .replaceAll(chara + "3", "<dark_aqua>")
                .replaceAll(chara + "4", "<dark_red>")
                .replaceAll(chara + "5", "<dark_purple>")
                .replaceAll(chara + "6", "<gold>")
                .replaceAll(chara + "7", "<gray>")
                .replaceAll(chara + "8", "<dark_gray>")
                .replaceAll(chara + "9", "<blue>")
                .replaceAll(chara + "a", "<green>")
                .replaceAll(chara + "b", "<aqua>")
                .replaceAll(chara + "c", "<red>")
                .replaceAll(chara + "d", "<light_purple>")
                .replaceAll(chara + "e", "<yellow>")
                .replaceAll(chara + "f", "<white>");

        if (concise) {
            miniMessage = miniMessage.replaceAll(chara + "n", "<u>")
                    .replaceAll(chara + "m", "<st>")
                    .replaceAll(chara + "k", "<obf>")
                    .replaceAll(chara + "o", "<i>")
                    .replaceAll(chara + "l", "<b>")
                    .replaceAll(chara + "r", "<r>");
        } else {
            miniMessage = miniMessage.replaceAll(chara + "n", "<underlined>")
                    .replaceAll(chara + "m", "<strikethrough>")
                    .replaceAll(chara + "k", "<obfuscated>")
                    .replaceAll(chara + "o", "<italic>")
                    .replaceAll(chara + "l", "<bold>")
                    .replaceAll(chara + "r", "<reset>");
        }

        if (rgb) {
            Pattern pattern = Pattern.compile(chara + "#([0-9a-fA-F]{6})");
            Matcher matcher = pattern.matcher(miniMessage);
            miniMessage = matcher.replaceAll("<#$1>");
        }

        return miniMessage;
    }
}
