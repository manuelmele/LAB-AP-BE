package core.wefix.lab.repository;


import core.wefix.lab.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    List<Gallery> findByUserIdAndDeletedGalleryFalse(Long userId);
    Gallery findByGalleryId(Long galleryId);
}