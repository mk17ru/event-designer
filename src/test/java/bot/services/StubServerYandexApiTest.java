package bot.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import bot.external.maps.MapRequest;
import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.Method;
import org.junit.jupiter.api.Test;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.method;
import static com.xebialabs.restito.semantics.Condition.startsWithUri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class StubServerYandexApiTest {
	private static final int PORT = 8080;

	private void withStubServer(int port, Consumer<StubServer> callback) {
		StubServer stubServer = null;
		try {
			stubServer = new StubServer(port).run();
			callback.accept(stubServer);
		} finally {
			if (stubServer != null) {
				stubServer.stop();
			}
		}
	}

	@Test
	public void checkSenderTest() {
		withStubServer(PORT, s -> {
			whenHttp(s)
					.match(method(Method.GET), startsWithUri("/locations"))
					.then(stringContent("{ 'data': [1, 2, 3]}"));

			URI uri = null;
			try {
				uri = new URI("http://yandex.api:" + PORT + "/locations");
			} catch (URISyntaxException e) {
				fail();
			}

			double actualResponseLen = new MapRequest().getRadiusLong();
			double expectedResponseLen = 0.03;

			assertEquals(expectedResponseLen, actualResponseLen);
		});
	}
}