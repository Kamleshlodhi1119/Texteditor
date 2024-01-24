import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class TextEditor extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;

    public TextEditor() {
        setTitle("Simple Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> newFile());
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> openFile());
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> saveFile());
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(e -> saveFileAs());
        fileMenu.add(saveAsMenuItem);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        JMenuItem copyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        JMenuItem cutMenuItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        JMenuItem pasteMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        JMenuItem selectAllMenuItem = new JMenuItem("Select All");
        selectAllMenuItem.addActionListener(e -> textArea.selectAll());
        editMenu.add(selectAllMenuItem);

        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(e -> deleteLastChar());
        editMenu.add(deleteMenuItem);

        JMenu formatMenu = new JMenu("Format");
        menuBar.add(formatMenu);

        JMenu fontSizeMenu = new JMenu("Change Font Size");
        formatMenu.add(fontSizeMenu);
        populateFontSizeMenu(fontSizeMenu);

        JMenu fontFamilyMenu = new JMenu("Change Font Family");
        formatMenu.add(fontFamilyMenu);
        populateFontFamilyMenu(fontFamilyMenu);
    }

    private void newFile() {
        textArea.setText("");
        setTitle("Untitled - Simple Text Editor");
    }

    private void openFile() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                setTitle(file.getName() + " - Simple Text Editor");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile() {
        if (fileChooser == null) {
            saveFileAs();
        } else {
            saveToFile(fileChooser.getSelectedFile());
        }
    }

    private void saveFileAs() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
        int returnVal = fileChooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            saveToFile(file);
        }
    }

    private void saveToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
            setTitle(file.getName() + " - Simple Text Editor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteLastChar() {
        int length = textArea.getText().length();
        if (length > 0) {
            textArea.setText(textArea.getText().substring(0, length - 1));
        }
    }

    private void populateFontSizeMenu(JMenu fontSizeMenu) {
       for (int size = 8; size <= 24; size++) {
    final int fontSize = size;  // Introduce a final variable
    JMenuItem sizeMenuItem = new JMenuItem(String.valueOf(size));
    sizeMenuItem.addActionListener(e -> changeFontSize(fontSize));
    fontSizeMenu.add(sizeMenuItem);
}

    }

    private void populateFontFamilyMenu(JMenu fontFamilyMenu) {
        String[] commonFontFamilies = {"Arial", "Times New Roman", "Courier New", "Verdana", "Comic Sans MS"};
        for (String family : commonFontFamilies) {
            JMenuItem familyMenuItem = new JMenuItem(family);
            familyMenuItem.addActionListener(e -> changeFontFamily(family));
            fontFamilyMenu.add(familyMenuItem);
        }
    }

    private void changeFontSize(int size) {
        Font currentFont = textArea.getFont();
        textArea.setFont(new Font(currentFont.getFontName(), currentFont.getStyle(), size));
    }

    private void changeFontFamily(String family) {
        Font currentFont = textArea.getFont();
        textArea.setFont(new Font(family, currentFont.getStyle(), currentFont.getSize()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextEditor editor = new TextEditor();
            editor.setVisible(true);
        });
    }
}
