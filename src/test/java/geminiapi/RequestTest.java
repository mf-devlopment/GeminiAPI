package geminiapi;

import geminiapi.structure.Content;
import geminiapi.structure.Contents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RequestTest {
  private static final Logger log = LogManager.getLogger(RequestTest.class);
  private String apiKey;
  private RequestGemini request;

  @BeforeEach
  void setUp() throws IOException {
    Properties properties = new Properties();
    InputStream source = ClassLoader.getSystemResourceAsStream("secret.properties");
    properties.load(source);
    apiKey = properties.getProperty("gemini_api_key");
    request = new RequestGemini(apiKey);
  }

  @Test
  void testApiKeyIsNotNull() {
    assertNotNull(apiKey, "API Key should not be null");
    log.info("API Key: {}", apiKey);
  }

  @Test
  void testGenerate() {
    final Contents build = Contents.builder()
        .add(Content.of("人をターゲットにした物語を書いて", "user"))
        .build();
    String resp = request.generate(build);
    log.info(resp);
  }

  @Test
  void testContTokens() {
    final Contents build = Contents.builder()
        .add(Content.of("人をターゲットにした物語を書いて", "user"))
        .build();
    int count = request.countTokens(build);
    log.info("トークン数: {}", count);
  }

  @Test
  void testStreamGenerateContent() {
    final Contents build = Contents.builder()
        .add(Content.of("魚をターゲットにした物語を書いて", "user"))
        .build();
    final Stream<String> stream = request.streamGenerateContent(build);
    stream.forEach(log::info);
  }
}
