package SearchEngine;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class MainFrame {

    /* Frame components */
    private JFrame frmMain;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnBrowse;
    private JButton btnClear;
    private JTextPane txtResult;
    private JScrollPane scrollPane;
    private JLabel lblSearchText;
    private JLabel lblPath;
    private JLabel lblResult;
    long timeStart;
    long timeEnd;

    /* Dimension values */
    private static final int MAIN_FORM_WIDTH = 535;
    private static final int MAIN_FORM_HEIGHT = 700;

    private static final int BTN_SEARCH_X = 10;
    private static final int BTN_SEARCH_Y = 10;
    private static final int BTN_SEARCH_WIDTH = 40;
    private static final int BTN_SEARCH_HEIGHT = 40;

    private static final int BTN_BROWSE_X = BTN_SEARCH_X + BTN_SEARCH_WIDTH + 15;
    private static final int BTN_BROWSE_Y = BTN_SEARCH_Y;
    private static final int BTN_BROWSE_WIDTH = BTN_SEARCH_WIDTH;
    private static final int BTN_BROWSE_HEIGHT = BTN_SEARCH_HEIGHT;

    private static final int BTN_CLEAR_X = BTN_BROWSE_X + BTN_SEARCH_WIDTH + 15;
    private static final int BTN_CLEAR_Y = BTN_BROWSE_Y;
    private static final int BTN_CLEAR_WIDTH = BTN_SEARCH_WIDTH;
    private static final int BTN_CLEAR_HEIGHT = BTN_SEARCH_HEIGHT;

    private static final int LBL_SEARCH_TEXT_X = BTN_SEARCH_X;
    private static final int LBL_SEARCH_TEXT_Y = BTN_SEARCH_Y + BTN_SEARCH_HEIGHT + 15;
    private static final int LBL_SEARCH_TEXT_WIDTH = 50;
    private static final int LBL_SEARCH_TEXT_HEIGHT = 10;

    private static final int TXT_SEARCH_X = LBL_SEARCH_TEXT_X;
    private static final int TXT_SEARCH_Y = LBL_SEARCH_TEXT_Y + LBL_SEARCH_TEXT_HEIGHT + 10;
    private static final int TXT_SEARCH_WIDTH = 510;
    private static final int TXT_SEARCH_HEIGHT = 25;

    private static final int LBL_PATH_X = BTN_SEARCH_X;
    private static final int LBL_PATH_Y = TXT_SEARCH_Y + TXT_SEARCH_HEIGHT + 5;
    private static final int LBL_PATH_WIDTH = 500;
    private static final int LBL_PATH_HEIGHT = 25;

    private static final int LBL_RESULT_X = BTN_SEARCH_X;
    private static final int LBL_RESULT_Y = LBL_PATH_Y + LBL_PATH_HEIGHT + 5;
    private static final int LBL_RESULT_WIDTH = 50;
    private static final int LBL_RESULT_HEIGHT = 25;

    private static final int TXT_RESULT_X = BTN_SEARCH_X;
    private static final int TXT_RESULT_Y = LBL_RESULT_Y + LBL_RESULT_HEIGHT + 5;
    private static final int TXT_RESULT_WIDTH = TXT_SEARCH_WIDTH;
    private static final int TXT_RESULT_HEIGHT = MAIN_FORM_HEIGHT - 215;

    public static Indexing index;

    public MainFrame() {
        initialize();
    }

    public static void main(String args[]) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.frmMain.setVisible(true);
        mainFrame.timeStart = System.currentTimeMillis();
        List<File> fileList = index.indexFileList("res/");

        if (!index.isIndexed("indexed/")) {
            index.buildIndex(fileList);
//            index.saveIndex();
            index.saveFileList();
        } else {
            index.readIndex();
            index.readFileList();
        }
        mainFrame.timeEnd = System.currentTimeMillis();
        System.out.println("Running time: "+(mainFrame.timeEnd - mainFrame.timeStart) / 1000);

        mainFrame.frmMain.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                index.clearIndexedFolder("indexed/");
            }

        });

    }

    private void initialize() {
        frmMain = new JFrame();
        frmMain.setTitle("Search Engine");
        frmMain.setSize(MAIN_FORM_WIDTH, MAIN_FORM_HEIGHT);
        frmMain.setResizable(false);
        frmMain.getContentPane().setLayout(null);
        frmMain.setLocationRelativeTo(null);
        frmMain.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        index = Indexing.getInstance();

        txtSearch = new JTextField();
        txtSearch.setBounds(TXT_SEARCH_X, TXT_SEARCH_Y, TXT_SEARCH_WIDTH, TXT_SEARCH_HEIGHT);
        frmMain.getContentPane().add(txtSearch);

        lblSearchText = new JLabel("Search");
        lblSearchText.setBounds(LBL_SEARCH_TEXT_X, LBL_SEARCH_TEXT_Y, LBL_SEARCH_TEXT_WIDTH, LBL_SEARCH_TEXT_HEIGHT);
        frmMain.getContentPane().add(lblSearchText);

        btnSearch = new JButton(new ImageIcon(this.getClass().getResource("/Image/Search.png")));

        btnSearch.setBounds(BTN_SEARCH_X, BTN_SEARCH_Y, BTN_SEARCH_WIDTH, BTN_SEARCH_HEIGHT);
        frmMain.getContentPane().add(btnSearch);
        frmMain.getRootPane().setDefaultButton(btnSearch);
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                performSearch();
            }
        });

        btnSearch.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                performSearch();
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });

        btnBrowse = new JButton(new ImageIcon(getClass().getResource("/Image/Browse.png")));
        btnBrowse.setBounds(BTN_BROWSE_X, BTN_BROWSE_Y, BTN_BROWSE_WIDTH, BTN_BROWSE_HEIGHT);
        frmMain.getContentPane().add(btnBrowse);
        btnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("./src/"));
                chooser.setDialogTitle("Select your index folder...");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//                    System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
                    String folderPath = chooser.getSelectedFile().getAbsolutePath();
                    lblPath.setText("Folder: " + folderPath);
                    List<File> fileList = index.indexFileList(folderPath);
                    index.buildIndex(fileList);
                    index.saveIndex();
                    index.saveFileList();
//                    index.saveFolderPath(folderPath);
                } else {
                    System.out.println("No Selection ");
                }
            }
        });

        btnClear = new JButton(new ImageIcon(getClass().getResource("/Image/Clear.png")));
        btnClear.setBounds(BTN_CLEAR_X, BTN_CLEAR_Y, BTN_CLEAR_WIDTH, BTN_CLEAR_HEIGHT);
        frmMain.getContentPane().add(btnClear);

//        if (Preferences.userRoot().node(Constants.PREF_NAME).get(Constants.FOLDER_PATH, "").equals("")) {
        lblPath = new JLabel("Folder: " + index.getDefaultPath());
//            index.saveFolderPath(index.getDefaultPath());
//        }
        lblPath.setBounds(LBL_PATH_X, LBL_PATH_Y, LBL_PATH_WIDTH, LBL_PATH_HEIGHT);
        lblPath.setFont(new Font("Serif", Font.TRUETYPE_FONT, 14));
        frmMain.getContentPane().add(lblPath);

        lblResult = new JLabel("Result: ");
        lblResult.setBounds(LBL_RESULT_X, LBL_RESULT_Y, LBL_RESULT_WIDTH, LBL_RESULT_HEIGHT);
        frmMain.getContentPane().add(lblResult);

        txtResult = new JTextPane();
        txtResult.setContentType("text/html");
        txtResult.setEditable(false);
        txtResult.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        scrollPane = new JScrollPane(txtResult);
        scrollPane.setBounds(TXT_RESULT_X, TXT_RESULT_Y, TXT_RESULT_WIDTH, TXT_RESULT_HEIGHT);
        frmMain.getContentPane().add(scrollPane);
    }

    private void performSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String keyword = txtSearch.getText().toString();
                String[] tokens = keyword.split("\\s+");
                if (tokens.length > 1) {
                    txtResult.setText(index.searchPhrase(keyword));
                } else {
                    txtResult.setText(index.searchOne(keyword));
                }
            }
        }).start();
    }
}