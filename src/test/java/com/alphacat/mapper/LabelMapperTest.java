package com.alphacat.mapper;

import com.alphacat.pojo.Label;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabelMapperTest {
    @Autowired
    LabelMapper labelMapper;

    @Test
    public void A_add() {
        ArrayList<String> choices1 = new ArrayList<String>();
        choices1.add("rabbittank");
        choices1.add("comicdiamond");
        ArrayList<String> choices2=new ArrayList<String>();
        choices2.add("van");
        choices2.add("billy");
        choices2.add("duang");
        labelMapper.add(new Label(2,"bestMatch", choices1));
        labelMapper.add(new Label(2,"king", choices2));
    }

    @Test
    public void B_get() {
        ArrayList<String> choices2=new ArrayList<String>();
        choices2.add("van");
        choices2.add("billy");
        choices2.add("duang");
        Collections.sort(choices2);
        assertEquals("bestMatch",labelMapper.get(2).get(0).getLabel());
        List<String> actualChoices = labelMapper.get(2).get(1).getChoices();
        Collections.sort(actualChoices);
        assertEquals(choices2, actualChoices);
    }


    @Test
    public void C_deleteLabels() {
        labelMapper.delete(2);
    }

}