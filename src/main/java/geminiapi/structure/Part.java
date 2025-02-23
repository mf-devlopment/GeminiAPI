package geminiapi.structure;

/**
 * メッセージの一部を表すクラス。
 */
public class Part implements Structure {
  private final String text;

  /**
   * コンストラクタ。
   *
   * @param text メッセージの内容
   */
  public Part(String text) {
    this.text = text;
  }

  /**
   * オブジェクトを JSON 形式の文字列に変換する。
   *
   * @return JSON 形式の文字列
   */
  @Override
  public String toJson() {
    return "{ \"text\": \"" + text + "\" }";
  }
}
