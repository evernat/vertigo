/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.tcp;

import io.vertigo.kernel.command.VCommand;
import io.vertigo.kernel.command.VCommandHandler;
import io.vertigo.kernel.command.VResponse;
import io.vertigoimpl.engines.command.tcp.VClient;
import io.vertigoimpl.engines.command.tcp.VServer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author pchretien
 */
public final class TcpTest {
	private static final String HOST = "localhost";
	//volontairement diff�rent de 4444
	private static final int PORT = 4443;

	@Test
	public void testRequestServer() throws InterruptedException {
		startServer();
		test(2, 10000);
		test(2, 10000);
		test(2, 10000);
		test(2, 10000);
		//test(2, 3);
	}

	private static void startServer() {
		new Thread(new VServer(new MyCommandHandler(), PORT)).start();
	}

	public void test(int threadCount, int count) throws InterruptedException {
		Thread[] threads = new Thread[threadCount];
		long start = System.currentTimeMillis();
		for (int j = 0; j < threadCount; j++) {
			threads[j] = new Thread(new Sender(count));
			threads[j].start();
		}
		for (int j = 0; j < threadCount; j++) {
			threads[j].join();
		}
		System.out.println("--------------------------------------------------- ");
		System.out.println("----- threads       : " + threadCount);
		System.out.println("----- count/thread  : " + count);
		System.out.println("----- elapsed time  : " + ((System.currentTimeMillis() - start) / 1000) + "s");
		System.out.println("--------------------------------------------------- ");

	}

	public static final class Sender implements Runnable {
		//private final int id;
		private final int count;

		Sender(int count) {
			this.count = count;
		}

		static VClient createClient() {
			return new VClient(HOST, PORT);
		}

		@Override
		public void run() {
			try (VClient tcpClient = createClient()) {
				for (int i = 0; i < count; i++) {
					//if (i % 10 == 0) {
					//	System.out.println(">>[" + id + "] :" + i);
					//}
					VResponse response = tcpClient.onCommand(new VCommand("ping"));
					Assert.assertFalse(response.hasError());
					//					if (response.hasError()) {
					//						System.out.println("KO >" + response.getResponse());
					//					} else {
					//						System.out.println("OK >" + response.getResponse());
					//					}
				}
			}
		}
	}

	private static class MyCommandHandler implements VCommandHandler {
		public VResponse onCommand(VCommand command) {
			if ("ping".equals(command.getName())) {
				return VResponse.createResponse("pong");
			} else if ("pong".equals(command.getName())) {
				return VResponse.createResponse("ping");
			}
			return VResponse.createResponseWithError("unknown command " + command.getName());
		}
	}
}
