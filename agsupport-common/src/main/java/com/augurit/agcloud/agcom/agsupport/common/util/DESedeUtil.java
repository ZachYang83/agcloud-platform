package com.augurit.agcloud.agcom.agsupport.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;

/*
 * 加密与解密工具类
 */
public class DESedeUtil {

    private static char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '+', '/'};

    private static byte[] base64DecodeChars = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1,
            -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
            12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33,
            34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};

    public static String encode64(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;

        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    public static byte[] decode64(String str) {
        byte[] data = str.getBytes();
        int len = data.length;
        ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
        int i = 0;
        int b1, b2, b3, b4;

        while (i < len) {
            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) {
                break;
            }

            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) {
                break;
            }
            buf.write((b1 << 2) | ((b2 & 0x30) >>> 4));

            do {
                b3 = data[i++];
                if (b3 == 61) {
                    return buf.toByteArray();
                }
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) {
                break;
            }
            buf.write(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2));

            do {
                b4 = data[i++];
                if (b4 == 61) {
                    return buf.toByteArray();
                }
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1) {
                break;
            }
            buf.write(((b3 & 0x03) << 6) | b4);
        }
        return buf.toByteArray();
    }

    public static String encode64ToString(String str) {
        String string = "";
        try {
            string = encode64(str.getBytes());
        } catch (Exception e) {
        }
        return string;
    }

    public static String decode64ToString(String str) {
        String string = "";
        try {
            string = new String(decode64(str));
        } catch (Exception e) {
        }
        return string;
    }


    private static final String KEY = "AGSUPPORT_AGCOM_AUGURIT";

    /**
     * 3DES加密
     *
     * @param key 密钥字符串 长度为24字节
     * @param src 明文字符串
     * @return 密文字符串
     */
    public static String desEncrypt(String key, String src) {
        if (src == null) {
            return null;
        }
        byte[] b = desEncrypt(genDESKey(getLimStr(key, 24).getBytes()), src.getBytes());
        return byteArrayToHexString(b);
    }

    public static String desEncrypt(String src) {
        return desEncrypt(KEY, src);
    }

    /**
     * 3DES解密
     *
     * @param key   密钥字符串
     * @param crypt 密文字符串
     * @return 明文字符串
     */
    public static String desDecrypt(String key, String crypt) {
        if (crypt == null) {
            return null;
        }
        byte[] b = desDecrypt(genDESKey(getLimStr(key, 24).getBytes()), hexStringToBytes(crypt));
        return new String(b);
    }

    public static String desDecrypt(String crypt) {
        return desDecrypt(KEY, crypt);
    }

    private static SecretKey genDESKey(byte[] key_byte) {
        return new SecretKeySpec(key_byte, "DESede");
    }

    private static byte[] desEncrypt(SecretKey key, byte[] src) {
        try {
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byteArrayToHexString(byte b[]) {
        String result = "";
        for (int i = 0; i < b.length; i++)
            result = result + byteToHexString(b[i]);
        return result;
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return HexCode[d1] + HexCode[d2];
    }

    private static String HexCode[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    private static byte[] hexStringToBytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < src.length() / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    private static byte[] desDecrypt(SecretKey key, byte[] crypt) {
        try {
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(crypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getLimStr(String key, int max) {
        String strkey = key;
        if (null == strkey) {
            strkey = "";
        }
        int keyLength = strkey.getBytes().length;
        if (keyLength == max) {
            return strkey;
        }
        if (keyLength < max) {
            for (int i = 0; i < max - keyLength; i++) {
                strkey += "K";
            }
        }
        String str = "";
        int i = 0;
        while (i < strkey.length() && (str + strkey.charAt(i)).getBytes().length <= max) {
            str += strkey.charAt(i);
            i++;
        }
        return str;
    }

    public static void main(String[] args) {
        String str = "123";
        System.out.println(encode64ToString(str));
        System.out.println(decode64ToString(encode64ToString(str)));
    }
}
