/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.comun;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Encargada de la encriptación de contraseñas
 */
public class EncriptadorPassword {

	private final static Integer ITERATIONS = 10000;
	private final static Integer KEY_LENGTH = 256;
	private final static String SAL_GLOBAL = "R��n�з�m�{|XŨ`m";

	/**
	 * Encripta un String.
	 *
	 * @param palabra
	 *            palabra a encriptar, se borrará al terminar.
	 * @param sal
	 *            sal para ocultar la palabra.
	 * @return String
	 */
	public String encriptar(char[] palabra, String sal) {
		try{
			return bytesToString(hashPassword(palabra, (sal + SAL_GLOBAL).getBytes("UTF-8"), ITERATIONS, KEY_LENGTH));
		} catch(UnsupportedEncodingException e){
			throw new RuntimeException(e);
		}
	}

	private String bytesToString(byte[] data) {
		String dataOut = "";
		for(int i = 0; i < data.length; i++){
			if(data[i] != 0x00){
				dataOut += (char) data[i];
			}
		}
		return dataOut;
	}

	private byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) {
		try{
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
			SecretKey key = skf.generateSecret(spec);
			byte[] res = key.getEncoded();
			Arrays.fill(password, '\u0012'); // clear sensitive data
			return res;
		} catch(NoSuchAlgorithmException | InvalidKeySpecException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * Genera una sal aleatoria segura
	 *
	 * @return sal
	 */
	public String generarSal() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		return bytesToString(bytes);
	}
}