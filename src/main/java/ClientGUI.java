import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private ServerWindow serverWindow;

    private boolean isConnected;
    private String clientName;

    JTextArea clientLog = new JTextArea();
    JTextField txtFieldIPAddress = new JTextField("Enter IP");
    JTextField txtFieldPort = new JTextField("Enter port");
    JTextField txtFieldLogin = new JTextField("Enter login");
    JTextField txtFieldMessage = new JTextField();
    JPasswordField txtFieldPassword = new JPasswordField("Enter password");
    JButton btnLogin = new JButton("Login");
    JButton btnSend = new JButton("Send");
    JPanel headerPanel = new JPanel(new GridLayout(2, 3));


    public ClientGUI(ServerWindow serverWindow, int position) {
        this.serverWindow = serverWindow;

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat client");
        setLocation(serverWindow.getX() - position, serverWindow.getY());

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        txtFieldLogin.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtFieldLogin.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(txtFieldLogin.getText().equals("")) {
                    txtFieldLogin.setText("Enter Login");
                }
            }
        });
        txtFieldIPAddress.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtFieldIPAddress.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(txtFieldIPAddress.getText().equals("")) {
                    txtFieldIPAddress.setText("Enter IP");
                }
            }
        });
        txtFieldPort.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtFieldPort.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(txtFieldPort.getText().equals("")) {
                    txtFieldPort.setText("Enter port");
                }
            }
        });
        txtFieldPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtFieldPassword.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(txtFieldPassword.getText().equals("")) {
                    txtFieldPassword.setText("Enter password");
                }
            }
        });


        headerPanel.add(txtFieldIPAddress);
        headerPanel.add(txtFieldPort);
        headerPanel.add(new JPanel());
        headerPanel.add(txtFieldLogin);
        headerPanel.add(txtFieldPassword);
        headerPanel.add(btnLogin);
        add(headerPanel, BorderLayout.NORTH);

        clientLog.setEditable(false);
        JScrollPane pane = new JScrollPane(clientLog);
        add(pane);

        JPanel footerPanel = new JPanel(new BorderLayout());

        txtFieldMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    if (isConnected) {
                        String text = txtFieldMessage.getText();
                        serverWindow.message(clientName + ": " + text);
                        txtFieldMessage.setText("");
                    } else {
                        clientLog.append("Нет подключения к серверу");
                    }
                }
            }
        });
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isConnected) {
                    String text = txtFieldMessage.getText();
                    serverWindow.message(String.format("%s: %s",clientName, text));
                    txtFieldMessage.setText("");
                } else {
                    clientLog.append("Нет подключения к серверу");
                }
            }
        });

        footerPanel.add(txtFieldMessage);
        footerPanel.add(btnSend, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void answer(String text) {
        clientLog.append(String.format("%s\n", text));
    }

    private void connectToServer() {
        if (serverWindow.connectUser(this)) {
            clientLog.append("Успешное подключение!\n");
            headerPanel.setVisible(false);
            isConnected = true;
            //txtFieldLogin.setText("");
            //txtFieldLogin.setToolTipText("Enter your login name");
            clientName = txtFieldLogin.getText();
            String thisLog = serverWindow.getLogs();
            if (thisLog != null) {
                clientLog.append(thisLog);
            }
        } else {
            clientLog.append("Подключение не удалось");
        }
    }
}
