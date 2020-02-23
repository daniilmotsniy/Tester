package tester;

public final class Encryption {
    private Encryption() {}

    public static String toEncryptedString(byte[] data, byte salt) {
        byte[] encrypted = encrypt(data, salt);

        StringBuilder builder = new StringBuilder();

        for (byte b : encrypted) {
            builder.append(b);
        }

        return builder.toString();
    }

    public static byte[] toDecryptedArray(String str, int count, byte salt) {
        byte[] result = new byte[count];

        for (int i = 0; i < count; ++i) {
            result[i] = (byte) (str.charAt(i) - '0');
        }

        return decrypt(result, count, salt);
    }

    public static byte[] encrypt(byte[] in, byte salt) {
        byte[] en1 = encode(in);

        xorAll(en1, salt);

        return decode(en1, in.length);
    }

    public static byte[] decrypt(byte[] data, int count, byte salt) {
        return decode(xorAll(encode(data), salt), count);
    }

    public static byte[] encode(byte[] data) {
        byte[] result = new byte[(data.length - 1) / 4 + 1];

        for (int i = 0; i < data.length; ++i) {
            result[i / 4] |= (byte) (data[i] << ((3 - (i & 0b11)) * 2));
        }

        return result;
    }

    public static byte[] decode(byte[] bytes, int count) {
        byte[] result = new byte[count];

        for (int i = 0; i < count; ++i) {
            result[i] = (byte) ((bytes[i / 4] >>> ((3 - (i & 0b11)) * 2)) & 0b11);
        }

        return result;
    }

    public static byte[] xorAll(byte[] data, byte salt) {
        for (int i = 0; i < data.length; ++i) {
            data[i] ^= salt;
        }

        return data;
    }
}
