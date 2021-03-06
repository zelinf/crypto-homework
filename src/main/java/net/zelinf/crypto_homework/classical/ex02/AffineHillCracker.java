package net.zelinf.crypto_homework.classical.ex02;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class cracks cipher text generated by Affine Hill Cipher
 */
public class AffineHillCracker {

    public static final int TEXT_SPACE_SIZE = 26;

    /**
     * Creates an AffineHillCracker with a cipher-clear text pair,
     * along with the width of the key.
     *
     * @param clearText  clear text corresponding to the cipher text
     * @param cipherText cipher text
     * @param keyWidth   width of the key
     * @throws FailedToInferKeyException If the key can not be inferred
     * @throws IllegalArgumentException  If clearText or cipherText is of length 0,
     *                                   or keyWidth is non-positive
     */
    public AffineHillCracker(byte[] clearText, byte[] cipherText, int keyWidth) {
        this.keyWidth = keyWidth;
        if (!(keyWidth > 0 && clearText.length != 0 && cipherText.length != 0)) {
            throw new IllegalArgumentException();
        }

        final int matElemCount = keyWidth * keyWidth;
        final List<ModularIntegerMatrix> clearTextMatrices = toMatrices(clearText);
        final List<ModularIntegerMatrix> cipherTextMatrices = toMatrices(cipherText);
        final int minLength = Integer.min(clearTextMatrices.size(), cipherTextMatrices.size());

        boolean succeeded = false;
        for (int i = 0; i < minLength - 1; ++i) {
            if (tryInferKey(clearTextMatrices.get(i), clearTextMatrices.get(i + 1),
                    cipherTextMatrices.get(i), cipherTextMatrices.get(i + 1))) {
                succeeded = true;
                break;
            }
        }

        if (!succeeded)
            throw new FailedToInferKeyException();
    }

    private int keyWidth;

    private Key key;

    private boolean tryInferKey(ModularIntegerMatrix clear1, ModularIntegerMatrix clear2,
                                ModularIntegerMatrix cipher1, ModularIntegerMatrix cipher2) {
        ModularIntegerMatrix clearDiff = clear1.subtract(clear2);
        ModularIntegerMatrix cipherDiff = cipher1.subtract(cipher2);
        Optional<ModularIntegerMatrix> clearDiffInverse = clearDiff.getInverse();
        if (!clearDiffInverse.isPresent())
            return false;
        key = new Key();
        key.factor = clearDiffInverse.get().multiply(cipherDiff);

        ModularIntegerMatrix remainMat = cipher1.subtract(clear1.multiply(key.factor));
        key.remain = new ArrayRealVector(remainMat.getColumnDimension());
        for (int i = 0; i < remainMat.getColumnDimension(); ++i) {
            key.remain.setEntry(i, remainMat.getEntry(0, i));
        }

        return true;
    }

    private List<ModularIntegerMatrix> toMatrices(byte[] text) {
        List<ModularIntegerMatrix> matrices = new ArrayList<>();

        for (int i = 0; i <= text.length - matElemCount(); i += matElemCount()) {
            ModularIntegerMatrix matrix = new ModularIntegerMatrix(TEXT_SPACE_SIZE, keyWidth, keyWidth);
            for (int row = 0; row < keyWidth; ++row) {
                for (int col = 0; col < keyWidth; ++col) {
                    matrix.setEntry(row, col, text[i + row * keyWidth + col]);
                }
            }
            matrices.add(matrix);
        }
        return matrices;
    }

    private int matElemCount() {
        return keyWidth * keyWidth;
    }

    public Key getKey() {
        return key;
    }

    public static class Key {
        private ModularIntegerMatrix factor;

        private RealVector remain;

        private Key() {
        }

        public ModularIntegerMatrix getFactor() {
            return factor;
        }

        public RealVector getRemain() {
            return remain;
        }
    }
}
