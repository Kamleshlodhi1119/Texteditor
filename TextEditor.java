import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TextEditor extends JFrame implements ActionListener {

    private JTextArea textArea;
    private JComboBox<String> fontComboBox;
    private JComboBox<Integer> sizeComboBox;
    private JToggleButton boldButton;
    private JToggleButton underlineButton;

    public TextEditor() {
        setTitle("Text Editor");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(this);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(this);

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        JMenu formatMenu = new JMenu("Format");
        boldButton = new JToggleButton("Bold");
        boldButton.addActionListener(this);
        underlineButton = new JToggleButton("Underline");
        underlineButton.addActionListener(this);
        JLabel fontLabel = new JLabel("Font:");
        fontComboBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontComboBox.addActionListener(this);
        JLabel sizeLabel = new JLabel("Size:");
        Integer[] sizes = { 8, 10, 12, 14, 16, 18, 20, 24, 28, 32 };
        sizeComboBox = new JComboBox<>(sizes);
        sizeComboBox.addActionListener(this);

        formatMenu.add(boldButton);
        formatMenu.add(underlineButton);
        formatMenu.addSeparator();
        formatMenu.add(fontLabel);
        formatMenu.add(fontComboBox);
        formatMenu.add(sizeLabel);
        formatMenu.add(sizeComboBox);
        menuBar.add(formatMenu);

        setJMenuBar(menuBar);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Open")) {
            openFile();
        } else if (e.getActionCommand().equals("Save")) {
            saveFile();
        } else if (e.getSource() == boldButton) {
            applyStyle(StyleConstants.Bold, boldButton.isSelected());
        } else if (e.getSource() == underlineButton) {
            applyStyle(StyleConstants.Underline, underlineButton.isSelected());
        } else if (e.getSource() == fontComboBox) {
            applyFont();
        } else if (e.getSource() == sizeComboBox) {
            applySize();
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                textArea.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
            } catch (IOException ex) {
                showError("Error reading the file");
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(textArea.getText());
            } catch (IOException ex) {
                showError("Error saving the file");
            }
        }
    }

    private void applyStyle(Object bold, boolean add) {
        StyledDocument doc = (StyledDocument) textArea.getDocument();
        int start = textArea.getSelectionStart();
        int end = textArea.getSelectionEnd();
        SimpleAttributeSet set = new SimpleAttributeSet();
        set.addAttribute(bold, add);
        doc.setCharacterAttributes(start, end - start, set, false);
    }

    private void applyFont() {
        String selectedFont = (String) fontComboBox.getSelectedItem();
        Font currentFont = textArea.getFont();
        Font newFont = new Font(selectedFont, currentFont.getStyle(), currentFont.getSize());
        textArea.setFont(newFont);
    }

    private void applySize() {
        int selectedSize = (Integer) sizeComboBox.getSelectedItem();
        Font currentFont = textArea.getFont();
        Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), selectedSize);
        textArea.setFont(newFont);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextEditor textEditor = new TextEditor();
            textEditor.setVisible(true);
        });
    }
}
