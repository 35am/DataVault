/**
 *  Copyright (C) 2012  Neil Taylor (https://github.com/qwerky/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package qwerky.tools.datavault;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DataVault {

  public static final String CIPHER_ALGORITHM = "AES";
  public static final String KEY_ALGORITHM = "AES";
  public static final String PASSWORD_HASH_ALGORITHM = "SHA-256";
  
  private File file;
  private Provider provider;
  
  public DataVault(File file) {
    this.file = file;
    this.provider = new BouncyCastleProvider();
  }
  
  public InputStream getInputStream(char[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, InvalidKeyException, UnsupportedEncodingException {
    Cipher cipher = buildCipher(password, Cipher.DECRYPT_MODE);
    return new CipherInputStream(new FileInputStream(file), cipher);
  }

  public OutputStream getOutputStream(char[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, InvalidKeyException, UnsupportedEncodingException {
    Cipher cipher = buildCipher(password, Cipher.ENCRYPT_MODE);
    return new CipherOutputStream(new FileOutputStream(file), cipher);
  }
  
  private Cipher buildCipher(char[] password, int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, provider);
    Key key = buildKey(password);
    cipher.init(mode, key);
    return cipher;
  }

  private Key buildKey(char[] password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digester = MessageDigest.getInstance(PASSWORD_HASH_ALGORITHM, provider);
    digester.update(String.valueOf(password).getBytes("UTF-8"));
    byte[] key = digester.digest();
    SecretKeySpec spec = new SecretKeySpec(key, KEY_ALGORITHM);
    return spec;
  }


}
