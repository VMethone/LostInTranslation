package translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new CanadaTranslator();

            // ===== language line =====
            JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            languagePanel.add(new JLabel("Language:"));
            java.util.List<String> languageCodes = translator.getLanguageCodes();
            JComboBox<String> languageCombo =
                    new JComboBox<>(languageCodes.toArray(new String[0]));
            if (!languageCodes.isEmpty()) languageCombo.setSelectedIndex(0);
            languagePanel.add(languageCombo);

            // ===== result =====
            JLabel resultLabel = new JLabel("Translation: ");
            JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            resultPanel.add(resultLabel);

            // ===== coutry line=====
            java.util.List<String> countryCodes = translator.getCountryCodes();
            JList<String> countryList = new JList<>(countryCodes.toArray(new String[0]));
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            countryList.setVisibleRowCount(12); // 你可以调这个行数控制可视高度
            if (!countryCodes.isEmpty()) countryList.setSelectedIndex(0);

            JScrollPane countryScroll = new JScrollPane(countryList);
            countryScroll.setBorder(BorderFactory.createTitledBorder("Countries"));
            countryScroll.getVerticalScrollBar().setUnitIncrement(16);

            Runnable doTranslate = () -> {
                String country = countryList.getSelectedValue();
                String language = (String) languageCombo.getSelectedItem();
                if (country == null || language == null) {
                    resultLabel.setText("Translation: (select country & language)");
                    return;
                }
                String result = translator.translate(country, language);
                resultLabel.setText("Translation: " + (result != null ? result : "no translation found!"));
            };

            doTranslate.run();

            languageCombo.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) doTranslate.run();
            });
            countryList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) doTranslate.run();
            });

            JPanel top = new JPanel();
            top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
            top.add(languagePanel);
            top.add(resultPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout(8, 8));
            frame.add(top, BorderLayout.NORTH);
            frame.add(countryScroll, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
