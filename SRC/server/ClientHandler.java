package server;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class ClientHandler implements Runnable {
    private Socket socket;
    private String socketID;
    private Server server;
    private InputStream input;
    private OutputStream output;

    public ClientHandler(Socket socket, String ID, Server server) {
        this.socket = socket;
        this.socketID = ID;
        this.server = server;
        try {
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            boolean alive = true;

            String rawSocketID = this.socketID;

            do {
                while ((bytesRead = input.read(buffer)) != -1) {
                    String message = new String(buffer, 0, bytesRead);

                    if (message.equals("req|quit")) {
                        server.sendSystemMessage("res|quit", this.socketID);
                        alive = false;
                        break;
                    }

                    if (message.equals("req|regis")) {
                        server.sendSystemMessage("res|regis", this.socketID);
                    }

                    if (message.equals("req|regis|empty")) {
                        server.sendSystemMessage("res|regis|empty", this.socketID);
                    }

                    if (message.contains("req|regis|send")) {
                        String[] userInfo = message.split(";");
                        String userName = userInfo[1];
                        String password = userInfo[2];
                        String confirmedPw = userInfo[3];

                        if (userName.length() >= 10) {
                            server.sendSystemMessage("res|regis|send|longUserName", this.socketID);
                            break;
                        }

                        if (!password.equals(confirmedPw)) {
                            server.sendSystemMessage("res|regis|send|wrongPassword", this.socketID);
                            break;
                        }

                        boolean isExisted = false;
                        FileInputStream fin = new FileInputStream("account.txt");
                        BufferedInputStream bis = new BufferedInputStream(fin);
                        int data = bis.read();
                        StringBuilder line = new StringBuilder();
                        while (data != -1) {
                            if ((((char) data) == '\n') || ((char) data == '\r')) {
                                String[] accountInfo = line.toString().split("\\|");
                                if (userName.equals(accountInfo[0])) {
                                    isExisted = true;
                                    break;
                                } else {
                                    line.delete(0, line.length());
                                    data = bis.read();
                                    continue;
                                }
                            }
                            line.append((char) data);
                            data = bis.read();
                        }
                        bis.close();

                        if (!isExisted) {
                            FileWriter writer = new FileWriter("account.txt", true);
                            BufferedWriter bufferWriter = new BufferedWriter(writer);
                            bufferWriter.write(userName + "|" + password);
                            bufferWriter.newLine();
                            bufferWriter.close();
                            server.sendSystemMessage("res|regis|send|success", this.socketID);
                        } else {
                            server.sendSystemMessage("res|regis|send|existedUserName", this.socketID);
                        }
                    }

                    if (message.equals("req|regis|invalidUserName")) {
                        server.sendSystemMessage("res|regis|invalidUserName", this.socketID);
                    }

                    if (message.equals("req|regis|invalidPassword")) {
                        server.sendSystemMessage("res|regis|invalidPassword", this.socketID);
                    }

                    if (message.equals("req|login")) {
                        server.sendSystemMessage("res|login", this.socketID);
                    }

                    if (message.equals("req|login|empty")) {
                        server.sendSystemMessage("res|login|empty", this.socketID);
                    }

                    if (message.equals("req|login|invalidUserName")) {
                        server.sendSystemMessage("res|login|invalidUserName", this.socketID);
                    }

                    if (message.equals("req|login|invalidPassword")) {
                        server.sendSystemMessage("res|login|invalidPassword", this.socketID);
                    }

                    if (message.contains("req|login|send")) {
                        String[] userInfo = message.split(";");
                        String userName = userInfo[1];
                        String password = userInfo[2];

                        boolean isExisted = false;
                        boolean isCorrect = false;
                        FileInputStream fin = new FileInputStream("account.txt");
                        BufferedInputStream bis = new BufferedInputStream(fin);
                        int data = bis.read();
                        StringBuilder line = new StringBuilder();
                        while (data != -1) {
                            if ((((char) data) == '\n') || ((char) data == '\r')) {
                                String[] accountInfo = line.toString().split("\\|");
                                if (userName.equals(accountInfo[0])) {
                                    isExisted = true;
                                    if (password.equals(accountInfo[1])) {
                                        this.socketID = accountInfo[0];
                                        isCorrect = true;
                                        break;
                                    } else {
                                        break;
                                    }
                                } else {
                                    line.delete(0, line.length());
                                    data = bis.read();
                                    continue;
                                }
                            }
                            line.append((char) data);
                            data = bis.read();
                        }
                        bis.close();

                        if (!isExisted) {
                            server.sendSystemMessage("res|login|send|notExistedUserName", this.socketID);
                        }

                        if (isExisted && !isCorrect) {
                            server.sendSystemMessage("res|login|send|wrongPassword", this.socketID);
                        }

                        if (isExisted && isCorrect) {
                            server.sendSystemMessage("res|login|send|success" + ";" + this.socketID, this.socketID);
                        }
                    }

                    if (message.equals("req|lobby|quit")) {
                        this.socketID = rawSocketID;
                        server.sendSystemMessage("res|lobby|quit" + ";" + rawSocketID, this.socketID);
                        alive = false;
                        break;
                    }

                    if (message.equals("req|getData")) {
                        FileInputStream userFin = new FileInputStream("account.txt");
                        BufferedInputStream userBis = new BufferedInputStream(userFin);
                        int userData = userBis.read();
                        StringBuilder userLine = new StringBuilder();
                        ArrayList<String> userList = new ArrayList<String>();
                        while (userData != -1) {
                            if ((((char) userData) == '\n') || ((char) userData == '\r')) {
                                String[] accountInfo = userLine.toString().split("\\|");
                                if (!accountInfo[0].isEmpty()) {
                                    userList.add(accountInfo[0]);
                                }
                                userLine.delete(0, userLine.length());
                                userData = userBis.read();
                                continue;
                            }
                            userLine.append((char) userData);
                            userData = userBis.read();
                        }
                        userBis.close();

                        Collections.sort(userList);
                        String getUserList = "";
                        for (int i = 0; i < userList.size(); i++) {
                            if (!this.getClientID().equals(userList.get(i))) {
                                getUserList += ";" + userList.get(i);
                            }
                        }

                        FileInputStream groupFin = new FileInputStream("group.txt");
                        BufferedInputStream groupBis = new BufferedInputStream(groupFin);
                        int groupData = groupBis.read();
                        StringBuilder groupLine = new StringBuilder();
                        ArrayList<String> groupList = new ArrayList<String>();
                        while (groupData != -1) {
                            if ((((char) groupData) == '\n') || ((char) groupData == '\r')) {
                                String[] groupInfo = groupLine.toString().split("\\|");
                                if (!groupInfo[0].isEmpty()) {
                                    String[] memberList = groupInfo[1].split(";");
                                    boolean isExisted = false;
                                    for (int i = 0; i < memberList.length; i++) {
                                        if (memberList[i].equals(this.socketID)) {
                                            isExisted = true;
                                            break;
                                        }
                                    }

                                    if (isExisted) {
                                        groupList.add(groupInfo[0]);
                                    }
                                }
                                groupLine.delete(0, groupLine.length());
                                groupData = groupBis.read();
                                continue;
                            }
                            groupLine.append((char) groupData);
                            groupData = groupBis.read();
                        }
                        groupBis.close();

                        String getGroupList = "";
                        if (groupList.size() != 0) {
                            Collections.sort(groupList);
                            for (int i = 0; i < groupList.size(); i++) {
                                if (!this.getClientID().equals(groupList.get(i))) {
                                    getGroupList += ";" + groupList.get(i);
                                }
                            }
                        } else {
                            getGroupList = "NOGROUPEXISTED";
                        }

                        server.sendSystemMessage("res|getData" + getUserList + "`" + getGroupList, this.socketID);
                    }

                    if (message.contains("req|sendMessage")) {
                        String[] messageInfo = message.split(";");
                        if (messageInfo[1].equals("No") && messageInfo[2].equals("No")) {
                            server.sendSystemMessage("res|sendMessage|sendNothingToNobody", this.socketID);
                        } else if (messageInfo[1].equals("No") && messageInfo[2].equals("Yes")) {
                            server.sendSystemMessage("res|sendMessage|sendToNobody", this.socketID);
                        } else if (messageInfo[1].equals("Yes") && messageInfo[2].equals("No")) {
                            server.sendSystemMessage("res|sendMessage|sendNothing", this.socketID);
                        } else {
                            String receive = "res|recieveMessage" + ";" + messageInfo[2] + ";" + this.socketID;
                            server.sendSystemMessage(receive, messageInfo[1]);

                            String history1 = this.socketID + ";" + messageInfo[1];
                            String history2 = messageInfo[1] + ";" + this.socketID;
                            String fileName = "";

                            FileInputStream fin = new FileInputStream("userHistory.txt");
                            BufferedInputStream bis = new BufferedInputStream(fin);
                            int data = bis.read();
                            StringBuilder line = new StringBuilder();
                            boolean isExisted = false;
                            while (data != -1) {
                                if ((((char) data) == '\n') || ((char) data == '\r')) {
                                    if (line.toString().equals(history1) || line.toString().equals(history2)) {
                                        fileName = line.toString();
                                        isExisted = true;
                                        break;
                                    }
                                    line.delete(0, line.length());
                                    data = bis.read();
                                    continue;
                                }
                                line.append((char) data);
                                data = bis.read();
                            }
                            bis.close();

                            if (!isExisted) {
                                File f = new File(this.socketID + ";" + messageInfo[1] + ".txt");
                                f.createNewFile();
                                FileWriter writer = new FileWriter("userHistory.txt", true);
                                BufferedWriter bufferWriter = new BufferedWriter(writer);
                                bufferWriter.write(f.getName().split(".txt")[0]);
                                bufferWriter.newLine();
                                bufferWriter.close();

                                writer = new FileWriter(f.getName(), true);
                                bufferWriter = new BufferedWriter(writer);
                                bufferWriter.write(this.socketID + ";" + messageInfo[2]);
                                bufferWriter.newLine();
                                bufferWriter.close();
                            } else {
                                FileWriter writer = new FileWriter(fileName + ".txt", true);
                                BufferedWriter bufferWriter = new BufferedWriter(writer);
                                bufferWriter.write(this.socketID + ";" + messageInfo[2]);
                                bufferWriter.newLine();
                                bufferWriter.close();
                            }
                        }
                    }

                    if (message.contains("req|loadUserHistory")) {
                        String[] messageInfo = message.split(";");
                        String userName = messageInfo[1];
                        String contact = messageInfo[2];

                        String history1 = userName + ";" + contact;
                        String history2 = contact + ";" + userName;
                        String fileName = "";

                        FileInputStream fin = new FileInputStream("userHistory.txt");
                        BufferedInputStream bis = new BufferedInputStream(fin);
                        int data = bis.read();
                        StringBuilder line = new StringBuilder();
                        boolean isExisted = false;
                        while (data != -1) {
                            if ((((char) data) == '\n') || ((char) data == '\r')) {
                                if (line.toString().equals(history1) || line.toString().equals(history2)) {
                                    fileName = line.toString();
                                    isExisted = true;
                                    break;
                                }
                                line.delete(0, line.length());
                                data = bis.read();
                                continue;
                            }
                            line.append((char) data);
                            data = bis.read();
                        }
                        bis.close();

                        if (!isExisted) {
                            server.sendSystemMessage("res|sendEmptyHistory", this.socketID);
                        } else {
                            FileInputStream sendFin = new FileInputStream(fileName + ".txt");
                            BufferedInputStream sendBis = new BufferedInputStream(sendFin);
                            int sendData = sendBis.read();
                            StringBuilder sendLine = new StringBuilder();
                            ArrayList<String> historyList = new ArrayList<>();
                            while (sendData != -1) {
                                if ((((char) sendData) == '\n') || ((char) sendData == '\r')) {
                                    if (!sendLine.toString().isEmpty()) {
                                        historyList.add(sendLine.toString());
                                    }
                                    sendLine.delete(0, sendLine.length());
                                    sendData = sendBis.read();
                                    continue;
                                }
                                sendLine.append((char) sendData);
                                sendData = sendBis.read();
                            }
                            sendBis.close();

                            String getHistoryList = "";
                            for (int i = 0; i < historyList.size(); i++) {
                                getHistoryList += "`" + historyList.get(i);
                            }
                            String sendUserHistory = "res|sendUserHistory" + getHistoryList;
                            server.sendSystemMessage(sendUserHistory, this.socketID);
                        }
                    }

                    if (message.equals("req|add|emptyName")) {
                        server.sendSystemMessage("res|add|emptyName", this.socketID);
                    }

                    if (message.equals("req|add|emptyMember")) {
                        server.sendSystemMessage("res|add|emptyMember", this.socketID);
                    }

                    if (message.equals("req|add|invalidName")) {
                        server.sendSystemMessage("res|add|invalidName", this.socketID);
                    }

                    if (message.contains("req|add|send")) {
                        String[] messageInfo = message.split("`");
                        String groupName = messageInfo[1];
                        String[] memberList = messageInfo[2].split(";");

                        if (groupName.length() >= 10) {
                            server.sendSystemMessage("res|add|send|tooLongGroupName", this.socketID);
                            break;
                        }

                        FileInputStream groupFin = new FileInputStream("group.txt");
                        BufferedInputStream groupBis = new BufferedInputStream(groupFin);
                        int groupData = groupBis.read();
                        StringBuilder groupLine = new StringBuilder();
                        boolean isExisted = false;
                        while (groupData != -1) {
                            if ((((char) groupData) == '\n') || ((char) groupData == '\r')) {
                                String[] groupInfo = groupLine.toString().split("\\|");
                                if (groupName.equals(groupInfo[0])) {
                                    isExisted = true;
                                    break;
                                }
                                groupLine.delete(0, groupLine.length());
                                groupData = groupBis.read();
                                continue;
                            }
                            groupLine.append((char) groupData);
                            groupData = groupBis.read();
                        }
                        groupBis.close();

                        if (isExisted) {
                            server.sendSystemMessage("res|add|send|existedGroup", this.socketID);
                            break;
                        } else {
                            FileWriter writer = new FileWriter("group.txt", true);
                            BufferedWriter bufferWriter = new BufferedWriter(writer);
                            bufferWriter.write(groupName + "|");
                            for (int i = 1; i < memberList.length - 1; i++) {
                                bufferWriter.write(memberList[i] + ";");
                            }
                            bufferWriter.write(memberList[memberList.length - 1]);
                            bufferWriter.newLine();
                            bufferWriter.close();
                        }

                        server.sendSystemMessage("res|add|send|success", this.socketID);
                    }

                    if (message.contains("req|loadGroupHistory")) {
                        String[] messageInfo = message.split(";");
                        String groupName = messageInfo[1];

                        FileInputStream groupFin = new FileInputStream("groupHistory.txt");
                        BufferedInputStream groupBis = new BufferedInputStream(groupFin);
                        int groupData = groupBis.read();
                        StringBuilder groupLine = new StringBuilder();
                        boolean isExisted = false;
                        while (groupData != -1) {
                            if ((((char) groupData) == '\n') || ((char) groupData == '\r')) {
                                if (groupName.equals(groupLine.toString())) {
                                    isExisted = true;
                                    break;
                                }
                                groupLine.delete(0, groupLine.length());
                                groupData = groupBis.read();
                                continue;
                            }
                            groupLine.append((char) groupData);
                            groupData = groupBis.read();
                        }
                        groupBis.close();

                        if (!isExisted) {
                            server.sendSystemMessage("res|sendEmptyHistory", this.socketID);
                        } else {
                            FileInputStream sendFin = new FileInputStream(groupName + ".txt");
                            BufferedInputStream sendBis = new BufferedInputStream(sendFin);
                            int sendData = sendBis.read();
                            StringBuilder sendLine = new StringBuilder();
                            ArrayList<String> historyList = new ArrayList<>();
                            while (sendData != -1) {
                                if ((((char) sendData) == '\n') || ((char) sendData == '\r')) {
                                    if (!sendLine.toString().isEmpty()) {
                                        historyList.add(sendLine.toString());
                                    }
                                    sendLine.delete(0, sendLine.length());
                                    sendData = sendBis.read();
                                    continue;
                                }
                                sendLine.append((char) sendData);
                                sendData = sendBis.read();
                            }
                            sendBis.close();

                            String getHistoryList = "";
                            for (int i = 0; i < historyList.size(); i++) {
                                getHistoryList += "`" + historyList.get(i);
                            }
                            String sendGroupHistory = "res|sendGroupHistory" + getHistoryList;
                            server.sendSystemMessage(sendGroupHistory, this.socketID);
                        }
                    }

                    if (message.contains("req|sendGroupMessage")) {
                        String[] messageInfo = message.split(";");

                        FileInputStream fin = new FileInputStream("group.txt");
                        BufferedInputStream bis = new BufferedInputStream(fin);
                        int data = bis.read();
                        StringBuilder line = new StringBuilder();
                        String user = "";
                        while (data != -1) {
                            if ((((char) data) == '\n') || ((char) data == '\r')) {
                                String[] groupInfo = line.toString().split("\\|");
                                if (messageInfo[1].equals(groupInfo[0])) {
                                    user = groupInfo[1];
                                    break;
                                }
                                line.delete(0, line.length());
                                data = bis.read();
                                continue;
                            }
                            line.append((char) data);
                            data = bis.read();
                        }
                        bis.close();

                        String[] memberList = user.split(";");
                        for (int i = 0; i < memberList.length; i++) {
                            if (!memberList[i].equals(this.socketID)) {
                                String receive = "res|recieveMessage" + ";" + this.socketID + ": " + messageInfo[2]
                                        + ";"
                                        + messageInfo[1];
                                server.sendSystemMessage(receive, memberList[i]);
                            }
                        }

                        FileInputStream sendFin = new FileInputStream("groupHistory.txt");
                        BufferedInputStream sendBis = new BufferedInputStream(sendFin);
                        int sendData = sendBis.read();
                        StringBuilder sendLine = new StringBuilder();
                        boolean isExisted = false;
                        while (sendData != -1) {
                            if ((((char) sendData) == '\n') || ((char) sendData == '\r')) {
                                if (messageInfo[1].equals(sendLine.toString())) {
                                    isExisted = true;
                                    break;
                                }
                                sendLine.delete(0, sendLine.length());
                                sendData = sendBis.read();
                                continue;
                            }
                            sendLine.append((char) sendData);
                            sendData = sendBis.read();
                        }
                        sendBis.close();

                        if (!isExisted) {
                            File f = new File(messageInfo[1] + ".txt");
                            f.createNewFile();

                            FileWriter writer = new FileWriter("groupHistory.txt", true);
                            BufferedWriter bufferWriter = new BufferedWriter(writer);
                            bufferWriter.write(messageInfo[1]);
                            bufferWriter.newLine();
                            bufferWriter.close();

                            writer = new FileWriter(f.getName(), true);
                            bufferWriter = new BufferedWriter(writer);
                            bufferWriter.write(this.socketID + ";" + messageInfo[2]);
                            bufferWriter.newLine();
                            bufferWriter.close();
                        } else {
                            FileWriter writer = new FileWriter(messageInfo[1] + ".txt", true);
                            BufferedWriter bufferWriter = new BufferedWriter(writer);
                            bufferWriter.write(this.socketID + ";" + messageInfo[2]);
                            bufferWriter.newLine();
                            bufferWriter.close();
                        }
                    }

                    if (message.contains("req|clearHistory")) {
                        String[] messageInfo = message.split(";");
                        String userName = messageInfo[1];
                        String contact = messageInfo[2];
                        String contactStatus = messageInfo[3];

                        String history1 = userName + ";" + contact;
                        String history2 = contact + ";" + userName;
                        String fileName = "";

                        if (contactStatus.equals("username")) {
                            File newFile = new File("temp.txt");
                            newFile.createNewFile();

                            FileWriter writer = new FileWriter("temp.txt", true);
                            BufferedWriter bufferWriter = new BufferedWriter(writer);

                            FileInputStream fin = new FileInputStream("userHistory.txt");
                            BufferedInputStream bis = new BufferedInputStream(fin);
                            int data = bis.read();
                            StringBuilder line = new StringBuilder();
                            while (data != -1) {
                                if ((((char) data) == '\n') || ((char) data == '\r')) {
                                    if (line.toString().equals(history1) || line.toString().equals(history2)) {
                                        fileName = line.toString();
                                    } else {
                                        if (!line.toString().isEmpty()) {
                                            bufferWriter.write(line.toString());
                                            bufferWriter.newLine();
                                        }
                                    }
                                    line.delete(0, line.length());
                                    data = bis.read();
                                    continue;
                                }
                                line.append((char) data);
                                data = bis.read();
                            }
                            bis.close();
                            bufferWriter.close();

                            Files.deleteIfExists(Paths.get("userHistory.txt"));

                            File newFileName = new File("userHistory.txt");
                            newFile.renameTo(newFileName);

                            Files.deleteIfExists(Paths.get(fileName + ".txt"));

                            server.sendSystemMessage("res|clearHistory", this.socketID);
                            server.sendSystemMessage("res|clearHistory", contact);
                        }

                        if (contactStatus.equals("group")) {
                            File newFile = new File("temp.txt");
                            newFile.createNewFile();

                            FileWriter writer = new FileWriter("temp.txt", true);
                            BufferedWriter bufferWriter = new BufferedWriter(writer);

                            FileInputStream fin = new FileInputStream("groupHistory.txt");
                            BufferedInputStream bis = new BufferedInputStream(fin);
                            int data = bis.read();
                            StringBuilder line = new StringBuilder();
                            String groupName = "";
                            while (data != -1) {
                                if ((((char) data) == '\n') || ((char) data == '\r')) {
                                    if (!contact.equals(line.toString()) && !line.toString().isEmpty()) {
                                        bufferWriter.write(line.toString());
                                        bufferWriter.newLine();
                                    } else {
                                        if (!line.toString().isEmpty()) {
                                            groupName = line.toString();
                                        }
                                    }
                                    line.delete(0, line.length());
                                    data = bis.read();
                                    continue;
                                }
                                line.append((char) data);
                                data = bis.read();
                            }
                            bis.close();
                            bufferWriter.close();

                            Files.deleteIfExists(Paths.get("groupHistory.txt"));

                            File newFileName = new File("groupHistory.txt");
                            newFile.renameTo(newFileName);

                            Files.deleteIfExists(Paths.get(contact + ".txt"));

                            FileInputStream groupFin = new FileInputStream("group.txt");
                            BufferedInputStream groupBis = new BufferedInputStream(groupFin);
                            int groupData = groupBis.read();
                            StringBuilder groupLine = new StringBuilder();
                            String groupInfo = "";

                            while (groupData != -1) {
                                if ((((char) groupData) == '\n') || ((char) groupData == '\r')) {
                                    if (groupName.equals(groupLine.toString().split("\\|")[0])) {
                                        groupInfo = groupLine.toString();
                                        break;
                                    }

                                    groupLine.delete(0, groupLine.length());
                                    groupData = groupBis.read();
                                    continue;
                                }
                                groupLine.append((char) groupData);
                                groupData = groupBis.read();
                            }
                            groupBis.close();

                            String[] groupDetail = groupInfo.split("\\|");
                            String[] memberList = groupDetail[1].split(";");
                            for (int i = 0; i < memberList.length; i++) {
                                server.sendSystemMessage("res|clearHistory", memberList[i]);
                            }
                        }
                    }

                    if (message.contains("req|sendFile|send")) {
                        String[] messageInfo = message.split("`");
                        server.sendSystemMessage(
                                "res|sendFile|success" + "`" + this.socketID + "`" + messageInfo[2] + "`"
                                        + messageInfo[3],
                                messageInfo[1]);
                    }

                    if (message.contains("req|sendGroupFile|send")) {
                        String[] messageInfo = message.split("`");

                        FileInputStream groupFin = new FileInputStream("group.txt");
                        BufferedInputStream groupBis = new BufferedInputStream(groupFin);
                        int groupData = groupBis.read();
                        StringBuilder groupLine = new StringBuilder();
                        String groupInfo = "";

                        while (groupData != -1) {
                            if ((((char) groupData) == '\n') || ((char) groupData == '\r')) {
                                if (messageInfo[1].equals(groupLine.toString().split("\\|")[0])) {
                                    groupInfo = groupLine.toString();
                                    break;
                                }

                                groupLine.delete(0, groupLine.length());
                                groupData = groupBis.read();
                                continue;
                            }
                            groupLine.append((char) groupData);
                            groupData = groupBis.read();
                        }
                        groupBis.close();

                        String[] groupDetail = groupInfo.split("\\|");
                        String[] memberList = groupDetail[1].split(";");
                        for (int i = 0; i < memberList.length; i++) {
                            if (!memberList[i].equals(this.socketID)) {
                                server.sendSystemMessage("res|sendGroupFile|success" + "`" + messageInfo[1] + "`"
                                        + this.socketID + ":" + messageInfo[2] + "`" + messageInfo[3], memberList[i]);
                            }
                        }
                    }

                    if (message.equals("req|sendFile|empty")) {
                        server.sendSystemMessage("res|sendFile|empty", this.socketID);
                    }
                }
            } while (alive);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            output.write(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getClientID() {
        return this.socketID;
    }
}
