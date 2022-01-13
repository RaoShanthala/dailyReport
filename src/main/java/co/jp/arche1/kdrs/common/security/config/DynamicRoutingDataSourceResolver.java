package co.jp.arche1.kdrs.common.security.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import co.jp.arche1.kdrs.common.security.config.DataSource.DataSourceType;

/**
 * 条件に応じてdataSourceを切り替えるクラス.
 */
public class DynamicRoutingDataSourceResolver extends AbstractRoutingDataSource {

  /**
   * 使用するdataSourceのキーを設定するメソッド.
   *
   * @return String 接続先のdataSourceを表す文字列
   */
  @Override
  protected Object determineCurrentLookupKey() {

    if (DataSourceContextHolder.getDataSourceType() == DataSourceType.KPMS) {
      return "kpms";
    } else if (DataSourceContextHolder.getDataSourceType() == DataSourceType.KDRS) {
      return "kdrs";
    }

    // デフォルトはKPMSを返却
    return "kpms";
  }

}
