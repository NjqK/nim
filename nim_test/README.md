# NIM Test

### Jmeter TcpSampler

**Jmeter version: Jmeter5.2**

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

**4.查看生成的xx.jtl的测试结果，可以在Jmeter里添加插件**

```
1.添加lib/report下的jmeter-plugins-manager-1.3.jar
2.打开jmeter找到options里的plugins manager
3.找到basic grahic 和 additional grahic并添加
4.打开test_script/jmeter下的report.jmx脚本，然后在对应的 \
栏目里打开xx.jtl文件，查看数据。
```

### Locust 

**1.cd到脚本所在目录**

**2.参照python脚本的main方法去执行命令**

```
locust -f netty_test.py --no-web（不启动web，删掉可以启动，然后打开localhost:8089） \
-c 1（创建用户数） -r 1（创建速率，单机推荐小于等于100，分布式可以增大） -t 20m(20分钟)
```