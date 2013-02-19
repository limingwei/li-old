package li.webqq;

/**
 * java版webqq3密码与验证码的MD5加密算法
 * 
 * @author 作者 E-mail:hangxin1940@gmail.com
 * @version 创建时间：2011-7-23 19:34:30
 */
public class MD5 {
    /*
     * A Java implementation of the RSA Data Security, Inc. MD5 Message Digest Algorithm, as defined in RFC 1321. Based on the JavaScript implementation of Paul Johnston Copyright (C) Paul Johnston 1999 - 2000. See http://pajhome.org.uk/site/legal.html for details. Java Version by Thomas Weber (Orange Interactive GmbH)
     */

    /*
     * Convert a 32-bit number to a hex string with ls-byte first
     */
    String hex_chr = "0123456789abcdef";

    private String rhex(int[] num) {

        String str = "";
        for (int n : num) {
            for (int j = 0; j <= 3; j++)
                str = str + hex_chr.charAt((n >> (j * 8 + 4)) & 0x0F) + hex_chr.charAt((n >> (j * 8)) & 0x0F);
        }
        // 默认全部大写
        return str.toUpperCase();
    }

    /*
     * 这里改动了 Add integers, wrapping at 2^32
     */
    private int add(int x, int y) {
        int C = (x & 65535) + (y & 65535);
        int B = (x >> 16) + (y >> 16) + (C >> 16);

        return (B << 16) | (C & 65535);
    }

    /*
     * Bitwise rotate a 32-bit number to the left
     */
    private int rol(int num, int cnt) {
        return (num << cnt) | (num >>> (32 - cnt));
    }

    /*
     * These functions implement the basic operation for each round of the algorithm.
     */
    private int cmn(int q, int a, int b, int x, int s, int t) {
        return add(rol(add(add(a, q), add(x, t)), s), b);
    }

    private int ff(int a, int b, int c, int d, int x, int s, int t) {
        return cmn((b & c) | ((~b) & d), a, b, x, s, t);
    }

    private int gg(int a, int b, int c, int d, int x, int s, int t) {
        return cmn((b & d) | (c & (~d)), a, b, x, s, t);
    }

    private int hh(int a, int b, int c, int d, int x, int s, int t) {
        return cmn(b ^ c ^ d, a, b, x, s, t);
    }

    private int ii(int a, int b, int c, int d, int x, int s, int t) {
        return cmn(c ^ (b | (~d)), a, b, x, s, t);
    }

    /*
     * 标准md5加密
     */
    public String calcMD5(String str) {
        int[] i = str2bin(str);
        int[] r = coreMD5(i, str.length() * 8);

        return rhex(r);

    }

    /*
     * 三遍md5加密，与webqq的实现一样
     */
    public String calcMD5_3(String str) {
        int[] i = str2bin(str);

        int[] r = coreMD5(i, str.length() * 8);
        r = coreMD5(r, 8 * 16);
        r = coreMD5(r, 8 * 16);

        return rhex(r);
    }

    /*
     * 这里改动比较大 Convert a string to a sequence of 16-word blocks, stored as an array. Append padding bits and the length, as described in the MD5 standard.
     */
    private int[] str2bin(String strlen) {

        int nblk = (strlen.length() / 4) + (strlen.length() % 4 > 0 ? 1 : 0);
        int[] blks = new int[nblk];

        for (int k = 0; k < strlen.length(); k++) {
            blks[k >> 2] |= strlen.charAt(k) << ((k % 4) * 8);
        }

        return blks;

    }

    /*
     * 这里改动比较大 Take a string and return the hex representation of its MD5.
     */
    private int[] coreMD5(int[] n, int length) {

        int[] x = new int[(((length + 64) >>> 9) << 4) + 14 + 2];
        for (int i = 0; i < n.length; i++) {
            x[i] = n[i];
        }

        x[length >> 5] |= 0x80 << ((length) % 32);
        x[(((length + 64) >>> 9) << 4) + 14] = length;

        int j = 1732584193;
        int i = -271733879;
        int h = -1732584194;
        int g = 271733878;

        for (int y = 0; y < x.length - 1; y += 16) {

            int e = j;
            int d = i;
            int b = h;
            int a = g;

            j = ff(j, i, h, g, x[y + 0], 7, -680876936);
            g = ff(g, j, i, h, x[y + 1], 12, -389564586);
            h = ff(h, g, j, i, x[y + 2], 17, 606105819);
            i = ff(i, h, g, j, x[y + 3], 22, -1044525330);
            j = ff(j, i, h, g, x[y + 4], 7, -176418897);
            g = ff(g, j, i, h, x[y + 5], 12, 1200080426);
            h = ff(h, g, j, i, x[y + 6], 17, -1473231341);
            i = ff(i, h, g, j, x[y + 7], 22, -45705983);
            j = ff(j, i, h, g, x[y + 8], 7, 1770035416);
            g = ff(g, j, i, h, x[y + 9], 12, -1958414417);
            h = ff(h, g, j, i, x[y + 10], 17, -42063);
            i = ff(i, h, g, j, x[y + 11], 22, -1990404162);
            j = ff(j, i, h, g, x[y + 12], 7, 1804603682);
            g = ff(g, j, i, h, x[y + 13], 12, -40341101);
            h = ff(h, g, j, i, x[y + 14], 17, -1502002290);
            i = ff(i, h, g, j, x[y + 15], 22, 1236535329);

            j = gg(j, i, h, g, x[y + 1], 5, -165796510);
            g = gg(g, j, i, h, x[y + 6], 9, -1069501632);
            h = gg(h, g, j, i, x[y + 11], 14, 643717713);
            i = gg(i, h, g, j, x[y + 0], 20, -373897302);
            j = gg(j, i, h, g, x[y + 5], 5, -701558691);
            g = gg(g, j, i, h, x[y + 10], 9, 38016083);
            h = gg(h, g, j, i, x[y + 15], 14, -660478335);
            i = gg(i, h, g, j, x[y + 4], 20, -405537848);
            j = gg(j, i, h, g, x[y + 9], 5, 568446438);
            g = gg(g, j, i, h, x[y + 14], 9, -1019803690);
            h = gg(h, g, j, i, x[y + 3], 14, -187363961);
            i = gg(i, h, g, j, x[y + 8], 20, 1163531501);
            j = gg(j, i, h, g, x[y + 13], 5, -1444681467);
            g = gg(g, j, i, h, x[y + 2], 9, -51403784);
            h = gg(h, g, j, i, x[y + 7], 14, 1735328473);
            i = gg(i, h, g, j, x[y + 12], 20, -1926607734);

            j = hh(j, i, h, g, x[y + 5], 4, -378558);
            g = hh(g, j, i, h, x[y + 8], 11, -2022574463);
            h = hh(h, g, j, i, x[y + 11], 16, 1839030562);
            i = hh(i, h, g, j, x[y + 14], 23, -35309556);
            j = hh(j, i, h, g, x[y + 1], 4, -1530992060);
            g = hh(g, j, i, h, x[y + 4], 11, 1272893353);
            h = hh(h, g, j, i, x[y + 7], 16, -155497632);
            i = hh(i, h, g, j, x[y + 10], 23, -1094730640);
            j = hh(j, i, h, g, x[y + 13], 4, 681279174);
            g = hh(g, j, i, h, x[y + 0], 11, -358537222);
            h = hh(h, g, j, i, x[y + 3], 16, -722521979);
            i = hh(i, h, g, j, x[y + 6], 23, 76029189);
            j = hh(j, i, h, g, x[y + 9], 4, -640364487);
            g = hh(g, j, i, h, x[y + 12], 11, -421815835);
            h = hh(h, g, j, i, x[y + 15], 16, 530742520);
            i = hh(i, h, g, j, x[y + 2], 23, -995338651);

            j = ii(j, i, h, g, x[y + 0], 6, -198630844);
            g = ii(g, j, i, h, x[y + 7], 10, 1126891415);
            h = ii(h, g, j, i, x[y + 14], 15, -1416354905);
            i = ii(i, h, g, j, x[y + 5], 21, -57434055);
            j = ii(j, i, h, g, x[y + 12], 6, 1700485571);
            g = ii(g, j, i, h, x[y + 3], 10, -1894986606);
            h = ii(h, g, j, i, x[y + 10], 15, -1051523);
            i = ii(i, h, g, j, x[y + 1], 21, -2054922799);
            j = ii(j, i, h, g, x[y + 8], 6, 1873313359);
            g = ii(g, j, i, h, x[y + 15], 10, -30611744);
            h = ii(h, g, j, i, x[y + 6], 15, -1560198380);
            i = ii(i, h, g, j, x[y + 13], 21, 1309151649);
            j = ii(j, i, h, g, x[y + 4], 6, -145523070);
            g = ii(g, j, i, h, x[y + 11], 10, -1120210379);
            h = ii(h, g, j, i, x[y + 2], 15, 718787259);
            i = ii(i, h, g, j, x[y + 9], 21, -343485551);

            j = add(j, e);
            i = add(i, d);
            h = add(h, b);
            g = add(g, a);
        }

        int[] r = { j, i, h, g };
        return r;
    }
}