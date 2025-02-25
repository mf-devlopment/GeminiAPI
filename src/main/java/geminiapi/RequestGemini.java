package geminiapi;

import com.google.gson.*;
import geminiapi.structure.Contents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

/**
 * Gemini API のリクエストを処理するクラス。
 */
public class RequestGemini {
  public static final String MODEL = "models/gemini-1.5-flash";
  public static final String GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/" + MODEL;
  private static final Logger log = LogManager.getLogger(RequestGemini.class);
  private final String apiKey;

  /**
   * Gemini API クライアントのコンストラクタ。
   *
   * @param apiKey APIキー
   */
  public RequestGemini(String apiKey) {
    this.apiKey = apiKey;
  }

  private static JsonElement post(String apiUrl, String jsonBody) {
    // HTTPリクエストの作成
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(apiUrl))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        var body = response.body();
        return JsonParser.parseString(body);
      }
    } catch (IOException | InterruptedException e) {
      log.error("リクエストに失敗しました", e);
    } catch (JsonSyntaxException e) {
      log.error(e);
    }
    return JsonNull.INSTANCE;
  }

  private static String getText(JsonElement response) {
    try {
      JsonObject root = response.getAsJsonObject();
      JsonArray candidates = root.getAsJsonArray("candidates");
      if (!candidates.isEmpty()) {
        JsonObject candidate = candidates.get(0).getAsJsonObject();
        JsonArray parts = candidate.getAsJsonObject("content").getAsJsonArray("parts");
        JsonObject part = parts.get(0).getAsJsonObject();
        return part.get("text").getAsString();
      }
    } catch (RuntimeException e) {
      log.error(e);
    }

    return "";
  }

  /**
   * APIに送信し、レスポンスをテキストで受信する
   *
   * @param content メッセージ内容の入ったデータ
   * @return レスポンス
   */
  public String generate(Contents content) {
    String apiUrl = url("generateContent");

    String jsonBody = content.toJson();
    JsonElement response = post(apiUrl, jsonBody);

    return getText(response);
  }

  /**
   * コンテンツのトークン数をカウントする。
   *
   * @param content メッセージ内容
   * @return トークン数（エラー時は -1）
   */
  public int countTokens(Contents content) {
    String apiUrl = url("countTokens");
    String jsonBody = content.toJson();
    JsonElement response = post(apiUrl, jsonBody);
    try {
      JsonObject root = response.getAsJsonObject();
      return root.getAsJsonPrimitive("totalTokens").getAsInt();
    } catch (Exception e) {
      return -1;
    }
  }

  /**
   * ストリーミングでコンテンツを生成する。
   *
   * @param content メッセージ内容
   * @return 生成されたテキストのストリーム
   */
  public Stream<String> streamGenerateContent(Contents content) {
    String apiUrl = url("streamGenerateContent");
    String jsonBody = content.toJson();
    JsonElement response = post(apiUrl, jsonBody);
    if (!response.isJsonArray()) {
      log.error("Response is not an array");
      return Stream.empty();
    }
    Stream<JsonElement> candidatesArray = response.getAsJsonArray().asList().stream();
    return candidatesArray.map(RequestGemini::getText);
  }

  /**
   * API のエンドポイント URL を生成する。
   *
   * @param method API のメソッド名
   * @return API の完全な URL
   */
  private String url(String method) {
    return GEMINI_BASE_URL + ":" + method + "?key=" + apiKey;
  }
}
