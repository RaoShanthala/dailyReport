package co.jp.arche1.kdrs.common.security.config;

/**
 * dataSourceを変更するときに用いるアノテーション.
 */

public @interface DataSource {

  /**
   * defaultはSTG.
   *
   * @return enumで定義したdatasource
   */
  DataSourceType value() default DataSourceType.KPMS;
  enum DataSourceType {
    KPMS,
    KDRS
  }
}

