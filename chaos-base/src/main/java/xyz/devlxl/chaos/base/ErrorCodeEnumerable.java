package xyz.devlxl.chaos.base;

/**
 * 错误代码枚举的限定接口
 * 
 * <h1>错误代码设计原则</h1>
 * <ul>
 * <li><strong>系统/子系统内只有一个错误代码清单，该清单是一个“有跟树”。该清单可能是隐式形成（即没有定义或查看该清单的地方或功能）</strong>
 * <li>错误代码只在微服务向外反馈结果时使用，微服务内部只使用异常（但前期为了方便可以为异常添加可选的错误代码成员变量）。
 * <li><strong>错误代码是一个具有标识作用的字符串，对内是一个枚举常量。</strong> TODO 后期要删除枚举常量表现形式，没多大必要了
 * <li>错误代码字符串，以“/一级子节点名/二级子节点名[/三级子节点名[/...]]”的形式进行命名，省略根节点的名称。树的组织和命名规则如下：
 * <ol>
 * <li>只有页节点是错误代码，中间节点都是分段或分组。
 * <li>一级子节点永远不会是页节点，名字只能从“G”、“S”中选一个，“G”表示代码分段：子系统内全局错误，“S”表示代码分段：某个微服务的错误。
 * <li>若一级子节点是“S”，则二级子节点只能是微服务的标识名称，可以使用缩写或别名。
 * <li>"B"是一个特殊的节点名称，它只能是中间节点，表示代表业务异常的错误代码分段。
 * <li>除了上面的规则外，剩余节点的组织和命名规则，由它们的父节点决定。
 * 例如，某个微服务可以决定它的错误代码，是“/S/SSS/L1_ERROR”、“/S/SSS/L1_GROUP/L2_ERROR”，或者“/S/SSS/L1_GROUP/L2_GROUP/L3_ERROR”。
 * <li>同一个父节点的页节点之间，不能通过为名称前缀的方式再分段。若要分段，必须插入中间节点。
 * <li>不强制要求区分系统错误和业务错误。但如果想区分，则建议不动系统错误代码，只将业务错误代码重新划分到新的子分段——“B”下。
 * </ol>
 * <li>错误代码枚举设计原则：枚举类的常量必定是叶节点；枚举类必定是父节点，但它的子节点不一定是页节点；枚举类必须提供从根节点到它的路径。暂时，只能从叶节点向根节点寻址，不能反向寻址。
 * </ul>
 * 
 * <h1>微服务内部类型限定</h1>
 * <ul>
 * <li><strong>除了实现该接口外，还必须是枚举</strong>。
 * <li>请优先使用<strong>E extends Enum&lt?&gt & ErrorCode</strong>进行限定，其次才是<strong>ErrorCode</strong>。
 * <li>不建议使用Enum&lt?&gt进行类型限定。如果有需要，可以使用下面的方式:
 * 
 * <pre>
 *     if (sumObj instanse of ErrorCode) {
 *         Enum&lt?&gt sumEnum = (Enum&lt?&gt)sumObj;
 *         .....
 *         
 *         Enum&ltCaseEnum&gt switchableEnum = (Enum&ltCaseEnum&gt)sumObj;
 *         switch (switchableEnum) {
 *             ...
 *         }
 *         .....
 *     }
 * </pre>
 * 
 * <li><strong>由于不能通过虚拟类为枚举提供统一行为，所以该接口中提供默认方法用以稍微弥补</strong>
 * </ul>
 * 
 * @author Liu Xiaolei
 * @date 2018/07/21
 */
public interface ErrorCodeEnumerable {
    /**
     * 父节点（枚举类定义﹝非具体常量﹞代表的节点）的路径，“/”开头，“/”作为分隔符，并省略根节点部分。
     * <p>
     * 对于具体的枚举常量，${parentPath}/${EUNM_CONSTANT_NAME}，即为该常量代表的错误代码字符串。
     * 
     * @return 父节点的路径
     */
    String parentPath();

    /**
     * 转换成标准的错误代码字符串
     * <p>
     * 默认情况下，等于：${grandParentPath}/${parentName}/${EUNM_CONSTANT_NAME}，即为该常量代表的错误代码字符串
     * 
     * @return 错误代码字符串
     */
    default String toStandardString() {
        Enum<?> enumThis = (Enum<?>)this;
        return new StringBuilder()
            .append(this.parentPath())
            .append("/").append(enumThis.name())
            .toString();

    }
}
