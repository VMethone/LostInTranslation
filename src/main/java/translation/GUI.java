package translation;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GUI {

    private JList<String> countryList;
    private JComboBox<String> languageBox;
    private JLabel resultLabel;

    private CountryCodeConverter countryConv;
    private LanguageCodeConverter langConv;
    private Translator translator;

    public GUI() throws Exception {
        countryConv = new CountryCodeConverter();
        langConv    = new LanguageCodeConverter();
        translator  = new JSONTranslator();

        Set<String> jsonLangCodes    = new HashSet<>(translator.getLanguageCodes());
        Set<String> jsonCountryCodes = new HashSet<>(translator.getCountryCodes());

        List<String> langNames = new ArrayList<>();
        for (String code : jsonLangCodes) {
            if (code == null) continue;
            String name = langConv.fromLanguageCode(code.toLowerCase(Locale.ROOT));
            if (name != null && !"Unknown Language".equals(name)) {
                langNames.add(name);
            }
        }
        Collections.sort(langNames);

        List<String> countryNames = new ArrayList<>();
        for (String a3 : jsonCountryCodes) {
            if (a3 == null) continue;
            String name = countryConv.fromCountryCode(a3.toLowerCase(Locale.ROOT));
            if (name != null && !"Unknown Country".equals(name)) {
                countryNames.add(name);
            }
        }
        Collections.sort(countryNames);

        JFrame frame = new JFrame("Country Name Translator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(8, 8));
        frame.setSize(780, 540);

        languageBox = new JComboBox<>(langNames.toArray(new String[0]));
        JPanel languagePanel = new JPanel(new BorderLayout(8, 8));
        languagePanel.add(new JLabel("Language:"), BorderLayout.WEST);
        languagePanel.add(languageBox, BorderLayout.CENTER);

        resultLabel = new JLabel("Select a country and a language.");
        resultLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(languagePanel);
        top.add(resultLabel);

        countryList = new JList<>(countryNames.toArray(new String[0]));
        countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        countryList.setVisibleRowCount(14);
        JScrollPane countryScroll = new JScrollPane(countryList);
        countryScroll.setBorder(BorderFactory.createTitledBorder("Countries"));
        countryScroll.getVerticalScrollBar().setUnitIncrement(16);

        frame.add(top, BorderLayout.NORTH);
        frame.add(countryScroll, BorderLayout.CENTER);

        languageBox.addActionListener(e -> update());
        countryList.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) update(); });

        if (countryList.getModel().getSize() > 0) countryList.setSelectedIndex(0);
        if (languageBox.getItemCount() > 0) languageBox.setSelectedIndex(0);
        update();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void update() {
        String countryName = countryList.getSelectedValue();
        String langName    = (String) languageBox.getSelectedItem();
        if (countryName == null || langName == null) {
            resultLabel.setText("Select a country and a language.");
            return;
        }

        String alpha3   = countryConv.fromCountry(countryName);
        String langCode = langConv.fromLanguage(langName);

        if (alpha3 == null || langCode == null
                || "Unknown Code".equals(alpha3) || "Unknown Code".equals(langCode)) {
            resultLabel.setText("Mapping failed. Check codes files.");
            return;
        }

        String translated = translator.translate(
                alpha3.toLowerCase(Locale.ROOT),
                langCode.toLowerCase(Locale.ROOT)
        );

        resultLabel.setText(
                translated == null || translated.isBlank()
                        ? "No translation for " + countryName + " \u2192 " + langName
                        : "“" + countryName + "” in " + langName + ": " + translated
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { new GUI(); }
            catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
