package cn.edu.zucc.utils.em;

/**
 * @author liangruuu
 */
public enum EmBgmOperatorType {
  // 每个BGM对应ZOOKEEPER节点的类型
  ADD("1", "添加BGM"),
  DELETE("2", "删除BGM");

  private final String type;
  private final String value;

  EmBgmOperatorType(String type, String value) {
    this.type = type;
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public static String getValueByKey(String key) {
    for (EmBgmOperatorType type : EmBgmOperatorType.values()) {
      if (type.getType().equals(key)) {
        return type.value;
      }
    }
    return null;
  }
}
