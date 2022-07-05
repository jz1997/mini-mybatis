package com.exmaple.small.mybatis.binding;

import com.exmaple.small.mybatis.mapping.SqlSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MappedStatement {
  // namespace + method name
  private String id;

  /** 配置文件的名称, 例如 UserMapper.xml */
  private String resource;

  /** */
  private SqlSource sqlSource;

  /** 参数类型 */
  private String parameterType;

  /** 返回值类型 */
  private String resultType;

  /** SELECT、INSERT、UPDATE、DELETE */
  private String sqlCommandType;
}
