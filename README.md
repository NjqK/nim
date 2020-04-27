# NIM Project

------------


## Profile:

- 分布式推送系统

- Tech：

		开发：
			语言：Java
			中间件：Redis Zookeeper Kafka Dubbo
			数据库： MySQL Druid Mybatis
			数据协议：Google Protobuf
			通讯框架：Netty
		
		测试：
			Python Locust
			Java  Apache Jmeter
- 目录介绍：

		api：项目接口协议和数据协议
			
			executor：存放各个系统的protoc，编译xx.proto用的
			
			modules：编译xx.proto生成的Maven项目，有对应的Java接口和类
			
			proto：自己编写的proto文件
			
			template：每个生成的module的pom的文件，pbGenerater.py会用
			
			pbGenerater.py：生成proto对应的类和接口
		
		common：各种工具类和个项目共用的东西
			
			dubbo.myprotobuf：使用dubbo自带的Protostuff，在使用Repeated Message的时，
			    Provider受到的请求一直是null，测试了一下    Protostuff，发现反序列化报错，估计是这个原因，
			    所以自己在Hessin2的基础上实现了Protobuf的序列化和反序列化
			
			guid：提供生成guid的功能，系统唯一自增id，用网上的雪花算法实现的，和机器时间有关系，正式的时候可以找一台
			    生成guid的服务器，我单机所以不会重复，但是项目支持通过Dubbo支持了分布式
		
		chat：作为服务的入口，本来应该有个真正的入口，主要是不想再建立项目了
			controller：RESTFUL接口，当然可以不用，直接使用Service暴露的服务，
			这就需要Admin（真正的系统入口，主要是寻找请求的服务等）
		
		push:
			
			service：PushServiceImpl.java只是简单的作了用户登陆状态的判断和消息的分发
		
		connector：提供了长连接服务，Springboot内嵌Netty，resources\ssl目录下有服务端和客户端的密钥库、受信库和证书
			
			netty：Netty服务的实现
				
				handler：Netty Handler
				
				initializer：提供了Default和Ssl两种
				
				listener：Netty 监听器
				
				ssl：SSLContextFactory Netty SSL 双向认证
		
		generator：mybatis生成
		
		nim-test：测试工程
			
			lib：要放到%JMETER_HOME%/lib/ext/下的jar文件
			
			src：Jmeter测试脚本用的自定义的TcpSampelr
			
			test_script：测试脚本
				
				Jmeter：
				
				Locust：
	
- Tips：

        dubbo连不上Zookeeper需要配置host文件，添加ip