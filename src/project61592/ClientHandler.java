package project61592;

import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ClientHandler class is used to handle each connected client in a separate
 * thread. For that reason it implements the {@link Runnable} interface. Here
 * lies the rest of the server functionality that handles the direct connection
 * to each client.
 */
public class ClientHandler implements Runnable {

    /**
     * A {@link Socket} instance represents the connected client's socket.
     */
    private Socket connection;
    /**
     * An {@link ObjectInputStream} instance that is used to receive data from
     * the client.
     */
    private ObjectInputStream input;
    /**
     * An {@link ObjectOutputStream} instance that is used to send data to the
     * client.
     */
    private ObjectOutputStream output;
    /**
     * A {@link Server} instance that is used to get access to the server's gui
     * and functionality.
     */
    private Server gui;
    /**
     * A boolean value used to mark whether a client has provided correct login
     * information
     */
    private boolean allowConnection = false;
    /**
     * A {@link User} instance representing the client that is sedning login
     * information.
     */
    private User connectingUser;

    /**
     * The constructor for {@link ClientHandler}.
     *
     * @param connection A {@link Socket} instance representing the client's
     * socket.
     * @param gui A {@link Server} instance.
     */
    public ClientHandler(Socket connection, Server gui) {
        this.connection = connection;
        this.gui = gui;
    }

    /**
     * Initializes the {@link ObjectInputStream} and {@link ObjectOutputStream}
     * instances with the {@link Socket} instance's input and output streams.
     *
     * @throws IOException
     */
    private void getStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
    }

    /**
     * Checks the login information sent by the client and cross-references it
     * in against the users in the {@link ArrayList} class data from
     * {@link Server} by getting it by invoking the getUsers() method. If the
     * login information is not correct closes the socket. If it is correct sets
     * the value of allowConnection to true.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void establishConnection() throws IOException, ClassNotFoundException {
        while (true) {
            connectingUser = (User) input.readObject();
            Users users = gui.getUsers();
            XStream xstream = new XStream();
            xstream.alias("user", User.class);
            for (int i = 0; i < users.users.size(); i++) {
                User user = users.users.get(i);
                if (connectingUser.getName().equals(user.getName())
                        && connectingUser.getPassword().equals(user.getPassword())) {
                    allowConnection = true;
                    connectingUser.setAccessLevel(user.getAccessLevel());
                    output.writeObject("Login successful!");
                    output.flush();
                    break;
                } else if (i == users.users.size() - 1) {
                    output.writeObject("Login information not correct! Closing socket.");
                    output.flush();
                    closeConnection();
                }
            }
            if (allowConnection) {
                break;
            }
        }
    }

    /**
     * Proccesses the connection between the server and the client. If the value
     * of allowConnection is true this method will proccess the information
     * being sent and received. The server receives {@link CreditCard} instances
     * from the client. If the card is to be encrypted, then the card's number
     * is checked according to Luhns formula by invoking the luchCheck method.
     * If it is not according to the Luhn formula a message containing an error
     * is sent to the client. Then it is checked whether this card is already
     * encrypted by cross-referencing it against the cards stored in the
     * {@link ArrayList} of ecnrypted cards which is retreived by invoking the
     * getEncryptedCards(). If the card is already encrypted a message
     * containing an error is sent to the client. If the client doesn't have
     * sufficient rights to encrypt/decrypt credit cards then a message
     * containing an error is sent to the client.
     *
     * @throws IOException
     */
    private void processConnection() throws IOException {
        while (allowConnection) {
            try {
                CreditCard card = (CreditCard) input.readObject();
                if (card.isToEncrypt()) {
                    card.setEncryptedNumber(encrypt(card));
                } else {
                    card.setCardNumber(decrypt(card));
                }
                if (!luhnCheck(card.getCardNumber()) && card.isToEncrypt()) {
                    sendData("Card number is not according to Luhn formula!");
                }
                if (luhnCheck(card.getCardNumber())
                        && gui.getEncryptedCards().contains(card)
                        && card.isToEncrypt()
                        && "user".equals(connectingUser.getAccessLevel())) {
                    sendData("This credit card is alredy encrypted with this "
                            + "cipher! Choose a different cipher.");
                } else if (luhnCheck(card.getCardNumber())
                        && !gui.getEncryptedCards().contains(card)
                        && card.isToEncrypt()
                        && "user".equals(connectingUser.getAccessLevel())) {
                    gui.addEncryptedCard(new CreditCard(card));
                    sendData(String.format("Encrypted: %s", card.getEncryptedNumber()));
                } else if (card.isToEncrypt() && luhnCheck(card.getCardNumber())) {
                    sendData("You do not have sufficient rights to encrypt!");
                }
                if (!card.isToEncrypt() && "user".equals(connectingUser.getAccessLevel())) {
                    sendData(String.format("Decrypted: %s", card.getCardNumber()));
                } else if (!card.isToEncrypt()) {
                    sendData("You do not have sufficient rights to decrypt!");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Closes the connection to the client by closing the {@link ObjectOutputStream},
     * {@link ObjectInputStream} and {@link Socket} instances.
     */
    private void closeConnection() {
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends data to the client containing decrypted/encrypted card keys or
     * error messages.
     *
     * @param data The {@link String} representation of the message to be sent.
     */
    private void sendData(String data) {
        try {
            output.writeObject(data);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Encrypts the received {@link CreditCard} object's cardNumber using the
     * Substitution cipher.
     *
     * @param card The {@link CreditCard} object received from the client.
     * @return {@link String} containing the encrypted card number.
     */
    private String encrypt(CreditCard card) {
        String cardNumber = card.getCardNumber();
        int cipher = card.getCipher();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            int number = (Character.getNumericValue(cardNumber.charAt(i)) + cipher) % 10;
            sb.append(number);
        }
        return sb.toString();
    }

    /**
     * Decrypts the received {@link CreditCard} object's encryptedCardNumber
     * using the substitution cipher.
     *
     * @param card The {@link CreditCard} object received from the client.
     * @return {@link String} containing the decrypted card number.
     */
    private String decrypt(CreditCard card) {
        String encryptedCardNumber = card.getEncryptedNumber();
        int cipher = card.getCipher();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encryptedCardNumber.length(); i++) {
            int number = (Character.getNumericValue('1' + encryptedCardNumber.charAt(i)) - cipher) % 10;
            if (number < 0) {
                number *= -1;
            }
            sb.append(number);
        }
        return sb.toString();
    }

    /**
     * Checks a {@link CreditCard} object's cardNumber if it is according to the
     * Luhn formula by performing the Luhn algorithm.
     *
     * @param cardNumber {@link String} containing the {@link CreditCard}
     * object's cardNumber class data.
     * @return Returns true if the cardNumber is according to the Luhn formula;
     * false otherwise.
     */
    private boolean luhnCheck(String cardNumber) {
        if (!cardNumber.matches("[3-6][0-9]{15}")) {
            return false;
        }
        int[] digits = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            digits[i] = Character.getNumericValue(cardNumber.charAt(i));
        }
        int sum = 0;
        int length = digits.length;
        for (int i = 0; i < length; i++) {

            // get digits in reverse order
            int digit = digits[length - i - 1];

            // every 2nd number multiply with 2
            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 16 == 0;
    }

    /**
     * The overriden {@link Runnable} run method. Gets the streams, establishes
     * the connection, proccesses the connection, and finally closes the
     * connection, using the methods getStreams(),establishConnection()
     * proccessConncetion() and closeConnection() respectively.
     */
    @Override
    public void run() {
        try {
            getStreams();
            establishConnection();
            processConnection();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

}
