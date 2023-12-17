import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private final JPanel panel = new JPanel(new GridLayout(1, 2));
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JTextArea serverLog = new JTextArea();
    private boolean isServerWorking;
    private String fileLogs = "logs.txt";
    private List<ClientGUI> clientList = new ArrayList<>();

    public ServerWindow(){
        isServerWorking = false;
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isServerWorking){
                    serverLog.append("Сервер уже остановлен\n");
                } else {
                    isServerWorking = false;

                    serverLog.append("Сервер остановлен\n");
                }
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isServerWorking) {
                    serverLog.append("Сервер уже запущен\n");
                } else {
                    isServerWorking = true;
                    serverLog.append("Сервер запущен\n");
                }
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat Server");
        setLocationRelativeTo(null);

        add(serverLog);

        panel.add(btnStart);
        panel.add(btnStop);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void saveLog(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileLogs, true))){
            writer.write(String.format("%s\n", text));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    String readLog() {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(fileLogs);){
            int c;
            while ((c = reader.read()) != -1){
                stringBuilder.append((char) c);
            }
            stringBuilder.append("\n");
            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public String getLogs(){
        return readLog();
    }

    public void message(String text){
        if(!isServerWorking){
            return;
        }
        text += "";
        serverLog.append(String.format("%s\n", text));
        for (ClientGUI client : clientList){
            client.answer(text);
        }
        saveLog(text);
    }

    public boolean connectUser(ClientGUI clientGUI){
        if (!isServerWorking){
            return false;
        }
        clientList.add(clientGUI);
        return true;
    }
}
