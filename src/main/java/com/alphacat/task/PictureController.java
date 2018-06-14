package com.alphacat.task;

import com.alibaba.fastjson.JSON;
import com.alphacat.service.PictureService;
import com.alphacat.vo.PictureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RequestMapping("/pic")
@RestController
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @PostMapping("/{taskId}")
    public void save(@PathVariable("taskId") int taskId, @RequestParam("file")MultipartFile file, @RequestParam("picIndex")int picIndex) {
        pictureService.uploadPic(file, taskId, picIndex);
    }

    @GetMapping("/{taskId}")
    public Object getPicURL(@PathVariable("taskId") int taskId) {
        File baseDir = new File("C:\\AlphaCatPic\\" + taskId);
        File[] files = baseDir.listFiles();
        PictureVO[] pictures = new PictureVO[files.length];
        for (int i = 0; i < pictures.length; i++) {
            String fileName = files[i].getName();
            int dot = fileName.indexOf('.');
            PictureVO picture = new PictureVO();
            picture.setName(fileName);
            picture.setUrl("http://localhost:8080/pic/"+taskId+"/"+fileName.substring(0, dot));
            pictures[i] = picture;
        }
        return pictures;
    }

    @GetMapping("/{taskId}/{picIndex}")
    public void get(@PathVariable("taskId") int taskId, @PathVariable("picIndex") int picIndex, HttpServletResponse response) {
        try {
            //here change the base url of picture
            File baseDir = new File("C:\\AlphaCatPic\\" + taskId);
            File[] files = baseDir.listFiles();
            String tempName = "";
            String suffix = "png";
            for (File f: files) {
                tempName = f.getName();
                int dot = tempName.indexOf('.');
                if(dot < 0) {
                    continue;
                }
                if (tempName.substring(0, dot).equals(String.valueOf(picIndex))) {
                    suffix = tempName.substring(dot + 1);
                    break;
                }
            }

            File file = new File(baseDir + "\\" + tempName);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[(int)file.length()];
            inputStream.read(data);
            inputStream.close();

            response.setContentType("image/" + suffix);

            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
