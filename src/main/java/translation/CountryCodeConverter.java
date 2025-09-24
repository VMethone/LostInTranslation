package translation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CountryCodeConverter {
    private final Map<String,String> nameToAlpha3 = new HashMap<>();
    private final Map<String,String> alpha3ToName = new HashMap<>();

    public CountryCodeConverter(Path tsv) throws IOException {
        try (InputStream in = Files.newInputStream(tsv)) {
            loadFromStream(in);
        }
    }

    public CountryCodeConverter(InputStream in) throws IOException {
        loadFromStream(in);
    }

    private void loadFromStream(InputStream in) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split("\t");
                if (parts.length < 3) continue;
                String name   = parts[0].trim();
                String alpha3 = parts[2].trim().toLowerCase(Locale.ROOT);
                nameToAlpha3.put(name, alpha3);
                alpha3ToName.put(alpha3, name);
            }
        }
    }

    public String toAlpha3(String englishName) { return nameToAlpha3.get(englishName); }
    public String toEnglish(String alpha3) { return alpha3ToName.get(alpha3.toLowerCase(Locale.ROOT)); }

    public java.util.List<String> allEnglishNames() {
        var l = new ArrayList<>(nameToAlpha3.keySet());
        Collections.sort(l);
        return l;
    }
}
