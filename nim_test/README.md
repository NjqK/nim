# NIM Test

### Jmeter TcpSampler Implement

**1.把lib目录下的jar包放到 %JMETER_HOME%/lib/ext/目录下**

**2.使用Jmeter GUI打开.jmx文件，按照需求修改测试数据**

**3.命令行执行.jmx下文件**

```
命令执行如：jmeter -n -t xx.jmx -l xx.jtl
常用参数解释如下：
-n：说明jmeter非GUI运行
-t：运行的测试计划名称，xxx.jmx路径+文件
-l：JTL文件去保存结果，路径+xxx.jtl
-r: 使用远程执行
-j：保存执行log
-H：代理机主机名或者ip地址
-P：代理机端口
```