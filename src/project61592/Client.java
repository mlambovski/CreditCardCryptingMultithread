/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project61592;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Client class provides functionality from the client side and also
 * providing a graphical user interface.
 */
public class Client extends javax.swing.JFrame {

    /**
     * A {@link Socket} instance that is used to connect to the server.
     */
    private Socket client;
    /**
     * An {@link ObjectInputStream} instance that is used to receive data from
     * the server.
     */
    private ObjectInputStream input;
    /**
     * An {@link ObjectOutputStream} instance that is used to send data to the
     * server.
     */
    private ObjectOutputStream output;

    /**
     * Creates new form Client. Sets the visibility of the components of the
     * form for encryption/decryption of credit cards until the client has
     * logged in.
     */
    public Client() {
        initComponents();
        lblCardNumber.setVisible(false);
        lblCipher.setVisible(false);
        txtCardNumber.setVisible(false);
        txtCipher.setVisible(false);
        btnDecrypt.setVisible(false);
        btnEncrypt.setVisible(false);
    }

    /**
     * Starts the client. Connects to the server, gets the streams, and
     * proccesses the connection, and finally closes the connection, using the
     * methods connectToServer(), getStreams(), proccessConncetion() and
     * closeConnection() respectively.
     */
    public void runClient() {
        try {
            connectToServer();
            getStreams();
            proccessConnection();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    /**
     * Initializes the {@link Socket} instance and connects to the server at
     * localhost 127.0.0.1 with port 12345. Displays message on the client gui
     * after connecting.
     *
     * @throws IOException
     */
    private void connectToServer() throws IOException {
        client = new Socket(InetAddress.getByName("127.0.0.1"), 12345);
        txtArea.append("Connected to server!\n");
    }

    /**
     * Initializes the {@link ObjectInputStream} and {@link ObjectOutputStream}
     * instances with the {@link Socket} instance's input and output streams.
     *
     * @throws IOException
     */
    private void getStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input = new ObjectInputStream(client.getInputStream());
    }

    /**
     * Proccesses the connection between the client and the server. After
     * submitting a user name and password and after clicking the Login button.
     * This method waits for confirmation from the server. If successful the
     * components for encryption/decryption are set to visible and the
     * components for login are set to hidden. This method also displays any
     * messages on the client gui sent from the server; if the login information
     * is incorrect, the socket is closed by the server.
     *
     * @throws IOException
     */
    private void proccessConnection() throws IOException {
        while (true) {
            try {
                String message = (String) input.readObject();
                if ("Login successful!".equals(message)) {
                    lblCardNumber.setVisible(true);
                    lblCipher.setVisible(true);
                    txtCardNumber.setVisible(true);
                    txtCipher.setVisible(true);
                    btnDecrypt.setVisible(true);
                    btnEncrypt.setVisible(true);
                    lblUsername.setVisible(false);
                    lblPassword.setVisible(false);
                    txtUsername.setVisible(false);
                    txtPassword.setVisible(false);
                    btnLogin.setVisible(false);
                }
                txtArea.append(String.format("%s%n", message));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Closes the connection to the server by closing the {@link ObjectOutputStream},
     * {@link ObjectInputStream}, {@link Socket} instances and displays a
     * message on the client gui that the connection is closed.
     */
    private void closeConnection() {
        try {
            output.close();
            input.close();
            client.close();
            txtArea.append("Connection closed!\n");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends {@link CreditCard} objects to the server.
     *
     * @param cardNumber A {@link String} instance of the card's number.
     * @param cipher An integer representing the cipher.
     * @param toEncrypt A boolean value - if true then the card is to be
     * encrypted; if false then it is to be decrypted.
     */
    private void sendData(CreditCard card) {
        try {
            output.writeObject(card);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends login data the client has entered in the gui to the server.
     *
     * @param user A {@link User} instance containing the login information the
     * client has entered.
     */
    private void sendLoginData(User user) {
        try {
            output.writeObject(user);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        lblCardNumber = new javax.swing.JLabel();
        lblCipher = new javax.swing.JLabel();
        txtCardNumber = new javax.swing.JTextField();
        txtCipher = new javax.swing.JTextField();
        btnEncrypt = new javax.swing.JButton();
        btnDecrypt = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client");

        txtArea.setEditable(false);
        txtArea.setColumns(20);
        txtArea.setRows(5);
        jScrollPane1.setViewportView(txtArea);

        lblUsername.setText("Username:");

        lblPassword.setText("Password:");

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        lblCardNumber.setText("Card Number:");

        lblCipher.setText("Cipher:");

        txtCipher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCipherActionPerformed(evt);
            }
        });

        btnEncrypt.setText("Encrypt");
        btnEncrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncryptActionPerformed(evt);
            }
        });

        btnDecrypt.setText("Decrypt");
        btnDecrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDecryptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsername)
                    .addComponent(lblPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPassword)
                    .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblCardNumber))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblCipher)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCardNumber)
                    .addComponent(txtCipher))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(btnLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(btnEncrypt)
                .addGap(18, 18, 18)
                .addComponent(btnDecrypt)
                .addGap(13, 13, 13))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsername)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCardNumber)
                    .addComponent(txtCardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(lblCipher)
                    .addComponent(txtCipher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnEncrypt)
                    .addComponent(btnDecrypt))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCipherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCipherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCipherActionPerformed

    private void btnEncryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncryptActionPerformed
        if (!txtCardNumber.getText().matches("[0-9]{16}")) {
            txtArea.append("The card number is not valid! "
                    + "It should be a 16 digit number.\n");
        } else if (!txtCipher.getText().matches("[0-9]+")) {
            txtArea.append("The cipher should a number!\n");
        } else {
            CreditCard card = new CreditCard();
            card.setCardNumber(txtCardNumber.getText());
            card.setCipher(Integer.parseInt(txtCipher.getText()));
            card.setToEncrypt(true);
            sendData(card);
        }
    }//GEN-LAST:event_btnEncryptActionPerformed

    private void btnDecryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDecryptActionPerformed
        if (!txtCardNumber.getText().matches("[0-9]{16}")) {
            txtArea.append("The card number is not valid! "
                    + "It should be a 16 digit number.\n");
        } else if (!txtCipher.getText().matches("[0-9]+")) {
            txtArea.append("The cipher should a number!\n");
        } else {
            CreditCard card = new CreditCard();
            card.setEncryptedNumber(txtCardNumber.getText());
            card.setCipher(Integer.parseInt(txtCipher.getText()));
            card.setToEncrypt(false);
            sendData(card);
        }
    }//GEN-LAST:event_btnDecryptActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String userName = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        if (!userName.matches("[0-9a-zA-Z]{3,20}")) {
            txtArea.append("The username must be 0-9, a-z or A-Z"
                    + " and must be between 3 and 20 characters long!\n");
        } else if (!password.matches("[0-9a-zA-Z]{3,20}")) {
            txtArea.append("The password must be 0-9, a-z or A-Z"
                    + " and must be between 3 and 20 characters long!\n");
        } else {
            sendLoginData(new User(userName, password));
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Client().setVisible(true);
//            }
//        });
        Client client = new Client();
        client.setVisible(true);
        client.runClient();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDecrypt;
    private javax.swing.JButton btnEncrypt;
    private javax.swing.JButton btnLogin;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCardNumber;
    private javax.swing.JLabel lblCipher;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTextArea txtArea;
    private javax.swing.JTextField txtCardNumber;
    private javax.swing.JTextField txtCipher;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
