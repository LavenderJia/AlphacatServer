package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HistoryTask {

    private int taskId;
    private String name;
    private Date startTime;
    private Date endTime;
    private int creditEarned;

}
