package org.jodaengine.node.activity.custom;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.Token;


/**
 * An activity that can be used to compute various hashes of given string. It stores them in a specified resultvariable.
 * This activity is especially useful for loadtesting, since cipheralgorithms are by design hard to compute to minimize
 * the success rate of brute force attacks.
 * 
 * It uses the Java Message Digest class which supports the following cipher algorithms: MD2 MD5 SHA-1: The Secure Hash
 * Algorithm, as defined in Secure Hash Standard, NIST FIPS 180-1. SHA-256 SHA-384 SHA-512 DSA RSA and many more ;-)
 */
public class HashComputationActivity extends AbstractActivity {

    private static final String DEFAULT_ALGORITHM = "SHA1";

    private static final int MAGIC_BITSHIFT = 4;

    private static final int MAGIC_FIFTEEN = 0x0f;

    private String toBeHashed;

    private String variableName;

    private String algorithmToBeUsed;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new hash computation activity.
     * 
     * @param variableName
     *            the name of the variable it will be stored in in the process instance
     * @param toBeHashed
     *            the String to be hashed (maybe a password)
     * @param algorithmToBeUsed
     *            the algorithm to be used, which must be one of the ones supported by java.security.MessageDigest
     */
    public HashComputationActivity(String variableName, String toBeHashed, String algorithmToBeUsed) {

        this.variableName = variableName;
        this.toBeHashed = toBeHashed;
        this.algorithmToBeUsed = algorithmToBeUsed;
    }

    /**
     * Instantiates a new hash computation activity using the default Algorithm.
     * 
     * @param variableName
     *            the name of the variable it will be stored in in the process instance
     * @param toBeHashed
     *            the String to be hashed (maybe a password)
     */
    public HashComputationActivity(String variableName, String toBeHashed) {

        this(variableName, toBeHashed, DEFAULT_ALGORITHM);
    }

    /**
     * Takes the bytes of the encrypted representation and transforms them to a more readable HEX-format.
     * 
     * @param b
     *            the byte array (encrypted representation)
     * @return the hexstring
     */
    private String bytesToHex(byte[] b) {

        char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < b.length; j++) {
            buf.append(hexDigit[(b[j] >> MAGIC_BITSHIFT) & MAGIC_FIFTEEN]);
            buf.append(hexDigit[b[j] & MAGIC_FIFTEEN]);
        }
        return buf.toString();
    }

    @Override
    protected void executeIntern(Token token) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance(this.algorithmToBeUsed);
            md.update(toBeHashed.getBytes());
            byte[] output = md.digest();
            String stringOutputString = bytesToHex(output);
            // logger.debug(stringOutputString);
            ProcessInstanceContext context = token.getInstance().getContext();
            context.setVariable(variableName, stringOutputString);
        } catch (NoSuchAlgorithmException e) {
            logger.error("No such Algorithm (hashcomputation!) " + this.algorithmToBeUsed
                + " and here you got your error: ", e);
        }
    }

}
