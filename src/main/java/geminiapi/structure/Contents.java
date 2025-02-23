package geminiapi.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Gemini API に送信するデータの構造を定義するクラス。
 * 複数の {@link Content} オブジェクトを保持し、JSON 形式で出力する機能を提供する。
 */
public class Contents implements Structure {
  private final List<Content> contents = new ArrayList<>();

  /**
   * コンストラクタ(Builderから作成)
   *
   * @see Contents#builder() ビルダーから作成
   * @param contents コンテンツのリスト
   */
  private Contents(List<Content> contents) {
    this.contents.addAll(contents);
  }

  /**
   * コンテンツを作成するためのビルダーを取得する。
   *
   * @return {@link ContentsBuilder} のインスタンス
   */
  public static ContentsBuilder builder() {
    return new ContentsBuilder();
  }

  /**
   * オブジェクトを JSON 形式の文字列に変換する。
   *
   * @return JSON 形式の文字列
   */
  @Override
  public String toJson() {
    StringBuilder builder = new StringBuilder("{ \"contents\": [ ");
    for (int i = 0; i < contents.size(); i++) {
      builder.append(contents.get(i).toJson());
      if (i != contents.size() - 1) {
        builder.append(", ");
      }
    }
    builder.append(" ] }");
    return builder.toString();
  }

  /**
   * {@link Contents} を生成するためのビルダークラス。
   */
  public static class ContentsBuilder {
    private final List<Content> contents = new ArrayList<>();

    /**
     * コンテンツを追加する。
     *
     * @param content 追加するコンテンツ
     * @return このビルダーのインスタンス
     */
    public ContentsBuilder add(Content content) {
      contents.add(content);
      return this;
    }

    /**
     * {@link Contents} のインスタンスを生成する。
     *
     * @return 生成された {@link Contents} インスタンス
     */
    public Contents build() {
      return new Contents(contents);
    }
  }
}
