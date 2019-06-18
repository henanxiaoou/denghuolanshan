1:运行主类
    http://127.0.0.1:8080/a/signin.jsp

	数据库脚本 init.sql
2:密码非MD5加密，而是采用自定义算法并发送到邮箱(后续可使用MD5加密,并且集成shiro安全框架) 

3:尽管集成了redis,但应用的并不是很好(主要是没咋用)

4:本项目虽然是SSM项目,也没有做分布式，但是容易扩展，日志方便,但毕竟不能算是自己的

5:后续可以改造成springboot微服务或者做分布式,后期要集成shiro,lucene或solr或ElasticSearch，以及Ehcache进程缓存框架，并且应用Redis,最后抛弃jsp选用vue或模板引擎

6:本项目的所有服务端由小欧开发，所有前端jsp页面非小欧开发
