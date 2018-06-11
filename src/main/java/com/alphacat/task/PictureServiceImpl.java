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
import java.io.File;
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
    public boolean uploadPic(MultipartFile file, int taskId, int picIndex) {
//        if (picIndex > 11) return false;
        if (!file.isEmpty()) {
            try {
                File dir = new File("C:\\AlphaCatPic\\" + taskId + '\\');
                if(!dir.exists() || !dir.isDirectory()) {
                    dir.mkdirs();
                }
                String uploadFileName = file.getOriginalFilename();
                String suffix = uploadFileName.substring(uploadFileName.indexOf("."));
                file.transferTo(new File(dir, picIndex + suffix));
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
            Files.delete(Paths.get("C:\\AlphaCatPic\\", taskId+""));
            pictureMapper.multiDelete(taskId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
