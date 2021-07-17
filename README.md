# 外文地名对应汉语地名转换
## 功能
按照国家、语种定义音译表、常用词。使用音译表和常用词构建AC自动机，对外文地名进行转换。转换结果经人工审核，确定外文地名对应的汉语地名。
## 代码说明
```
后端：Spring Boot工程，提供api接口
前端：使用了[PanJiaChen/vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)
AC自动机：使用了[robert-bor/aho-corasick](https://github.com/robert-bor/aho-corasick)
```
