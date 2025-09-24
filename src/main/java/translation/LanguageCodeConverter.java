package translation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LanguageCodeConverter {
    private final Map<String,String> nameToCode = new HashMap<>();
    private final Map<String,String> codeToName = new HashMap<>();

    public LanguageCodeConverter(Path tsv) throws IOException {
        try (InputStream in = Files.newInputStream(tsv)) {
            loadFromStream(in);
        }
    }

    public LanguageCodeConverter(InputStream in) throws IOException {
        loadFromStream(in);
    }

    private void loadFromStream(InputStream in) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split("\t");
                if (parts.length < 2) continue;
                String name = parts[0].trim();
                String code = parts[1].trim().toLowerCase(Locale.ROOT);
                nameToCode.put(name, code);
                codeToName.put(code, name);
            }
        }
    }

    public String toCode(String englishName) { return nameToCode.get(englishName); }
    public String toEnglish(String code) { return codeToName.get(code.toLowerCase(Locale.ROOT)); }

    public java.util.List<String> allEnglishNames() {
        var l = new ArrayList<>(nameToCode.keySet());
        Collections.sort(l);
        return l;
    }
}
