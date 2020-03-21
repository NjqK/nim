package com.example;

import static org.junit.Assert.assertTrue;

import com.example.proto.common.common.Common;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
//        int value = 128;
//        if ((value & ~0x7F) == 0) {
//            return;
//        } else {
//            System.out.println(Integer.toBinaryString((value & 0x7F) | 0x80));
//            value >>>= 7;
//            System.out.println(Integer.toBinaryString(value));
//        }
        System.out.println(0xAC02);
        Common.MsgType kick = Common.MsgType.valueOf("KICK");
        System.out.println(kick);
    }

}
