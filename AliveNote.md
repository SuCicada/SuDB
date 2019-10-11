
-- 10/6
- 完成 Session中的 update, delete, get方法
- 封装ClassElement 类


-- 10/1
- 增加按照列排版的query方法
- 实现从xml映射表中创造javabean代码,并保存
- 实现创造项目结构
- [?] 关于数据库表的列名,因为默认的数据库表不分大小写.所以我是否应该直接将列名进行格式化.
- [?] 像是在CodeCreator中 packageName就一直传到了JAVACodeManager中,有继续传到了JAVACode中. 这种模式有没有更好的方式

-- 9/30
- 刪除整個目錄


---- 9/29
- xml創造器完成
數據庫表 -自動-> xml映射表 -自動-> javaBean代碼
xml映射表 -自動-> 數據庫表

---- 9/22
- 创建DBconfig 数据库配置类, 接触配置(DBConfig)与管理(DBManager)的耦合
- 更改实体类生成规则, 将命名优先改为配置文件映射



