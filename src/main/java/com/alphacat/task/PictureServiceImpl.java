package com.alphacat.task;

import com.alphacat.mapper.PictureMapper;
import com.alphacat.mapper.TaskRecordMapper;
import com.alphacat.pojo.Picture;
import com.alphacat.pojo.TaskRecord;
import com.alphacat.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private PictureMapper pictureMapper;
    @Autowired
    private TaskRecordMapper taskRecordMapper;

    @Override
    public int[] getPicOrder(int taskId, int workerId) {
        TaskRecord record = taskRecordMapper.get(workerId, taskId);
        String picOrder;
        if(record == null) {
            picOrder = genPicOrder(taskId);
            taskRecordMapper.add(new TaskRecord(
                    workerId, taskId, picOrder, 0));
        } else {
            picOrder = record.getPicOrder();
        }
        return convertPicOrder(picOrder);
    }

    @Override
    public int getFirstPic(int taskId, int workerId) {
        TaskRecord taskRecord = taskRecordMapper.get(workerId, taskId);
        if (taskRecord == null) { // task has not been started
            String picOrder = genPicOrder(taskId);
            taskRecordMapper.add(new TaskRecord(workerId, taskId, picOrder, 0));
            return Integer.parseInt(picOrder.substring(0, 2));
        } else {
            String picOrder = taskRecord.getPicOrder();
            int num = taskRecord.getPicDoneNum();
            if (num == picOrder.length() / 2) { // task has been done
                return Integer.parseInt(picOrder.substring(0, 2));
            }
            return Integer.parseInt(picOrder.substring(num*2, num*2+2));
        }
    }

    @Override
    public int getPrePic(int taskId, int workerId, int picIndex) {
        TaskRecord taskRecord = taskRecordMapper.get(workerId, taskId);
        String picOrder = taskRecord.getPicOrder();
        int n = indexOf(picOrder, picIndex);
        if (n == 0) return -1;
        else return Integer.parseInt(picOrder.substring(n-2, n));
    }

    @Override
    public int getNextPic(int taskId, int workerId, int picIndex) {
        TaskRecord taskRecord = taskRecordMapper.get(workerId, taskId);
        String picOrder = taskRecord.getPicOrder();
        int n = indexOf(picOrder, picIndex);
        if (n == picOrder.length() - 2) return -1;
        else return Integer.parseInt(picOrder.substring(n+2, n+4));
    }

    @Override
    public boolean uploadPic(MultipartFile file, int taskId, int picIndex) {
        if (picIndex > 11) return false;
        if (!file.isEmpty()) {
            try {
                String uploadFileName = file.getOriginalFilename();
                String suffix = uploadFileName.substring(uploadFileName.indexOf("."));
                String path = "C:\\AppServ\\www\\image";
                Files.copy(file.getInputStream(), Paths.get(path,
                        taskId + "-" + picIndex + suffix));
                pictureMapper.add(new Picture(picIndex, taskId));
                return true;
            } catch (IOException |RuntimeException e) {
                e.printStackTrace();
                return false;
            }

        }
        return false;
    }

    @Override
    public boolean delete(int taskId) {
        try {
            Files.delete(Paths.get("/pic", taskId+""));
            pictureMapper.multiDelete(taskId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int indexOf(String order, int num) {
        int index = 0;
        for (;index < order.length(); index += 2) {
            if (Integer.parseInt(order.substring(index, index+2)) == num) break;
        }
        return index;
    }

    private String genPicOrder(int taskId) {
        int count = pictureMapper.count(taskId);
        StringBuffer picOrder = new StringBuffer();
        for (int i = 0; i < count; i++) {
            picOrder.append(String.format("%02d", i));
        }
        // generate random order
        for (int i = 0; i < count; i++) {
            int p = (int) (Math.random()*count);
            p *= 2;
            reverse(picOrder, 0, p);
            reverse(picOrder, p, count * 2);
            reverse(picOrder, 0, count * 2);
        }
        return picOrder.toString();
    }

    private void reverse(StringBuffer buffer, int from, int to) {
        to--;
        while(from < to) {
            char c = buffer.charAt(from);
            buffer.setCharAt(from, buffer.charAt(to));
            buffer.setCharAt(to, c);
            from++;
            to--;
        }
    }

    private int[] convertPicOrder(String picOrder) {
        int len = picOrder.length();
        int[] result = new int[len / 2];
        for(int i = 0; i < len / 2; i++) {
            result[i] = Integer.parseInt(picOrder.substring(i*2, i*2+2));
        }
        return result;
    }

}
