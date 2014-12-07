import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChatClient extends JFrame implements ActionListener {
    private JTextField textFieldName;
    private JTextField textFieldRoom;
    private JTextArea textArea;
    private JTextField textFieldMessage;
    private JButton sendButton;
    private JPanel mainPanel;
    private JButton connectButton;

    private String messageToSend;
    private String name;
    private String room;

    public ChatClient() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sendButton.addActionListener(this);
        connectButton.addActionListener(this);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String date = new SimpleDateFormat("[hh:mm:ss]").format(new Date());
            messageToSend = date + " " + name + ": " + textFieldMessage.getText();

            Thread send = new Thread(sendToServer);
            send.start();

            textFieldMessage.setText(""); // Очистка поля ввода для сообщения
        } else if (e.getSource() == connectButton) {
            name = textFieldName.getText();
            room = textFieldRoom.getText(); // Название комнаты/канала

            Thread receive = new Thread(connectToServer);
            receive.start();
        }
    }

    Runnable sendToServer = new Runnable() {
        @Override
        public void run() {
            try {
                // Отправка сообщения на сервер
                ConnectionFactory factory = new ConnectionFactory();
                factory.setUri(Constants.uri);
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.exchangeDeclare(room, "fanout");

                channel.basicPublish(room, "", null, messageToSend.getBytes());

                channel.close();
                connection.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Cannot send a message to the server", "Connection error", JOptionPane.WARNING_MESSAGE);
            }
        }
    };

    Runnable connectToServer = new Runnable() {
        @Override
        public void run() {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setUri(Constants.uri);
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.exchangeDeclare(room, "fanout");
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, room, "");

                String date = new SimpleDateFormat("[hh:mm:ss]").format(new Date());
                messageToSend = date + " " + name + " connected to the chat";
                channel.basicPublish(room, "", null, messageToSend.getBytes());

                textArea.append("Connected to the server!\n\n");

                QueueingConsumer consumer = new QueueingConsumer(channel);
                channel.basicConsume(queueName, true, consumer);

                while (true) {
                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                    String message = new String(delivery.getBody());

                    textArea.append(message + "\n");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Cannot connect to the server", "Connection error", JOptionPane.WARNING_MESSAGE);
            }
        }
    };

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
    }
}
