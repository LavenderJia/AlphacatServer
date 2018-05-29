package com.alphacat.user.avatar;

import com.alphacat.mapper.AvatarMapper;
import com.alphacat.pojo.Avatar;
import com.alphacat.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarServiceImpl implements AvatarService{

    private AvatarMapper mapper;

    @Autowired
    public AvatarServiceImpl(AvatarMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void upload(MultipartFile file, String name, int type) {
        try {
            Avatar avatar = mapper.get(name, type);
            byte[] blob = file.getBytes();
            if(avatar == null) {
                mapper.add(new Avatar(name, type, blob));
            } else {
                mapper.update(new Avatar(name, type, blob));
            }
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save avatar for user: " + name + " of type: " + type);
        }
    }

    @Override
    public void delete(String name, int type) {
        mapper.delete(name, type);
    }

    @Override
    public byte[] get(String name, int type) {
        Avatar avatar = mapper.get(name, type);
        return avatar.getBlob();
    }

}
