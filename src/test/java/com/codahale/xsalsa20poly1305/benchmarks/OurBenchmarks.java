/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codahale.xsalsa20poly1305.benchmarks;

import com.codahale.xsalsa20poly1305.SecretBox;
import com.codahale.xsalsa20poly1305.SimpleBox;
import java.util.concurrent.TimeUnit;
import okio.ByteString;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

@SuppressWarnings("WeakerAccess")
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class OurBenchmarks {

  private static final ByteString key = ByteString.of(new byte[32]);
  private static final SecretBox box = new SecretBox(key);
  private static final SimpleBox simpleBox = new SimpleBox(key);
  private static final ByteString nonce = ByteString.of(new byte[24]);
  private static final ByteString msg100 = ByteString.of(new byte[100]);
  private static final ByteString msg1K = ByteString.of(new byte[1024]);
  private static final ByteString msg10K = ByteString.of(new byte[10 * 1024]);

  @Benchmark
  public ByteString seal100Bytes() {
    return box.seal(nonce, msg100);
  }

  @Benchmark
  public ByteString seal1K() {
    return box.seal(nonce, msg1K);
  }

  @Benchmark
  public ByteString seal10K() {
    return box.seal(nonce, msg10K);
  }

  @Benchmark
  public ByteString simpleSeal100Bytes() {
    return simpleBox.seal(msg100);
  }

  @Benchmark
  public ByteString simpleSeal1K() {
    return simpleBox.seal(msg1K);
  }

  @Benchmark
  public ByteString simpleSeal10K() {
    return simpleBox.seal(msg10K);
  }
}
