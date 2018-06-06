package com.alphacat.tag.irregular;

import com.alphacat.pojo.IrregularTag;
import com.alphacat.pojo.SquareTag;
import com.alphacat.tag.square.SquareTagConverter;
import com.alphacat.vo.IrregularTagVO;
import com.alphacat.vo.PointVO;
import com.alphacat.vo.SquareVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SquareTagConverterTest {
    @Autowired
    private SquareTagConverter squareTagConverter;
    private MockMvc mvc;
    private RequestBuilder request = null;


    @Test
    public void toPOJO() {
        Map<String, String> labelData1 = new HashMap<>();
        labelData1.put("sex", "male");
        labelData1.put("age","16");
        Map<String, String> labelData2 = new HashMap<>();
        labelData2.put("cup", "c");
        labelData2.put("hobby", "yyx");
        SquareVO squareVO1 = new SquareVO(0,1,2,3,4, labelData1, null);
        SquareVO squareVO2 = new SquareVO(1,1,2,3,0, labelData2, "huaq");


        SquareTag expected1 = new SquareTag(1,1,1,0,1,2,4,3,"sex:male,age:16", null);
        SquareTag expected2 = new SquareTag(1,1,1,1,1,2,0,3,"cup:c,hobby:yyx", "huaq");

        SquareTag actual1 = squareTagConverter.toPOJO(squareVO1, 1,1,1);
        SquareTag actual2 = squareTagConverter.toPOJO(squareVO2, 1,1,1);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toVO() {
        SquareTag s1 = new SquareTag(1,1,1,0,1,2,4,2,"sex:male,age:16", "description");

        Map<String, String> labelData1 = new HashMap<>();
        labelData1.put("sex", "male");
        labelData1.put("age","16");
        SquareVO expected = new SquareVO(0,1,2,2,4,labelData1,"description");
        SquareVO actual = squareTagConverter.toVO(s1);
        assertEquals(expected, actual);
    }

    @Test
    public void toVOList() {
        SquareTag s1 = new SquareTag(1,1,1,0,1,2,4,2,"sex:male,age:16", null);
        SquareTag s2 = new SquareTag(1,1,1,1,1,2,0,3,"cup:c,hobby:yyx", "huaq");
        Map<String, String> labelData1 = new HashMap<>();
        labelData1.put("sex", "male");
        labelData1.put("age","16");
        Map<String, String> labelData2 = new HashMap<>();
        labelData2.put("cup", "c");
        labelData2.put("hobby", "yyx");

        SquareVO expected1 = new SquareVO(0,1,2,2,4, labelData1, null);
        SquareVO expected2 = new SquareVO(1,1,2,3,0, labelData2, "huaq");

        List<SquareTag> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);

        List<SquareVO> expected = new ArrayList<>();
        expected.add(expected1);
        expected.add(expected2);

        List<SquareVO> actual = squareTagConverter.toVOList(list);

        assertEquals(expected, actual);
    }
}