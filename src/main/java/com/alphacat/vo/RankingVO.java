package com.alphacat.vo;

import lombok.Data;

import java.util.List;

/**
 * 排行界面使用的VO，工人自身信息从session里面获得
 * 两个List分别为对应排名前十
 * @author 161250102
 */
@Data
public class RankingVO {

    private int expRanking;
    private int creditRanking;
    private List<WorkerVO> expRankList;
    private List<WorkerVO> creditRankList;

}
