package gui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import client.Client;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.util.List;

public class Gui {
    private JFrame mainFrame;
    private JFrame lobbyFrame;
    private JDialog dialog;
    private JDialog regisDialog;
    private JDialog loginDialog;
    private JDialog addDialog;
    private JButton loginButton;
    private JButton regisButton;
    private JButton quitButton;
    private JButton confirmButton;
    private JButton cancelButton;
    private JButton closeButton;
    private JTextField userName;
    private JTextField password;
    private JTextField confirmedPw;
    private JScrollPane userScroll;
    private JScrollPane groupScroll;
    private JPanel userPanel;
    private JPanel groupPanel;
    private JButton reloadButton;
    private JButton groupButton;
    private JPanel textPanel;
    private JPanel mainPanel;
    private JPanel lobbyPanel;
    private JTextField textField = new JTextField(10);
    private JScrollPane textScroll;
    private JPanel messagePanel;
    private Client client;
    private List<String> memberList = new ArrayList<>();
    private JButton clearButton;
    private JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    private JButton fileChooserButton;

    public Gui(Client client) {
        this.client = client;
    }

    public JFrame createHomePage() {
        mainFrame = new JFrame("Java Chat Application");
        mainFrame.setSize(500, 300);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.ORANGE);

        JLabel title = new JLabel("Welcome to Java Chat Application");
        title.setPreferredSize(new Dimension(500, 80));
        title.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.BLUE);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login to existed account");
        loginButton.setPreferredSize(new Dimension(300, 50));
        loginButton.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        loginButton.setForeground(Color.BLACK);
        loginButton.setBackground(Color.CYAN);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusable(false);

        regisButton = new JButton("Create a new account");
        regisButton.setPreferredSize(new Dimension(300, 50));
        regisButton.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        regisButton.setForeground(Color.BLACK);
        regisButton.setBackground(Color.GREEN);
        regisButton.setOpaque(true);
        regisButton.setBorderPainted(false);
        regisButton.setFocusable(false);

        quitButton = new JButton("Close the application");
        quitButton.setPreferredSize(new Dimension(300, 50));
        quitButton.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        quitButton.setForeground(Color.BLACK);
        quitButton.setBackground(Color.RED);
        quitButton.setOpaque(true);
        quitButton.setBorderPainted(false);
        quitButton.setFocusable(false);
        buttonPanel.setBackground(Color.ORANGE);

        buttonPanel.add(loginButton);
        buttonPanel.add(regisButton);
        buttonPanel.add(quitButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(title, BorderLayout.NORTH);

        mainFrame.add(mainPanel);
        return mainFrame;
    }

    public JDialog createNotification(String notiMessage) {
        dialog = new JDialog();

        dialog.setLayout(new FlowLayout());
        dialog.getContentPane().setBackground(Color.ORANGE);
        dialog.setSize(new Dimension(300, 160));
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setTitle("System notification");
        JLabel notiTitle = new JLabel("Notification", JLabel.CENTER);
        notiTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        notiTitle.setForeground(Color.BLUE);
        dialog.add(notiTitle);

        JLabel noti = new JLabel(notiMessage, JLabel.CENTER);
        noti.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        noti.setBackground(Color.ORANGE);
        dialog.add(noti);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JPanel space = new JPanel();
        space.setPreferredSize(new Dimension(300, 20));
        space.setBackground(Color.ORANGE);
        closeButton = new JButton("Close notification");
        closeButton.setForeground(Color.BLACK);
        closeButton.setBackground(Color.WHITE);
        closeButton.setOpaque(true);
        closeButton.setBorderPainted(false);
        closeButton.setFocusable(false);
        buttonPanel.add(space);
        buttonPanel.add(closeButton);
        buttonPanel.setPreferredSize(new Dimension(300, 60));
        buttonPanel.setBackground(Color.ORANGE);
        dialog.add(buttonPanel);

        return dialog;
    }

    public JDialog createRegisForm() {
        regisDialog = new JDialog(mainFrame);
        regisDialog.setSize(new Dimension(250, 300));
        regisDialog.setLayout(new FlowLayout());
        regisDialog.getContentPane().setBackground(Color.ORANGE);
        regisDialog.setLocationRelativeTo(null);
        regisDialog.setTitle("Register Form");
        regisDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JLabel title = new JLabel("Register to Chat Application", JLabel.CENTER);
        title.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        title.setPreferredSize(new Dimension(250, 50));
        title.setForeground(Color.BLUE);
        title.setBackground(Color.ORANGE);

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setPreferredSize(new Dimension(200, 40));
        JLabel userNameLabel = new JLabel("User name: ", JLabel.LEFT);
        userName = new JTextField(10);
        userPanel.add(userNameLabel, BorderLayout.NORTH);
        userPanel.add(userName, BorderLayout.CENTER);
        userPanel.setBackground(Color.ORANGE);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setPreferredSize(new Dimension(200, 40));
        JLabel passwordLabel = new JLabel("Password: ", JLabel.LEFT);
        password = new JPasswordField(10);
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(password, BorderLayout.CENTER);
        passwordPanel.setBackground(Color.ORANGE);

        JPanel confirmPasswordPanel = new JPanel(new BorderLayout());
        confirmPasswordPanel.setPreferredSize(new Dimension(200, 40));
        JLabel confirmPasswordLabel = new JLabel("Confirm password: ", JLabel.LEFT);
        confirmedPw = new JPasswordField(10);
        confirmPasswordPanel.add(confirmPasswordLabel, BorderLayout.NORTH);
        confirmPasswordPanel.add(confirmedPw, BorderLayout.CENTER);
        confirmPasswordPanel.setBackground(Color.ORANGE);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel space = new JPanel();
        space.setPreferredSize(new Dimension(200, 20));
        space.setBackground(Color.ORANGE);
        buttonPanel.setPreferredSize(new Dimension(200, 50));

        cancelButton = new JButton("Cancel");
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setBackground(Color.RED);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusable(false);

        confirmButton = new JButton("Confirm");
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setBackground(Color.GREEN);
        confirmButton.setOpaque(true);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusable(false);
        buttonPanel.add(cancelButton, BorderLayout.WEST);
        buttonPanel.add(confirmButton, BorderLayout.EAST);
        buttonPanel.add(space, BorderLayout.NORTH);
        buttonPanel.setBackground(Color.ORANGE);

        regisDialog.add(title);
        regisDialog.add(userPanel);
        regisDialog.add(passwordPanel);
        regisDialog.add(confirmPasswordPanel);
        regisDialog.add(buttonPanel);

        return dialog;
    }

    public JDialog createLoginForm() {
        loginDialog = new JDialog(mainFrame);
        loginDialog.setSize(new Dimension(250, 250));
        loginDialog.setLayout(new FlowLayout());
        loginDialog.getContentPane().setBackground(Color.ORANGE);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setTitle("Login Form");
        loginDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JLabel title = new JLabel("Login to Chat Application", JLabel.CENTER);
        title.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        title.setPreferredSize(new Dimension(250, 50));
        title.setForeground(Color.BLUE);
        title.setBackground(Color.ORANGE);

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setPreferredSize(new Dimension(200, 40));
        JLabel userNameLabel = new JLabel("User name: ", JLabel.LEFT);
        userName = new JTextField(10);
        userPanel.add(userNameLabel, BorderLayout.NORTH);
        userPanel.add(userName, BorderLayout.CENTER);
        userPanel.setBackground(Color.ORANGE);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setPreferredSize(new Dimension(200, 40));
        JLabel passwordLabel = new JLabel("Password: ", JLabel.LEFT);
        password = new JPasswordField(10);
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(password, BorderLayout.CENTER);
        passwordPanel.setBackground(Color.ORANGE);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel space = new JPanel();
        space.setPreferredSize(new Dimension(200, 20));
        space.setBackground(Color.ORANGE);
        buttonPanel.setPreferredSize(new Dimension(200, 50));

        cancelButton = new JButton("Cancel");
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setBackground(Color.RED);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusable(false);

        confirmButton = new JButton("Confirm");
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setBackground(Color.GREEN);
        confirmButton.setOpaque(true);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusable(false);
        buttonPanel.add(cancelButton, BorderLayout.WEST);
        buttonPanel.add(confirmButton, BorderLayout.EAST);
        buttonPanel.add(space, BorderLayout.NORTH);
        buttonPanel.setBackground(Color.ORANGE);

        loginDialog.add(title);
        loginDialog.add(userPanel);
        loginDialog.add(passwordPanel);
        loginDialog.add(buttonPanel);

        return loginDialog;
    }

    public JScrollPane createUserScroll(String[] userList) {
        JPanel panel = new JPanel();

        GridLayout grid = new GridLayout(0, 1);
        grid.setVgap(10);

        JPanel userWrapper = new JPanel(grid);
        userWrapper.setBackground(Color.ORANGE);

        for (int i = 0; i < userList.length; i++) {
            JButton button = new JButton(userList[i]);
            button.setForeground(Color.BLACK);
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setFocusable(false);
            userWrapper.add(button);

            String userName = userList[i];

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.setContact(userName);
                    client.setContactStatus("username");

                    BorderLayout lobbyLayout = (BorderLayout) lobbyPanel.getLayout();
                    lobbyPanel.remove(lobbyLayout.getLayoutComponent(BorderLayout.CENTER));

                    JButton currentButton = new JButton(userName);
                    currentButton = button;
                    userWrapper.remove(button);
                    userWrapper.add(currentButton, 0);

                    JPanel panel = createTextPanel(userName);
                    lobbyPanel.add(panel, BorderLayout.CENTER);
                    lobbyPanel.invalidate();
                    lobbyPanel.validate();

                    String loadUserHistory = "req|loadUserHistory" + ";" + client.getSocketID()
                            + ";"
                            + client.getContact();
                    try {
                        client.getSocket().getOutputStream().write(loadUserHistory.getBytes());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

        panel.add(userWrapper);
        panel.setBackground(Color.ORANGE);

        userScroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        userScroll.setPreferredSize(new Dimension(100, 340));

        return userScroll;
    }

    public JScrollPane createGroupScroll(String[] groupList) {
        JPanel panel = new JPanel();

        GridLayout grid = new GridLayout(0, 1);
        grid.setVgap(10);

        JPanel groupWrapper = new JPanel(grid);
        groupWrapper.setBackground(Color.ORANGE);

        for (int i = 0; i < groupList.length; i++) {
            JButton button = new JButton(groupList[i]);
            button.setForeground(Color.BLACK);
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setFocusable(false);
            groupWrapper.add(button);

            String userName = groupList[i];

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.setContact(userName);
                    client.setContactStatus("group");

                    BorderLayout lobbyLayout = (BorderLayout) lobbyPanel.getLayout();
                    lobbyPanel.remove(lobbyLayout.getLayoutComponent(BorderLayout.CENTER));

                    JButton currentButton = new JButton(userName);
                    currentButton = button;
                    groupWrapper.remove(button);
                    groupWrapper.add(currentButton, 0);

                    JPanel panel = createTextPanel(userName);
                    lobbyPanel.add(panel, BorderLayout.CENTER);
                    lobbyPanel.invalidate();
                    lobbyPanel.validate();

                    String loadGroupHistory = "req|loadGroupHistory" + ";" + userName;
                    try {
                        client.getSocket().getOutputStream().write(loadGroupHistory.getBytes());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

        panel.add(groupWrapper);
        panel.setBackground(Color.ORANGE);

        groupScroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        groupScroll.setPreferredSize(new Dimension(100, 340));

        return groupScroll;
    }

    public JScrollPane createTextScroll() {
        JPanel panel = new JPanel();

        GridLayout grid = new GridLayout(0, 1);
        grid.setVgap(10);

        messagePanel = new JPanel(grid);
        messagePanel.setBackground(Color.ORANGE);
        panel.add(messagePanel);
        panel.setBackground(Color.ORANGE);

        textScroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textScroll.setPreferredSize(new Dimension(400, 350));

        return textScroll;
    }

    public JPanel createUserPanel(String[] userList) {
        userPanel = new JPanel(new FlowLayout());

        JLabel title = new JLabel("USER", JLabel.CENTER);
        title.setFont(new Font(Font.DIALOG, Font.ITALIC, 15));
        title.setBackground(Color.ORANGE);

        userScroll = createUserScroll(userList);

        reloadButton = new JButton("RELOAD");
        reloadButton.setForeground(Color.BLACK);
        reloadButton.setBackground(Color.YELLOW);
        reloadButton.setOpaque(true);
        reloadButton.setBorderPainted(false);
        reloadButton.setFocusable(false);

        userPanel.add(title);
        userPanel.add(userScroll);
        userPanel.add(reloadButton);

        userPanel.setPreferredSize(new Dimension(100, 550));
        userPanel.setBackground(Color.ORANGE);
        return userPanel;
    }

    public JPanel createGroupPanel(String[] groupList) {
        groupPanel = new JPanel(new FlowLayout());

        JLabel title = new JLabel("GROUP", JLabel.CENTER);
        title.setFont(new Font(Font.DIALOG, Font.ITALIC, 15));
        title.setBackground(Color.ORANGE);

        groupScroll = createGroupScroll(groupList);

        groupButton = new JButton("ADD GROUP");
        groupButton.setForeground(Color.BLACK);
        groupButton.setBackground(Color.GREEN);
        groupButton.setOpaque(true);
        groupButton.setBorderPainted(false);
        groupButton.setFocusable(false);

        groupPanel.add(title);
        groupPanel.add(groupScroll);
        groupPanel.add(groupButton);

        groupPanel.setPreferredSize(new Dimension(100, 550));
        groupPanel.setBackground(Color.ORANGE);
        return groupPanel;
    }

    public JPanel createTextPanel(String titleString) {
        textPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("are texting with " + titleString, JLabel.CENTER);
        title.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        title.setOpaque(true);
        title.setBackground(Color.ORANGE);
        title.setForeground(Color.BLUE);
        title.setPreferredSize(new Dimension(280, 30));

        textScroll = createTextScroll();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(280, 40));

        fileChooserButton = new JButton("Upload file");
        fileChooserButton.setForeground(Color.BLACK);
        fileChooserButton.setBorderPainted(false);
        fileChooserButton.setFocusable(false);

        panel.add(textField, BorderLayout.CENTER);
        panel.add(fileChooserButton, BorderLayout.EAST);

        textPanel.add(title, BorderLayout.NORTH);
        textPanel.add(textScroll, BorderLayout.CENTER);
        textPanel.add(panel, BorderLayout.SOUTH);

        return textPanel;
    }

    public JFrame createChatLobby(String userName) {
        lobbyFrame = new JFrame("Java Chat Application");
        lobbyFrame.setSize(500, 500);
        lobbyFrame.setLocationRelativeTo(null);
        lobbyFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        lobbyPanel = new JPanel(new BorderLayout());
        lobbyPanel.setBackground(Color.ORANGE);

        JLabel title = new JLabel(userName, JLabel.CENTER);
        title.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
        title.setForeground(Color.BLUE);
        title.setBackground(Color.ORANGE);

        JPanel utilPanel = new JPanel(new GridLayout(1, 2));
        utilPanel.setPreferredSize(new Dimension(500, 20));
        utilPanel.setBackground(Color.ORANGE);

        clearButton = new JButton("Clear history");
        clearButton.setForeground(Color.BLACK);
        clearButton.setBackground(Color.GRAY);
        clearButton.setOpaque(true);
        clearButton.setBorderPainted(false);
        clearButton.setFocusable(false);

        quitButton = new JButton("Close the application");
        quitButton.setForeground(Color.BLACK);
        quitButton.setBackground(Color.RED);
        quitButton.setOpaque(true);
        quitButton.setBorderPainted(false);
        quitButton.setFocusable(false);

        utilPanel.add(clearButton);
        utilPanel.add(quitButton);

        lobbyPanel.add(title, BorderLayout.NORTH);
        lobbyPanel.add(utilPanel, BorderLayout.SOUTH);
        lobbyPanel.add(userPanel, BorderLayout.WEST);
        lobbyPanel.add(groupPanel, BorderLayout.EAST);
        lobbyPanel.add(textPanel, BorderLayout.CENTER);

        lobbyFrame.add(lobbyPanel);
        return lobbyFrame;
    }

    public JPanel getSendMessagePanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 30));

        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.blue);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(message.length() * 7, 30));

        panel.add(label, BorderLayout.EAST);
        panel.setBackground(Color.ORANGE);
        return panel;
    }

    public JPanel getReceiveMesagePanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 30));

        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.LIGHT_GRAY);
        label.setForeground(Color.BLACK);
        label.setPreferredSize(new Dimension(message.length() * 7, 30));

        panel.add(label, BorderLayout.WEST);
        panel.setBackground(Color.ORANGE);
        return panel;
    }

    public JPanel getSendFile(String fileName, String data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 30));

        JButton button = new JButton(fileName);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(fileName.length() * 10, 30));

        String[] content = data.split(";");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f = new File(fileName);
                try {
                    f.createNewFile();
                    FileWriter writer = new FileWriter(fileName);
                    BufferedWriter bufferWriter = new BufferedWriter(writer);
                    for (int i = 1; i < content.length; i++) {
                        bufferWriter.write(content[i]);
                        bufferWriter.newLine();
                    }
                    bufferWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        panel.add(button, BorderLayout.EAST);
        panel.setBackground(Color.ORANGE);
        return panel;
    }

    public JPanel getReceiveFile(String fileName, String data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 30));

        JButton button = new JButton(fileName);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(fileName.length() * 10, 30));

        String[] content = data.split(";");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f = new File(fileName);
                try {
                    f.createNewFile();
                    FileWriter writer = new FileWriter(fileName);
                    BufferedWriter bufferWriter = new BufferedWriter(writer);
                    for (int i = 1; i < content.length; i++) {
                        bufferWriter.write(content[i]);
                        bufferWriter.newLine();
                    }
                    bufferWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        panel.add(button, BorderLayout.WEST);
        panel.setBackground(Color.ORANGE);
        return panel;
    }

    public JPanel getReceiveGroupFile(String fileName, String data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 30));

        JButton button = new JButton(fileName);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(fileName.length() * 10, 30));

        String[] content = data.split(";");

        String[] realFileName = fileName.split(":");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f = new File(realFileName[1]);
                try {
                    f.createNewFile();
                    FileWriter writer = new FileWriter(realFileName[1]);
                    BufferedWriter bufferWriter = new BufferedWriter(writer);
                    for (int i = 1; i < content.length; i++) {
                        bufferWriter.write(content[i]);
                        bufferWriter.newLine();
                    }
                    bufferWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        panel.add(button, BorderLayout.WEST);
        panel.setBackground(Color.ORANGE);
        return panel;
    }

    public JDialog createNewGroupDialog(String[] userList) {
        List<String> list = new ArrayList<>();

        list.add(client.getSocketID());
        for (int i = 0; i < userList.length; i++) {
            list.add(userList[i]);
        }

        addDialog = new JDialog();

        addDialog.setLayout(new FlowLayout());
        addDialog.getContentPane().setBackground(Color.ORANGE);
        addDialog.setSize(new Dimension(250, 320));
        addDialog.setLocationRelativeTo(null);
        addDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addDialog.setTitle("Adding New Group Form");

        JLabel title = new JLabel("Adding New Group", JLabel.CENTER);
        title.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        title.setPreferredSize(new Dimension(250, 50));
        title.setForeground(Color.BLUE);
        title.setBackground(Color.ORANGE);

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setPreferredSize(new Dimension(200, 40));
        JLabel userNameLabel = new JLabel("Group name: ", JLabel.LEFT);
        userName = new JTextField(10);
        userPanel.add(userNameLabel, BorderLayout.NORTH);
        userPanel.add(userName, BorderLayout.CENTER);
        userPanel.setBackground(Color.ORANGE);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        JLabel passwordLabel = new JLabel("Choose members: ", JLabel.LEFT);
        passwordLabel.setPreferredSize(new Dimension(200, 20));

        GridLayout grid = new GridLayout(0, 1);
        grid.setVgap(5);

        JPanel groupWrapper = new JPanel(grid);
        groupWrapper.setBackground(Color.ORANGE);

        for (int i = 0; i < list.size(); i++) {
            JCheckBox cb = new JCheckBox(list.get(i));
            cb.setHorizontalAlignment(JCheckBox.LEFT);
            cb.setBackground(Color.ORANGE);
            groupWrapper.add(cb);

            cb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    memberList.add(cb.getText());
                }
            });
        }

        passwordPanel.add(groupWrapper, BorderLayout.CENTER);
        passwordPanel.setBackground(Color.ORANGE);

        userScroll = new JScrollPane(passwordPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        userScroll.setPreferredSize(new Dimension(100, 90));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel space = new JPanel();
        space.setPreferredSize(new Dimension(200, 20));
        space.setBackground(Color.ORANGE);
        buttonPanel.setPreferredSize(new Dimension(200, 50));

        cancelButton = new JButton("Cancel");
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setBackground(Color.RED);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusable(false);

        confirmButton = new JButton("Confirm");
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setBackground(Color.GREEN);
        confirmButton.setOpaque(true);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusable(false);
        buttonPanel.add(cancelButton, BorderLayout.WEST);
        buttonPanel.add(confirmButton, BorderLayout.EAST);
        buttonPanel.add(space, BorderLayout.NORTH);
        buttonPanel.setBackground(Color.ORANGE);

        addDialog.add(title);
        addDialog.add(userPanel);
        addDialog.add(passwordLabel);
        addDialog.add(userScroll);
        addDialog.add(buttonPanel);

        return addDialog;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getRegisButton() {
        return regisButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    public JButton getCloseButton() {
        return closeButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public JFrame getLobbyFrame() {
        return lobbyFrame;
    }

    public JDialog getDialog() {
        return dialog;
    }

    public JTextField getUserName() {
        return userName;
    }

    public JTextField getPassword() {
        return password;
    }

    public JTextField getConfirmedPw() {
        return confirmedPw;
    }

    public JDialog getRegisDialog() {
        return regisDialog;
    }

    public JDialog getLoginDialog() {
        return loginDialog;
    }

    public JScrollPane getUserScroll() {
        return userScroll;
    }

    public JScrollPane getGroupScroll() {
        return groupScroll;
    }

    public JPanel getUserPanel() {
        return userPanel;
    }

    public JPanel getGroupPanel() {
        return groupPanel;
    }

    public JButton getReloadButton() {
        return reloadButton;
    }

    public JButton getGroupButton() {
        return groupButton;
    }

    public JPanel getTextPanel() {
        return textPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getLobbyPanel() {
        return lobbyPanel;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JScrollPane getTextScroll() {
        return textScroll;
    }

    public JPanel getMessagePanel() {
        return messagePanel;
    }

    public List<String> getMemberList() {
        return memberList;
    }

    public JDialog getAddDialog() {
        return addDialog;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public JButton getChooserButton() {
        return fileChooserButton;
    }
}
