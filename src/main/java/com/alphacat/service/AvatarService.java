package com.alphacat.service;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    /**
     * Add or update a user's avatar.
     * If the user had an avatar before, update it.
     * If not, add a new one.
     */
    void upload(MultipartFile file, String name, int type);

    /**
     * If a user changes its name, delete its avatar and
     * upload a new one.
     * @see #upload(MultipartFile, String, int)
     */
    void delete(String name, int type);

    /**
     * @return the avatar's bytes
     */
    byte[] get(String name, int type);

}
