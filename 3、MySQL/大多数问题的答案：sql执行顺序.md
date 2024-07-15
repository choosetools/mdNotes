# **SELECT查询执行的顺序**：

![SQL查询语句的执行顺序解析](.\images\format,png)

# todo!







我们来看一个案例来具体查看一下执行顺序：

```sql
SELECT 班级, avg(数学成绩) as 数学平均成绩

FROM 学生信息表

WHERE 数学成绩 IS NOT NULL 

GROUP BY 班级

HAVING 数学平均成绩 > 75

ORDER BY 数学平均成绩 DESC

LIMIT 3;
```

上例的SQL执行顺序，如下：

1. 首先执行FROM子句，从学生成绩表中组装数据源的数据。
2. 执行WHERE子句，筛选学生成绩表中所有学生的数学成绩不为NULL的数据。
3. 执行GROUP BY子句，把学生成绩表按“班级”字段进行分组。
4. 计算avg聚合函数，按照每个班级分组求出数学平均成绩。
5. 执行HAVING子句，筛选出班级数学平均成绩大于75分的。
6. 执行SELECT语句，返回指定数据。
7. 执行ORDER BY子句，把最后的结果按“数学平均成绩”进行排序。
8. 执行LIMIT，限制仅返回3条数据，结合ORDER BY子句，即返回所有班级中数学平均成绩的前三名的班级及其数学平均成绩。



若将语句改成如下所示，会怎样？

```sql
SELECT 班级, avg(数学成绩) as 数学平均成绩

FROM 学生信息表

WHERE 数学成绩 IS NOT NULL AND avg(数学成绩) > 75

GROUP BY 班级

ORDER BY 数学平均成绩 DESC

LIMIT 3;
```

我们发现，若将avg(数学成绩) > 75放到WHERE子句中，此时GROUP BY语句还未执行，因此此时聚合值avg(数学成绩)还是未知的，所以会报错。