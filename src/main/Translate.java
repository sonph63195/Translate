/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import data.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author SONPH
 */
public final class Translate extends JFrame {

    private static final long serialVersionUID = 1L;
    /**
     *
     */

    private static final String NULL = "null";

    BinarySearchTree<Words> viLang = new BinarySearchTree<>();
    BinarySearchTree<Words> enLang = new BinarySearchTree<>();

    Words curViWord;
    Words curEnWord;

    String fileName = "data.txt";
    String settingsData = "settings.ini";

    boolean EnToVi;
    boolean addNew = false;

    public Translate() throws HeadlessException {
        initComponents();
        loadSettings();
        showOrHideSavebtn();
        translateLanguage();
        loadResouce();
//        enLang.breadthFirst();
//        System.out.println("-----");
//        viLang.breadthFirst();
    }

    /**
     * Load resource
     */
    private void loadResouce() {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int flag = 0;
            while ((line = br.readLine()) != null) {
                //boolean isVi = line.charAt(line.length() - 1) == ':';
                String[] wordRead = line.split("[:,]+");
                //check first character in utf-8
                if (flag == 0) {
                    String[] split = wordRead[0].split("\\W");
                    wordRead[0] = "";
                    for (int i = 1; i < split.length; i++) {
                        wordRead[0] += split[i]; 
                        if (i < split.length - 1) {
                            wordRead[0] += " ";
                        }
                    }
                    System.out.println(wordRead[0]);
                } 
                //insert to tree
                for (int i = 0; i < wordRead.length; i++) {
                    if (i == 0) {
                        curViWord = new Words(wordRead[0]);
                    } else {
                        curViWord.setMean(wordRead[i]);
                        //set for en lang
                        curEnWord = new Words(wordRead[i]);
                        TreeNode<Words> node = enLang.search(curEnWord);
                        if (node == null) {
                            curEnWord.setMean(wordRead[0]);
                            enLang.insert(curEnWord);
                        } else {
                            node.getKey().setMean(wordRead[0]);
                        }
                    }
                    viLang.insert(curViWord);
                    flag++;
                }
            }
            br.close();
            fr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveResource() {
        try {
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            Queue<TreeNode<Words>> queue = this.viLang.getPreOrderList();
            while (!queue.isEmpty()) {
                TreeNode<Words> node = queue.poll();
                String key = node.getKey().getWord();
                bw.write(key + ":");
                //bw.newLine();
                LinkedList<String> mean = node.getKey().getMeanlist();
                for (String e : mean) {
                    if (e != null) {
                        bw.write(e + ",");
                        //bw.newLine();
                    }
                }
                bw.newLine();
            }
            bw.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() {
        try {
            FileReader fr = new FileReader(settingsData);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineRead = line.split("=");
                if (lineRead[0].trim().equalsIgnoreCase("enTovi")) {
                    if (lineRead[1].trim().equalsIgnoreCase("true")) {
                        this.EnToVi = true;
                    } else {
                        this.EnToVi = false;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void initComponents() {
        //LEFT Panel
        this.leftPanel = new JPanel(new FlowLayout(0, 0, 0));
        this.leftPanel.setPreferredSize(new Dimension(300, 350));
        //this.leftPanel.setBackground(Color.red);

        //Top Left
        this.topLeft = new JPanel(new FlowLayout(0, 5, 5));
        this.topLeft.setPreferredSize(new Dimension(300, 50));
        //this.topLeft.setBackground(Color.blue);
        // add top left to Left Panel
        this.leftPanel.add(this.topLeft);

        this.btnLeftVi = new JButton("Tiếng Việt");
        this.btnLeftVi.setPreferredSize(new Dimension(100, 40));
        this.btnLeftVi.setBorderPainted(false);
        this.btnLeftVi.setFocusPainted(false);
        //this.btnLeftVi.setContentAreaFilled(false);
        this.btnLeftVi.setBackground(new Color(213, 166, 19));
        this.btnLeftVi.setForeground(Color.WHITE);
        this.btnLeftVi.setFont(new Font("Roboto", 700, 14));
        this.btnLeftVi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //Add action Listioner
        this.btnLeftVi.addActionListener((ActionEvent evt) -> btnLeftViActionPerformed(evt));

        this.btnLeftEn = new JButton("English");
        this.btnLeftEn.setPreferredSize(new Dimension(100, 40));
        this.btnLeftEn.setFocusPainted(false);
        this.btnLeftEn.setBorderPainted(false);
        this.btnLeftEn.setBackground(new Color(213, 166, 19));
        this.btnLeftEn.setForeground(Color.WHITE);
        this.btnLeftEn.setFont(new Font("Roboto", 700, 14));
        this.btnLeftEn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //add action listioner
        this.btnLeftEn.addActionListener((ActionEvent evt) -> btnLeftEnActionPerformed(evt));

        //Add two button to Top Left
        this.topLeft.add(this.btnLeftVi);
        this.topLeft.add(this.btnLeftEn);

        // Contain Left
        this.containLeft = new JPanel(new FlowLayout(0, 5, 5));
        this.containLeft.setPreferredSize(new Dimension(300, 300));
        //this.containLeft.setBackground(Color.yellow);
        //add contain left to Left Panel
        this.leftPanel.add(this.containLeft);

        //TextArea 
        this.txtContent = new JTextArea(15, 25);
        this.txtContent.setLineWrap(true);
        this.txtContent.setTabSize(4);
        this.txtContent.setFont(new Font("Roboto", 0, 14));
        JScrollPane txtScroll = new JScrollPane();
        txtScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        txtScroll.setViewportView(this.txtContent);
        //add txtContent to contain left
        this.containLeft.add(txtScroll);
        //add key listioner
        this.txtContent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContentKeyPressed(evt);
            }
        });

        //RIGHT Panel
        this.rightPanel = new JPanel(new FlowLayout(0, 0, 0));
        this.rightPanel.setPreferredSize(new Dimension(300, 350));
        //this.rightPanel.setBackground(Color.GREEN);

        //Top Right
        this.topRight = new JPanel(new FlowLayout(0, 5, 5));
        this.topRight.setPreferredSize(new Dimension(300, 50));
        //this.topRight.setBackground(Color.BLACK);
        //add top right to Right Panel
        this.rightPanel.add(this.topRight);

        // Buttons
        ImageIcon iconTranslate = new ImageIcon("src/images/translate.png");
        Image scaledImgTranslate = iconTranslate.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        iconTranslate = new ImageIcon(scaledImgTranslate);

        this.btnTranslate = new JButton(iconTranslate);
        this.btnTranslate.setPreferredSize(new Dimension(40, 40));
        this.btnTranslate.setBorderPainted(false);
        this.btnTranslate.setFocusPainted(false);
        this.btnTranslate.setContentAreaFilled(false);
        this.btnTranslate.setHorizontalTextPosition(JButton.CENTER);
        this.btnTranslate.setVerticalTextPosition(JButton.CENTER);
        this.btnTranslate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.btnTranslate.setToolTipText("Translate");
        this.topRight.add(this.btnTranslate);
        //add action listioner
        this.btnTranslate.addActionListener(this::btnTranslateActionPerformed);

        this.btnRightVi = new JButton("Tiếng Việt");
        this.btnRightVi.setPreferredSize(new Dimension(100, 40));
        this.btnRightVi.setBorderPainted(false);
        this.btnRightVi.setFocusPainted(false);
        this.btnRightVi.setBackground(new Color(213, 166, 19));
        this.btnRightVi.setForeground(Color.WHITE);
        this.btnRightVi.setFont(new Font("Roboto", 700, 14));
        this.btnRightVi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //add action listioner
        this.btnRightVi.addActionListener((ActionEvent evt) -> btnRightViActionPerformed(evt));

        this.btnRightEn = new JButton("English");
        this.btnRightEn.setPreferredSize(new Dimension(100, 40));
        this.btnRightEn.setBorderPainted(false);
        this.btnRightEn.setFocusPainted(false);
        this.btnRightEn.setBackground(new Color(213, 166, 19));
        this.btnRightEn.setForeground(Color.WHITE);
        this.btnRightEn.setFont(new Font("Roboto", 700, 14));
        this.btnRightEn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //add action listioner
        this.btnRightEn.addActionListener(this::btnRightEnActionPerformed);

        //add two buttont Top Right
        this.topRight.add(this.btnRightEn);
        this.topRight.add(this.btnRightVi);

        // Contain Right
        this.containRight = new JPanel(new FlowLayout(0, 5, 5));
        this.containRight.setPreferredSize(new Dimension(300, 300));
        //this.containRight.setBackground(Color.GRAY);
        //add contain left to Left Panel
        this.rightPanel.add(this.containRight);

        //txt
        this.txtTranslateContent = new JTextArea(15, 25);
        this.txtTranslateContent.setLineWrap(true);
        this.txtTranslateContent.setTabSize(4);
        this.txtTranslateContent.setFont(new Font("Roboto", 0, 14));
        this.txtTranslateContent.setEditable(false);
        JScrollPane txtScrollTranslate = new JScrollPane();
        txtScrollTranslate.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        txtScrollTranslate.setViewportView(this.txtTranslateContent);
        //add txtContent to contain left
        this.containRight.add(txtScrollTranslate);

        //BOTTOM Panel
        this.bottomPanel = new JPanel(new FlowLayout(0, 5, 5));
        //this.bottomPanel.setBackground(Color.BLACK);
        this.bottomPanel.setPreferredSize(new Dimension(600, 50));

        //button
        this.btnEdit = new JButton("Edit or Add");
        this.btnEdit.setPreferredSize(new Dimension(100, 40));
        this.bottomPanel.add(this.btnEdit);
        // add action Listioner
        this.btnEdit.addActionListener(this::btnEditActionPerformed);

        this.btnSave = new JButton("Save");
        this.btnSave.setPreferredSize(new Dimension(100, 40));
        this.bottomPanel.add(this.btnSave);
        // add action Listioner
        this.btnSave.addActionListener(this::btnSaveActionPerformed);

//        this.btnDelete = new JButton("Delete");
//        this.btnDelete.setPreferredSize(new Dimension(100, 40));
//        this.bottomPanel.add(this.btnDelete);
//        //add action listioner
//        this.btnDelete.addActionListener(this::btnDeleteActionPerformed);

        ///////////////////////////////////////
        Container container = getContentPane();
        container.setLayout(new FlowLayout(0, 0, 0));
        container.add(this.leftPanel);
        container.add(this.rightPanel);
        container.add(this.bottomPanel);
    }

    private void translateLanguage() {
        if (this.EnToVi) {
            this.btnLeftEn.setBackground(Color.GRAY);
            this.btnLeftEn.setCursor(Cursor.getDefaultCursor());
            this.btnRightVi.setBackground(Color.GRAY);
            this.btnRightVi.setCursor(Cursor.getDefaultCursor());
            //return default
            this.btnLeftVi.setBackground(new Color(213, 166, 19));
            this.btnLeftVi.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.btnRightEn.setBackground(new Color(213, 166, 19));
            this.btnRightEn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            this.btnLeftVi.setBackground(Color.GRAY);
            this.btnLeftVi.setCursor(Cursor.getDefaultCursor());
            this.btnRightEn.setBackground(Color.GRAY);
            this.btnRightEn.setCursor(Cursor.getDefaultCursor());
            // return default
            this.btnLeftEn.setBackground(new Color(213, 166, 19));
            this.btnLeftEn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.btnRightVi.setBackground(new Color(213, 166, 19));
            this.btnRightVi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    void btnLeftViActionPerformed(ActionEvent evt) {
        this.EnToVi = false;
        translateLanguage();
    }

    void btnLeftEnActionPerformed(ActionEvent evt) {
        this.EnToVi = true;
        translateLanguage();
    }

    void btnRightViActionPerformed(ActionEvent evt) {
        this.EnToVi = true;
        translateLanguage();
    }

    void btnRightEnActionPerformed(ActionEvent evt) {
        this.EnToVi = false;
        translateLanguage();
    }

    void btnTranslateActionPerformed(ActionEvent evt) {
        String leftWord = this.txtContent.getText().trim();

        if (leftWord.isEmpty()) {
            this.txtContent.requestFocus();
            return;
        }

        Words key = new Words(leftWord);
        TreeNode<Words> node;
        if (this.EnToVi) {
            node = this.enLang.search(key);
        } else {
            node = this.viLang.search(key);
        }

        if (node != null) {
            this.txtTranslateContent.setText(node.getKey().getMean());
            this.txtTranslateContent.setEditable(false);
            this.addNew = false;
        } else {
            this.txtTranslateContent.setText("");
            int request = JOptionPane.showConfirmDialog(this, "\"" + leftWord + "\"" + " doesn't"
                    + " have meaning! Woud you like add new mean for this word?",
                    "Meaing?", JOptionPane.YES_NO_OPTION);
            if (request == JOptionPane.YES_OPTION) {
                btnEditActionPerformed(null);
                this.txtTranslateContent.requestFocus();
            } else {
                this.txtTranslateContent.setEditable(false);
                this.addNew = false;
            }
        }

        showOrHideSavebtn();
    }

    void btnEditActionPerformed(ActionEvent evt) {
        this.txtTranslateContent.setEditable(true);
        this.addNew = true;
        showOrHideSavebtn();
    }

    void btnSaveActionPerformed(ActionEvent evt) {
        String leftWord = this.txtContent.getText().trim();
        String rightWord = this.txtTranslateContent.getText().trim();

        if (leftWord.isEmpty() || rightWord.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the word!");
            return;
        }

        String[] mean = rightWord.split("[,]+");

        if (this.addNew) {
            for (String e : mean) {
                addToDictionary(leftWord, e.trim(), this.EnToVi);
                addToDictionary(e.trim(), leftWord, !this.EnToVi);
            }
        }
        JOptionPane.showMessageDialog(this, "Added successfull!");
        this.addNew = false;
        showOrHideSavebtn();
        saveResource();
    }

    void addToDictionary(String word, String mean, boolean curEntoVi) {
        BinarySearchTree<Words> tree;
        if (curEntoVi) {
            tree = this.enLang;
        } else {
            tree = this.viLang;
        }
        Words key = new Words(word);
        TreeNode<Words> node = tree.search(key);
        if (this.addNew) {
            if (node == null) {
                key.setMean(mean);
                tree.insert(key);
            } else {
                LinkedList<String> listMean = node.getKey().getMeanlist();
                for (int i = 0; i < listMean.size(); i++) {
                    if (mean.equals(listMean.get(i))) {
                        return;
                    }
                }
                node.getKey().setMean(mean);
            }
        }
    }

    void btnDeleteActionPerformed(ActionEvent evt) {
        String leftWord = this.txtContent.getText().trim();
        if (hasInTree(leftWord, this.EnToVi)) {
            int request = JOptionPane.showConfirmDialog(this, "Do you want to "
                    + "delete this word?", "Delete?", JOptionPane.YES_NO_OPTION);
            if (request == JOptionPane.YES_OPTION) {
                Words key = new Words(leftWord.trim());
                TreeNode<Words> node = findNode(key, EnToVi);

                if (node != null) {
                    LinkedList<String> meanList = node.getKey().getMeanlist();
                    meanList.stream()
                            .map((e) -> new Words(e))
                            //.forEach((e) -> deleteWord(e, this.EnToVi))
                            //.filter((String e) -> !e.isEmpty())

                            .forEach((e) -> deleteWord(e, !EnToVi));
                    deleteWord(node.getKey(), this.EnToVi);
                } else {
                    System.out.println("Node is null");
                }
                //JOptionPane.showMessageDialog(this, "Delete successfull!");
            }
        } else {
            int request = JOptionPane.showConfirmDialog(this, "This word \"" + leftWord + "\" "
                    + "does not have in the database, Would you like to add new "
                    + "mean?", "Mean?", JOptionPane.YES_NO_OPTION);
            if (request == JOptionPane.YES_OPTION) {
                btnEditActionPerformed(null);
                this.txtTranslateContent.requestFocus();
            } else {
                this.txtContent.requestFocus();
            }
        }
        saveResource();
    }

    void deleteWord(Words key, boolean curEntoVi) {
        if (curEntoVi) {
            enLang.delete(key);
        } else {
            viLang.delete(key);
        }
    }

    TreeNode<Words> findNode(Words key, boolean curEnToVi) {
        if (curEnToVi) {
            return enLang.search(key);
        } else {
            return viLang.search(key);
        }
    }

    boolean hasInTree(String word, boolean curEntoVi) {
        Words key = new Words(word);
        BinarySearchTree<Words> tree;

        if (curEntoVi) {
            tree = enLang;
        } else {
            tree = viLang;
        }

        TreeNode<Words> node = tree.search(key);
        return node != null;
    }

    void showOrHideSavebtn() {
        if (this.addNew) {
            this.btnSave.setVisible(true);
        } else {
            this.btnSave.setVisible(false);
        }
    }

    void txtContentKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnTranslateActionPerformed(null);
        }
    }

    public static void main(String[] args) {
        Translate app = new Translate();
        app.setDefaultCloseOperation(EXIT_ON_CLOSE);
        app.setResizable(false);
        app.setVisible(true);
        app.setSize(606, 429);
        app.setLocationRelativeTo(null);
        app.setIconImage(Toolkit.getDefaultToolkit().getImage("src/images/add.png"));
        app.setTitle("Translate");
    }

    //Layout
    JPanel leftPanel;
    JPanel rightPanel;
    JPanel topLeft;
    JPanel topRight;
    JPanel containLeft;
    JPanel containRight;
    JPanel bottomPanel;
    //Elements
    // Button
    JButton btnLeftVi, btnLeftEn;
    JButton btnRightVi, btnRightEn;
    JButton btnTranslate;
    JButton btnEdit;
    JButton btnSave;
    JButton btnDelete;
    // Text area
    JTextArea txtContent;
    JTextArea txtTranslateContent;
    // Label
    // Combo Box
    //JComboBox<String> cmbLanguage;
}
