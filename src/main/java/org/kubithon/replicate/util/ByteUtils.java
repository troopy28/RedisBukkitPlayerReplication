package org.kubithon.replicate.util;


public class ByteUtils {

    private ByteUtils() {
    }

    /**
     * Converts an array of <b>B</b>yte into an array of <b>b</b>ytes.
     *
     * @param oBytes Byte array to convert.
     * @return Return the array of the byte primitive. The content shouldn't be changed during the conversion.
     */
    public static byte[] toPrimitives(Byte[] oBytes) { // From StackOverflow directly
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }

    /**
     * Converts an array of <b>b</b>yte into an array of <b>B</b>ytes.
     *
     * @param bytesPrim byte array to convert.
     * @return Return the array of the Byte object. The content shouldn't be changed during the conversion.
     */
    public static Byte[] toObjects(byte[] bytesPrim) { // From StackOverflow directly
        Byte[] bytes = new Byte[bytesPrim.length];

        int i = 0;
        for (byte b : bytesPrim)
            bytes[i++] = b; // Autoboxing

        return bytes;
    }
}