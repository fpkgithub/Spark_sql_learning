这是一个web+echarts的小案例

Mysql数据库存储着日志数据
然后通过Dao查询到--->demain封装到，通过web的servlet来查询--->html的echarts显示

通过Ajax.GET来获取结果然后填充数据


day_video_access_topn_stat
```shell
mysql> desc day_video_access_topn_stat;
+--------+------------+------+-----+---------+-------+
| Field  | Type       | Null | Key | Default | Extra |
+--------+------------+------+-----+---------+-------+
| day    | varchar(8) | NO   | PRI | NULL    |       |
| cms_id | bigint(10) | NO   | PRI | NULL    |       |
| times  | bigint(10) | NO   |     | NULL    |       |
+--------+------------+------+-----+---------+-------+
3 rows in set (0.01 sec)

mysql> select * from day_video_access_topn_stat;
+----------+--------+--------+
| day      | cms_id | times  |
+----------+--------+--------+
| 20170511 |  14322 |  55102 |
| 20170511 |   4500 |  55366 |
| 20170511 |   4600 |  55501 |
| 20170511 |  14623 |  55621 |
| 20170511 |  14390 |  55683 |
| 20170511 |  14704 |  55701 |
| 20170511 |   4000 |  55734 |
| 20170511 |  14540 | 111027 |
+----------+--------+--------+
8 rows in set (0.00 sec)

```
