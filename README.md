# 外文地名对应汉语地名转换
## 功能
- 按照国家、语种定义音译表、常用词。
- 使用音译表和常用词构建AC自动机，对外文地名进行转换。
- 转换结果经人工审核，最终确定外文地名对应的汉语地名。

## 代码说明

- 后端：Spring Boot工程，提供api接口
- 前端：使用了[PanJiaChen/vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)
- AC自动机：使用了[robert-bor/aho-corasick](https://github.com/robert-bor/aho-corasick)

## 代码生成

1. 定义数据库表结构
2. 使用mybatis.generator生成Model、Mapper、Mapping XML
3. 使用自定义的工具生成：Service、ServiceImpl、Controller，前端的页面、api接口，还有权限相关的SQL。
  - 自定义工具读取Mapper目录的文件名，获得需要生成代码列表。字段定义读取的是information_schema表。

