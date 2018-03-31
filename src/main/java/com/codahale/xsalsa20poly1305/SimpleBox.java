/*
 * Copyright © 2017 Coda Hale (coda.hale@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codahale.xsalsa20poly1305;

import java.util.Optional;
import okio.Buffer;
import okio.ByteString;

/**
 * Convenience functions for encryption without requiring nonce management.
 *
 * <p>Compatible with RbNaCl's SimpleBox construction, but generates misuse-resistant nonces.
 */
public class SimpleBox {

  private final SecretBox box;

  /**
   * Create a new {@link SimpleBox} instance with the given secret key.
   *
   * @param secretKey a 32-byte secret key
   */
  public SimpleBox(ByteString secretKey) {
    this.box = new SecretBox(secretKey);
  }

  /**
   * Create a new {@link SecretBox} instance given a Curve25519 public key and a Curve25519 private
   * key.
   *
   * @param publicKey a Curve25519 public key
   * @param privateKey a Curve25519 private key
   */
  public SimpleBox(ByteString publicKey, ByteString privateKey) {
    this.box = new SecretBox(publicKey, privateKey);
  }

  /**
   * Encrypt the plaintext with the given key.
   *
   * @param plaintext any arbitrary bytes
   * @return the ciphertext
   */
  public ByteString seal(ByteString plaintext) {
    final ByteString nonce = box.nonce(plaintext);
    final ByteString ciphertext = box.seal(nonce, plaintext);
    return new Buffer().write(nonce).write(ciphertext).readByteString();
  }

  /**
   * Decrypt the ciphertext with the given key.
   *
   * @param ciphertext an encrypted message
   * @return an {@link Optional} of the original plaintext, or if either the key, nonce, or
   *     ciphertext was modified, an empty {@link Optional}
   */
  public Optional<ByteString> open(ByteString ciphertext) {
    if (ciphertext.size() < SecretBox.NONCE_SIZE) {
      return Optional.empty();
    }
    final ByteString nonce = ciphertext.substring(0, SecretBox.NONCE_SIZE);
    final ByteString x = ciphertext.substring(SecretBox.NONCE_SIZE, ciphertext.size());
    return box.open(nonce, x);
  }
}
