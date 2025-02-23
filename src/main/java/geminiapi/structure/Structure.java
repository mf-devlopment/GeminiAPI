package geminiapi.structure;

/**
 * JSON 形式に変換可能なオブジェクトのインターフェース。
 */
public interface Structure {
  /**
   * オブジェクトを JSON 形式の文字列に変換する。
   *
   * @return JSON 形式の文字列
   */
  String toJson();
}
