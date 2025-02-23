package geminiapi.structure;

import java.util.ArrayList;
import java.util.List;


/**
 * APIに送信するメッセージの構造を定義するクラス。
 * {@link Part} のリストと送信者の役割を持つ。
 */
public class Content implements Structure {
  private final List<Part> parts;
  private final String role;

  /**
   * コンストラクタ（Builder から作成）
   *
   * @param parts メッセージの内容を表すパーツのリスト
   * @param role メッセージの送信者の役割
   */
  private Content(List<Part> parts, String role) {
    this.parts = parts;
    this.role = role;
  }

  /**
   * テキストとロールを指定してインスタンスを作成する。
   *
   * @param text メッセージの内容
   * @param role メッセージの送信者の役割
   */
  private Content(String text, String role) {
    this(List.of(new Part(text)), role);
  }

  /**
   * {@link Content} を作成するためのビルダーを取得する。
   *
   * @return {@link Builder} のインスタンス
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * ゼロショットでメッセージ送信
   *
   * @param text メッセージの内容
   * @param role メッセージの送信者の役割(`user`または`model`のみ)
   * @return 生成された {@link Content} インスタンス
   */
  public static Content of(String text, String role) {
    return new Content(text, role);
  }

  /**
   * オブジェクトを JSON 形式の文字列に変換する。
   *
   * @return JSON 形式の文字列
   */
  @Override
  public String toJson() {
    StringBuilder json = new StringBuilder("{ \"parts\": [");
    for (int i = 0; i < parts.size(); i++) {
      json.append(parts.get(i).toJson());
      if (i < parts.size() - 1) {
        json.append(",");
      }
    }
    json.append("]");
    if (role != null) {
      json.append(", \"role\": \"").append(role).append("\"");
    }
    return json.append(" }").toString();
  }

  /**
   * {@link Content} を生成するためのビルダークラス。
   */
  public static class Builder {
    private final List<Part> parts = new ArrayList<>();
    private String role = "user";
    private Builder() {
    }

    /**
     * メッセージの内容を追加する。
     *
     * @param part 追加するパーツ
     * @return このビルダーのインスタンス
     */
    public Builder part(Part part) {
      parts.add(part);
      return this;
    }

    /**
     * テキストを追加する。
     *
     * @param text メッセージの内容
     * @return このビルダーのインスタンス
     */
    public Builder part(String text) {
      parts.add(new Part(text));
      return this;
    }

    /**
     * 送信者の役割を設定する。
     *
     * @param role 送信者の役割
     * @return このビルダーのインスタンス
     */
    public Builder role(String role) {
      this.role = role;
      return this;
    }

    /**
     * {@link Content} のインスタンスを生成する。
     *
     * @return 生成された {@link Content} インスタンス
     */
    public Content build() {
      return new Content(parts, role);
    }
  }
}
