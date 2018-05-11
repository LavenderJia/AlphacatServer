package com.alphacat.util;

import com.alphacat.mapper.EmailMapper;
import com.alphacat.mapper.LabelMapper;
import com.alphacat.mapper.PictureMapper;
import com.alphacat.mapper.TaskRecordMapper;
import com.alphacat.pojo.*;
import com.alphacat.vo.*;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 将PO和VO互转的工具类
 * @author 161250102
 */
@Component
public class BeanMapper {

    private static Mapper mapper = DozerBeanMapperBuilder.create().withMappingFiles("config/dozer-mapping.xml").build();
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private TaskRecordMapper taskRecordMapper;
    @Autowired
    private PictureMapper pictureMapper;
    @Autowired
    private EmailMapper emailMapper;

    public static BeanMapper beanMapper;

    @PostConstruct
    public void init() {
        beanMapper = this;
    }

    public static RequesterVO toRequesterVO(Requester requester) {
        return mapper.map(requester, RequesterVO.class);
    }

    public static Requester toRequesterPOJO(RequesterVO requesterVO) {
        return mapper.map(requesterVO, Requester.class);
    }

    public static WorkerVO toWorkerVO(Worker worker) {
        return mapper.map(worker, WorkerVO.class);
    }

    public static Worker toWorkerPOJO(WorkerVO workerVO) {
        return mapper.map(workerVO, Worker.class);
    }

    public static R_TaskVO toR_TaskVO(Task task) {
        R_TaskVO result = mapper.map(task, R_TaskVO.class);

        // 获取任务状态
        Date date = new Date();
        Calendar cal_end = Calendar.getInstance();
        cal_end.add(Calendar.DAY_OF_MONTH, -1);
        if (date.before(task.getStartTime())) result.setState(0);
        else if (cal_end.after(task.getEndTime())) {
            result.setState(2);
            result.setWorkerCount(0);
            result.setTagRate(0);
            return result;
        }
        else result.setState(1);
        // 获取当前任务人数
        int workerNum = (beanMapper.taskRecordMapper.getWorkerNum(task.getId())== null) ? 1:beanMapper.taskRecordMapper.getWorkerNum(task.getId());
        result.setWorkerCount(workerNum);
        //计算标注率
        Integer tagSum = beanMapper.taskRecordMapper.getTagSum(task.getId());
        Integer picNum = beanMapper.pictureMapper.getPictureNum(task.getId());
        if (tagSum == null) tagSum = 0;
        if (picNum == null) picNum = 0;
        double tagRate = (double) tagSum / picNum;
        result.setTagRate((double) Math.round(tagRate*1000)/1000);

        return result;
    }

    public static W_TaskVO toW_TaskVO(Task task, int workerId) {
        W_TaskVO result = mapper.map(task, W_TaskVO.class);

        // 获取任务状态
        TaskRecord record = beanMapper.taskRecordMapper.getTaskRecord(task.getId(), workerId);
        if (record == null) result.setState(0);
        else if (record.getPicDoneNum() == beanMapper.pictureMapper.getPictureNum(task.getId())) result.setState(2);
        else result.setState(1);
        // 获取当前任务人数
        int workerNum = beanMapper.taskRecordMapper.getWorkerNum(task.getId());
        result.setWorkerCount(workerNum);

        return result;
    }

    /**
     * 转换为历史任务列表的记录，此处赚取的积分没有填入
     * @param task 任务
     * @return 历史任务
     */
    public static HistoryTaskVO toHistoryTaskVO(Task task) {
        return mapper.map(task, HistoryTaskVO.class);
    }

    public static TaskVO toTaskVO(Task task) {
        TaskVO taskVO = mapper.map(task, TaskVO.class);
        List<Label> labels = beanMapper.labelMapper.getAllLabels(task.getId());
        LabelVO[] labelVOS = new LabelVO[labels.size()];
        for (int i = 0; i < labels.size(); i++) {
            labelVOS[i] = mapper.map(labels.get(i), LabelVO.class);
        }
        taskVO.setLabels(labelVOS);
        return taskVO;
    }

    public static Task toTaskPOJO(TaskVO taskVO) {
        return mapper.map(taskVO, Task.class);
    }

    public static SquareVO toSquareVO(SquareTag squareTag) {
        SquareVO square = mapper.map(squareTag, SquareVO.class);

        String[] labels = squareTag.getLabelData().split(",");
        Map<String, String> labelData = new HashMap<>();
        for (String str : labels) {
            int index = str.indexOf(':');
            labelData.put(str.substring(0,index), str.substring(index + 1));
        }
        square.setLabelData(labelData);

        return square;
    }

    public static SquareTag toSquareTag(SquareVO squareVO, int workerId, int taskId, int picIndex) {
        SquareTag squareTag = mapper.map(squareVO, SquareTag.class);

        String labelData = "";
        for(Map.Entry<String,String > entry:squareVO.getLabelData().entrySet()) {
            labelData += (entry.getKey() + ":" + entry.getValue() + ",");
        }
        squareTag.setLabelData(labelData.substring(0,labelData.length() - 1));

        squareTag.setWorkerId(workerId);
        squareTag.setTaskId(taskId);
        squareTag.setPicIndex(picIndex);

        return squareTag;
    }

    public static Email toEmail(EmailVO emailVO) {
        Email email = mapper.map(emailVO, Email.class);
        int id = beanMapper.emailMapper.getNewId() == null ? 1 : beanMapper.emailMapper.getNewId();
        email.setId(id);
        return email;
    }

    public static EmailShowVO toEmailShow(Email email) {
        return mapper.map(email, EmailShowVO.class);
    }

    public static IrregularTag toIrregularTag(IrregularTagVO tag, int taskId) {
        IrregularTag result = mapper.map(tag, IrregularTag.class);
        result.setTaskId(taskId);

        String figure = "";
        for (int i = 0; i < tag.getPaths().size(); i++) {
            for (int j = 0; j < tag.getPaths().get(i).size(); j++) {
                if (j == 0) figure += "{";
                PointVO point = tag.getPaths().get(i).get(j);
                figure += (point.getX() + "," + point.getY());
                if (j == tag.getPaths().get(i).size() - 1) figure += "}";
                else figure += ",";
            }
            if (i != tag.getPaths().size() - 1) figure += ".";
        }
        result.setFigure(figure);
        return result;
    }

    public static IrregularTagVO toIrregularTagVO(IrregularTag tag) {
        IrregularTagVO result = mapper.map(tag, IrregularTagVO.class);

        String figure = tag.getFigure();
        List<List<PointVO>> pathList = new LinkedList<>();
        String[] paths = figure.contains(".") ? figure.split(".") : new String[]{figure};
        for (String path : paths) {
            List<PointVO> pointList = new LinkedList<>();
            String[] points = path.substring(1, path.length() - 1).split(",");
            for (int i = 0; i < points.length; i += 2) {
                int x = Integer.parseInt(points[i]);
                int y = Integer.parseInt(points[i+1]);
                pointList.add(new PointVO(x, y));
            }
            pathList.add(pointList);
        }
        result.setPaths(pathList);
        return result;
    }
}
