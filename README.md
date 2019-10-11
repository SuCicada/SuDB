# SuDB

- dao框架代码生成器
    - 自动生成 数据库表与JavaBe的XML映射文件
    - 自动生成 JavaBean代码文件
    - 自动生成 框架目录结构
    - 一键生成数据库表 
- dao框架
    - 使用单例模式创建的数据库连接类
    - dbcp2连接池
    - 对常用SQL进行封装
    - 数据库配置Properties 转换为 Map
    - 实现ORM，通过反射将Map转为JavaBean
    

-[x] 需要根据sql文件和配置文件  -->  连接数据库，创建表。
-[x] 需要根据数据库中的表  -->  实体类代码文件。
-[-]  (Session)

-[x] 根据行数据 -->  javabean
-[-] sql 封装
-[ ] annotation的javabean
-[-] 封装xml的element操作, 在createJava和session时候可用
-[ ] 根据xml映射表创建数据库表
-[ ] 在xml中查找某一个表的class的Element结点,目前采用遍历找, 试采用map等更高效方法
-[x] 封装ClassElement 类
-[ ] 事务
