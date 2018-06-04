package com.alphacat.task;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RequestMapping("/pic")
@RestController
public class PictureController {

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
