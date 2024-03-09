### 报错：1248 - Every derived table must have its own alias

其实这是一个很简单的问题，就是在进行多级查询的过程中，`需要给每一级查询的结果集（表）取一个别名`。

例如：

错误的查询方式：

```sql
SELECT *
FROM (
	SELECT s.sno
	FROM student s
	WHERE s.sex = '女'
);
```

正确的查询方式：

```sql
SELECT *
FROM (
	SELECT s.sno
	FROM student s
	WHERE s.sex = '女'
) as mid_sno;
```

其实区别就在于背后有没有那个`as mid_sno`。

这是为了保证每个派生出来的表都需要有一个自己的别名。