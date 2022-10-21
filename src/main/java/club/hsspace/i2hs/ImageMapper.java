package club.hsspace.i2hs;

import club.hsspace.i2hs.entity.ChainEntity;
import club.hsspace.i2hs.entity.FileEntity;
import club.hsspace.i2hs.entity.ImageEntity;
import club.hsspace.i2hs.entity.VisitEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ImageMapper {

    int insertFile(FileEntity fileEntity);

    int insertImage(ImageEntity imageEntity);

    int insertChain(ChainEntity chainEntity);

    int selectImageCount(Long userId);

    List<ImageEntity> selectImage(@Param("userId") Long userId, @Param("base") Integer base, @Param("offset") Integer offset);

    ImageEntity selectImageOnce(@Param("imageId") Long imageId);

    FileEntity selectFileOnce(@Param("fileId") Long fileId);

    ChainEntity selectChainByChainAddr(@Param("chainAddr") String chainAddr);

    Map<String, Object> lookupImage(@Param("imageId") Long imageId, @Param("userId") Long userId);

    int deleteImage(@Param("imageId")Long imageId, @Param("userId")Long userId);

    int insertVisit(VisitEntity visitEntity);

}
