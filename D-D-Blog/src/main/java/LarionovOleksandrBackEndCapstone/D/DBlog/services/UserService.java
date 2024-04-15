package LarionovOleksandrBackEndCapstone.D.DBlog.services;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinaryUploader;


    public Page<User> getUsers(int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return userRepository.findAll(pageable);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.valueOf(id)));
    }
    @Transactional
    public void delete(UUID id) {
        User found = this.findById(id);
        userRepository.delete(found);
    }


    public User findByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with  email: " + email + " not found!"));
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public String uploadPicture(MultipartFile file, UUID userId) throws IOException {
        String url = (String) cloudinaryUploader.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap())
                .get("url");
        User found = this.findById(userId);
        found.setProfileImage(url);
        userRepository.save(found);
        return "Img profile saved";
    }
    public String blogBackgroundImage(MultipartFile file, UUID userId) throws IOException {
        String url = (String) cloudinaryUploader.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap())
                .get("url");
        User found = this.findById(userId);
        found.setBlogBackgroundImage(url);
        userRepository.save(found);
        return "Img Comments Changed";
    }

    public void enableUser(String email) {
        userRepository.enableUser(email);
    }
}
