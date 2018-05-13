package com.alphacat.user.requester;

import com.alphacat.pojo.Requester;
import com.alphacat.vo.RequesterVO;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

public class RequesterConverterTest {

    private RequesterConverter requesterConverter;

    public RequesterConverterTest() {
        requesterConverter = new RequesterConverter();
    }

    @Test
    public void toRequesterVO() {
        Date date = Date.valueOf("1995-11-18");
		RequesterVO expected = new RequesterVO(1, "r1", "1995-11-18", 1, "sss", "aaa", "ddd", 0);
        RequesterVO result = requesterConverter.toVO(new Requester(1, "r1",date,1,"sss","aaa","ddd",0));
		assertEquals(expected, result);
    }

    @Test
    public void toRequesterPOJO() {
		Date date = Date.valueOf("1995-11-18");
		Requester expected = new Requester(1, "r1", date, 1, "sss", "aaa", "ddd", 0);
        Requester result = requesterConverter.toPOJO(new RequesterVO(1, "r1","1995-11-18",1,"sss","aaa","ddd",0));
		assertEquals(expected, result);
    }


}
