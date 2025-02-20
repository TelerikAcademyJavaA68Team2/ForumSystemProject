package com.example.forumproject.helpers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.forumproject.models.User;
import com.example.forumproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryHelper {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    private final UserService userService;

    @Autowired
    public CloudinaryHelper(UserService userService) {
        this.userService = userService;
    }

    private Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadUserProfilePhoto(MultipartFile multipartFile, User user) throws IOException {

        Cloudinary cloudinary = getCloudinary();

        Map<String, Object> uploadResult = cloudinary.uploader()
                .upload(multipartFile.getBytes(), ObjectUtils.emptyMap());

        String photoUrl = uploadResult.get("url").toString();

        user.setPhoto(photoUrl);
        userService.update(user);
        return photoUrl;
    }
}
