# SQL 方言适配细则

通用规范（关键字大写、缩进换行、不为逻辑改动）对所有方言生效。本文件列出各方言**格式化时必须保护的专有语法**——识别出方言后，按对应章节执行。

## Hive SQL

- 分区语法保持原样：`PARTITIONED BY`、`PARTITION (dt='2026-01-01')`，分区值不改动。
- `LATERAL VIEW explode(...) tmp AS col`：`LATERAL VIEW` 与函数名同行，`AS` 别名不拆行。
- `SORT BY` / `DISTRIBUTE BY` / `CLUSTER BY` 与 `ORDER BY` 相互独立，**不做互换改写**。
- `LEFT SEMI JOIN` / `LEFT ANTI JOIN` 单独一行，与其他 JOIN 同级缩进。
- 特有函数保持原写法：`collect_set`、`collect_list`、`get_json_object`、`nvl`。
- `INSERT OVERWRITE TABLE ... PARTITION (...)` 分区子句不拆散。
- 复杂类型字面量保持原格式：`ARRAY<STRING>`、`MAP<STRING,INT>`、`STRUCT<name:STRING,age:INT>`，不插空格改变语义。

## Spark SQL

- 数组/高阶函数保持原样：`transform`、`filter`、`aggregate`、`array_contains`。
- `COALESCE`、`NVL`、`IFNULL` 不改写互换。
- `DISTRIBUTE BY ... SORT BY ...` 保持原顺序。
- `JOIN t2 USING (id)` 语法不展开为 `ON`。
- `LATERAL` 子查询、`explode` 函数写法保持原样。
- Delta / Iceberg 的 `MERGE INTO ... USING ... ON ...` 结构完整保留，`WHEN MATCHED THEN UPDATE SET` 各子句分行。

## Flink SQL / Paimon

- Flink 特有 hint 原样保留、位置紧随所属 SELECT/JOIN：`/*+ OPTIONS('scan.startup.mode'='earliest') */`、`/*+ BROADCAST(t) */`，不移动、不拆行。
- Paimon 全限定表名 `paimon.catalog.db.table` 保持不改。
- 数组/映射字面量保持原样：`ARRAY[...]`、`MAP[...]`、`SIZE(arr)`。
- 窗口 TVF 参数不改：`TUMBLE(TABLE t, DESCRIPTOR(ts), INTERVAL '1' HOUR)`、`HOP(...)`、`CUMULATE(...)`。
- `LEFT SEMI JOIN` / `LEFT ANTI JOIN` 单独一行（与 Hive 写法一致）。
- `WATERMARK FOR ... AS ...` 属于 DDL，非用户明确要求不做排版。
- `INSERT INTO ... SELECT` 完整结构保留；`STATEMENT SET`（多语句提交）不合并、不拆分。

## MySQL

- 反引号标识符保持反引号：`` `order` `` 不替换为双引号或裸名。
- `LIMIT n, m` 与 `LIMIT n OFFSET m` 两种写法各自保持，**不互换**。
- `IF()`、`IFNULL()`、`GROUP_CONCAT(... SEPARATOR ',')` 不改写。
- 字符串常量引号风格保持原样（单引号/双引号不互换）。

## Presto / Trino

- `WITH` CTE 定义保留；`CROSS JOIN UNNEST(arr) AS t (col)` 的 `AS t (col)` 别名结构完整保留。
- 数组函数原样：`element_at`、`array_agg`、`cardinality`。
- **绝不把 `CROSS JOIN UNNEST` 改写为 `LATERAL VIEW`**（方言间语法不混淆）。
- `JSON_EXTRACT` / `JSON_QUERY` 的路径字符串不改。

## ClickHouse

- `FINAL` 修饰符紧随表名：`FROM t FINAL` 不拆行。
- `PREWHERE` 独立于 `WHERE`，条件分行规则同 WHERE（AND 置行首），**绝不合并进 WHERE**。
- `ARRAY JOIN` / `LEFT ARRAY JOIN` 单独一行。
- `SETTINGS k=v` 子句保持语句末尾，键值不改。
- DDL（`ENGINE = MergeTree() ORDER BY ... PARTITION BY ...`）不在美化范围，仅美化 DML。

## Oracle

- `ROWNUM`、`ROWNUM <= 10` 保持原写法，**不改写为** `FETCH FIRST 10 ROWS ONLY`。
- `DECODE(...)` 保持原样，不改写为 `CASE WHEN`。
- `(+)` 外连接标记紧跟条件列：`WHERE a.id = b.id(+)` 不破坏。
- `DUAL` 表保持。
- `NVL`、`TO_CHAR`、`TO_DATE` 的格式串（如 `'YYYY-MM-DD HH24:MI:SS'`）原样保留。
- 语句结束符（`;` 或 `/`）不增删。
