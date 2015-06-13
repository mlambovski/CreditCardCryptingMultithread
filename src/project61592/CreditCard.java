package project61592;

import java.io.Serializable;
import java.util.Objects;

/**
 * The CreditCard class represent credit cards.
 */
public class CreditCard implements Serializable {

    /**
     * A {@link String} instance of the credit card's number.
     */
    private String cardNumber;
    /**
     * A {@link String} instance of the credit card's encrypted number.
     */
    private String encryptedNumber;
    /**
     * An integer that represents the cipher.
     */
    private int cipher;
    /**
     * If true - the credit card is to be encrypted. If false - the credit card
     * is to be decrypted.
     */
    private boolean toEncrypt;

    /**
     * A standard all-purpose constructor.
     *
     * @param cardNumber A {@link String} instance of the credit card's number.
     * @param cipher An integer that represents the cipher.
     * @param toEncrypt If true - the credit card is to be encrypted. If false -
     * the credit card is to be decrypted.
     * @param encryptedNumber A {@link String} instance of the credit card's
     * encrypted number.
     */
    public CreditCard(String cardNumber, String encryptedNumber, int cipher, boolean toEncrypt) {
        setCardNumber(cardNumber);
        setEncryptedNumber(encryptedNumber);
        setCipher(cipher);
        setToEncrypt(toEncrypt);
    }

    public CreditCard() {
        this("", "", 0, true);
    }

    /**
     * A standard copy constructor. Used when adding an encrypted card to the
     * list of ecnrypted cards.
     *
     * @param card A {@link CreditCard} instance.
     */
    public CreditCard(CreditCard card) {
        this(card.getCardNumber(), card.getEncryptedNumber(), card.getCipher(), card.toEncrypt);
    }

    /**
     * Gets the {@link String} instance of the credit card's number.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the {@link String} instance of the credit card's number.
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Gets the integer that represents the cipher.
     */
    public int getCipher() {
        return cipher;
    }

    /**
     * Sets the integer that represents the cipher.
     */
    public void setCipher(int cipher) {
        this.cipher = cipher;
    }

    /**
     * Gets the boolean valie of toEncrypt.
     */
    public boolean isToEncrypt() {
        return toEncrypt;
    }

    /**
     * Sets the boolean valie of toEncrypt.
     */
    public void setToEncrypt(boolean toEncrypt) {
        this.toEncrypt = toEncrypt;
    }

    /**
     * Gets the {@link String} instance of the credit card's encrypted number.
     */
    public String getEncryptedNumber() {
        return encryptedNumber;
    }

    /**
     * Sets the {@link String} instance of the credit card's encrypted number.
     */
    public void setEncryptedNumber(String encryptedNumber) {
        this.encryptedNumber = encryptedNumber;
    }

    /**
     * Checks if two instances of {@link CreditCard} have equal class datas.
     *
     * @param obj An instance of {@link CreditCard}
     * @return true if they are equal; false if not.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CreditCard other = (CreditCard) obj;
        if (!Objects.equals(this.cardNumber, other.cardNumber)) {
            return false;
        }
        if (!Objects.equals(this.encryptedNumber, other.encryptedNumber)) {
            return false;
        }
        if (this.cipher != other.cipher) {
            return false;
        }
        return true;
    }

    /**
     * Converts the {@link CreditCard} to {@link String}.
     *
     * @return {@link String} representation of an instance of the
     * {@link CreditCard} class.
     */
    @Override
    public String toString() {
        return String.format("Card number: %s, Encrypted Number: %s,"
                + " Cipher: %s, toEncrypt: %b", cardNumber, encryptedNumber, cipher, toEncrypt);
    }

}
