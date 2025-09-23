package translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

// Task D solved: GUI updated to align with README-style UI (dropdowns, no manual typing).
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For now we use CanadaTranslator; you can later swap in JSONTranslator.
            Translator translator = new CanadaTranslator();

            // ===== Country row =====
            JPanel countryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            countryPanel.add(new JLabel("Country:"));
            List<String> countryCodes = translator.getCountryCodes();
            JComboBox<String> countryCombo =
                    new JComboBox<>(countryCodes.toArray(new String[0]));
            if (!countryCodes.isEmpty()) countryCombo.setSelectedIndex(0);
            countryPanel.add(countryCombo);

            // ===== Language row =====
            JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            languagePanel.add(new JLabel("Language:"));
            List<String> languageCodes = translator.getLanguageCodes();
            JComboBox<String> languageCombo =
                    new JComboBox<>(languageCodes.toArray(new String[0]));
            if (!languageCodes.isEmpty()) languageCombo.setSelectedIndex(0);
            languagePanel.add(languageCombo);

            // ===== Result row =====
            JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            resultPanel.add(new JLabel("Translation:"));
            JLabel resultLabel = new JLabel(" ");
            resultPanel.add(resultLabel);

            // ===== Behavior: auto-translate on selection change =====
            Runnable doTranslate = () -> {
                String country = (String) countryCombo.getSelectedItem();
                String language = (String) languageCombo.getSelectedItem();
                String result = translator.translate(country, language);
                resultLabel.setText(result != null ? result : "no translation found!");
            };

            // Initial translate
            if (countryCombo.getItemCount() > 0 && languageCombo.getItemCount() > 0) {
                doTranslate.run();
            }

            // Listeners
            countryCombo.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) doTranslate.run();
            });
            languageCombo.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) doTranslate.run();
            });

            // ===== Main panel =====
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(resultPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(mainPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}