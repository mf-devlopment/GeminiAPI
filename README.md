[![](https://jitpack.io/v/mf-devlopment/GeminiAPI.svg)](https://jitpack.io/#mf-devlopment/GeminiAPI)


テスト実行時は`test/resources/secret.properties`ファイルを作成し、
```properties
gemini_api_key=YOUR_GEMINI_API_KEY
```
を書く

# 使い方

インスタンス生成
```java
RequestGemini request = new RequestGemini(YOUR_API_KEY);
```

### Geminiによる生成されたテキストを取得
```java
final Contents build = Contents.builder()
    .add(Content.of("人をターゲットにした物語を書いて", "user"))
    .build();

String resp = request.generate(build);
```

### ストリーム形式で受信(gpiの少しづつ文章が出るような生成)

```java
final Contents build = Contents.builder()
    .add(Content.of("魚をターゲットにした物語を書いて", "user"))
    .build();

final Stream<String> stream = request.streamGenerateContent(build);
stream.forEach(System.out::println);
```

### トークン数の取得
```java
final Contents build = Contents.builder()
    .add(Content.of("人をターゲットにした物語を書いて", "user"))
    .build();

int count = request.countTokens(build);
```
