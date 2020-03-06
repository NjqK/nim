package com.example.chat;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

@Slf4j
public class ConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //开始时间
        long startTime = System.currentTimeMillis();
        //执行接口调用逻辑
        Result result = invoker.invoke(invocation);
        log.info("result:{}", result);
        //调用耗时
        long elapsed = System.currentTimeMillis() - startTime;
        log.info("cost:{}", elapsed);
        //返回结果响应结果
        return result;
    }

}
