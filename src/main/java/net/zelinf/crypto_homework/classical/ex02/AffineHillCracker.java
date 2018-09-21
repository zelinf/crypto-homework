package net.zelinf.crypto_homework.classical.ex02;

import java.util.Optional;

/**
 * This class cracks cipher text generated by Affine Hill Cipher
 */
public class AffineHillCracker {

    /**
     * Creates an AffineHillCracker with a cipher-clear text pair,
     * along with the width of the key.
     *
     * @param cipherText cipher text
     * @param clearText  clear text corresponding to the cipher text
     * @param keyWidth   width of the key
     * @throws FailedToInferKeyException If the key can not be inferred
     */
    public AffineHillCracker(byte[] cipherText, byte[] clearText, int keyWidth) {

    }

    /**
     * Each byte of 'cipherText' must be within [0..25],
     * otherwise it will be ignored.
     *
     * @param cipherText cipher text
     * @return clear text
     */
    public Optional<byte[]> crack(byte[] cipherText) {
        Optional<byte[]> result = Optional.empty();

        return result;
    }
}