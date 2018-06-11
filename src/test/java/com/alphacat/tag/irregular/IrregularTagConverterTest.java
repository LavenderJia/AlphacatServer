package com.alphacat.tag.irregular;

import com.alphacat.pojo.IrregularTag;
import com.alphacat.vo.IrregularTagVO;
import com.alphacat.vo.PointVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IrregularTagConverterTest {
    @Autowired
    private IrregularTagConverter irregularTagConverter;
    private MockMvc mvc;
    private RequestBuilder request = null;


    @Test
    public void toPOJO() {
        List<PointVO> pointVO1 = new ArrayList<>();
        pointVO1.add(new PointVO(1,2));
        pointVO1.add(new PointVO(2,2));
        pointVO1.add(new PointVO(12,23));

        List<List<PointVO>> paths1 = new ArrayList<>();
        paths1.add(pointVO1);

        List<PointVO> pointVO2 = new ArrayList<>();
        pointVO2.add(new PointVO(12,2));
        pointVO2.add(new PointVO(-200,1232));
        pointVO2.add(new PointVO(-12,-23));
        paths1.add(pointVO2);

        IrregularTagVO irregularTagVO1 = new IrregularTagVO(paths1);


        IrregularTag expected1 = new IrregularTag(1, 1, 1, "1,2:2,2:12,23;12,2:-200,1232:-12,-23");
        IrregularTag actual1 = irregularTagConverter.toPOJO(irregularTagVO1, 1, 1, 1);
        assertEquals(expected1, actual1);

        List<List<PointVO>> paths2 = new ArrayList<>();
        paths2.add(pointVO2);

        IrregularTagVO irregularTagVO2 = new IrregularTagVO(paths2);

        IrregularTag expected2 = new IrregularTag(5, 10, 20, "12,2:-200,1232:-12,-23");
        IrregularTag actual2 = irregularTagConverter.toPOJO(irregularTagVO2, 10, 5, 20);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toVO() {
        IrregularTag irregularTag1 = new IrregularTag(5, 10, 20, "12,2:-200,1232:-12,-23");
        IrregularTag irregularTag2 = new IrregularTag(1, 1, 1, "1,2:2,2:12,23;12,2:-200,1232:-12,-23");

        List<PointVO> pointVO1 = new ArrayList<>();
        pointVO1.add(new PointVO(1,2));
        pointVO1.add(new PointVO(2,2));
        pointVO1.add(new PointVO(12,23));

        List<List<PointVO>> paths1 = new ArrayList<>();
        List<List<PointVO>> paths2 = new ArrayList<>();
        paths1.add(pointVO1);

        List<PointVO> pointVO2 = new ArrayList<>();
        pointVO2.add(new PointVO(12,2));
        pointVO2.add(new PointVO(-200,1232));
        pointVO2.add(new PointVO(-12,-23));
        paths1.add(pointVO2);
        paths2.add(pointVO2);

        IrregularTagVO expected1 = new IrregularTagVO(paths2);
        IrregularTagVO expected2 = new IrregularTagVO(paths1);

        IrregularTagVO actual1= irregularTagConverter.toVO(irregularTag1);
        IrregularTagVO actual2= irregularTagConverter.toVO(irregularTag2);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toVOList() {
        IrregularTag irregularTag1 = new IrregularTag(5, 10, 20, "12,2:-200,1232:-12,-23");
        IrregularTag irregularTag2 = new IrregularTag(1, 1, 1, "1,2:2,2:12,23;12,2:-200,1232:-12,-23");

        List<PointVO> pointVO1 = new ArrayList<>();
        pointVO1.add(new PointVO(1,2));
        pointVO1.add(new PointVO(2,2));
        pointVO1.add(new PointVO(12,23));

        List<List<PointVO>> paths1 = new ArrayList<>();
        List<List<PointVO>> paths2 = new ArrayList<>();
        paths1.add(pointVO1);

        List<PointVO> pointVO2 = new ArrayList<>();
        pointVO2.add(new PointVO(12,2));
        pointVO2.add(new PointVO(-200,1232));
        pointVO2.add(new PointVO(-12,-23));
        paths1.add(pointVO2);
        paths2.add(pointVO2);

        IrregularTagVO expected1 = new IrregularTagVO(paths2);
        IrregularTagVO expected2 = new IrregularTagVO(paths1);

        List<IrregularTagVO> expected = new ArrayList<>();
        expected.add(expected1);
        expected.add(expected2);

        List<IrregularTag> list = new ArrayList<>();
        list.add(irregularTag1);
        list.add(irregularTag2);

        List<IrregularTagVO> actual = irregularTagConverter.toVOList(list);

        assertEquals(expected, actual);

    }
}