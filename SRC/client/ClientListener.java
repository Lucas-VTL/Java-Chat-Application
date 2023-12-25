package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JScrollBar;
import javax.swing.filechooser.FileSystemView;

import gui.Gui;

public class ClientListener implements Runnable {
    private InputStream input;
    private OutputStream output;
    private Client client;

    public ClientListener(Client client) {
        this.client = client;
        try {
            this.input = client.getSocket().getInputStream();
            this.output = client.getSocket().getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Gui gui = new Gui(client);
            this.client.setFrame(gui.createHomePage());
            gui.getMainFrame().setVisible(true);

            gui.getLoginButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String login = "req|login";
                    try {
                        output.write(login.getBytes());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            gui.getRegisButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String regis = "req|regis";
                    try {
                        output.write(regis.getBytes());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            gui.getQuitButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String quit = "req|quit";
                    try {
                        output.write(quit.getBytes());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            String[] tempUserList = new String[0];
            String[] tempGroupList = new String[0];
            gui.createUserPanel(tempUserList);
            gui.createGroupPanel(tempGroupList);
            gui.createTextPanel("");
            gui.createChatLobby(client.getSocketID());

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) { // If the input didn't get any character, bytesRead
                                                             // will
                                                             // return -1
                String message = new String(buffer, 0, bytesRead);

                if (message.equals("res|quit")) {
                    gui.getMainFrame().setVisible(false);
                    gui.createNotification("Thank you for using our Chat Application");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            System.exit(0);
                        }
                    });
                }

                if (message.equals("res|regis")) {
                    gui.getMainFrame().setVisible(false);
                    gui.createRegisForm();
                    gui.getRegisDialog().setVisible(true);
                    gui.getCancelButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getRegisDialog().setVisible(false);
                            gui.getMainFrame().setVisible(true);
                        }
                    });
                    gui.getConfirmButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String userName = gui.getUserName().getText();
                            String password = gui.getPassword().getText();
                            String confirmedPw = gui.getConfirmedPw().getText();

                            if (!userName.isEmpty() && !password.isEmpty() && !confirmedPw.isEmpty()) {
                                if (userName.contains("|") || userName.contains("`") || userName.contains(";")) {
                                    String confirm = "req|regis|invalidUserName";
                                    try {
                                        output.write(confirm.getBytes());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                } else if (password.contains("|") || password.contains("`")
                                        || password.contains(";")) {
                                    String confirm = "req|regis|invalidPassword";
                                    try {
                                        output.write(confirm.getBytes());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                } else {
                                    String confirm = "req|regis|send" + ";" + userName + ";" + password + ";"
                                            + confirmedPw;
                                    try {
                                        output.write(confirm.getBytes());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            } else {
                                String confirm = "req|regis|empty";
                                try {
                                    output.write(confirm.getBytes());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }

                if (message.equals("res|regis|empty")) {
                    gui.getRegisDialog().setVisible(false);
                    gui.createNotification("Please fill in all the fields in the form");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getRegisDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|regis|invalidUserName")) {
                    gui.getRegisDialog().setVisible(false);
                    gui.createNotification("Username can't contain these elements: '`', `;`, `|`");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getRegisDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|regis|invalidPassword")) {
                    gui.getRegisDialog().setVisible(false);
                    gui.createNotification("Password can't contain these elements: '`', `;`, `|`");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getRegisDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|regis|send|longUserName")) {
                    gui.getRegisDialog().setVisible(false);
                    gui.createNotification("Username can get maximum 10 characters");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getRegisDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|regis|send|wrongPassword")) {
                    gui.getRegisDialog().setVisible(false);
                    gui.createNotification("Confirmed password didn't match");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getRegisDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|regis|send|existedUserName")) {
                    gui.getRegisDialog().setVisible(false);
                    gui.createNotification("This username is existed. Try new one");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getRegisDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|regis|send|success")) {
                    gui.getRegisDialog().setVisible(false);
                    gui.createNotification("Register new account successfully");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getMainFrame().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|login")) {
                    gui.getMainFrame().setVisible(false);
                    gui.createLoginForm();
                    gui.getLoginDialog().setVisible(true);
                    gui.getCancelButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getLoginDialog().setVisible(false);
                            gui.getMainFrame().setVisible(true);
                        }
                    });
                    gui.getConfirmButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String userName = gui.getUserName().getText();
                            String password = gui.getPassword().getText();

                            if (!userName.isEmpty() && !password.isEmpty()) {
                                if (userName.contains("|") || userName.contains("`") || userName.contains(";")) {
                                    String confirm = "req|login|invalidUserName";
                                    try {
                                        output.write(confirm.getBytes());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                } else if (password.contains("|") || password.contains("`")
                                        || password.contains(";")) {
                                    String confirm = "req|login|invalidPassword";
                                    try {
                                        output.write(confirm.getBytes());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                } else {
                                    String confirm = "req|login|send" + ";" + userName + ";" + password;
                                    try {
                                        output.write(confirm.getBytes());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            } else {
                                String confirm = "req|login|empty";
                                try {
                                    output.write(confirm.getBytes());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }

                if (message.equals("res|login|empty")) {
                    gui.getLoginDialog().setVisible(false);
                    gui.createNotification("Please fill in all the fields in the form");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getLoginDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|login|invalidUserName")) {
                    gui.getLoginDialog().setVisible(false);
                    gui.createNotification("Username can't contain these elements: '`', `;`, `|`");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getLoginDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|login|invalidPassword")) {
                    gui.getLoginDialog().setVisible(false);
                    gui.createNotification("Password can't contain these elements: '`', `;`, `|`");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getLoginDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|login|send|notExistedUserName")) {
                    gui.getLoginDialog().setVisible(false);
                    gui.createNotification("This account doesn't existed. Regis new one");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.createRegisForm();
                            gui.getRegisDialog().setVisible(true);
                            gui.getCancelButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    gui.getRegisDialog().setVisible(false);
                                    gui.getMainFrame().setVisible(true);
                                }
                            });
                            gui.getConfirmButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String userName = gui.getUserName().getText();
                                    String password = gui.getPassword().getText();
                                    String confirmedPw = gui.getConfirmedPw().getText();

                                    if (!userName.isEmpty() && !password.isEmpty() && !confirmedPw.isEmpty()) {
                                        if (userName.contains("|") || userName.contains("`")
                                                || userName.contains(";")) {
                                            String confirm = "req|regis|invalidUserName";
                                            try {
                                                output.write(confirm.getBytes());
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        } else if (password.contains("|") || password.contains("`")
                                                || password.contains(";")) {
                                            String confirm = "req|regis|invalidPassword";
                                            try {
                                                output.write(confirm.getBytes());
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        } else {
                                            String confirm = "req|regis|send" + ";" + userName + ";" + password + ";"
                                                    + confirmedPw;
                                            try {
                                                output.write(confirm.getBytes());
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    } else {
                                        String confirm = "req|regis|empty";
                                        try {
                                            output.write(confirm.getBytes());
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    });
                }

                if (message.equals("res|login|send|wrongPassword")) {
                    gui.getLoginDialog().setVisible(false);
                    gui.createNotification("Wrong password. Try again or regis new account");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getLoginDialog().setVisible(true);
                        }
                    });
                }

                if (message.contains("res|login|send|success")) {
                    String[] userInfo = message.split(";");
                    String userName = userInfo[1];
                    client.setSocketID(userName);

                    gui.getLoginDialog().setVisible(false);
                    gui.createNotification("Login to your account successfully");
                    gui.getDialog().setVisible(true);

                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String getData = "req|getData";
                            try {
                                output.write(getData.getBytes());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                            gui.getDialog().setVisible(false);

                            gui.getQuitButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String quit = "req|lobby|quit";
                                    try {
                                        output.write(quit.getBytes());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });

                            gui.getReloadButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String getData = "req|getData";
                                    try {
                                        output.write(getData.getBytes());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });

                            gui.getTextField().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String sendMessage = "req|sendMessage";
                                    if (gui.getTextField().getText().isEmpty() && client.getContact().isEmpty()) {
                                        String sendNothingtoNobody = sendMessage + ";" + "No" + ";" + "No";
                                        try {
                                            output.write(sendNothingtoNobody.getBytes());
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        gui.getTextField().setText("");
                                    }
                                    if (!gui.getTextField().getText().isEmpty() && client.getContact().isEmpty()) {
                                        String sendToNobody = sendMessage + ";" + "No" + ";" + "Yes";
                                        try {
                                            output.write(sendToNobody.getBytes());
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        gui.getTextField().setText("");
                                    }
                                    if (gui.getTextField().getText().isEmpty() && !client.getContact().isEmpty()) {
                                        String sendNothing = sendMessage + ";" + "Yes" + ";" + "No";
                                        try {
                                            output.write(sendNothing.getBytes());
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        gui.getTextField().setText("");
                                    }
                                    if (!gui.getTextField().getText().isEmpty() && !client.getContact().isEmpty()) {
                                        if (client.getContactStatus() == "username") {
                                            gui.getClearButton().setOpaque(true);
                                            gui.getClearButton().setBackground(Color.GREEN);
                                            String sendSuccess = sendMessage + ";" + client.getContact() + ";"
                                                    + gui.getTextField().getText();
                                            try {
                                                output.write(sendSuccess.getBytes());
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                            gui.getMessagePanel()
                                                    .add(gui.getSendMessagePanel(gui.getTextField().getText()));
                                            gui.getMessagePanel().invalidate();
                                            gui.getMessagePanel().validate();

                                            gui.getTextScroll().invalidate();
                                            gui.getTextScroll().validate();

                                            JScrollBar vertical = gui.getTextScroll().getVerticalScrollBar();
                                            vertical.setValue(vertical.getMaximum());

                                            gui.getTextScroll().invalidate();
                                            gui.getTextScroll().validate();

                                            gui.getTextField().setText("");
                                        } else {
                                            gui.getClearButton().setOpaque(true);
                                            gui.getClearButton().setBackground(Color.GREEN);
                                            sendMessage = "req|sendGroupMessage";
                                            String sendSuccess = sendMessage + ";" + client.getContact() + ";"
                                                    + gui.getTextField().getText();
                                            try {
                                                output.write(sendSuccess.getBytes());
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                            gui.getMessagePanel()
                                                    .add(gui.getSendMessagePanel(gui.getTextField().getText()));
                                            gui.getMessagePanel().invalidate();
                                            gui.getMessagePanel().validate();

                                            gui.getTextScroll().invalidate();
                                            gui.getTextScroll().validate();

                                            JScrollBar vertical = gui.getTextScroll().getVerticalScrollBar();
                                            vertical.setValue(vertical.getMaximum());

                                            gui.getTextScroll().invalidate();
                                            gui.getTextScroll().validate();

                                            gui.getTextField().setText("");
                                        }
                                    }
                                }
                            });
                        }
                    });
                }

                if (message.contains("res|lobby|quit")) {
                    String[] userInfo = message.split(";");
                    String userName = userInfo[1];
                    client.setSocketID(userName);

                    gui.getLobbyFrame().setVisible(false);
                    gui.createNotification("Thank you for using our Chat Application");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            System.exit(0);
                        }
                    });
                }

                if (message.contains("res|getData")) {
                    String[] rawInfo = message.split("`");

                    String[] userInfo = rawInfo[0].split(";");
                    String[] userAccount = Arrays.copyOfRange(userInfo, 1, userInfo.length);
                    if (rawInfo[1] == "NOGROUPEXISTED") {
                        gui.createGroupPanel(tempGroupList);
                    } else {
                        String[] groupInfo = rawInfo[1].split(";");
                        String[] groupAccount = Arrays.copyOfRange(groupInfo, 1, groupInfo.length);
                        gui.createGroupPanel(groupAccount);
                    }

                    gui.createUserPanel(userAccount);

                    client.getFrame().setVisible(false);
                    client.setFrame(gui.getLobbyFrame());
                    client.setFrame(gui.createChatLobby(client.getSocketID()));
                    gui.getLobbyFrame().setVisible(true);

                    gui.getQuitButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String quit = "req|lobby|quit";
                            try {
                                output.write(quit.getBytes());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });

                    gui.getReloadButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String getUserList = "req|getData";
                            try {
                                output.write(getUserList.getBytes());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });

                    gui.getGroupButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getLobbyFrame().setVisible(false);
                            gui.createNewGroupDialog(userAccount);
                            gui.getAddDialog().setVisible(true);

                            gui.getCancelButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    gui.getAddDialog().setVisible(false);
                                    gui.getLobbyFrame().setVisible(true);
                                }
                            });

                            gui.getConfirmButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!gui.getUserName().getText().isEmpty()) {
                                        if (gui.getUserName().getText().contains("`")
                                                || gui.getUserName().getText().contains(";")
                                                || gui.getUserName().getText().contains("|")) {
                                            String invalidName = "req|add|invalidName";
                                            try {
                                                output.write(invalidName.getBytes());
                                                return;
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                        if (gui.getMemberList().size() >= 2) {
                                            String getMemberList = "";
                                            for (int i = 0; i < gui.getMemberList().size(); i++) {
                                                getMemberList += ";" + gui.getMemberList().get(i);
                                            }
                                            String addGroup = "req|add|send" + "`" + gui.getUserName().getText()
                                                    + "`"
                                                    + getMemberList;
                                            try {
                                                output.write(addGroup.getBytes());
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        } else {
                                            String noMemberGroup = "req|add|emptyMember";
                                            try {
                                                output.write(noMemberGroup.getBytes());
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    } else {
                                        String noNameGroup = "req|add|emptyName";
                                        try {
                                            output.write(noNameGroup.getBytes());
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    });

                    gui.getClearButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Color color = gui.getClearButton().getBackground();
                            if (color.getRGB() == Color.GREEN.getRGB()) {
                                String clearHistory = "req|clearHistory" + ";" + client.getSocketID() + ";"
                                        + client.getContact() + ";" + client.getContactStatus();
                                try {
                                    output.write(clearHistory.getBytes());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });

                    gui.getChooserButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (!client.getContact().isEmpty()) {
                                    gui.getLobbyFrame().setVisible(false);
                                    int returnValue = gui.getFileChooser().showOpenDialog(null);
                                    File selectedFile = new File("");

                                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                                        selectedFile = gui.getFileChooser().getSelectedFile();
                                        gui.getLobbyFrame().setVisible(true);
                                    }

                                    String filePath = selectedFile.getAbsolutePath().replace("\\", "\\\\");
                                    ArrayList<String> content = new ArrayList<>();

                                    FileInputStream fin = new FileInputStream(filePath);
                                    BufferedInputStream bis = new BufferedInputStream(fin);
                                    int data = bis.read();
                                    StringBuilder line = new StringBuilder();
                                    while (data != -1) {
                                        if ((((char) data) == '\n') || ((char) data == '\r')) {
                                            if (!line.toString().isEmpty()) {
                                                content.add(line.toString());
                                            }
                                            line.delete(0, line.length());
                                            data = bis.read();
                                            continue;
                                        }
                                        line.append((char) data);
                                        data = bis.read();
                                    }
                                    bis.close();

                                    String getContent = "";
                                    for (int i = 0; i < content.size(); i++) {
                                        getContent += ";" + content.get(i);
                                    }

                                    gui.getMessagePanel()
                                            .add(gui.getSendFile(selectedFile.getName(), getContent));
                                    gui.getMessagePanel().invalidate();
                                    gui.getMessagePanel().validate();

                                    gui.getTextScroll().invalidate();
                                    gui.getTextScroll().validate();

                                    JScrollBar vertical = gui.getTextScroll().getVerticalScrollBar();
                                    vertical.setValue(vertical.getMaximum());

                                    gui.getTextScroll().invalidate();
                                    gui.getTextScroll().validate();

                                    gui.getTextField().setText("");

                                    if (client.getContactStatus() == "username") {
                                        String sendFile = "req|sendFile|send" + "`" + client.getContact() + "`"
                                                + selectedFile.getName() + "`" + getContent;
                                        output.write(sendFile.getBytes());
                                    } else {
                                        String sendFile = "req|sendGroupFile|send" + "`" + client.getContact() + "`"
                                                + selectedFile.getName() + "`" + getContent;
                                        output.write(sendFile.getBytes());
                                    }
                                } else {
                                    output.write("req|sendFile|empty".getBytes());
                                }

                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                }

                if (message.equals("res|sendMessage|sendNothingToNobody")) {
                    gui.getLobbyFrame().setVisible(false);
                    gui.createNotification("Choose 1 person and write them something");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getLobbyFrame().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|sendMessage|sendToNobody")) {
                    gui.getLobbyFrame().setVisible(false);
                    gui.createNotification("Choose someone to send messages");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getLobbyFrame().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|sendMessage|sendNothing")) {
                    gui.getLobbyFrame().setVisible(false);
                    gui.createNotification("Write some messages to send");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getLobbyFrame().setVisible(true);
                        }
                    });
                }

                if (message.contains("res|recieveMessage")) {
                    String[] messageInfo = message.split(";");
                    if (messageInfo[2].equals(client.getContact())) {
                        gui.getClearButton().setOpaque(true);
                        gui.getClearButton().setBackground(Color.GREEN);

                        gui.getMessagePanel()
                                .add(gui.getReceiveMesagePanel(messageInfo[1]));
                        gui.getMessagePanel().invalidate();
                        gui.getMessagePanel().validate();

                        gui.getTextScroll().invalidate();
                        gui.getTextScroll().validate();

                        JScrollBar vertical = gui.getTextScroll().getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum());

                        gui.getTextScroll().invalidate();
                        gui.getTextScroll().validate();
                    }
                }

                if (message.contains("res|sendUserHistory")) {
                    gui.getClearButton().setOpaque(true);
                    gui.getClearButton().setBackground(Color.GREEN);
                    String[] messageInfo = message.split("`");
                    String[] historyMessage = Arrays.copyOfRange(messageInfo, 1, messageInfo.length);
                    for (int i = 0; i < historyMessage.length; i++) {
                        String[] detailMessage = historyMessage[i].split(";");
                        if (detailMessage[0].equals(client.getSocketID())) {
                            gui.getMessagePanel()
                                    .add(gui.getSendMessagePanel(detailMessage[1]));
                            gui.getMessagePanel().invalidate();
                            gui.getMessagePanel().validate();

                            gui.getTextScroll().invalidate();
                            gui.getTextScroll().validate();
                        } else {
                            gui.getMessagePanel()
                                    .add(gui.getReceiveMesagePanel(detailMessage[1]));
                            gui.getMessagePanel().invalidate();
                            gui.getMessagePanel().validate();

                            gui.getTextScroll().invalidate();
                            gui.getTextScroll().validate();
                        }
                    }
                    JScrollBar vertical = gui.getTextScroll().getVerticalScrollBar();
                    vertical.setValue(vertical.getMaximum());

                    gui.getTextScroll().invalidate();
                    gui.getTextScroll().validate();
                }

                if (message.equals("res|add|emptyName")) {
                    gui.getAddDialog().setVisible(false);
                    gui.createNotification("Please set a name for your group");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getAddDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|add|emptyMember")) {
                    gui.getAddDialog().setVisible(false);
                    gui.createNotification("Add at least 2 users to your group");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getAddDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|add|invalidName")) {
                    gui.getAddDialog().setVisible(false);
                    gui.createNotification("Group name can't contain these elements: '`', `;`, `|`");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getAddDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|add|send|tooLongGroupName")) {
                    gui.getAddDialog().setVisible(false);
                    gui.createNotification("Group name can get maximum 10 characters");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getAddDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|add|send|existedGroup")) {
                    gui.getAddDialog().setVisible(false);
                    gui.createNotification("This group name is existed. Try new one");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getAddDialog().setVisible(true);
                        }
                    });
                }

                if (message.equals("res|add|send|success")) {
                    gui.getAddDialog().setVisible(false);
                    gui.createNotification("Adding new group successfully");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getLobbyFrame().setVisible(true);
                        }
                    });
                }

                if (message.contains("res|sendGroupHistory")) {
                    gui.getClearButton().setOpaque(true);
                    gui.getClearButton().setBackground(Color.GREEN);
                    String[] messageInfo = message.split("`");
                    String[] historyMessage = Arrays.copyOfRange(messageInfo, 1, messageInfo.length);
                    for (int i = 0; i < historyMessage.length; i++) {
                        String[] detailMessage = historyMessage[i].split(";");
                        if (detailMessage[0].equals(client.getSocketID())) {
                            gui.getMessagePanel()
                                    .add(gui.getSendMessagePanel(detailMessage[1]));
                            gui.getMessagePanel().invalidate();
                            gui.getMessagePanel().validate();

                            gui.getTextScroll().invalidate();
                            gui.getTextScroll().validate();
                        } else {
                            gui.getMessagePanel()
                                    .add(gui.getReceiveMesagePanel(detailMessage[0] + ": " + detailMessage[1]));
                            gui.getMessagePanel().invalidate();
                            gui.getMessagePanel().validate();

                            gui.getTextScroll().invalidate();
                            gui.getTextScroll().validate();
                        }
                    }
                    JScrollBar vertical = gui.getTextScroll().getVerticalScrollBar();
                    vertical.setValue(vertical.getMaximum());

                    gui.getTextScroll().invalidate();
                    gui.getTextScroll().validate();
                }

                if (message.equals("res|sendEmptyHistory")) {
                    gui.getClearButton().setOpaque(true);
                    gui.getClearButton().setBackground(Color.GRAY);
                }

                if (message.equals("res|clearHistory")) {
                    gui.getClearButton().setOpaque(true);
                    gui.getClearButton().setBackground(Color.GRAY);

                    gui.getMessagePanel().removeAll();
                    gui.getMessagePanel().invalidate();
                    gui.getMessagePanel().validate();

                    gui.getTextScroll().invalidate();
                    gui.getTextScroll().validate();
                }

                if (message.equals("res|sendFile|empty")) {
                    gui.getLobbyFrame().setVisible(false);
                    gui.createNotification("Choose 1 person or a group to send them file");
                    gui.getDialog().setVisible(true);
                    gui.getCloseButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gui.getDialog().setVisible(false);
                            gui.getLobbyFrame().setVisible(true);
                        }
                    });
                }

                if (message.contains("res|sendFile|success")) {
                    String[] messageInfo = message.split("`");
                    if (messageInfo[1].equals(client.getContact())) {
                        gui.getMessagePanel()
                                .add(gui.getReceiveFile(messageInfo[2], messageInfo[3]));
                        gui.getMessagePanel().invalidate();
                        gui.getMessagePanel().validate();

                        gui.getTextScroll().invalidate();
                        gui.getTextScroll().validate();

                        JScrollBar vertical = gui.getTextScroll().getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum());

                        gui.getTextScroll().invalidate();
                        gui.getTextScroll().validate();
                    }
                }

                if (message.contains("res|sendGroupFile|success")) {
                    String[] messageInfo = message.split("`");
                    if (messageInfo[1].equals(client.getContact())) {
                        gui.getMessagePanel()
                                .add(gui.getReceiveGroupFile(messageInfo[2], messageInfo[3]));
                        gui.getMessagePanel().invalidate();
                        gui.getMessagePanel().validate();

                        gui.getTextScroll().invalidate();
                        gui.getTextScroll().validate();

                        JScrollBar vertical = gui.getTextScroll().getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum());

                        gui.getTextScroll().invalidate();
                        gui.getTextScroll().validate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
