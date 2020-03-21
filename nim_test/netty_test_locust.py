# coding: utf-8
# author：njq
import time
import os
from gevent import socket
from locust import TaskSet, task, between, Locust, events

"""
因为Jmeter在测试机器上只能创建10000左右的长连接，
不然就OOM，所以用Locust实现了同样的长连接和心跳的测试脚本。
FurtherMore，我不熟悉在python下的protobuf使用，所以直接用
python的字节数组来表示请求:1（Length）+4（Protobuf Data）
PING = bytes([4, 10, 2, 16, 2])
PONG = bytes([4, 10, 2, 16, 2])
因为服务端采用Netty和Protobuf，并使用Netty自带的protobuf半包，粘包，encoder，decoder:

ProtobufVarint32FrameDecoder的作用： 
 * <pre>
 * BEFORE DECODE (302 bytes)       AFTER DECODE (300 bytes)
 * +--------+---------------+      +---------------+
 * | Length | Protobuf Data |----->| Protobuf Data |
 * | 0xAC02 |  (300 bytes)  |      |  (300 bytes)  |
 * +--------+---------------+      +---------------+
 * </pre>
 
 ProtobufVarint32LengthFieldPrepender的作用：
* <pre>
 * BEFORE ENCODE (300 bytes)       AFTER ENCODE (302 bytes)
 * +---------------+               +--------+---------------+
 * | Protobuf Data |-------------->| Length | Protobuf Data |
 * |  (300 bytes)  |               | 0xAC02 |  (300 bytes)  |
 * +---------------+               +--------+---------------+
 * </pre> *

我的服务添加的handler：没有添加ssl，主要测试用：
        pipeline.addLast(new IdleStateHandler(30, 0, 0));
        pipeline.addLast("IdleTriggerHandler", new IdleTrigger());
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast("decoder", new ProtobufDecoder(Common.Msg.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast("encode", new ProtobufEncoder());
        pipeline.addLast("HeartBeatHandler", new HeartBeatHandler());
        pipeline.addLast("BizHandler", new BizHandler());


"""

# 客户端
PING = bytes([4, 10, 2, 16, 2])
# 服务端
PONG = bytes([4, 10, 2, 16, 2])


class SocketClient(object):

    def __init__(self):
        # 仅在新建实例的时候创建socket.
        self._socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def __getattr__(self, name):
        # print(name)
        skt = self._socket

        def wrapper(*args, **kwargs):
            start_time = time.time()
            if name == "connect":
                try:
                    skt.connect(args[0])
                except Exception as e:
                    # print(e)
                    total_time = int((time.time() - start_time) * 1000)
                    events.request_failure.fire(request_type="connect", name=name, response_time=total_time,
                                                response_length=0, exception=e)
                else:
                    total_time = int((time.time() - start_time) * 1000)
                    events.request_success.fire(request_type="connect", name=name, response_time=total_time,
                                                response_length=0)
            elif name == "send":
                try:
                    skt.sendall(args[0])
                    data = skt.recv(5)
                    # 因为服务端返回的心跳pong和ping是一个格式，所以是和ping一样的
                    if data == PONG:
                        print("right resp")
                    else:
                        print("error resp")
                except Exception as e:
                    # print(e)
                    total_time = int((time.time() - start_time) * 1000)
                    events.request_failure.fire(request_type="send", name=name, response_time=total_time,
                                                response_length=0, exception=e)
                else:
                    total_time = int((time.time() - start_time) * 1000)
                    events.request_success.fire(request_type="send", name=name, response_time=total_time,
                                                response_length=0)
            elif name == "close":
                skt.close()

        return wrapper


class UserBehavior(TaskSet):
    def on_start(self):
        self._client = SocketClient()
        self._client.connect((self.locust.host, self.locust.port))

    def on_stop(self):
        print("end conn")
        self._client.close()

    @task(1)
    def sendAddCmd(self):
        self._client.send(PING)


class SocketLocust(Locust):
    def __init__(self, *args, **kwargs):
        super(SocketLocust, self).__init__(*args, **kwargs)


class SocketUser(SocketLocust):
    host = "192.168.0.106"
    port = 10859
    task_set = UserBehavior
    wait_time = between(1000, 10000)


# def trans(s):
#     return "b'%s'" % ''.join('\\x%.2x' % x for x in s)


if __name__ == "__main__":
    os.system("locust -f netty_test.py --no-web -c 1 -r 1 -t 20m")
    # client = SocketClient()
    # client.connect(("192.168.0.106", 10859))
    # client.send(bytes([4, 10, 2, 16, 2]))
